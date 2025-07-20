package com.example.be_prac.controller;

import com.example.be_prac.dto.*;
import com.example.be_prac.service.ImmigrationService;
import com.example.be_prac.service.PassportService;
import com.example.be_prac.service.VisaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/immigration")
public class ImmigrationController {
    private final ImmigrationService immigrationService;
    private final PassportService passportService;
    private final VisaService visaService;
    private final ObjectMapper jacksonObjectMapper;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(
            @RequestBody Map<String,String> userReq,  // { "message": "유저가 보낸 문장" }
            HttpSession session
    ) throws JsonProcessingException {
        // 1) 세션에서 이전에 만든 OpenAiReqDto 꺼내기
        OpenAiReqDto aiReq = (OpenAiReqDto) session.getAttribute("aiReq");
        if (aiReq == null) {
            // 최초 호출: 시스템 프롬프트 한 번만 세팅
            aiReq = new OpenAiReqDto("gpt-4.1-mini", new ArrayList<>());
            PassportVerifyDto verifyDto = (PassportVerifyDto) session.getAttribute("passport");

            if (verifyDto == null) throw new IllegalArgumentException("세션 정보 없음");

            String passportNo = verifyDto.getPassportNo();

            // 1. 여권 번호 기반으로 여권 응답dto 가져오기
            PassportResDto passportInfo = passportService.getPassport(passportNo);
            // 2. 여권 번호 기반으로 해당 국가 비자 들고오기
            List<VisaResDto> visaInfo = visaService.getVisa(passportNo);

            // system 메시지로 입국심사관 역할 + 종료조건 명시
            aiReq.getMessages().add(
                    new OpenAiReqDto.Message(
                            "system",
                                "너는 입국 심사관이야." +
                                        "지원자 여권정보 : " + passportInfo.toString() +
                                        "\n발급된 비자정보 : " + visaInfo.toString() +
                                        "\n현재 날짜와 시간 : " + LocalDate.now() +
                                        "\n지원자의 답변을 듣고 계속 질문하다가" +
                                        "더 물어볼 게 없으면 JSON으로만 반환해. 다른 형태는 안돼.\n" +
                                        "{\"action\":\"approve\"|\"reject\",\"reason\":\"20자 이내\"}" +
                                        "\n 그리고 지원자 여권정보나 비자정보가 올바르지 않다면 " +
                                        "사유와 함께 바로 반려해버리면 돼.\n" +
                                        "유저가 국가나 공항 정보로 어디에 왔는지에 대해 알려줄꺼야 " +
                                        "그런 정보가 누락되어 있다면 그냥 말하도록 유도하면 돼." +
                                        "상황이 여러개 존재하니까 혹시 방문인건지 귀국인건지도 파악하면 좋지." +
                                        "\n나머지는 너가 유연하게 파악해서 비자가 없다던지, 아니면 국가별로 무비자 입국도 가능하니, " +
                                        "그런 국제정세 부분들도 면밀히 조사해서 물어보고 승인 반려를 정하면 돼."
                    )
            );
        }
        // 2) 유저 메시지 추가
        String userMsg = userReq.get("message");
        aiReq.getMessages().add(new OpenAiReqDto.Message("user", userMsg));


        // 3) OpenAI API 호출 (네가 이미 만든 서비스 메서드)
        OpenAiResDto aiRes = immigrationService.getChatResponse(aiReq.getMessages());

        // 4) 어시스턴트 응답도 세션에 쌓아두기
        String assistantText = aiRes.getChoices().get(0).getMessage().getContent();
        aiReq.getMessages().add(
                new OpenAiReqDto.Message("assistant", assistantText)
        );

        // 5) 업데이트된 요청 DTO를 세션에 다시 저장
        session.setAttribute("aiReq", aiReq);

        // 6) 입국 심사가 종료되었는지 검사
        if(Pattern.compile("\"action\"\\s*:\\s*\"(approve|reject)\"")
                .matcher(assistantText)
                .find()){
            session.invalidate();
            JsonNode result = jacksonObjectMapper.readTree(assistantText);
            return ResponseEntity.ok(result);
        }
        else
            return ResponseEntity.ok(aiRes);
    }
}

