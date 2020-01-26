package com.elmenus.hello.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

public interface BasketService extends Service {

  /**
   * curl http://localhost:9000/api/basket/{id}
   */
  ServiceCall<NotUsed, Basket> getBasket(String basketID);


  /**
   * curl -H "Content-Type: application/json" -X POST -d '{"message":
   * "Hi"}' http://localhost:9000/api/basket/{id}
   */
  ServiceCall<BasketItem, Done> addToBasket(String basketID);

  @Override
  default Descriptor descriptor() {
    return named("basket").withCalls(
        pathCall("/api/basket/:id",  this::getBasket),
        pathCall("/api/basket/:id", this::addToBasket)
      ).withAutoAcl(true);
  }
}
