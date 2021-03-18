package com.learnkafka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor //generate all args constructor
@NoArgsConstructor //generate no args constructor
@Data //generate Setter, Getters, EqualsAndHashCode, and toString methods
@Builder //Api style of library
public class LibraryEvent {

    private Integer libraryEventId;
    private Book book;
}
