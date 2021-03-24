package com.learnkafka.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor //generate all args constructor
@NoArgsConstructor //generate no args constructor
@Data //generate Setter, Getters, EqualsAndHashCode, and toString methods
@Builder //Api style of library
@Entity //classify class as an entity
public class LibraryEvent {

    @Id //classify as Primary key
    @GeneratedValue //Automatically generate libraryEventId
    private Integer libraryEventId;
    @Enumerated(EnumType.STRING)
    private LibraryEventType libraryEventType;
    @OneToOne(mappedBy = "libraryEvent", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Book book;
}
