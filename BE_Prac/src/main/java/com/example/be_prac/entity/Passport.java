package com.example.be_prac.entity;

import com.example.be_prac.dto.PassportReqDto;
import com.example.be_prac.dto.PassportResDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor

public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_code")
    private Country country;

    @Column(nullable = false, unique = true)
    private String passportNo;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    // 빌더를 통한 생성자 실행, 주입하지 않은 정보는 null 처리됨
    @Builder
    public Passport(Country country, String passportNo, String fullName, LocalDate birthDate) {
        this.country = country;
        this.passportNo = passportNo;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.issueDate = LocalDate.now(); // 생성 시점의 날짜를 저장하면 됨
        this.expiryDate = LocalDate.now().plusYears(10);
    }

    public PassportResDto toDto() {
        return new PassportResDto(this.id, this.passportNo, this.fullName, this.country.getName(),
                this.birthDate.toString(), this.issueDate.toString(), this.expiryDate.toString());
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
