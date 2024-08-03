package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponseDto extends ResponseDto {
    @JsonProperty("exception_message")
    private String exceptionMessage;
}
