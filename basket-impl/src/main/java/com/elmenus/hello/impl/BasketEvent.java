package com.elmenus.hello.impl;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;

import lombok.Value;

public interface BasketEvent extends Jsonable {

    @Value
    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    public final class ItemAdded implements BasketEvent {
        String basketId;
        String itemId;
        int quantity;
        float price;

        @JsonCreator
        public ItemAdded(String basketId, String itemId, int quantity, float price) {
            this.basketId = Preconditions.checkNotNull(basketId);
            this.itemId = Preconditions.checkNotNull(itemId);
            this.quantity = Preconditions.checkNotNull(quantity, 0);
            this.price = Preconditions.checkNotNull(price, 0);
        }
    }
}
