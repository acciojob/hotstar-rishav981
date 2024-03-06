package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){
        ProductionHouse newProductionhouse = new ProductionHouse();
        newProductionhouse.setName(productionHouseEntryDto.getName());
        newProductionhouse.setRatings(0);
        return  productionHouseRepository.save(newProductionhouse).getId();
    }



}
