package com.example.starter;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {

  private static final Logger LOGGER = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.debug("success");
      context.completeNow();
    });
    LOGGER.debug("End");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOGGER.debug("failed");
      context.completeNow();
    });
    LOGGER.debug("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("success");
      LOGGER.debug("success");
    });
    final Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOGGER.debug("Result: {}", result);
      context.completeNow();
    }).onFailure(context::failNow);
  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOGGER.debug("Timer done");
    });
    final Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOGGER.debug("Result: {}", result);
      context.completeNow();
    }).onFailure(err -> {
      LOGGER.debug("Result : ", err);
      context.completeNow();
    });
  }

  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("success");
      LOGGER.debug("success");
    });
    final Future<String> future = promise.future();
    future
        .map(asString -> {
          LOGGER.debug("Map string to json object");
          return new JsonObject().put("key", asString);
        })
        .map(jsonObject -> new JsonArray().add(jsonObject))
        .onSuccess(result -> {
          LOGGER.debug("Result: {}", result);
          context.completeNow();
        }).onFailure(context::failNow);
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
        .requestHandler(request -> LOGGER.debug("{}", request))
        .listen(10_000)
        .compose(server -> {
          LOGGER.info("Another task");
          return Future.succeededFuture(server);
        })
        .onFailure(context::failNow)
        .onSuccess(server -> {
          LOGGER.debug("Server started on port {}", server.actualPort());
          context.completeNow();
        });
  }

  @Test
  void future_composition(Vertx vertx, VertxTestContext context) {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

    CompositeFuture.all(futureOne, futureTwo, futureThree)
        .onFailure(context::failNow)
        .onSuccess(result -> {
          LOGGER.debug("Success");
          context.completeNow();
        });

    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
//      three.fail("Three fail");
    });
  }
}
