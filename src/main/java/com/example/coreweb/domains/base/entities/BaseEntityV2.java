package com.example.coreweb.domains.base.entities;

import com.example.auth.config.security.SecurityContext;
import com.example.common.utils.DateUtil;
import com.example.common.utils.TimeUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntityV2 implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "uuid_str", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private boolean deleted;

    @PrePersist
    private void onBasePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.createdBy = this.getLoggedInUsername();
        this.uuid = UUID.randomUUID().toString();
    }

    @PreUpdate
    private void onBaseUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = this.getLoggedInUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntityV2 that = (BaseEntityV2) o;
        return Objects.equals(id, that.id);
    }

    public boolean isNew() {
        return this.id == null;
    }

    @JsonIgnore
    public String getLoggedInUsername() {
        return SecurityContext.getLoggedInUsername();
    }

    @JsonIgnore
    public String getCreatedAtReadable() {
        return TimeUtility.readableDateTimeFromInstant(this.createdAt);
    }

    @JsonIgnore
    public String getUpdatedAtReadable() {
        return TimeUtility.readableDateTimeFromInstant(this.updatedAt);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getReadableDate(Date date) {
        return DateUtil.getReadableDate(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
