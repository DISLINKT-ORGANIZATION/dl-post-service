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


}
