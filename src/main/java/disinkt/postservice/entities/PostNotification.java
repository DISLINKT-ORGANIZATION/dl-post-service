package disinkt.postservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostNotification {

    private long id;
    private long senderId;
    private long recipientId;
    private long timestamp;

}
