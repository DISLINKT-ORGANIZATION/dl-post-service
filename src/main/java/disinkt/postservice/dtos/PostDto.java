package disinkt.postservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private List<ImageDto> images = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();

}
