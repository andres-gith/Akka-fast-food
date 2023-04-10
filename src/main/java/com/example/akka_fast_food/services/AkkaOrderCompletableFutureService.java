package com.example.akka_fast_food.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import com.example.akka_fast_food.akka_extensions.SpringExtension;
import com.example.akka_fast_food.models.HamburgerOrder;
import com.example.akka_fast_food.models.Packaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.concurrent.duration.FiniteDuration;


import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

@Service
public class AkkaOrderCompletableFutureService {
    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;

    public CompletableFuture<Packaging> processOrder(HamburgerOrder hamburgerOrder) {
        CompletableFuture<Packaging> completableFuture = new CompletableFuture<>();
        ActorRef orderReceiverActor = actorSystem.actorOf(springExtension.props("orderReceiverActor", completableFuture));
        orderReceiverActor.tell(hamburgerOrder, null);
        return completableFuture;
    }

    public CompletableFuture<Object> processOrderCompletionStage(HamburgerOrder hamburgerOrder) {
        ActorRef orderReceiverActor = actorSystem.actorOf(springExtension.props("orderReceiverActor"));
        CompletionStage<Object> completionStage = ask(orderReceiverActor, hamburgerOrder, Duration.ofSeconds(5));
        return (CompletableFuture<Object>) completionStage;
    }
}
