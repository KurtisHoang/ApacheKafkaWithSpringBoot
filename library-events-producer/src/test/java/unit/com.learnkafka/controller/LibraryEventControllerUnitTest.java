package com.learnkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.Controller.LibraryEventsController;
import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class) //unit test for controller layers
@AutoConfigureMockMvc //unit test cases
public class LibraryEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    LibraryEventProducer libraryEventProducer;

    @Test
    void postLibraryEvents() throws Exception {
        //given
        //build test book
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Dilip")
                .bookName("Kafka Using Spring Boot")
                .build();

        //build test LibraryEvent
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent); //convert libraryEvent to string
        //need to use sendLibraryEvent_Approach2() since unit test only uses LibraryEventsController class
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        //when
        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); //check if created

        //then
    }

    //handles when book/book's contents is null
    //unit test for 4xx client error
    @Test
    void postLibraryEvents_4xx() throws Exception {
        //given
        //build test book
        Book book = Book.builder()
                .bookId(null)
                .bookAuthor(null)
                .bookName("Kafka Using Spring Boot")
                .build();

        //build test LibraryEvent
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent); //convert libraryEvent to string
        //need to use sendLibraryEvent_Approach2() since unit test only uses LibraryEventsController class
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        //expect
        String expectedErrorMessage = "book.bookAuthor - must not be blank, book.bookId - must not be null";
        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()) //check if created
        .andExpect(content().string(expectedErrorMessage)); //check if it outputs the ame error Message when book contents are not valid
    }

    @Test
    void updateLibraryEvents() throws Exception {
        //given
        //build test book
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Dilip")
                .bookName("Kafka Using Spring Boot")
                .build();

        //build test LibraryEvent
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(999)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent); //convert libraryEvent to string
        //need to use sendLibraryEvent_Approach2() since unit test only uses LibraryEventsController class
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        //when
        mockMvc.perform(put("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); //check if ok

        //then
    }

    @Test
    void updateLibraryEvents_withoutLibraryEventId() throws Exception {
        //given
        //build test book
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Dilip")
                .bookName("Kafka Using Spring Boot")
                .build();

        //build test LibraryEvent
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent); //convert libraryEvent to string
        //need to use sendLibraryEvent_Approach2() since unit test only uses LibraryEventsController class
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        //when
        mockMvc.perform(put("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) //check if bad request
        .andExpect(content().string("Please pass the LibraryEventId")); //check it it outputs this statement

        //then
    }
}
