package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        return userRepository.save(user).getId();

        //Jut simply add the user to the Db and return the userId returned by the repository
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){
        List<WebSeries> w = webSeriesRepository.findAll();
        int cnt=0;
        User u = userRepository.findById(userId).get();
        for(WebSeries it : w){
            if(it.getAgeLimit()<=u.getAge()){
                if((it.getSubscriptionType()).equals(SubscriptionType.BASIC)){
                    cnt++;
                }
                else if((it.getSubscriptionType()).equals(SubscriptionType.PRO)){
                    if(!u.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)) {
                        cnt++;
                    }
                }
                else{
                    if(!u.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)) {
                        cnt++;
                    }
                }
            }
        }

        return cnt;
        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

    }


}
