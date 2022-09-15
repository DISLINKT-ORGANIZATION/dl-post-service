package disinkt.postservice.mappers;

import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostDtoMapper {

    private final CommentDtoMapper commentMapper;
    private final ImageDtoMapper imageMapper;

    @Autowired
    public PostDtoMapper(CommentDtoMapper commentMapper, ImageDtoMapper imageMapper) {
        this.commentMapper = commentMapper;
        this.imageMapper = imageMapper;
    }

    public PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUserId(),
                post.getText(),
                post.getDatePosted(),
                post.getDislikes(),
                post.getLikes(),
                this.imageMapper.toCollectionDto(post.getImages()),
                this.commentMapper.toCollectionDto(post.getComments())
        );
    }

    public List<PostDto> toCollectionDto(List<Post> posts) {
        return posts.stream().map(this::toDto).collect(Collectors.toList());
    }
}
