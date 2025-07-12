package com.example.be_prac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class VisaResDto {
    private Long id;
    private String passportNo;
    private String countryCode;
    private String startDate;
    private String endDate;
}
