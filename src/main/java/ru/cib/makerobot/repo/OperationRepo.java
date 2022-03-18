package ru.cib.makerobot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cib.makerobot.Opertype;
import ru.cib.makerobot.entity.Listing;
import ru.cib.makerobot.entity.Operation;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long> {
    List<Operation> getByListingAndType(Listing listing, Opertype type);

    Integer countByYearAndType(Integer year, Opertype type);

    List<Operation> getByYearLessThanEqual(Integer year);

    @Query("select max(year) from Operation")
    Integer getyearopermax();
}
