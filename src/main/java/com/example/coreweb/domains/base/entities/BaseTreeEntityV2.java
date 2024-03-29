package com.example.coreweb.domains.base.entities;

import javax.persistence.*;
import java.util.Optional;

@MappedSuperclass
public abstract class BaseTreeEntityV2<T extends BaseTreeEntityV2<T>> extends BaseEntityV2 {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private T parent;

    private String path;

    @PrePersist
    @PreUpdate
    private void onNodePersist() {
        if (parent == null) return;

        // child can't be own parent (required during update)
        if (!this.isNew()) {
            if (this.getId().equals(this.parent.getId()))
                throw new RuntimeException("Entity can't be it's own parent!");
        }
        this.syncPath();
    }

    public void syncPath() {
        path = (parent.getPath() == null || parent.getPath().isEmpty())
                ? parent.getId().toString() : parent.getPath() + ":" + parent.getId();
    }

    public Long getRootId() {
        return (path == null || path.isEmpty()) ?
                getId() : Long.parseLong(getPath().split(":")[0]);
    }

    protected boolean hasParent(T parent) {
        if (parent.isNew()) throw new RuntimeException("Entity that isn't persisted yet can't be a parent.");
        return parent.getId().equals(this.parent.getId());
    }

    public String getAbsolutePath() {
        if (this.isNew()) throw new RuntimeException("Entity that isn't persisted yet can't have absolute path.");
        if (path == null) return String.valueOf(getId());
        return path + ":" + getId();
    }

    public abstract T getImpl();

    public int getNodeLevel() {
        return getParent().map(pParent -> pParent.getNodeLevel() + 1).orElse(1);
    }

    public String getPath() {
        return path;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public Optional<T> getParent() {
        return Optional.ofNullable(parent);
    }

    public Optional<Long> getParentId() {
        return parent == null ? Optional.empty() : Optional.of(parent.getId());
    }

}
