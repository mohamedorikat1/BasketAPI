package com.elmenus.hello.impl;

import java.util.Optional;

import com.elmenus.hello.impl.BasketCommand.AddItem;
import com.elmenus.hello.impl.BasketCommand.Get;
import com.elmenus.hello.impl.BasketEvent.ItemAdded;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import akka.Done;

public class BasketEntity extends PersistentEntity<BasketCommand, BasketEvent, BasketState> {

    @Override
    public Behavior initialBehavior(Optional<BasketState> snapshotState) {

        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(BasketState.EMPTY));

        b.setCommandHandler(AddItem.class,
                (cmd, ctx) -> ctx.thenPersist(new ItemAdded(entityId(), cmd.getItemId(), cmd.getQuantity(), cmd.getPrice()),
                        evt -> ctx.reply(Done.getInstance())));

        b.setEventHandler(ItemAdded.class, event -> state().addItem(event.getItemId(), event.getQuantity(), event.getPrice()));

        b.setReadOnlyCommandHandler(Get.class, (cmd, ctx) -> ctx.reply(state()));

        return b.build();
    }

}
