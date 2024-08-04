package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCodeAndMessage {


    NECESSARY_FIELDS_MISSING(2, "Следующие поля отсутствуют, имеют неправильные названия в теле запроса или имеют значение null: "),
    TRANSACTION_ID_INCORRECT_FORMAT(3, "Неверный формат Transaction ID (должен быть в формате long)"),
    INCORRECT_DATE_FORMAT(4, "Неверный формат даты. Дата должна быть в формате гггг-ММ-дд"),
    INTERNAL_SERVER_ERROR(500, "Внутренняя ошибка сервера"),
    JOURNAL_ENTRY_OK(0, "Операция успешно выполнена"),
    JOURNAL_ENTRY_ERROR(1),
    CLEARING_OK(0),
    CLEARING_VALIDATE_ERROR(1, "Произошла ошибка при валидации исходящего клиринга"),
    CLEARING_CREATE_ERROR(1, "Произошла ошибка при создании исходящего клиринга");

    private final int code;
    private String message;

    StatusCodeAndMessage(int code) {
        this.code = code;
    }
}