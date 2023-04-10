package com.example.akka_fast_food.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.Packaging;
import com.example.akka_fast_food.services.HamburgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component("cookerActor")
@Scope("prototype")
public class CookerActor extends UntypedAbstractActor {
    @Autowired
    HamburgerService hamburgerService;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Hamburger) {
            Hamburger hamburger = hamburgerService.cookHamburger((Hamburger) message);
            getSender().tell(hamburger, getSelf());
            /*ActorRef sender = getSender();
            CompletableFuture<Hamburger> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return hamburgerService.cookHamburger((Hamburger) message);
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
