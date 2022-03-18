package ru.cib.makerobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cib.makerobot.Opertype;
import ru.cib.makerobot.Response;
import ru.cib.makerobot.entity.Account;
import ru.cib.makerobot.entity.Listing;
import ru.cib.makerobot.entity.Operation;
import ru.cib.makerobot.repo.OperationRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperationService {

    @Autowired
    OperationRepo operationRepo;
    @Autowired
    AccountService accountService;
    @Autowired
    ListingService listingService;

    public List<Operation> findAll() {
        return operationRepo.findAll();
    }

    public Operation findOne(Long id) {
        return operationRepo.findById(id).orElse(null);
    }

    public Integer getyearopermax() {
        return operationRepo.getyearopermax();
    }

    // Сделка по покупке или продажи облигации
    public Response deal(Opertype type, Long id, Integer amount, Integer year) {
        if (year < listingService.getyearmin() || year > listingService.getyearmax()) {
            return new Response(-1, "Год в не интервала действия разрешения");
        }
        if (operationRepo.countByYearAndType(year, Opertype.PRC) > 0) {
            return new Response(-1, "Сделки в " + year + " уже закрыты");
        }
        Integer getyearopermax = operationRepo.getyearopermax();
        if (operationRepo.countByYearAndType(year-1, Opertype.PRC) >0) getyearopermax = year;
        if (getyearopermax == null) getyearopermax = listingService.getyearmin();

        if (!year.equals(getyearopermax)) return new Response(-1, "В " + year + " сделки запрещены");

        Listing listing = listingService.findOne(id);
        Account account = accountService.getAccount();
        if (listing == null) {
            return new Response(-1, "Облигации с id:" + id + " не существует");
        }
        double price;
        if (type == Opertype.RED) {
            // В год выкупа все облигации выкупаются по номиналу
            price = listing.getNominal().doubleValue();
            if (!listing.getYear().equals(year)) {
                return new Response(-1, "Срок выкупа данной облигации в " + year + " еще не настал");
            }
            amount = operationRepo.getByListingAndType(listing, Opertype.BUY).stream().mapToInt(Operation::getAmount).sum() -
                    operationRepo.getByListingAndType(listing, Opertype.SELL).stream().mapToInt(Operation::getAmount).sum();
            if (amount <= 0) {
                return new Response(-1, "Облигации с кодом " + listing.getId() + " нет в вашем портфеле.");
            }
        } else if (type == Opertype.SELL) {
            price = listing.getPrice().doubleValue();
            int ost = operationRepo.getByListingAndType(listing, Opertype.BUY).stream().mapToInt(Operation::getAmount).sum() -
                    operationRepo.getByListingAndType(listing, Opertype.SELL).stream().mapToInt(Operation::getAmount).sum();
            if (amount > ost) {
                return new Response(-1, "Облигаций с кодом " + listing.getId() + " в портфеле " + ost + " шт.");
            }

        } else {
            price = -listing.getPrice().doubleValue();
            if (listing.getYear() < year) {
                return new Response(-1, "Срок действия данной облигации истек");
            }
            if (account.getSumma().doubleValue() < -price * amount) {
                return new Response(-1, "Суммы на счете (" + account.getSumma() + ") недостаточно");
            }
        }

        Operation oper = new Operation(listing, type, amount, BigDecimal.valueOf(price * amount), year);
        operationRepo.save(oper);
        accountService.correctAcc(oper.getSumma());
        return new Response(1, "Сделка " + type + " по облигации " + listing.getId() + " совершена на сумму " + oper.getSumma());
    }

    public void delAllOperation() {
        operationRepo.deleteAll();
    }

    @Transactional
    public Response delOperation(Long id) {
        var operation = operationRepo.findById(id).orElse(null);
        if (operation != null) {
            operationRepo.deleteById(id);
            accountService.correctAcc(operation.getSumma().negate());
            return new Response(1, "Операция удалена. Сумма (" + operation.getSumma() + ") возвращена на счет");
        } else return new Response(-1, "Нет такой операции");
    }

    // Контроль закрытия года
    public List<Response> finishYear(Integer year) {
        List<Response> responses = new ArrayList<>();
        if (operationRepo.countByYearAndType(year, Opertype.PRC) > 0) {
            responses.add(new Response(-1, "Итоги за " + year + " год уже подведены"));
            return responses;
        }
        var operations = operationRepo.getByYearLessThanEqual(year).stream()
                .filter(operation -> (operation.getType() == Opertype.BUY || operation.getType() == Opertype.SELL || operation.getType() == Opertype.RED))
                .collect(Collectors.toList());
        if (operations.stream().noneMatch(operation -> operation.getYear().equals(year))) {
            responses.add(new Response(-1, "Отсутствуют операции за " + year + " год"));
            return responses;
        }
        // Проверка диверсификации
        var allBuys = operations.stream()
                .filter(operation -> operation.getListing().getYear() >= year)
                .collect(Collectors.groupingBy(Operation::getListing, Collectors.summingDouble(value -> value.getSumma().doubleValue())));
        var sum = allBuys.values().stream().mapToDouble(Double::doubleValue).sum();
        if (sum > 0) {
            responses.add(new Response(-1, "Отсутствуют облигации за " + year + " год"));
            return responses;
        }
        var divprc = accountService.getAccount().getDivprc();

        allBuys.forEach((listing, aDouble) -> {
            if (((aDouble / sum) * 100) > divprc) {
                responses.add(new Response(-1, "Диверсификация нарушена. Сумма облигаций с кодом " + listing.getId() +
                        "(" + -aDouble + ") превышает " + divprc + "% от стоимости портфеля облигаций (" + -sum + ")"));
            }
        });
        if (!responses.isEmpty()) return responses;

        // Расчитываем процентную доходность
        var collect = operations.stream().filter(operation -> operation.getListing().getYear() >= year)
                .collect(Collectors.groupingBy(Operation::getListing,
                        Collectors.summingDouble(value -> value.getListing().getNominal().doubleValue() * (value.getType() == Opertype.BUY ? value.getAmount() : -value.getAmount()) * value.getListing().getPrc().doubleValue() / 100)));
        var summa = collect.values().stream().mapToDouble(Double::doubleValue).sum();

        // Производится выкуп облигаций по номиналу
        var list = operations.stream().filter(operation -> operation.getListing().getYear().equals(year))
                .collect(Collectors.groupingBy(Operation::getListing, Collectors.summingInt(value -> (value.getType() == Opertype.BUY ? value.getAmount() : -value.getAmount()))));
        list.forEach((l, i) -> {
            if (i > 0) responses.add(deal(Opertype.RED, l.getId(), null, year));
        });

        Operation oper = new Operation(null, Opertype.PRC, 1, BigDecimal.valueOf(summa), year);
        operationRepo.save(oper);
        accountService.correctAcc(oper.getSumma());

        responses.add(new Response(1, "За " + year + " зачислено " + summa + "р."));
        return responses;
    }
}
