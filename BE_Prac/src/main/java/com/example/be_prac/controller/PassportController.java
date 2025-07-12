package com.example.be_prac.controller;

import com.example.be_prac.dto.PassportReqDto;
import com.example.be_prac.dto.PassportResDto;
import com.example.be_prac.entity.Passport;
import com.example.be_prac.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passport")

public class PassportController {
    private final PassportService passportService;

    // 여권 정보 개별 조회
    @GetMapping("/{passportNo}")
    public ResponseEntity<?> getPassport(@PathVariable String passportNo) {
        PassportResDto findInfo = passportService.getPassport(passportNo);
        return (findInfo != null) ?
                ResponseEntity.ok(findInfo) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("찾을 수 없습니다. 여권 번호를 확인해주세요.");
//                ResponseEntity.notFound().build();
        // 위 두개의 형식의 차이를 알기 -> 메시지를 넣을 수 있는 형식과 아닌 형식 외에 다른 점이 있는것일까
    }

    // 여권 정보 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllPassport() {
        List<PassportResDto> allOfPassport = passportService.getAllPassport();
        return ResponseEntity.ok(allOfPassport);
    }

    // 여권 정보 생성
    @PostMapping
    public ResponseEntity<?> makePassport(@RequestBody PassportReqDto passportReqDto) {
        Passport createdPassport = passportService.makePassport(passportReqDto);
        return (createdPassport != null) ?
                ResponseEntity.created(URI.create("/api/v1/passport/" + createdPassport.getPassportNo())).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력 형식이 잘못되었습니다. 확인 바랍니다.");
//                ResponseEntity.badRequest().build();
        // 여기도 위 두 형식 차이 마찬가지로
    }

    // 여권 정보 수정
    @PatchMapping("/{passportNo}")
    public ResponseEntity<?> updatePassport(@PathVariable String passportNo, @RequestBody PassportReqDto passportReqDto) {
        PassportResDto updated = passportService.updatePassport(passportNo, passportReqDto);
        return (updated != null) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 정보 입력을 정확히 입력해주세요.");
    }

    // 여권 정보 삭제
    @DeleteMapping("/{passportNo}")
    public ResponseEntity<?> deletePassport(@PathVariable String passportNo) {
        PassportResDto deletedPassport = passportService.deletePassport(passportNo);
        return (deletedPassport != null) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("찾을 수 없습니다. 여권 번호를 확인해주세요.");
    }

}
