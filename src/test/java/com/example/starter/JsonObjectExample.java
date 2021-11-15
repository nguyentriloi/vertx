package com.example.starter;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectExample {

  @Test
  void jsonObjectCanMapped() {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", 1);
    assertEquals("{\"id\":1}", jsonObject.encode());
  }
}
