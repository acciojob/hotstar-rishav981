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
            throw new IllegalArgumentException("Production House not found");
        }
        ProductionHouse productionHouse = oproductionHouse.get();
        WebSeries newWebseries = new WebSeries();

        newWebseries.setSeriesName(webSeriesEntryDto.getSeriesName());
        newWebseries.setRating(webSeriesEntryDto.getRating());
        newWebseries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        newWebseries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        newWebseries.setProductionHouse(productionHouse);
        WebSeries savedWebSeries = webSeriesRepository.save(newWebseries);


        productionHouse.getWebSeriesList().add(newWebseries);

        double newRating = (webSeriesEntryDto.getRating()+productionHouse.getRatings())/(productionHouse.getWebSeriesList().size());
        productionHouse.setRatings(newRating);
        productionHouseRepository.save(productionHouse);

        return savedWebSeries.getId();
    }

}

//package com.driver.services;
//
//import com.driver.EntryDto.WebSeriesEntryDto;
//import com.driver.model.ProductionHouse;
//import com.driver.model.User;
//import com.driver.model.WebSeries;
//import com.driver.repository.ProductionHouseRepository;
//import com.driver.repository.UserRepository;
//import com.driver.repository.WebSeriesRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class WebSeriesService {
//
//    @Autowired
//    public WebSeriesRepository webSeriesRepository;
//
//    @Autowired
//    public ProductionHouseRepository productionHouseRepository;
//
//    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{
//
//        //Add a webSeries to the database and update the ratings of the productionHouse
//        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
//        //use function written in Repository Layer for the same
//        //Dont forget to save the production and webseries Repo
//        WebSeries existingWebSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
//        if (existingWebSeries != null) {
//            throw new Exception("Series is already present");
//        }
//        WebSeries webSeries = new WebSeries();
//        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
//        webSeries.setRating(webSeriesEntryDto.getRating());
//        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
//        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
//
//        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId())
//                .orElseThrow(() -> new IllegalArgumentException("Production House not found"));
//
//        webSeries.setProductionHouse(productionHouse);
//
//        // Save the WebSeries entity to the database
//        WebSeries savedWebSeries = webSeriesRepository.save(webSeries);
//
//        // Update the ratings of the associated production house
//        updateProductionHouseRatings(productionHouse , savedWebSeries);
//
//        // Return the ID of the saved web series
//        return savedWebSeries.getId();
//
//    }
//
//    private void updateProductionHouseRatings(ProductionHouse productionHouse, WebSeries newWebSeries) {
//        // Get the existing web series list from the production house
//        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();
//
//        // Calculate the new average rating
//        double totalRatings = webSeriesList.stream().mapToDouble(WebSeries::getRating).sum();
//        int numberOfWebSeries = webSeriesList.size();
//
//        // Add the new web series to the list
//        webSeriesList.add(newWebSeries);
//
//        // Update the production house's average rating and number of web series
//        double newAverageRating = (totalRatings + newWebSeries.getRating()) / (numberOfWebSeries + 1);
//        productionHouse.setRatings(newAverageRating);
//
//        // Save the updated production house to the database (assuming you save changes to the production house)
//        productionHouseRepository.save(productionHouse);
//    }
//
//
//}
