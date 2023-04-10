package com.example.akka_fast_food.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.HamburgerToppings;
import com.example.akka_fast_food.services.HamburgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component("toppingActor")
@Scope("prototype")
public class ToppingActor extends UntypedAbstractActor {

    @Autowired
    HamburgerService hamburgerService;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ToppingMessage) {
            String topping = ((ToppingMessage) message).topping;

            try {
                HamburgerToppings hamburgerTopping = hamburgerService.prepareTopping(topping);
                getSender().tell(new ToppingAddedMessage(hamburgerTopping), getSelf());
            } catch (IllegalArgumentException e) {
                String note = "We don't have this topping:" + topping + ".";
                getSender().tell(new MissingToppingMessage(note), getSelf());
            }
            /*ActorRef sender = getSender();
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    HamburgerToppings hamburgerTopping =  hamburgerService.prepareTopping(topping);
                    return new ToppingAddedMessage(hamburgerTopping);
                } catch (IllegalArgumentException e) {
                    String note = "We don't have this topping:" + topping + ".";
                    return new MissingToppingMessage(note);
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

class ToppingMessage {
    Hamburger hamburger;
    String topping;

    public ToppingMessage(Hamburger hamburger, String topping) {
        this.hamburger = hamburger;
        this.topping = topping;
    }
}
