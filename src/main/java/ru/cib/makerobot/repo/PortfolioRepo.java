package ru.cib.makerobot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cib.makerobot.entity.Portfolio;

public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {
}
