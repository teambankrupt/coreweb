package com.example.coreweb.configs.rabbitmq.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * @author mir00r on 6/2/23
 * @project IntelliJ IDEA
 */
public enum ExchangeTypes {
    DIRECT("direct"),
    TOPIC("topic"),
    FANOUT("fanout"),
    HEADERS("headers");

    @JsonProperty("label")
    private final String label;

    ExchangeTypes(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
