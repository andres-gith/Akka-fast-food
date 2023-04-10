package com.example.akka_fast_food.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;
import com.example.akka_fast_food.akka_extensions.SpringExtension;
import com.example.akka_fast_food.models.Hamburger;
import com.example.akka_fast_food.models.HamburgerToppings;
import com.example.akka_fast_food.services.HamburgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component("toppingsProcessorActor")
@Scope("prototype")
public class ToppingsProcessorActor extends UntypedAbstractActor {
    @Autowired
    private ActorSystem actorSystem;
    @Autowired
    private SpringExtension springExtension;

    @Autowired
    HamburgerService hamburgerService;

    int toppingsSize = 0;
    int toppingsProcessed = 0;

    Hamburger hamburger;

    String notes = "";
    ActorRef OrderReceiverRef;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ToppingsMessage) {
            OrderReceiverRef = getSender();
            ToppingsMessage toppingsMessage = (ToppingsMessage) message;
            hamburger = toppingsMessage.hamburger;
            //ArrayList<HamburgerToppings> toppingsArrayList = new ArrayList<>();
            //toppingMessage.toppings.forEach(topping -> toppingsArrayList.add(HamburgerToppings.valueOf(topping)));
            //hamburgerService.addToppings(hamburger, toppingsArrayList);

            if(toppingsMessage.toppings != null) {
                toppingsSize = toppingsMessage.toppings.size();
                int index = 0;
                for(String topping : toppingsMessage.toppings) {
                    String id = topping + "_" + index;
                    processTopping(hamburger, topping, id);
                }
            }
        } else if (message instanceof ToppingAddedMessage || message instanceof MissingToppingMessage) {
            toppingsProcessed++;
            if(message instanceof MissingToppingMessage) {
                if (!((MissingToppingMessage) message).note.isBlank()) {
                    notes += ((MissingToppingMessage) message).note;
                }
            }

            if(message instanceof ToppingAddedMessage) {
                hamburgerService.addTopping(hamburger, ((ToppingAddedMessage) message).hamburgerTopping);
            }

            if(toppingsSize == toppingsProcessed) {
                OrderReceiverRef.tell(new HamburgerWithToppingsMessage(hamburger, notes), getSelf());
                getContext().stop(self());
            }
        } else {
            unhandled(message);
        }

    }

    private void processTopping(Hamburger hamburger, String topping, String id) {
        ActorRef toppingsProcessorActor = actorSystem.actorOf(springExtension.props("toppingActor"));
        ToppingMessage message = new ToppingMessage(hamburger, topping);
        toppingsProcessorActor.tell(message, getSelf());
    }
}

class ToppingsMessage {
    Hamburger hamburger;
    List<String> toppings;

    public ToppingsMessage(Hamburger hamburger, List<String> toppings) {
        this.hamburger = hamburger;
        this.toppings = toppings;
    }
}

class ToppingAddedMessage {
    HamburgerToppings hamburgerTopping;

    public ToppingAddedMessage(HamburgerToppings hamburgerTopping) {
        this.hamburgerTopping = hamburgerTopping;
    }
}

class MissingToppingMessage {
    String note;

    public MissingToppingMessage( String note) {
        this.note = note;
    }
}