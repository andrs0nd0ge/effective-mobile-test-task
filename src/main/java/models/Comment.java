package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private long id;
    private String comment;
    private long taskId;
}
