package com.example.be_prac.service;

import com.example.be_prac.dto.PassportReqDto;
import com.example.be_prac.dto.PassportResDto;
import com.example.be_prac.entity.Passport;
import com.example.be_prac.repository.CountryRepository;
import com.example.be_prac.repository.PassportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassportService {
    private final PassportRepository passportRepository;
    private final CountryRepository countryRepository;

    public PassportResDto getPassport(String passportNo) {
        // Optional를 통해 null 체크를 강제함, 코드상에서 의도 확실히 명시하기 위함
        Passport target = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(target == null)
            return null;
        else
            return target.toDto();
    }

    public List<PassportResDto> getAllPassport() {
        List<Passport> allPassport = passportRepository.findAll();
        List<PassportResDto> allPassportResDto = new ArrayList<>();
        // 엔티티의 보안 위험으로 인한 DTO변환
        for (Passport passport : allPassport) allPassportResDto.add(passport.toDto());

        return allPassportResDto;
    }

    public Passport makePassport(PassportReqDto passportReqDto) {
        // 이미 존재하는 여권번호는 제외하기 위함, 존재하지 않는 국가일때
        if(passportRepository.findByPassportNo(passportReqDto.getPassportNo()).isPresent()
         || countryRepository.findByCode(passportReqDto.getCountryCode()) == null)
            return null;

        Passport created = passportReqDto.toEntity();
        // 만약 여기서 null이면 그건 허용되지 않는 정보가 포함되거나 누락된 것임.
        if(created == null) return null;

        // 국가 연결
        created.setCountry(countryRepository.findByCode(passportReqDto.getCountryCode()));

        return passportRepository.save(created);
    }

    @Transactional
    public PassportResDto deletePassport(String passportNo) {
        Passport passport = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(passport == null) return null;
        else{
            passportRepository.delete(passport);
            return passport.toDto();
        }
    }

    @Transactional
    public PassportResDto updatePassport(String passportNo, PassportReqDto passportReqDto) {
        Passport original = passportRepository.findByPassportNo(passportNo).orElseThrow(
                () -> new EntityNotFoundException("여권번호가 존재하지 않아 수정 불가능합니다. : " + passportNo)
        );
        if(countryRepository.findByCode(passportReqDto.getCountryCode()) == null)
            throw new IllegalArgumentException("해당 국가코드는 존재하지 않습니다.");

        original.patch(passportReqDto);
        original.setCountry(countryRepository.findByCode(passportReqDto.getCountryCode()));
        passportRepository.save(original);
        return original.toDto();
    }
}
