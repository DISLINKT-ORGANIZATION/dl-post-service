package dislinkt.postservice;

import disinkt.postservice.PostServiceApplication;
import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = PostServiceApplication.class)
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Test
    public void retrievePost_PostsExist() {
        List<Long> ids = Arrays.asList(4L, 5L, 9L);
        UserIds userIds = new UserIds(ids);
        List<PostDto> posts = postService.retrievePosts(userIds);
        assertEquals(posts.size(), 1);
    }

    @Test
    public void retrievePost_PostsNotExist() {
        List<Long> ids = Arrays.asList(11L, 12L, 13L);
        UserIds userIds = new UserIds(ids);
        List<PostDto> posts = postService.retrievePosts(userIds);
        assertEquals(posts.size(), 0);
    }


}
