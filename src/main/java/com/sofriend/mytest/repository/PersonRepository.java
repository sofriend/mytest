package com.sofriend.mytest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sofriend.mytest.vo.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    public Optional<Person> findByAccount(String account);
    
}
