package com.example.akka_fast_food.services;

import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.HamburgerCondiments;
import com.example.akka_fast_food.models.HamburgerToppings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HamburgerServiceImpl implements HamburgerService{
    @Override
    public Hamburger cookHamburger(Hamburger hamburger) throws InterruptedException {
        //Thread.sleep(20);
        TimeUnit.MILLISECONDS.sleep(5);
        hamburger.cookHamburger();
        return hamburger;
    }

    @Override
    public Hamburger addToppings(Hamburger hamburger, List<HamburgerToppings> toppings) throws InterruptedException {
        //Thread.sleep(1000);
        TimeUnit.MILLISECONDS.sleep(1000);
        toppings.forEach(hamburger::addTopping);
        return hamburger;
    }

    @Override
    public Hamburger addTopping(Hamburger hamburger, HamburgerToppings topping) {
        hamburger.addTopping(topping);
        return hamburger;
    }

    @Override
    public HamburgerToppings prepareTopping(String topping) throws InterruptedException, IllegalArgumentException {
        //Thread.sleep(5);
        TimeUnit.MILLISECONDS.sleep(200);
        return HamburgerToppings.valueOf(topping);
    }

    @Override
    public Hamburger addCondiments(Hamburger hamburger, List<HamburgerCondiments> condiments) throws InterruptedException {
        //Thread.sleep(1000);
        TimeUnit.MILLISECONDS.sleep(1000);
        condiments.forEach(hamburger::addCondiment);
        return hamburger;
    }

    @Override
    public Hamburger addCondiment(Hamburger hamburger, HamburgerCondiments condiment) throws InterruptedException {
        //Thread.sleep(5);
        TimeUnit.MILLISECONDS.sleep(200);
        hamburger.addCondiment(condiment);
        return hamburger;
    }
}
