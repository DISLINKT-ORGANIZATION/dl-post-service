package disinkt.postservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;
    private Long userId;
    private String text;
    private long datePosted;
    private int dislikes;
    private int likes;
    private List<ImageDto> images;
    private List<CommentDto> comments;

}
