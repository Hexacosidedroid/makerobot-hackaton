package ru.cib.makerobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cib.makerobot.Response;
import ru.cib.makerobot.entity.Account;
import ru.cib.makerobot.repo.AccountRepo;

import java.math.BigDecimal;

@Slf4j
@Service
public class AccountService {

    @Autowired
    AccountRepo accountRepo;
    @Autowired
    OperationService operationService;


    public Account getAccount() {
        return accountRepo.findById(1L).orElse(null);
    }

    // Инициализация счета
    public Response initAcc(BigDecimal summa) {
        Account account = accountRepo.findById(1L).orElse(null);
        if (account == null) {
            log.error("Can`t find the Account");
            return null;
        }
        if (account.getSumma().doubleValue() != 0) {
            return new Response(-1,"Счет уже инициализирован. Продолжайте работу, либо сбросьте счет");
        }
        account.setSumma(summa);
        accountRepo.save(account);
        return new Response(1,"Счет инициализирован на сумму " + summa.toString());
    }

    // Сброс счета
    public Response resetAcc() {
        Account account = accountRepo.findById(1L).orElse(null);
        if (account == null) {
            log.error("Can`t find the Account");
            return null;
        }
        account.setSumma(BigDecimal.ZERO);
        accountRepo.save(account);
        operationService.delAllOperation();
        return new Response(1,"Счет сброшен.");
    }

    public void correctAcc(BigDecimal summa) {
        Account account = accountRepo.findById(1L).orElse(null);
        if (account == null) {
            log.error("Can`t find the Account");
            return;
        }
        account.setSumma(account.getSumma().add(summa));
        accountRepo.save(account);
    }
}
