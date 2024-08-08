package models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Task {
    private long id;
    private String header;
    private String description;
    private String status;
    private String priority;
    private String author;
    private String executor;
    private List<Comment> comments = new ArrayList<>();
}
