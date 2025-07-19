//package com.example.be_prac.service;
//
//import com.example.be_prac.dto.OpenAiReqDto;
//import com.example.be_prac.dto.OpenAiResDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OpenAiService {
//
//    @Value("${openai.api.key}")
//    private String key;
//
//    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
//
//    public String getChatResponse(String prompt) {
//        // 1. 메시지 생성
//        OpenAiReqDto.Message userMessage = new OpenAiReqDto.Message("user", prompt);
//
//        // 2. 요청 객체 생성
//        OpenAiReqDto req = new OpenAiReqDto("gpt-4.1-mini", List.of(userMessage));
//
//        // 3. HTTP 헤더 구성
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(key);
//
//        // 4. HTTP 요청 생성
//        HttpEntity<OpenAiReqDto> httpEntity = new HttpEntity<>(req, headers);
//
//        // 5. API 호출
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<OpenAiResDto> res = restTemplate.exchange(
//                API_URL,
//                HttpMethod.POST,
//                httpEntity,
//                OpenAiResDto.class
//        );
//
//        // 예외 방어
//        if(res.getBody() == null)
//            throw new IllegalArgumentException("응답이 비어있거나 잘못됨");
//
//        // 6. 응답에서 content 추출
//        return res.getBody().getChoices().get(0).getMessage().getContent();
//    }
//
//
//}
