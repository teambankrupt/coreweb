package com.example.coreweb.domains.base.entities;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Optional;

@MappedSuperclass
public abstract class BaseTreeEntity<T extends BaseTreeEntity<T>> extends BaseEntity {

    @ManyToOne
    private T parent;

    private String path;

    @PrePersist
    @PreUpdate
    private void onNodePersist() {
        if (parent == null) return;

        path = (parent.getPath() == null || parent.getPath().isEmpty())
                ? parent.getId().toString() : parent.getPath() + ":" + parent.getId();
    }

    protected Long getRootId() {
        return (path == null || path.isEmpty()) ?
                getId() : Long.parseLong(getPath().split(":")[0]);
    }

    protected boolean hasParent(T parent) {
        if (parent.isNew()) throw new RuntimeException("Entity that isn't persisted yet can't be a parent.");
        return parent.getId().equals(this.parent.getId());
    }

    protected String getAbsolutePath() {
        if (this.isNew()) throw new RuntimeException("Entity that isn't persisted yet can't have absolute path.");
        if (path == null) return String.valueOf(getId());
        return path + ":" + getId();
    }

    public abstract T getImpl();

    public int getLevel() {
        return getParent().map(pParent -> pParent.getLevel() + 1).orElse(1);
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
