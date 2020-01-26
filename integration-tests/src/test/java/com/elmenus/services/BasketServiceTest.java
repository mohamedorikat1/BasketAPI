package com.elmenus.services;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.elmenus.hello.api.Basket;
import com.elmenus.hello.api.BasketItem;
import com.elmenus.hello.api.BasketService;
import com.lightbend.lagom.javadsl.client.integration.LagomClientFactory;

import akka.actor.ActorSystem;

public class BasketServiceTest {

    private static final String SERVICE_LOCATOR_URI = "http://localhost:8000";

    private static LagomClientFactory clientFactory;
    private static BasketService basketService;
    private static ActorSystem system;
    private static String entityId = "basket-id";

    @BeforeClass
    public static void setup() {
        clientFactory = LagomClientFactory.create("integration-test", BasketServiceTest.class.getClassLoader());
        basketService = clientFactory.createDevClient(BasketService.class, URI.create(SERVICE_LOCATOR_URI));
        system = ActorSystem.create();
    }
    
    @Test
    public void getEmptyBasketTest() throws Exception {
        List<BasketItem> items = new ArrayList<BasketItem>();
        
        Basket basket = new Basket(entityId.concat("-1"), items);
        
        Basket output = basketService.getBasket(entityId.concat("-1")).invoke().toCompletableFuture().get(10, SECONDS);
        assertEquals(basket, output);
    }
    
    @Test
    public void addToBasketTest() throws Exception {
        List<BasketItem> items = new ArrayList<BasketItem>();
        items.add(new BasketItem("item-id-1", 1, 10.5F));
        items.add(new BasketItem("item-id-2", 2, 89.5F));
        
        Basket basket = new Basket(entityId.concat("-2"), items);
        basketService.addToBasket(entityId.concat("-2")).invoke(new BasketItem("item-id-1", 1, 10.5F)).toCompletableFuture().get(10, SECONDS);
        basketService.addToBasket(entityId.concat("-2")).invoke(new BasketItem("item-id-2", 2, 89.5F)).toCompletableFuture().get(10, SECONDS);
        
        Basket output = basketService.getBasket(entityId.concat("-2")).invoke().toCompletableFuture().get(10, SECONDS);
        assertEquals(basket, output);
    }
    
    @Test
    public void addToBasketCalculationsTest() throws Exception {
        List<BasketItem> items = new ArrayList<BasketItem>();
        items.add(new BasketItem("item-id-1", 1, 10.5F));
        items.add(new BasketItem("item-id-2", 2, 89.5F));
        
        basketService.addToBasket(entityId.concat("-3")).invoke(new BasketItem("item-id-1", 1, 10.5F)).toCompletableFuture().get(10, SECONDS);
        basketService.addToBasket(entityId.concat("-3")).invoke(new BasketItem("item-id-2", 2, 89.5F)).toCompletableFuture().get(10, SECONDS);
        
        Basket basket = basketService.getBasket(entityId.concat("-3")).invoke().toCompletableFuture().get(10, SECONDS);
        assertTrue(basket.getSubtotal() == 189.5F);
        assertTrue(basket.getTax() == (18.95F));
        assertTrue(basket.getTotal() == 208.45F);
    }

    @AfterClass
    public static void tearDown() {
        if (clientFactory != null) {
            clientFactory.close();
        }
        if (system != null) {
            system.terminate();
        }
    }
}
