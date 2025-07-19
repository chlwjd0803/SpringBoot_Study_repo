package com.example.be_prac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PassportResDto {
    private Long id;
    private String passportNo;
    private String fullName;
    private String countryName;
    private String birthDate;
    private String issueDate;
    private String expiryDate;
}
