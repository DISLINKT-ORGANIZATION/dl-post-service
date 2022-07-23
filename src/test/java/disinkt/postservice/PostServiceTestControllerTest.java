package disinkt.postservice;

import disinkt.postservice.PostServiceTestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostServiceTestControllerTest {

    @Autowired
    PostServiceTestController postServiceTestController;

    @Test
    void version() {
        assertEquals( "The actual version is 1.0.0", postServiceTestController.version());
    }

}
