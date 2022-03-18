package ru.cib.makerobot.repo;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.cib.makerobot.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Long> {
}
