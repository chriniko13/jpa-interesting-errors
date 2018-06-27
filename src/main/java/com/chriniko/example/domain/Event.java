package com.chriniko.example.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString

@javax.persistence.Entity
@Table(
        name = "events",
        indexes = {
                @Index(name = "message_idx", columnList = "message", unique = false)
        }
)


@NamedQueries(
        value = {
                @NamedQuery(
                        name = Event.FIND_BY_MESSAGE,
                        query = "select ev from Event ev where ev.message = :m",
                        lockMode = LockModeType.OPTIMISTIC_FORCE_INCREMENT),
                @NamedQuery(
                        name = Event.FIND_ALL,
                        query = "select ev from Event ev"
                ),
                @NamedQuery(
                        name = Event.DELETE_ALL,
                        query = "delete from Event as ev")
        }
)
public class Event extends Entity {

    public static final String FIND_BY_MESSAGE = "Event.findByMessage";
    public static final String FIND_ALL = "Event.findAll";
    public static final String DELETE_ALL = "Event.deleteAll";

    @Id
    private String id;

    private String message;

    public Event(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public Event() {
    }
}
