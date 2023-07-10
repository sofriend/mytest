package com.sofriend.mytest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sofriend.mytest.vo.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    public Optional<Person> findByAccount(String account);
    
}
