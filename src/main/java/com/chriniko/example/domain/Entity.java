package com.chriniko.example.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data

@MappedSuperclass

public abstract class Entity implements Serializable {

    @Version
    protected Long version;

    @PrePersist
    void prePersist() {
        System.out.println(this.getClass().getSimpleName() + " --- PRE-PERSIST");
    }

    @PostPersist
    void postPersist() {
        System.out.println(this.getClass().getSimpleName() + " --- PRE-PERSIST");
    }

    @PreRemove
    void preRemove() {
        System.out.println(this.getClass().getSimpleName() + " --- PRE-REMOVE");
    }

    @PostRemove
    void postRemove() {
        System.out.println(this.getClass().getSimpleName() + " --- POST-REMOVE");
    }

    @PreUpdate
    void preUpdate() {
        System.out.println(this.getClass().getSimpleName() + " --- PRE-UPDATE");
    }

    @PostUpdate
    void postUpdate() {
        System.out.println(this.getClass().getSimpleName() + " --- POST-UPDATE");
    }

}
