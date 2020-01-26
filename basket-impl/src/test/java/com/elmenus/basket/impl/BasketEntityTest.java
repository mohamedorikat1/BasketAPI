package com.elmenus.basket.impl;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.elmenus.hello.impl.BasketCommand;
import com.elmenus.hello.impl.BasketEntity;
import com.elmenus.hello.impl.BasketEvent;
import com.elmenus.hello.impl.BasketState;
import com.elmenus.hello.impl.BasketCommand.AddItem;
import com.elmenus.hello.impl.BasketCommand.Get;
import com.elmenus.hello.impl.BasketEvent.ItemAdded;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class BasketEntityTest {
    private static ActorSystem system;
    private static final String ENTITY_ID = "basket-1";

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("HelloEntityTest");
    }

    @AfterClass
    public static void teardown() {
        Duration dur = FiniteDuration.create(10, TimeUnit.SECONDS);
        TestKit.shutdownActorSystem(system, dur, false);
        system = null;
    }

    private PersistentEntityTestDriver<BasketCommand, BasketEvent, BasketState> driver;

    @Before
    public void setupDriver() {
        driver = new PersistentEntityTestDriver<>(system, new BasketEntity(), ENTITY_ID);
    }

    @Test
    public void shoppingCartShouldAllowAddingAnItem() {
        Outcome<BasketEvent, BasketState> outcome = driver.run(new AddItem("item-id-1", 1, 55.5F));

        assertThat(outcome.getReplies(), contains(Done.getInstance()));
        assertThat(outcome.events(), contains(new ItemAdded(ENTITY_ID, "item-id-1", 1, 55.5F)));
        assertThat(outcome.state(), equalTo(BasketState.EMPTY.addItem("item-id-1", 1, 55.5F)));
    }


    @Test
    public void shoppingCartShouldAllowGettingTheState() {
        driver.run(new AddItem("item-id-2", 1, 55.5F));
        Outcome<BasketEvent, BasketState> outcome = driver.run(Get.INSTANCE);

        assertThat(outcome.getReplies(), contains(BasketState.EMPTY.addItem("item-id-2", 1, 55.5F)));
        assertThat(outcome.events(), empty());
    }
}
