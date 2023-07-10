package com.sofriend.mytest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sofriend.mytest.service.PersonService;
import com.sofriend.mytest.vo.Person;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PutMapping(value="/person/add")
    public ResponseEntity<Person> personAdd(Person person) {
        return new ResponseEntity<>(personService.save(person), HttpStatus.OK);
    }
}
