package dislinkt.postservice;

import disinkt.postservice.PostServiceApplication;
import disinkt.postservice.entities.Comment;
import disinkt.postservice.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = PostServiceApplication.class)
public class CommentServiceIntegrationTest {
    @Autowired
    CommentService service;


    @Test
    void saveComment_CommentSuccess() {
        Comment comment = new Comment();
        comment.setUserId(1L);
        Date now = new Date();
        comment.setDatePosted(now.getTime());
        comment.setContent("Test");
        Comment savedComment = service.saveComment(comment);
        assertNotNull(savedComment);
        assertNotEquals(0L, savedComment.getId());
    }
}
