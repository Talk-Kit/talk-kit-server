package com.canal.post.dto;

public record RequestNHNToken(Auth auth) {

    public RequestNHNToken(String tenantId, String username, String password) {
        this(new Auth(tenantId, new PasswordCredentials(username, password)));
    }

    public record Auth(String tenantId, PasswordCredentials passwordCredentials) {}

    public record PasswordCredentials(String username, String password) {}
}

