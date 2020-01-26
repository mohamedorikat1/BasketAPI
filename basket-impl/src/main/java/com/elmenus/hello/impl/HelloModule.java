package com.elmenus.hello.impl;

import com.elmenus.hello.api.BasketService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class HelloModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(BasketService.class, BasketServiceImpl.class));
  }
}
