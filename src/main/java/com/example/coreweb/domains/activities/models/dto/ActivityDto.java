package com.example.coreweb.domains.activities.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ActivityDto(
        @JsonProperty("user_agent")
        String userAgent,

        @JsonProperty("ip")
        String ip,

        @JsonProperty("expires")
        String expires,

        @JsonProperty("request_method")
        String requestMethod,

        @JsonProperty("url")
        String url,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("username")
        String username,

        @JsonProperty("total_visitors")
        Long totalVisitors,

        @JsonProperty("created_at")
        Instant createdAt,

        @JsonProperty("updated_at")
        Instant updatedAt
) {
}