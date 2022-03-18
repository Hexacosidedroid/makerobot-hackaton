package ru.cib.makerobot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cib.makerobot.entity.Listing;

public interface ListingRepo extends JpaRepository<Listing, Long> {

    @Query("select min(year) from Listing ")
    Integer getyearmin();

    @Query("select max(year) from Listing ")
    Integer getyearmax();
}
