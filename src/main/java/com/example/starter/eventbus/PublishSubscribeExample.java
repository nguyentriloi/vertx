package com.example.starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishSubscribeExample {

  private static final Logger LOGGER = LoggerFactory.getLogger(PublishSubscribeExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscriber1());
    vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
  }

  public static class Publish extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(5000, id ->
        vertx.eventBus().publish(Publish.class.getName(), "A message for everyone")
      );
    }
  }

  public static class Subscriber1 extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publish.class.getName(),
        message -> LOGGER.debug("Received 1: {}", message.body()));
    }
  }

  public static class Subscriber2 extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publish.class.getName(),
        message -> LOGGER.debug("Received 2: {}", message.body()));
    }
  }
}
