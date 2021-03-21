package com.learnkafka.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.domain.LibraryEventType;
import com.learnkafka.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController //combines @Controller & @ResponseBody
//@controller indicates the annotated class is a controller
//@ResponseBody maps the HttpRequest body to a transfer or domain object, enabling automatic deserialization
@Slf4j
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        //invoke kafka producer

        libraryEvent.setLibraryEventType(LibraryEventType.NEW); //setting libraryEventType to NEW

        /*Approach 1*/
        //libraryEventProducer.sendLibraryEvent(libraryEvent);

        /*Approach 2*/
        libraryEventProducer.sendLibraryEvent_Approach2(libraryEvent);

        /*Approach 3*/
        /*
        SendResult<Integer,String> sendResult = libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
        log.info("SendResult is {}", sendResult.toString());
        */

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    //put
    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        //invoke kafka producer

        if(libraryEvent.getLibraryEventId() == null) //if id == null send BAD_REQUEST
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }

        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE); //setting libraryEventType to UPDATE

        /*Approach 1*/
        //libraryEventProducer.sendLibraryEvent(libraryEvent);

        /*Approach 2*/
        libraryEventProducer.sendLibraryEvent_Approach2(libraryEvent);

        /*Approach 3*/
        /*
        SendResult<Integer,String> sendResult = libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
        log.info("SendResult is {}", sendResult.toString());
        */

        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }
}
