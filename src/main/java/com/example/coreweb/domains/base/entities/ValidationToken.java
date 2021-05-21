package com.example.coreweb.domains.base.entities;


import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.Instant;
import java.util.Date;

@MappedSuperclass
public class ValidationToken extends BaseEntity {
    private String token;
    private boolean tokenValid;
    private Instant tokenValidUntil;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTokenValid() {
        if (tokenValidUntil == null) return false;
        return tokenValid && !Instant.now().isAfter(tokenValidUntil);
    }

    public void setTokenValid(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    public Instant getTokenValidUntil() {
        return tokenValidUntil;
    }

    public void setTokenValidUntil(Instant tokenValidUntil) {
        this.tokenValidUntil = tokenValidUntil;
    }

    @Override
    public String toString() {
        return "ValidationToken{" +
                "token='" + token + '\'' +
                ", tokenValid=" + tokenValid +
                "} " + super.toString();
    }

}
