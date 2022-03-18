package ru.cib.makerobot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@ToString
@RequiredArgsConstructor
@Entity
@Schema(description = "Таблица информации на счете")
public class Account {
    @Id
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, precision = 12, scale = 2)
    @Schema (description = "Остаток на счете")
    private BigDecimal summa;
    @Column(nullable = false)
    @Schema (description = "Процент диверсификации")
    private Integer divprc;

    public void setSumma(BigDecimal summa) {
        this.summa = summa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
