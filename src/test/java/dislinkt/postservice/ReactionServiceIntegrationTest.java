package dislinkt.postservice;

import disinkt.postservice.PostServiceApplication;
import disinkt.postservice.entities.Comment;
import disinkt.postservice.entities.Post;
import disinkt.postservice.entities.Reaction;
import disinkt.postservice.entities.ReactionType;
import disinkt.postservice.service.ReactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PostServiceApplication.class)
public class ReactionServiceIntegrationTest {

    @Autowired
    private ReactionService reactionService;

    @Test
    void retrieveReaction_ReactionExist() {
        Post post = new Post();
        post.setId(1L);
        post.setUserId(4L);
        post.setDatePosted(1663239692000L);
        post.setLikes(2);
        post.setDislikes(0);
        post.setText("It would be great if we could save every stray cat and dog on this planet. And also in the upside down");
        post.setImage(null);
        Reaction reaction1 = new Reaction(1L, 5L, ReactionType.LIKE, post);
        Reaction reaction2 = new Reaction(1L, 9L, ReactionType.LIKE, post);
        List<Reaction> reactions = Arrays.asList(reaction1, reaction2);
        post.setReactions(reactions);
        Comment comment1 = new Comment(1L, 5L, "Very cute post", 1663241348579L);
        Comment comment2 = new Comment(2L, 5L, "I totally love this", 1663241370613L);
        List<Comment> comments = Arrays.asList(comment1, comment2);
        post.setComments(comments);

        Reaction reaction = reactionService.findReaction(post, 5L);
        assertNotNull(reaction);
    }

    @Test
    void retrieveReaction_ReactionNotExist() {
        Post post = new Post();
        post.setId(1L);
        post.setUserId(4L);
        post.setDatePosted(1663239692000L);
        post.setLikes(2);
        post.setDislikes(0);
        post.setText("It would be great if we could save every stray cat and dog on this planet. And also in the upside down");
        post.setImage(null);
        Reaction reaction1 = new Reaction(1L, 5L, ReactionType.LIKE, post);
        Reaction reaction2 = new Reaction(1L, 9L, ReactionType.LIKE, post);
        List<Reaction> reactions = Arrays.asList(reaction1, reaction2);
        post.setReactions(reactions);
        Comment comment1 = new Comment(1L, 5L, "Very cute post", 1663241348579L);
        Comment comment2 = new Comment(2L, 5L, "I totally love this", 1663241370613L);
        List<Comment> comments = Arrays.asList(comment1, comment2);
        post.setComments(comments);

        Reaction reaction = reactionService.findReaction(post, 100L);
        assertNull(reaction);
    }

    @Test
    void retrieveReactionByUserId_ReactionExist() {
        List<Reaction> reactions = reactionService.findReactionByUserId(5L);
        assertEquals(1, reactions.size());

    }

    @Test
    void retrieveReactionByUserId_ReactionNotExist() {
        List<Reaction> reactions = reactionService.findReactionByUserId(100L);
        assertEquals(0, reactions.size());
    }
}
