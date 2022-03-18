package ru.cib.makerobot.restapi;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cib.makerobot.Opertype;
import ru.cib.makerobot.Response;
import ru.cib.makerobot.entity.Operation;
import ru.cib.makerobot.service.OperationService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/v1/oper")
@ResponseStatus(HttpStatus.OK)
@Tag(name = "Операции", description = "api для проведения операций с облигациями")
class OperationController {

    @Autowired
    OperationService operationService;

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Полный список операций",
            description = "Показывает весь список операций"
    )
    @GetMapping("/getall")
    public List<Operation> getAll() {
        return operationService.findAll();
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Информация по операции",
            description = "Показывает всю информацию по операции"
    )
    @GetMapping("/get")
    public Operation getone(@RequestParam(name = "id") @Parameter (description = "id операции") Long id) {
        return operationService.findOne(id);
    }
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Удаление операции",
            description = "Удаляет операцию, при этом сумма удаленной операции будет возвращена/списана со счета"
    )
    @DeleteMapping("/del")
    public Response delOper(@RequestParam(name = "id") @Parameter (description = "id операции") Long id) {
        return operationService.delOperation(id);
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Сделка",
            description = "Проведение покупок. Покупки необходимо проводить с помощью этого api"
    )
    @PutMapping("/deal")
    public Response deal(@RequestParam(name = "type") @Parameter (description = "Тип сделки") Opertype type,
                         @RequestParam(name = "id") @Parameter (description = "id облигации, которая будет приобретена") Long id,
                         @RequestParam(name = "amount") @Parameter (description = "Количество") Integer amount,
                         @RequestParam(name = "year") @Min(2022) @Max(2025) @Parameter (description = "Год, в который будет произведена сделка")Integer year) {

        return operationService.deal(type, id, amount, year);
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Результат по итогам года",
            description = "Необходимо запускать после формирования сделок за каждый год, начиная с 2021г до 2025г." +
                    " Сформировали сделки за 2021г, после этого /finish, и так каждый год до 2025г."
    )
    @PutMapping("/finish")
    public List<Response> deal(@RequestParam(name = "year") @Min(2022) @Max(2025) @Parameter(description = "Год") Integer year) {
        return operationService.finishYear(year);
    }
}
