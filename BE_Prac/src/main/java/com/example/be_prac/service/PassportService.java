package com.example.be_prac.service;

import com.example.be_prac.dto.PassportDto;
import com.example.be_prac.entity.Passport;
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

    public PassportDto getPassport(String passportNo) {
        // Optional를 통해 null 체크를 강제함, 코드상에서 의도 확실히 명시하기 위함
        Passport target = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(target == null)
            return null;
        else
            return target.toDto();
    }

    public List<PassportDto> getAllPassport() {
        List<Passport> allPassport = passportRepository.findAll();
        List<PassportDto> allPassportDto = new ArrayList<>();
        // 엔티티의 보안 위험으로 인한 DTO변환
        for (Passport passport : allPassport) allPassportDto.add(passport.toDto());

        return allPassportDto;
    }

    public Passport makePassport(PassportDto passportDto) {
        // 이미 존재하는 여권번호는 제외하기 위함
        if(passportRepository.findByPassportNo(passportDto.getPassportNo()).isPresent())
            return null;

        Passport created = passportDto.toEntity();
        // 만약 여기서 null이면 그건 허용되지 않는 정보가 포함되거나 누락된 것임.

        if(created == null) return null;
        else{
            return passportRepository.save(created);
        }
    }

    @Transactional
    public PassportDto deletePassport(String passportNo) {
        Passport passport = passportRepository.findByPassportNo(passportNo).orElse(null);
        if(passport == null) return null;
        else{
            passportRepository.delete(passport);
            return passport.toDto();
        }
    }

    public PassportDto updatePassport(String passportNo, PassportDto passportDto) {
        Passport original = passportRepository.findByPassportNo(passportNo).orElseThrow(
                () -> new EntityNotFoundException("여권번호가 존재하지 않아 수정 불가능합니다. : " + passportNo)
        );

        original.patch(passportDto);
        passportRepository.save(original);
        return original.toDto();
    }
}
