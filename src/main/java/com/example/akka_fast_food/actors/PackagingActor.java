package com.example.akka_fast_food.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.example.akka_fast_food.models.Food;
import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.Packaging;
import com.example.akka_fast_food.services.PackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component("packagingActor")
@Scope("prototype")
public class PackagingActor extends UntypedAbstractActor {
    @Autowired
    PackagingService packagingService;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PackageHamburgerMessage packageHamburgerMessage) {
            Packaging packaging = packagingService.packageFood(packageHamburgerMessage.food, packageHamburgerMessage.orderStart, packageHamburgerMessage.notes);
            getSender().tell(packaging, getSelf());
            /*ActorRef sender = getSender();
            CompletableFuture<Packaging> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return packagingService.packageFood(packageHamburgerMessage.food, packageHamburgerMessage.orderStart, packageHamburgerMessage.notes);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            completableFuture.whenComplete((msg, error) -> {
                sender.tell(msg, getSelf());
                getContext().stop(self());
            });*/
        } else {
            unhandled(message);
        }
        getContext().stop(self());
    }
}

class PackageHamburgerMessage {
    long orderStart;
    Food food;

    String notes;

    public PackageHamburgerMessage(Food food, long orderStart, String notes) {
        this.orderStart = orderStart;
        this.food = food;
        this.notes = notes;
    }
}
