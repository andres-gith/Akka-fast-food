package com.example.akka_fast_food.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Packaging {
    Food _food;
    Long _orderStart;
    Long _orderEnd;

    String _notes;

    public Packaging(Food food, long orderStart, long orderEnd, String notes){
        _food = food;
        _orderStart = orderStart;
        _orderEnd = orderEnd;
        _notes = notes;
    }

    public Food getFood() {
        return _food;
    }

    public Date getOrderStart() {
        return new Date(_orderStart);
    }

    public Date getOrderEnd() {
        return new Date(_orderEnd);
    }

    public Long getOrderDurationInMilliseconds() {
        return (_orderEnd - _orderStart);
    }

    public String getNotes() {
        return _notes;
    }
}
