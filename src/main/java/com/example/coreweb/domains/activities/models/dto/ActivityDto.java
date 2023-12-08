package com.example.coreweb.domains.activities.models.dto;

import com.example.auth.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        Long totalVisitors
) {
}