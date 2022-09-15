package disinkt.postservice.mappers;

import disinkt.postservice.dtos.CommentDto;
import disinkt.postservice.entities.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentDtoMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getDatePosted()
        );
    }

    public List<CommentDto> toCollectionDto(List<Comment> comments) {
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }

}
