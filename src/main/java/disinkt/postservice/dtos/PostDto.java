package disinkt.postservice.dtos;


import lombok.*;

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
    private byte[] image;
    private List<CommentDto> comments = new ArrayList<>();
    private boolean userLikesPost;
    private boolean userDislikesPost;

    public PostDto(Long id, Long userId, String text, Long datePosted,
                   int dislikes, int likes, byte[] image, List<CommentDto> comments) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.datePosted = datePosted;
        this.dislikes = dislikes;
        this.likes = likes;
        this.image = image;
        this.comments = comments;
    }

}
