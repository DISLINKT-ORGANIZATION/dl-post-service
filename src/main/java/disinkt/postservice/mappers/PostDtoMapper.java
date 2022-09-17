package disinkt.postservice.mappers;

import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.entities.Post;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostDtoMapper {

    private final CommentDtoMapper commentMapper;

    @Autowired
    public PostDtoMapper(CommentDtoMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUserId(),
                post.getText(),
                post.getDatePosted(),
                post.getDislikes(),
                post.getLikes(),
                post.getImage(),
                this.commentMapper.toCollectionDto(post.getComments())
        );
    }

    public List<PostDto> toCollectionDto(List<Post> posts) {
        return posts.stream().map(this::toDto).collect(Collectors.toList());
    }
}
