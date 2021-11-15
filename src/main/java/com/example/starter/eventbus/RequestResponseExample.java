package com.example.starter.eventbus;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseExample.class);
  public static final String ADDRESS = "my.request.address";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle(), logOnError());
    vertx.deployVerticle(new ResponseVerticle(), logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        LOGGER.error("err", ar.cause());
      }
    };
  }

  static class RequestVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      EventBus eventBus = vertx.eventBus();
      String message = "Hello world";
      LOGGER.debug("Sending : {}", message);
      eventBus.<String>request(ADDRESS, message, reply -> {
        if (reply.failed()) {
          LOGGER.error("Failed ", reply.cause());
          return;
        }
        LOGGER.debug("Response : {}", reply.result().body());
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(ADDRESS, message -> {
        LOGGER.debug("Received message: {}", message.body());
        message.reply("Received your message, Thanks");
      }).exceptionHandler(error -> {
        LOGGER.error("Error ", error);
      });
    }
  }
}
