package com.learnkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.entity.LibraryEvent;
import com.learnkafka.jpa.LibraryEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("libraryEvent : {}", libraryEvent);

        if(libraryEvent.getLibraryEventId() != null && libraryEvent.getLibraryEventId() == 000){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        switch (libraryEvent.getLibraryEventType())
        {
            case NEW:
                //save operation
                save(libraryEvent);
                break;
            case UPDATE:
                //update operation
                //validate the libraryevent
                validate(libraryEvent);
                //save
                save(libraryEvent);
                break;
            default:
                log.info("invalid Library Event Type");
                break;
        }
    }

    private void save(LibraryEvent libraryEvent)
    {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent); //insert data into database
        log.info("Successfully Persisted the library Event : {}", libraryEvent);
    }

    private void validate(LibraryEvent libraryEvent)
    {
        if(libraryEvent.getLibraryEventId() == null) //check if libraryEventId is null
        {
            throw new IllegalArgumentException("Library Event Id is Missing");
        }

        Optional<LibraryEvent> libraryEventOptional = libraryEventsRepository.findById(libraryEvent.getLibraryEventId());
        if(!libraryEventOptional.isPresent()) //if present
        {
            throw new IllegalArgumentException("Not a valid Library Event Id");
        }

        log.info("Validation is successful for the library Event : {}", libraryEventOptional.get());
    }
}
