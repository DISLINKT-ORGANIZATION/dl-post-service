package disinkt.postservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostServiceTestController {

    @GetMapping("/version")
    public String version() {
        return "The actual version is 1.0.0";
    }

}
