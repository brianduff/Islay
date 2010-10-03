package org.dubh.islay.hub.server.facebook;

import org.dubh.islay.hub.server.JsonObjectFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryProvider;

public class FacebookModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(JsonObjectFactory.class).toInstance(
        JsonObjectFactory.builder()
            .withIsoDateConverter()
            .withRecursiveConverter(FacebookObject.class)
            .build());
    bind(FacebookServiceFactory.class).toProvider(
        FactoryProvider.newFactory(FacebookServiceFactory.class, FacebookService.class));
  }
}
