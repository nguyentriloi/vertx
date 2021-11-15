package com.example.starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {

  private static final Logger LOGGER = LoggerFactory.getLogger(PointToPointExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000, id -> {
        // send a message every second
        vertx.eventBus().send(Sender.class.getName(), "Sending a message...");
      });
    }
  }

  static class Receiver extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Sender.class.getName(),
        message -> LOGGER.debug("Received: {}", message.body()));
    }
  }
}
