package disinkt.postservice.kafka;

import java.io.Serializable;

public enum KafkaNotificationType implements Serializable {
    NEW_POST,
    LIKE,
    COMMENT
}
