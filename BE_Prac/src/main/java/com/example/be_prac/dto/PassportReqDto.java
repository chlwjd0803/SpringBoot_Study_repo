package com.example.be_prac.dto;

import com.example.be_prac.entity.Passport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Getter
public class PassportReqDto {
    private Long id;
    private String countryCode;
    private String passportNo;
    private String fullName;
    private String birthDate;


    // 여권 번호는 BL13$$**** 형태로 만들어질것, 여기서 $는 알파벳 대문자, *은 0~9 범위의 정수
    public Passport toEntity() {
        if(this.fullName == null || this.countryCode == null) return null;
        if(this.passportNo == null || !this.passportNo.matches("BL13[A-Z]{2}[0-9]{4}")) return null;
        return new Passport(null, this.passportNo, this.fullName, LocalDate.parse(this.birthDate));
    }
}
