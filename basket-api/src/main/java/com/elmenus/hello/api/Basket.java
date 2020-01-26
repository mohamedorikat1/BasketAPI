package com.elmenus.hello.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@JsonDeserialize
public class Basket {

    String uuid;
    List<BasketItem> items;

    @JsonProperty("subtotal")
    float subtotal;

    @JsonProperty("tax")
    float tax;

    @JsonProperty("total")
    float total;

    @JsonCreator
    public Basket(@JsonProperty("uuid") String uuid, @JsonProperty("items") List<BasketItem> items) {
        this.uuid = Preconditions.checkNotNull(uuid, "uuid");
        this.items = Preconditions.checkNotNull(items, "items");
        float subtotalLocal = 0;
        for (BasketItem item : items) subtotalLocal += item.getPrice() * item.getQuantity();
        this.subtotal = subtotalLocal;
        tax = (float) (subtotalLocal * 0.1);
        total = tax + subtotal;
    }

    public Optional<BasketItem> get(String itemId) {
        return items.stream().filter(item -> item.getUuid().equals(itemId)).findFirst();
    }
}
