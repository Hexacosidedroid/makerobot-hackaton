package ru.cib.makerobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cib.makerobot.entity.Listing;
import ru.cib.makerobot.repo.ListingRepo;

import java.util.List;

@Slf4j
@Service
public class ListingService {
    @Autowired
    ListingRepo listingRepo;

    public List<Listing> findAll() {
        return listingRepo.findAll();
    }

    public Listing findOne(Long id) {
        return listingRepo.findById(id).orElse(null);
    }

    public Integer getyearmin() {
        return listingRepo.getyearmin();
    }
    public Integer getyearmax() {
        return listingRepo.getyearmax();
    }
}
