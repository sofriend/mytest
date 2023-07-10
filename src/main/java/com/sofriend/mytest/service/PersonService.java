package com.sofriend.mytest.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sofriend.mytest.repository.PersonRepository;
import com.sofriend.mytest.vo.Person;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;

    public Person save(Person person) {
        if (person.getAccount().length() != 5) {
            throw new RuntimeException("account length must be 5");
        }
        return personRepository.save(person);
    }

    /**
     * account 길이는 5자리
     * @param account
     * @return
     */
    public Person save(String account) {
        Person p = findByAccount(account);
        if (p == null) {
            p = Person.builder().account(account).build();
            save(p);
        } else {
            throw new RuntimeException("account was exists");
        }
        return p;
    }

    public Person findByAccount(String account) {
        Optional<Person> p = personRepository.findByAccount(account);
        return p.isEmpty() ? null : p.get();
    }
}
