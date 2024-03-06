package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Don't forget to save the production and webseries Repo

        WebSeries pres = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(pres!=null){
            throw new Exception("Series is already present");
        }
        Optional<ProductionHouse> oproductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if(!oproductionHouse.isPresent()){
            return -1;
        }
        ProductionHouse productionHouse = oproductionHouse.get();
        WebSeries newWebseries = new WebSeries();

        newWebseries.setSeriesName(webSeriesEntryDto.getSeriesName());
        newWebseries.setRating(webSeriesEntryDto.getRating());
        newWebseries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        newWebseries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        newWebseries.setProductionHouse(productionHouse);
        Double newRating = (webSeriesEntryDto.getRating()+productionHouse.getRatings())/(productionHouse.getWebSeriesList().size()+1);
        productionHouse.setRatings(newRating);
        productionHouse.getWebSeriesList().add(newWebseries);
        productionHouseRepository.save(productionHouse);
        webSeriesRepository.save(newWebseries);

        return newWebseries.getId();
    }

}
