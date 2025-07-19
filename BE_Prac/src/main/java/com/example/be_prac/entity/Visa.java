package com.example.be_prac.entity;

import com.example.be_prac.dto.VisaResDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Visa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    public Visa(Passport passport, Country country, LocalDate startDate, LocalDate endDate) {
        this.passport = passport;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public VisaResDto toDto() {
        return new VisaResDto(this.id, this.passport.getPassportNo(), this.country.getName(), this.startDate.toString(), this.endDate.toString());
    }
}
