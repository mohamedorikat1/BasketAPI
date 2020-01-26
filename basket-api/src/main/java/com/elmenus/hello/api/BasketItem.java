package com.elmenus.hello.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import lombok.Value;

@Value
@JsonDeserialize
public final class BasketItem {

    String uuid;
    int quantity;
    float price;

    @JsonCreator
    public BasketItem(@JsonProperty("uuid")String uuid, @JsonProperty("quantity")int quantity, @JsonProperty("price")float price) {
        this.uuid = Preconditions.checkNotNull(uuid);
        this.quantity = Preconditions.checkNotNull(quantity, 0);
        this.price = Preconditions.checkNotNull(price, 0);
    }
}
