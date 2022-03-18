package ru.cib.makerobot.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portf_generator")
    @SequenceGenerator(name = "portf_generator", sequenceName = "portf_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    private Integer amount;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal avgprice;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal summa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Portfolio portfolio = (Portfolio) o;
        return id != null && Objects.equals(id, portfolio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
