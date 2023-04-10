package com.example.akka_fast_food.controllers;


import com.example.akka_fast_food.models.*;
import com.example.akka_fast_food.services.HamburgerService;
import com.example.akka_fast_food.services.PackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/hamburgers")
public class HamburgersController {

    @Autowired
    PackagingService packagingService;
    @Autowired
    HamburgerService hamburgerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Packaging orderHamburger(@RequestBody HamburgerOrder hamburgerOrder) {
        Hamburger hamburger = new Hamburger();
        ArrayList<HamburgerToppings> toppingsArrayList = new ArrayList<>();
        ArrayList<HamburgerCondiments> condimentsArrayList = new ArrayList<>();

        try {
            long orderStart = System.currentTimeMillis();
            hamburgerService.cookHamburger(hamburger);
            if(hamburgerOrder.toppings != null) {
                for(String topping : hamburgerOrder.toppings) {
                    HamburgerToppings hamburgerTopping = hamburgerService.prepareTopping(topping);
                    hamburgerService.addTopping(hamburger, hamburgerTopping);
                }
            }
             if(hamburgerOrder.condiments != null) {
                for(String condiment : hamburgerOrder.condiments) {
                    HamburgerCondiments hamburgerCondiment = HamburgerCondiments.valueOf(condiment);
                    hamburgerService.addCondiment(hamburger, hamburgerCondiment);
                }
            }
            return packagingService.packageFood(hamburger, orderStart, "");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
