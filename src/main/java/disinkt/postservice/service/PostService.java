package disinkt.postservice.service;

import disinkt.postservice.dtos.CommentDto;
import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.ReactionDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.entities.Comment;
import disinkt.postservice.entities.Post;
import disinkt.postservice.entities.Reaction;
import disinkt.postservice.entities.ReactionType;
import disinkt.postservice.exception.ResourceNotFoundException;
import disinkt.postservice.mappers.CommentDtoMapper;
import disinkt.postservice.mappers.PostDtoMapper;
import disinkt.postservice.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PostService(PostRepository repository, PostDtoMapper mapper, ReactionService reactionService, CommentService commentService, CommentDtoMapper commentMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.reactionService = reactionService;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    public List<PostDto> retrievePosts(UserIds userIds) {
        List<Post> posts = repository.findAllByUserIdInOrderByDatePostedDesc(userIds.getIds());
        List<Reaction> usersReactions = this.reactionService.findReactionByUserId(userIds.getLoggedInUserId());
        List<PostDto> dtoPosts = new ArrayList<>();
        for (Post post: posts) {
            PostDto dto = mapper.toDto(post);
            for (Reaction reaction: usersReactions) {
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
        this.repository.save(post);
    }

    public void reactToPost(Long postId, ReactionDto reactionDto) {
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
        return commentMapper.toDto(comment);
    }
}
