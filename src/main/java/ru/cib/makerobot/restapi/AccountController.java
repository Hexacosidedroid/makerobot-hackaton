package ru.cib.makerobot.restapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cib.makerobot.Response;
import ru.cib.makerobot.entity.Account;
import ru.cib.makerobot.service.AccountService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/account")
@ResponseStatus(HttpStatus.OK)
@Tag(name = "Счет", description = "Позволяет взаимодействовать со счетом")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Operation(
            summary = "Информация о счете",
            description = "Показывает состояние Вашего счета"
    )
    @GetMapping
    public Account getAccount() {
        return accountService.getAccount();
    }

    @Operation(
            summary = "Сброс счета",
            description = "Полностью удаляет все операции и сбрасывает счет в 0"
    )
    @PutMapping("/reset")
    public Response accountReset() {
        return accountService.resetAcc();
    }

    @Operation(
            summary = "Инициализация счета",
            description = "Пополняет счет. Пополнять счет можно только один раз."
    )
    @PutMapping
    public Response initAccount(@RequestParam(name = "summa") @Parameter(description = "Сумма пополнения") BigDecimal summa) {
        return accountService.initAcc(summa);
    }

}
