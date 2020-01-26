package com.elmenus.hello.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.elmenus.hello.api.Basket;
import com.elmenus.hello.api.BasketItem;
import com.elmenus.hello.api.BasketService;
import com.elmenus.hello.impl.BasketCommand.AddItem;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import akka.Done;
import akka.NotUsed;

public class BasketServiceImpl implements BasketService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public BasketServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(BasketEntity.class);
    }

    private PersistentEntityRef<BasketCommand> entityRef(String id) {
        return persistentEntityRegistry.refFor(BasketEntity.class, id);
    }

    @Override
    public ServiceCall<NotUsed, Basket> getBasket(String basketId) {
        return request -> entityRef(basketId).ask(BasketCommand.Get.INSTANCE)
                .thenApply(basket -> convertBasketType(basketId, basket));
    }

    @Override
    public ServiceCall<BasketItem, Done> addToBasket(String basketID) {
        return item -> {
            PersistentEntityRef<BasketCommand> ref = persistentEntityRegistry.refFor(BasketEntity.class, basketID);
            return ref.ask(new AddItem(item.getUuid(), item.getQuantity(), item.getPrice()));
        };
    }

    private Basket convertBasketType(String id, BasketState basket) {
        List<BasketItem> items = new ArrayList<>();
        for (Map.Entry<String, BasketItem> item : basket.getItems().entrySet()) {
            items.add(item.getValue());
        }
        return new Basket(id, items);
    }

}
