package com.example.akka_fast_food.services;

import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.HamburgerCondiments;
import com.example.akka_fast_food.models.HamburgerToppings;

import java.util.List;

public interface HamburgerService {
    Hamburger cookHamburger(Hamburger hamburger) throws InterruptedException;
    Hamburger addToppings(Hamburger hamburger, List<HamburgerToppings> toppings) throws InterruptedException;
    Hamburger addTopping(Hamburger hamburger, HamburgerToppings topping);
    HamburgerToppings prepareTopping(String topping) throws InterruptedException, IllegalArgumentException;
    Hamburger addCondiments(Hamburger hamburger, List<HamburgerCondiments> condiments) throws InterruptedException;
    Hamburger addCondiment(Hamburger hamburger, HamburgerCondiments condiment) throws InterruptedException;
}
