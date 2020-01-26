package com.elmenus.hello.impl;

import javax.annotation.concurrent.Immutable;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import com.elmenus.hello.api.BasketItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;

import lombok.Value;

@Value
@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public final class BasketState implements CompressedJsonable {

    PMap<String, BasketItem> items;
    public static BasketState EMPTY = new BasketState(HashTreePMap.empty());

    @JsonCreator
    public BasketState(PMap<String, BasketItem> items) {
        this.items = Preconditions.checkNotNull(items, "message1");
    }

    public BasketState addItem(String productId, int quantity, float price) {
        PMap<String, BasketItem> newItems;
        BasketItem item = new BasketItem(productId, quantity, price);
        if (quantity == 0) {
            newItems = items.minus(item);
        } else {
            newItems = items.plus(productId, item);
            newItems = items.plus(productId, item);
        }
        return new BasketState(newItems);
    }
}
