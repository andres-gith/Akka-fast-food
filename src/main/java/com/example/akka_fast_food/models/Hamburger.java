package com.example.akka_fast_food.models;

import java.util.ArrayList;

public class Hamburger implements Food {

    private HamburgerMeatStates _meatState;
    private ArrayList<HamburgerCondiments> _condiments;
    private ArrayList<HamburgerToppings> _toppings;

    public Hamburger() {
        _meatState = HamburgerMeatStates.RAW;
        _toppings = new ArrayList();
        _condiments = new ArrayList();
    }

    public void cookHamburger() {
        _meatState = HamburgerMeatStates.COOKED;
    }

    public void addCondiment(HamburgerCondiments condiment) {
        _condiments.add(condiment);
    }

    public void addTopping(HamburgerToppings topping) {
        _toppings.add(topping);
    }

    public HamburgerMeatStates getMeatState() {
        return _meatState;
    }

    public ArrayList<HamburgerCondiments> getCondiments() {
        return _condiments;
    }

    public ArrayList<HamburgerToppings> getToppings() {
        return _toppings;
    }
}

