package com.example.be_prac.service;

import com.example.be_prac.dto.VisaReqDto;
import com.example.be_prac.dto.VisaResDto;
import com.example.be_prac.entity.Country;
import com.example.be_prac.entity.Passport;
import com.example.be_prac.entity.Visa;
import com.example.be_prac.repository.CountryRepository;
import com.example.be_prac.repository.PassportRepository;
import com.example.be_prac.repository.VisaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VisaService {
    private final VisaRepository visaRepository;
    private final PassportRepository passportRepository;
    private final CountryRepository countryRepository;


    // 메소드 이름 수정이 필요할 듯
    public List<VisaResDto> getVisa(String passportNo) {
        Passport passport = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(passport == null) return null;

        // 엔티티에서 DTO로 옮기기 위한 배열 선언
        List<Visa> visas = visaRepository.findByPassport(passport);
        List<VisaResDto> dtos = new ArrayList<>();

        for(Visa visa : visas) dtos.add(visa.toDto());

        return dtos;
    }

    // 중복 코드 수정이 필요할듯
    public VisaResDto getVisaByPassportAndCountry(String passportNo, String countryCode) {
        Passport passport = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(passport == null) return null;

        Country country = countryRepository.findByCode(countryCode);
        if(country == null) return null;

        Visa visa = visaRepository.findByPassportAndCountry(passport, country);
        if(visa == null) return null;

        return visa.toDto();
    }

    public Visa makeVisa(VisaReqDto visaReqDto) {
        // 1. 여권 먼저 조회
        Passport passport = passportRepository.findByPassportNo(visaReqDto.getPassportNo()).orElse(null);
        if(passport == null) return null; // 여권이 없음
        // 2. 해당 국가 조회
        Country country = countryRepository.findByCode(visaReqDto.getCountryCode());
        if(country == null) return null;

        // 3. 이미 해당국가에 대해 발급된 비자인가.
        if(visaRepository.findByPassportAndCountry(passport, country) != null)
            return null;

        // 4. DTO에서 바로 저장할 수 있는 정보들만 저장.
        Visa created = visaReqDto.toEntity();
        if(created == null) return null;

        // 5. 이후 작업
        created.setPassport(passport);
        created.setCountry(country);

        return visaRepository.save(created);
    }

    @Transactional
    public VisaResDto deleteVisa(String passportNo, String countryCode) {

        Passport passport = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(passport == null) return null;

        Country country = countryRepository.findByCode(countryCode);
        if(country == null) return null;

        Visa visa = visaRepository.findByPassportAndCountry(passport, country);
        if(visa == null) return null;

        visaRepository.delete(visa);
        return visa.toDto();
    }
}
