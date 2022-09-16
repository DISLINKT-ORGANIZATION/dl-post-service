package dislinkt.postservice;

import disinkt.postservice.PostServiceApplication;
import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.entities.*;
import disinkt.postservice.repositories.PostRepository;
import disinkt.postservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = PostServiceApplication.class)
public class PostServiceUnitTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @Test
    public void get_UserIdsExist_PostReturned() {
        List<Long> ids = Arrays.asList(4L, 5L, 9L);
        UserIds userIds = new UserIds(ids);
        List<Image> images = new ArrayList<>();
        Reaction reaction1 = new Reaction(1L, 5L, ReactionType.LIKE);
        Reaction reaction2 = new Reaction(1L, 9L, ReactionType.LIKE);
        List<Reaction> reactions = Arrays.asList(reaction1, reaction2);
        Comment comment1 = new Comment(1L, 5L, "Very cute post", 1663241348579L);
        Comment comment2 = new Comment(2L, 5L, "I totally love this", 1663241370613L);
        List<Comment> comments = Arrays.asList(comment1, comment2);
        Post post = new Post(
            1L, 4L,
            "It would be great if we could save every stray cat and dog on this planet. And also in the upside down",
            1663239692000L, 2, 0,
            images, comments, reactions
        );
        List<Post> posts = List.of(post);
        given(postRepository.findAllByUserIdIn(ids)).willReturn(posts);
        List<PostDto> postDtos = postService.retrievePosts(userIds);
        assertEquals(1, posts.size());
        assertEquals(1, postDtos.size());
    }

    @Test
    public void get_UserIdsNotExist_PostNotReturned() {
        List<Long> ids = Arrays.asList(105L, 106L, 107L);
        List<Post> posts = new ArrayList<>();
        given(postRepository.findAllByUserIdIn(ids)).willReturn(posts);
        assertEquals(0, posts.size());
    }

}
