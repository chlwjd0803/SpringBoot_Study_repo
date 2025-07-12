package com.example.be_prac.controller;

import com.example.be_prac.dto.VisaReqDto;
import com.example.be_prac.dto.VisaResDto;
import com.example.be_prac.entity.Visa;
import com.example.be_prac.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visa")
@RequiredArgsConstructor
public class VisaController {
    private final VisaService visaService;

    // 여권 번호 기반 비자조회
    @GetMapping("/{passportNo}")
    public ResponseEntity<?> getVisa(@PathVariable String passportNo) {
        List<VisaResDto> findInfos = visaService.getVisa(passportNo);
        return (findInfos != null) ?
                ResponseEntity.ok(findInfos) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("여권 번호가 존재하지 않습니다.");
    }

    // 비자를 모든사람에게서 전체조회하는 기능은 좀 이상해서 넣지않음


    // 비자 발급
    @PostMapping
    public ResponseEntity<?> makeVisa(@RequestBody VisaReqDto visaReqDto) {
        Visa createdVisa = visaService.makeVisa(visaReqDto);
        return (createdVisa != null) ?
                ResponseEntity.created(URI.create("/api/v1/visa" + createdVisa.getPassport().getPassportNo())).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력 형식이 잘못되었습니다. 확인 바랍니다.");
    }



    // 비자 수정 사례도 존재하지 않아서 넣지않음


    // 비자 삭제
    @DeleteMapping("/{passportNo}/{countryCode}")
    public ResponseEntity<?> deleteVisa(@PathVariable String passportNo, @PathVariable String countryCode) {
        VisaResDto deletedVisa = visaService.deleteVisa(passportNo, countryCode);
        return (deletedVisa != null) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("여권번호가 존재하지 않거나, 삭제할 비자가 없습니다.");
    }

}
