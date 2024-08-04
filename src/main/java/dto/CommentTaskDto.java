package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentTaskDto {
    @JsonProperty("task_id")
    private Long taskId;
    private String comment;
}
