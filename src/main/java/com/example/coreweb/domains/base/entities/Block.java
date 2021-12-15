package com.example.coreweb.domains.base.entities;

import javax.persistence.*;
import java.time.Instant;
import java.util.Optional;

@MappedSuperclass
public abstract class Block<T extends Block<T>> extends BaseEntityV2 {

    @ManyToOne
    @JoinColumn(name = "previous_block_id")
    private T previousBlock;

    @Column(name = "chained", nullable = false)
    private boolean chained;

    @JoinColumn(name = "hash", nullable = false)
    private String hash;

    @PrePersist
    @PreUpdate
    private void onBlockPersist() {
        if (this.isChained()) {
            long start = Instant.now().toEpochMilli();
            System.out.println("Mining started.");
            this.hash = this.calculateHash();
            System.out.println("Mining completed. Taken: " + (Instant.now().toEpochMilli() - start) + " millis");
            this.startMining(this);
        } else this.hash = "";
    }

    public String calculateHash() {
        Optional<T> previousBlock = getImpl().getPreviousBlock();
        String objectHash = this.getImpl().generateHash();

        if (!previousBlock.isPresent())
            return String.valueOf(objectHash);

        String toHash = objectHash + this.previousBlock.getHash();
        return String.valueOf(toHash.hashCode());
    }

    public void startMining(Block<T> block) {
        if (block.isGenesisBlock())
            return;
        String previousBlockHash = block.previousBlock.getHash();

        if (!block.previousBlock.calculateHash().equals(previousBlockHash))
            throw new RuntimeException("Block Invalid. Hash: " + block.previousBlock.getHash());

        this.startMining(block.previousBlock);
    }

    public boolean isGenesisBlock() {
        return this.previousBlock == null;
    }

    public abstract T getImpl();

    public void setChained(boolean chained) {
        this.chained = chained;
        if (this.hash == null || this.hash.trim().isEmpty())
            this.hash = this.calculateHash();
    }

    public boolean isChained() {
        return chained;
    }

    public String getHash() {
        return hash;
    }

    public int getBlockLevel() {
        return this.getPreviousBlock().map(pParent -> pParent.getBlockLevel() + 1).orElse(1);
    }

    public void setPreviousBlock(T previousBlock) {
        this.previousBlock = previousBlock;
    }

    public Optional<T> getPreviousBlock() {
        return Optional.ofNullable(this.previousBlock);
    }

    protected abstract String generateHash();

}
