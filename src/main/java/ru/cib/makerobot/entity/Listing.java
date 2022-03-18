package ru.cib.makerobot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "listing")

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema (description = "Таблица ценных бумаг")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listing_generator")
    @SequenceGenerator(name = "listing_generator", sequenceName = "listing_seq", allocationSize = 1)
    @Schema (description = "Идентификатор")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema (description = "Название")
    private String name;
    @Column(nullable = false, precision = 8, scale = 2)
    @Schema (description = "Стоимость по номиналу. По этой стоимости будет производиться выкуп облигации в конце ее действия")
    private BigDecimal nominal;
    @Column(nullable = false, precision = 8, scale = 2)
    @Schema (description = "Цена облигации при покупке или продаже. Мы предполагаем, что стоимость облигации не меняется со временем.")
    private BigDecimal price;
    @Column(nullable = false, precision = 8, scale = 2)
    @Schema (description = "Годовая процентная ставка")
    private BigDecimal prc;
    @Column(nullable = false)
    @Schema (description = "Год, в который произойдет погашение облигации (выкуп ее по номиналу)")
    private Integer year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Listing listing = (Listing) o;
        return id != null && Objects.equals(id, listing.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
