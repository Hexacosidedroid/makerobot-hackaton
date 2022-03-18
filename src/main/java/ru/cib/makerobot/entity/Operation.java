package ru.cib.makerobot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonKey;
import lombok.*;
import org.hibernate.Hibernate;
import ru.cib.makerobot.Opertype;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oper_generator")
    @SequenceGenerator(name = "oper_generator", sequenceName = "oper_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    private Opertype type;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal summa;
    @Column(nullable = false)
    private Integer year;

    public Operation(Listing listing, Opertype type, Integer amount, BigDecimal summa, Integer year) {
        this.listing = listing;
        this.type = type;
        this.amount = amount;
        this.summa = summa;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Operation operation = (Operation) o;
        return id != null && Objects.equals(id, operation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
