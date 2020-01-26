package com.elmenus.hello.impl;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;

import akka.Done;
import lombok.Value;

public interface BasketCommand extends Jsonable {

    @Value
    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    public final class AddItem implements BasketCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        String itemId;
        int quantity;
        float price;

        @JsonCreator
        public AddItem(String itemId, int quantity, float price) {
            this.itemId = Preconditions.checkNotNull(itemId);
            this.quantity = Preconditions.checkNotNull(quantity, 0);
            this.price =  Preconditions.checkNotNull(price, 0);
        }
    }

    enum Get implements BasketCommand, PersistentEntity.ReplyType<BasketState> {
        INSTANCE
    }
}
