package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){
        Subscription newSub = new Subscription();
        newSub.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        newSub.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        newSub.setStartSubscriptionDate(new Date());

//        For Basic Plan: 500 + 200 * noOfScreensSubscribed
//        For PRO Plan: 800 + 250 * noOfScreensSubscribed
//        For ELITE Plan: 1000 + 350 * noOfScreensSubscribed
        int cost =0;
        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.BASIC)){
            cost = 500+200*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.PRO)){
            cost = 800+250*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else{
            cost = 1000+350*subscriptionEntryDto.getNoOfScreensRequired();
        }
        newSub.setTotalAmountPaid(cost);
        User u = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        newSub.setUser(u);
        u.setSubscription(newSub);
        userRepository.save(u);
        return cost;
        //Save The subscription Object into the Db and return the total Amount that user has to pay
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{
        User u = userRepository.findById(userId).get();
        if(u.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        else if(u.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)){
            u.getSubscription().setSubscriptionType(SubscriptionType.PRO);
            int nofs = u.getSubscription().getNoOfScreensSubscribed();
            int diff = 300+50*(nofs);
            u.getSubscription().setTotalAmountPaid(800+250*(nofs));
            userRepository.save(u);
            return diff;
        }
        else{
            u.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
            int nofs = u.getSubscription().getNoOfScreensSubscribed();
            int diff = 200+100*(nofs);
            u.getSubscription().setTotalAmountPaid(1000+350*(nofs));
            userRepository.save(u);
            return diff;
        }
        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repositor
    }

    public Integer calculateTotalRevenueOfHotstar(){
        List<Subscription> s = subscriptionRepository.findAll();
        int total =0;
        for (Subscription it : s){
            total+=it.getTotalAmountPaid();
        }
        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        return total;
    }

}
