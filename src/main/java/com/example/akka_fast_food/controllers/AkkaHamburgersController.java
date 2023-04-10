package com.example.akka_fast_food.controllers;


import com.example.akka_fast_food.models.*;
import com.example.akka_fast_food.services.AkkaOrderCompletableFutureService;
import com.example.akka_fast_food.services.HamburgerService;
import com.example.akka_fast_food.services.PackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v1/akka/hamburgers")
public class AkkaHamburgersController {
    private static final Long DEFERRED_RESULT_TIMEOUT = 6000L;

    @Autowired
    AkkaOrderCompletableFutureService akkaOrderCompletableFutureService;

    /*@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeferredResult<Packaging> orderHamburger(@RequestBody HamburgerOrder hamburgerOrder) {
        DeferredResult<Packaging> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);
        CompletableFuture<Packaging> completableFuture = akkaOrderCompletableFutureService.processOrder(hamburgerOrder);
        completableFuture.whenComplete((result, error) -> {
            if (error != null) {
                deferredResult.setErrorResult(error);
            } else {
                deferredResult.setResult(result);
            }
        });
        return deferredResult;
    }*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeferredResult<Packaging> orderHamburger(@RequestBody HamburgerOrder hamburgerOrder) {
        DeferredResult<Packaging> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);
        CompletableFuture<Object> completionStage = akkaOrderCompletableFutureService.processOrderCompletionStage(hamburgerOrder);

        completionStage.whenComplete((result, error) -> {
            if (error != null) {
                deferredResult.setErrorResult(error);
            } else {
                deferredResult.setResult((Packaging) result);
            }
        });
        return deferredResult;
    }
}
