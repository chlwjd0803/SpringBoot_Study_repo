package com.example.be_prac.repository;

import com.example.be_prac.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {

    List<Passport> findAll();

    Optional<Passport> findByPassportNo(String passportNo);
}
