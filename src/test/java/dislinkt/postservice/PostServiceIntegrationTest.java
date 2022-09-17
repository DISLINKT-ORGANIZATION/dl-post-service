package dislinkt.postservice;

import disinkt.postservice.PostServiceApplication;
import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.ReactionDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.entities.Post;
import disinkt.postservice.entities.Reaction;
import disinkt.postservice.service.PostService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PostServiceApplication.class)
class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Test
    void retrievePost_PostsExist() {
        List<Long> ids = Arrays.asList(4L, 5L, 9L);
        UserIds userIds = new UserIds(ids, 4L);
        List<PostDto> posts = postService.retrievePosts(userIds);
        assertEquals(1, posts.size());
    }

    @Test
    void retrievePost_PostsNotExist() {
        List<Long> ids = Arrays.asList(11L, 12L, 13L);
        UserIds userIds = new UserIds(ids, 4L);
        List<PostDto> posts = postService.retrievePosts(userIds);
        assertEquals(0,posts.size());
    }

    @Test
    void reactToPost_DislikePost() {
        Long postId = 1L;
        ReactionDto dto = new ReactionDto();
        dto.setReaction(1);
        dto.setRemoveReaction(false);
        dto.setUserId(4L); // max
        postService.reactToPost(postId, dto, false);

        Post post = postService.getById(postId);
        assertEquals(1, post.getDislikes());

        List<Reaction> newReactions = post.getReactions().stream().filter(el -> el.getUserId() != 4L).collect(Collectors.toList());
        post.setReactions(newReactions);
        postService.save(post);
        dto.setRemoveReaction(true);
        postService.reactToPost(postId, dto, false);
        post = postService.getById(postId);
        assertEquals(0, post.getDislikes());
    }

    @Test
    void reactToPost_LikePost() {
        Long postId = 1L;
        ReactionDto dto = new ReactionDto();
        dto.setReaction(0);
        dto.setRemoveReaction(false);
        dto.setUserId(4L); // max
        postService.reactToPost(postId, dto, false);

        Post post = postService.getById(postId);
        assertEquals(3, post.getLikes());

        List<Reaction> newReactions = post.getReactions().stream().filter(el -> el.getUserId() != 4L).collect(Collectors.toList());
        post.setReactions(newReactions);
        postService.save(post);
        dto.setRemoveReaction(true);
        postService.reactToPost(postId, dto, false);
        post = postService.getById(postId);
        assertEquals(2, post.getLikes());
    }


    @Test
    void getPostById_PostExist() {
        Post post = this.postService.getById(1L);
        assertNotNull(post);
        assertEquals(1L, post.getId());
    }

    @Test
    void getPostById_PostNotExist() {
        Post post = this.postService.getById(5L);
        assertNull(post);
    }


}
