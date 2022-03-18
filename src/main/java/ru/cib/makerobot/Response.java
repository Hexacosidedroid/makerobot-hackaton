package ru.cib.makerobot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Формат ответа по каждому не GET вызова api")
public class Response {
    @Schema(description = "Возвращает -1 если происходит ошибка или 1 если действие выполнено")
    int code;
    @Schema(description = "Сообщение об ошибке или выполненном действии")
    String message;
}
