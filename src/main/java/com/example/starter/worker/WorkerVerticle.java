package com.example.starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug("worker running");
    startPromise.complete();
    Thread.sleep(5000);
    LOGGER.debug("Blocking operation done.");
  }
}
