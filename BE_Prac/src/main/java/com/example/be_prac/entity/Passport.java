package com.example.be_prac.entity;

import com.example.be_prac.dto.PassportReqDto;
import com.example.be_prac.dto.PassportResDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor

public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String passportNo;

    @Column(nullable = false)
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "country_code")
    private Country country;

    // 빌더를 통한 생성자 실행, 주입하지 않은 정보는 null 처리됨
    @Builder
    public Passport(String passportNo, String fullName, Country country) {
        this.passportNo = passportNo;
        this.fullName = fullName;
        this.country = country;
    }

    public PassportResDto toDto() {
        return new PassportResDto(this.id, this.passportNo, this.fullName, this.country.getName());
    }

    public void patch(PassportReqDto passportReqDto) {
        if(passportReqDto.getFullName() != null && !passportReqDto.getFullName().equals(this.fullName)
                && !passportReqDto.getPassportNo().matches("BL13[A-Z]{2}[0-9]{4}"))
            this.passportNo = passportReqDto.getPassportNo();

        if(passportReqDto.getFullName() != null && !passportReqDto.getFullName().equals("")
                && !passportReqDto.getFullName().equals(this.fullName))
            this.fullName = passportReqDto.getFullName();
    }
}
