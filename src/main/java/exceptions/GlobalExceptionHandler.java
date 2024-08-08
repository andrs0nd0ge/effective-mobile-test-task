package exceptions;

import dto.ResponseDto;
import enums.StatusCodeAndMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ResponseDto> getExceptionResponse(StatusCodeAndMessage codeAndMessage) {
        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(codeAndMessage.getStatus());
        responseDto.setMessage(codeAndMessage.getMessage());

        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(codeAndMessage.getStatus()));
    }
    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;

        while (cause.getCause() != null && cause != cause.getCause()) {
            cause = cause.getCause();
        }

        return cause;
    }

    @ExceptionHandler({RuntimeException.class, UnhandledException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseDto> handleUnhandled(Exception ex) {
        ResponseDto responseDto = new ResponseDto();

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        Throwable cause = getRootCause(ex);

        int status = httpStatus.value();

        String exceptionCause = cause != null ? cause.getLocalizedMessage() : ex.getLocalizedMessage();

        responseDto.setStatus(status);
        responseDto.setMessage(exceptionCause);

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleExceptions() {
        StatusCodeAndMessage codeAndMessage = StatusCodeAndMessage.FORBIDDEN;

        return getExceptionResponse(codeAndMessage);
    }

    @ExceptionHandler(TaskIdNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleTaskIdNull() {
        StatusCodeAndMessage codeAndMessage = StatusCodeAndMessage.TASK_ID_NULL;

        return getExceptionResponse(codeAndMessage);
    }

    @ExceptionHandler(UserIdNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleUserIdNull() {
        StatusCodeAndMessage codeAndMessage = StatusCodeAndMessage.USER_ID_NULL;

        return getExceptionResponse(codeAndMessage);
    }

    @ExceptionHandler(TasksNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ResponseDto> handleTasksNotFound() {
        StatusCodeAndMessage codeAndMessage = StatusCodeAndMessage.TASKS_NOT_FOUND;

        return getExceptionResponse(codeAndMessage);
    }
}
