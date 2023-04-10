package com.example.akka_fast_food.actors;

import akka.actor.*;
import com.example.akka_fast_food.akka_extensions.SpringExtension;
import com.example.akka_fast_food.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component("orderReceiverActor")
@Scope("prototype")
public class OrderReceiverActor extends UntypedAbstractActor {
    private final CompletableFuture<Packaging> completableFuture;
    private final long orderStart;

    private ActorRef orginalSender;
    private HamburgerOrder hamburgerOrder;
    private boolean hasToppings;
    private boolean toppingsReady;
    private boolean hasCondiments;
    private boolean condimentsReady;

    private String notes = "";
    @Autowired
    private ActorSystem actorSystem;
    @Autowired
    private SpringExtension springExtension;

    public OrderReceiverActor(CompletableFuture<Packaging> completableFuture) {
        this.completableFuture = completableFuture;
        this.orderStart = System.currentTimeMillis();
    }

    public OrderReceiverActor() {
        this.completableFuture = null;
        this.orderStart = System.currentTimeMillis();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof HamburgerOrder) {
            this.orginalSender = getSender();
            processHamburgerOrder((HamburgerOrder) message);
        } else if (message instanceof Hamburger) {
            Hamburger hamburger = (Hamburger) message;
            if (hamburger.getMeatState() == HamburgerMeatStates.RAW) {
                getHamburgerCooked(hamburger);
            } else {
                if (hasToppings) {
                    addToppings(hamburger, hamburgerOrder.toppings);
                }
                if (hasCondiments) {
                    addCondiments(hamburger, hamburgerOrder.condiments);
                }
            }
        } else if (message instanceof HamburgerWithToppingsMessage){
            toppingsReady = true;
            notes += ((HamburgerWithToppingsMessage) message).notes;
            if(toppingsReady && condimentsReady) {
                packageHamburger(((HamburgerWithToppingsMessage) message).food, notes);
            }
        } else if (message instanceof HamburgerWithCondimentsMessage){
            condimentsReady = true;
            if(toppingsReady && condimentsReady) {
                packageHamburger(((HamburgerWithCondimentsMessage) message).food, notes);
            }
        } else if (message instanceof Packaging) {
            //completableFuture.complete((Packaging) message);
            orginalSender.tell((Packaging) message, getSelf());
            getContext().stop(self());
        } else {
            unhandled(message);
        }

    }

    private void processHamburgerOrder(HamburgerOrder hamburgerOrder) {
        this.hamburgerOrder = hamburgerOrder;
        hasToppings = hamburgerOrder.toppings != null && !hamburgerOrder.toppings.isEmpty();
        toppingsReady = !hasToppings;
        hasCondiments = hamburgerOrder.condiments !=null && !hamburgerOrder.condiments.isEmpty();
        condimentsReady =!hasCondiments;
        Hamburger hamburger = new Hamburger();
        getHamburgerCooked(hamburger);
    }

    private void getHamburgerCooked(Hamburger hamburger) {
        ActorRef cookerActor = actorSystem.actorOf(springExtension.props("cookerActor"));
        cookerActor.tell(hamburger, getSelf());
    }

    private void addToppings(Hamburger hamburger, List<String> toppings) {
        ActorRef toppingsProcessorActor = actorSystem.actorOf(springExtension.props("toppingsProcessorActor"));
        ToppingsMessage message = new ToppingsMessage(hamburger, toppings);
        toppingsProcessorActor.tell(message, getSelf());
    }

    private void addCondiments(Hamburger hamburger, List<String> condiments) {
        ActorRef condimentsActor = actorSystem.actorOf(springExtension.props("condimentsActor"));
        CondimentsMessage message = new CondimentsMessage(hamburger, condiments);
        condimentsActor.tell(message, getSelf());
    }

    private void packageHamburger(Food food, String notes) {
        ActorRef packageActor = actorSystem.actorOf(springExtension.props("packagingActor"));
        PackageHamburgerMessage message = new PackageHamburgerMessage(food, orderStart, notes);
        packageActor.tell(message, getSelf());
    }

}

class HamburgerWithToppingsMessage {
    public Food food;
    public String notes;

    public HamburgerWithToppingsMessage(Food food, String notes) {
        this.food = food;
        this.notes = notes;
    }
}

class HamburgerWithCondimentsMessage {
    public Food food;

    public HamburgerWithCondimentsMessage(Food food) {
        this.food = food;
    }
}