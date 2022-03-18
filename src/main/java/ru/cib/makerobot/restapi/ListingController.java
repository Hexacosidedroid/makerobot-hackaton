package ru.cib.makerobot.restapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cib.makerobot.entity.Listing;
import ru.cib.makerobot.service.ListingService;

import java.util.List;

@RestController
@RequestMapping("/v1/listing")
@ResponseStatus(HttpStatus.OK)
@Tag(name = "Список ценных бумаг", description = "Справочник ценных бумаг(облигаций) для покупки")
class ListingController {

    @Autowired
    ListingService listingService;

    @Operation(
            summary = "Полный список облигаций",
            description = "Показывает весь список облигаций"
    )
   @GetMapping("/getall")
    public List<Listing> findAll () {
       return listingService.findAll();
   }

    @Operation(
            summary = "Получить информацию по облигации",
            description = "Показывает информацию по конкретной облигации"
    )
   @GetMapping("/get")
    public Listing getOne (@RequestParam (name = "id") @Parameter (description = "id облигации") Long id) {
       return listingService.findOne(id);
   }
}
