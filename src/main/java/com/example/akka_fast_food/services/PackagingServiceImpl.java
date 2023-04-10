package com.example.akka_fast_food.services;

import com.example.akka_fast_food.models.Food;
import com.example.akka_fast_food.models.Packaging;
import org.springframework.stereotype.Service;

@Service
public class PackagingServiceImpl implements PackagingService {

    @Override
    public Packaging packageFood(Food food, long orderStart, String notes) throws InterruptedException {
        Thread.sleep(10);
        long orderEnd = System.currentTimeMillis();
        return new Packaging(food, orderStart, orderEnd, notes);
    }
}
