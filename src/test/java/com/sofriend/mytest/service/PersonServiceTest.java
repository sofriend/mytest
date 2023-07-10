package com.sofriend.mytest.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.sofriend.mytest.vo.Person;

@SpringBootTest
@Rollback(true)
public class PersonServiceTest {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PersonService personService;

    @Test
    public void testSave() {
        Person p = personService.save(Person.builder().account("23456").build());
        log.info("person id : {}", p.getId());
    }
}
