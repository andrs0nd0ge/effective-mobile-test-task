package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.Comment;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    private String comment;

    public static CommentDto from(Comment comment) {
        return builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .build();
    }
}
