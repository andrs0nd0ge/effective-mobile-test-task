package enums;

import lombok.Getter;

@Getter
public enum StatusCodeAndMessage {
    FORBIDDEN(403, "Access denied"),
    TASK_ID_NULL(400, "Task ID cannot be null or empty"),
    USER_ID_NULL(400, "User ID cannot be null or empty"),
    TASKS_NOT_FOUND(204, "Tasks were not found");
    private final int status;
    private final String message;

    StatusCodeAndMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }
}