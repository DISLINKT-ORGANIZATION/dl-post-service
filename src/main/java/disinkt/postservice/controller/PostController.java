package disinkt.postservice.controller;

import disinkt.postservice.dtos.CommentDto;
import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.ReactionDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<PostDto>> retrievePosts(@RequestBody UserIds userIds) {
        List<PostDto> dto = this.postService.retrievePosts(userIds);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> createPost(@RequestPart(value = "body") PostDto dto, @RequestPart(value = "picture") MultipartFile picture) throws IOException {
        dto.setImage(picture.getBytes());
        postService.createPost(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/reaction")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> reactToPost(@PathVariable Long postId, @RequestBody ReactionDto reactionDto) {
        postService.reactToPost(postId, reactionDto, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comment")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> commentToPost(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        CommentDto dto = postService.commentToPost(postId, commentDto);
        return ResponseEntity.ok(dto);
    }

}
