package com.learnkafka.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@AllArgsConstructor //generate all args constructor
@NoArgsConstructor //generate no args constructor
@Data //generate Setter, Getters, EqualsAndHashCode, and toString methods
@Builder //Api style of library
@Entity //classify class as an entity
public class Book {

    @Id //classify as Primary key
    private Integer bookId;
    private String bookName;
    private String bookAuthor;
    @OneToOne
    @JoinColumn(name = "libraryEventId")
    private LibraryEvent libraryEvent;
}
