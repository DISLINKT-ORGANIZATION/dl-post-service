package disinkt.postservice.controller;

import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<PostDto>> retrievePosts(@RequestBody UserIds userIds) {
        List<PostDto> dto = this.postService.retrievePosts(userIds);
        return ResponseEntity.ok(dto);
    }


}
