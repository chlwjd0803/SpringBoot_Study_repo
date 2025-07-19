package com.example.be_prac.dto;

import com.example.be_prac.entity.Visa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Getter
public class VisaReqDto {
    private String passportNo;
    private String countryCode;

    public Visa toEntity() {
        return new Visa(null, null, LocalDate.now(), LocalDate.now().plusYears(1));
    }
}
