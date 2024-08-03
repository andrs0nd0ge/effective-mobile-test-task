package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private long id;
    private String header;
    private String description;
    private long statusId;
    private long priorityId;
    private long authorId;
    private long executorId;
}
