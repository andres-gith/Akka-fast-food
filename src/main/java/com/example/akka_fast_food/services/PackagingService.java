package com.example.akka_fast_food.services;

import com.example.akka_fast_food.models.Food;
import com.example.akka_fast_food.models.Packaging;

public interface PackagingService {
    Packaging packageFood(Food food, long orderStart, String notes) throws InterruptedException;
}
