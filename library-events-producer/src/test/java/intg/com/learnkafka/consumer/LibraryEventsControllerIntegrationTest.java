package com.learnkafka.consumer;

import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //random port everytime
public class LibraryEventsControllerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void postLibraryEvent() {
        //given
        //build test book
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Dilip")
                .bookName("Kafka using Spring Boot")
                .build();

        //build test LibraryEvent
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        //build test header
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);
        //when
        //test post with request
        ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST, request, LibraryEvent.class);

        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}
