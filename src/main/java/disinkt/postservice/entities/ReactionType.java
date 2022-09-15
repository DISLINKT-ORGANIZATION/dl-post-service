package disinkt.postservice.entities;

import java.util.HashMap;
import java.util.Map;

public enum ReactionType {

    LIKE(0),
    DISLIKE(1);

    private int value;
    private static Map map = new HashMap<>();

    ReactionType(int value) {
        this.value = value;
    }

    static {
        for (ReactionType skillType : ReactionType.values()) {
            map.put(skillType.value, skillType);
        }
    }

    public static ReactionType valueOfInt(int skillType) {
        return (ReactionType) map.get(skillType);
    }

    public int getValue() {
        return value;
    }

}
