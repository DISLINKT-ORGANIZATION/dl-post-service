package disinkt.postservice.service;

import disinkt.postservice.dtos.CommentDto;
import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.ReactionDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.entities.*;
import disinkt.postservice.exception.ResourceNotFoundException;
import disinkt.postservice.kafka.KafkaNotification;
import disinkt.postservice.kafka.KafkaNotificationType;
import disinkt.postservice.mappers.CommentDtoMapper;
import disinkt.postservice.mappers.PostDtoMapper;
import disinkt.postservice.model.EventKafka;
import disinkt.postservice.model.EventType;
import disinkt.postservice.repositories.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

	private final PostRepository repository;
	private final PostDtoMapper mapper;
	private final ReactionService reactionService;
	private final CommentService commentService;
	private final CommentDtoMapper commentMapper;
	private final KafkaTemplate<String, KafkaNotification> kafkaTemplate;
	private final KafkaTemplate<String, EventKafka> eventKafkaTemplate;

	@Autowired
	public PostService(PostRepository repository, PostDtoMapper mapper, ReactionService reactionService,
			CommentService commentService, CommentDtoMapper commentMapper,
			KafkaTemplate<String, KafkaNotification> kafkaTemplate,
			KafkaTemplate<String, EventKafka> eventKafkaTemplate) {
		this.repository = repository;
		this.mapper = mapper;
		this.reactionService = reactionService;
		this.commentService = commentService;
		this.commentMapper = commentMapper;
		this.kafkaTemplate = kafkaTemplate;
		this.eventKafkaTemplate = eventKafkaTemplate;
	}

	public List<PostDto> retrievePosts(UserIds userIds) {
		List<Post> posts = repository.findAllByUserIdInOrderByDatePostedDesc(userIds.getIds());
		List<Reaction> usersReactions = this.reactionService.findReactionByUserId(userIds.getLoggedInUserId());
		List<PostDto> dtoPosts = new ArrayList<>();
		for (Post post : posts) {
			PostDto dto = mapper.toDto(post);
			for (Reaction reaction : usersReactions) {
				if (post.getReactions().contains(reaction)) {
					if (reaction.getReactionType().equals(ReactionType.LIKE)) {
						dto.setUserLikesPost(true);
					} else {
						dto.setUserDislikesPost(true);
					}
				}
			}
			dtoPosts.add(dto);
		}
		return dtoPosts;
	}

	public void createPost(PostDto dto) {
		Post post = new Post();
		Date today = new Date();
		post.setDatePosted(today.getTime());
		post.setUserId(dto.getUserId());
		post.setText(dto.getText());
		post.setImage(dto.getImage());
		post = this.repository.save(post);
		for (Long notifyUserId : dto.getUsersToNotify()) {
			publishNotification(post.getUserId(), notifyUserId, today.getTime(), KafkaNotificationType.NEW_POST);
		}
		EventKafka event = new EventKafka(today, "User with id  " + post.getUserId() + " created new post.",
				EventType.CREATED_POST);
		eventKafkaTemplate.send("dislinkt-events", event);
	}

	private void publishNotification(Long senderId, Long recipientId, Long timestamp, KafkaNotificationType type) {
		PostNotification postNotification = new PostNotification();
		postNotification.setSenderId(senderId);
		postNotification.setRecipientId(recipientId);
		postNotification.setTimestamp(timestamp);
		KafkaNotification kafkaNotification = new KafkaNotification(postNotification, type);
		kafkaTemplate.send("dislinkt-user-notifications", kafkaNotification);
	}

	public void reactToPost(Long postId, ReactionDto reactionDto, boolean sendNotification) {
		Post post = this.repository.findOneById(postId);
		entityExistsCheck(post);
		boolean removeReaction = reactionDto.isRemoveReaction();
		Reaction reaction = reactionService.findReaction(post, reactionDto.getUserId());
		if (removeReaction) {
			entityExistsCheck(reaction);
			post.getReactions().remove(reaction);
			if (reactionDto.getReaction() == 0) {
				post.setLikes(post.getLikes() - 1);
			} else {
				post.setDislikes(post.getDislikes() - 1);
			}
			this.repository.save(post);
			this.reactionService.deleteReaction(reaction);
			return;
		}
		Reaction newReaction = new Reaction();
		newReaction.setUserId(reactionDto.getUserId());
		// like
		if (reactionDto.getReaction() == 0) {
			// if reaction already exists - dislike
			if (!Objects.isNull(reaction)) {
				post.getReactions().remove(reaction);
				post.setDislikes(post.getDislikes() - 1);
				this.reactionService.deleteReaction(reaction);
			}
			newReaction.setReactionType(ReactionType.LIKE);
			newReaction.setPost(post);
			newReaction = this.reactionService.saveReaction(newReaction);
			post.getReactions().add(newReaction);
			post.setLikes(post.getLikes() + 1);
			this.repository.save(post);
			Date today = new Date();
			if (sendNotification) {
				publishNotification(newReaction.getUserId(), post.getUserId(), today.getTime(),
						KafkaNotificationType.LIKE);
			}
			return;

		}
		// dislike
		if (reactionDto.getReaction() == 1) {
			// if reaction already exists - like
			if (!Objects.isNull(reaction)) {
				post.getReactions().remove(reaction);
				post.setLikes(post.getLikes() - 1);
				this.reactionService.deleteReaction(reaction);
			}
			newReaction.setReactionType(ReactionType.DISLIKE);
			newReaction.setPost(post);
			newReaction = this.reactionService.saveReaction(newReaction);
			post.getReactions().add(newReaction);
			post.setDislikes(post.getDislikes() + 1);
			this.repository.save(post);
		}
	}

	private void entityExistsCheck(Object object) {
		if (Objects.isNull(object)) {
			throw new ResourceNotFoundException("Post doesn't exist.");
		}
	}

	public Post save(Post post) {
		return this.repository.save(post);
	}

	public Post getById(Long id) {
		return this.repository.findOneById(id);
	}

	public CommentDto commentToPost(Long postId, CommentDto commentDto) {
		Post post = this.repository.findOneById(postId);
		entityExistsCheck(post);
		Date today = new Date();
		Comment comment = new Comment();
		comment.setDatePosted(today.getTime());
		comment.setUserId(commentDto.getUserId());
		comment.setContent(commentDto.getContent());
		comment = commentService.saveComment(comment);
		post.getComments().add(comment);
		this.repository.save(post);
		publishNotification(comment.getUserId(), post.getUserId(), today.getTime(), KafkaNotificationType.COMMENT);
		EventKafka event = new EventKafka(today,
				"User with id  " + comment.getUserId() + " added a comment on post with id " + postId + ".",
				EventType.ADDED_COMMENT);
		eventKafkaTemplate.send("dislinkt-events", event);
		return commentMapper.toDto(comment);
	}
}
