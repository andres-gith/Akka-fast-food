package com.example.akka_fast_food.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.HamburgerCondiments;
import com.example.akka_fast_food.models.HamburgerToppings;
import com.example.akka_fast_food.models.Packaging;
import com.example.akka_fast_food.services.HamburgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component("condimentsActor")
@Scope("prototype")
public class CondimentsActor extends UntypedAbstractActor {
    @Autowired
    HamburgerService hamburgerService;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof CondimentsMessage) {
            CondimentsMessage condimentsMessage = (CondimentsMessage) message;
            Hamburger hamburger = condimentsMessage.hamburger;
            //ArrayList<HamburgerCondiments> condimentsArrayList = new ArrayList<>();
            //condimentsMessage.toppings.forEach(condiment -> condimentsArrayList.add(HamburgerCondiments.valueOf(condiment)));
            //hamburgerService.addCondiments(hamburger, condimentsArrayList);
            for(String condiment : condimentsMessage.condiments) {
               hamburgerService.addCondiment(hamburger, HamburgerCondiments.valueOf(condiment));
            }
            getSender().tell(new HamburgerWithCondimentsMessage(hamburger), getSelf());
            /*ActorRef sender = getSender();
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    for(String condiment : condimentsMessage.condiments) {
                        hamburgerService.addCondiment(hamburger, HamburgerCondiments.valueOf(condiment));
                    }
                    return new HamburgerWithCondimentsMessage(hamburger);
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

class CondimentsMessage {
    Hamburger hamburger;
    List<String> condiments;

    public CondimentsMessage(Hamburger hamburger, List<String> condiments) {
        this.hamburger = hamburger;
        this.condiments = condiments;
    }
}
