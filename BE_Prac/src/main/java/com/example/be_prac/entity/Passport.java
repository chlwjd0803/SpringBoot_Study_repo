package com.example.be_prac.entity;

import com.example.be_prac.dto.PassportDto;
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

    @Column(nullable = false)
    private String nation;

    // 빌더를 통한 생성자 실행, 주입하지 않은 정보는 null 처리됨
    @Builder
    public Passport(String passportNo, String fullName, String nation) {
        this.passportNo = passportNo;
        this.fullName = fullName;
        this.nation = nation;
    }

    public PassportDto toDto() {
        return new PassportDto(this.id, this.passportNo, this.fullName, this.nation);
    }

    public void patch(PassportDto passportDto) {
        if(passportDto.getFullName() != null && !passportDto.getFullName().equals(this.fullName)
                && !passportDto.getPassportNo().matches("BL13[A-Z]{2}[0-9]{4}"))
            this.passportNo = passportDto.getPassportNo();

        if(passportDto.getFullName() != null && !passportDto.getFullName().equals("")
                && !passportDto.getFullName().equals(this.fullName))
            this.fullName = passportDto.getFullName();

        if(passportDto.getNation() != null && !passportDto.getNation().equals("")
                && !passportDto.getNation().equals(this.nation))
            this.nation = passportDto.getNation();

    }
}
