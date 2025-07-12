package com.example.be_prac.repository;

import com.example.be_prac.entity.Country;
import com.example.be_prac.entity.Passport;
import com.example.be_prac.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Long> {
    List<Visa> findByPassport(Passport passport);

    Visa findByPassportAndCountry(Passport passport, Country country);
}
