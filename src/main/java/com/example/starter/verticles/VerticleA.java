package com.example.starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleA extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(VerticleA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug("Start {}" ,getClass().getName());
    vertx.deployVerticle(new VerticleAA(), whenDeployed -> {
      LOGGER.debug("Deployed {}", VerticleAA.class.getName());
      vertx.undeploy(whenDeployed.result());
    });
    vertx.deployVerticle(new VerticleAB(), whenDeployed -> {
      LOGGER.debug("Deployed {}", VerticleAB.class.getName());
      // do not undeploy
    });
    startPromise.complete();
  }
}
