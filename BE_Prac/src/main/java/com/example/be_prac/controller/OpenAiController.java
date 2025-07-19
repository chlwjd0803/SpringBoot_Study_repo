package com.example.be_prac.controller;

import com.example.be_prac.dto.OpenAiReqDto;
import com.example.be_prac.dto.OpenAiResDto;
import com.example.be_prac.service.OpenAiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/openai")
public class OpenAiController {
    private final OpenAiService openAiService;

    @PostMapping("/chat")
    public OpenAiResDto chat(
            @RequestBody Map<String,String> userReq,  // { "message": "유저가 보낸 문장" }
            HttpSession session
    ) {
        // 1) 세션에서 이전에 만든 OpenAiReqDto 꺼내기
        OpenAiReqDto aiReq = (OpenAiReqDto) session.getAttribute("aiReq");
        if (aiReq == null) {
            // 최초 호출: 시스템 프롬프트 한 번만 세팅
            aiReq = new OpenAiReqDto("gpt-4.1-mini", new ArrayList<>());

            // system 메시지로 입국심사관 역할 + 종료조건 명시
            aiReq.getMessages().add(
                    new OpenAiReqDto.Message(
                            "system",
                            """
                            너는 입국심사관이야.
                            지원자의 답변을 듣고 계속 질문하다가,
                            더 물어볼 게 없으면 {"action":"approve"|"reject","reason":"20자 이내"} JSON만 출력해.
                            """
                    )
            );
            // 2) 유저 초기 메시지
            aiReq.getMessages().add(new OpenAiReqDto.Message("user", "안녕하세요. 입국심사 하러 왔습니다."));
        } else {
            // 2) 유저 메시지 추가
            String userMsg = userReq.get("message");
            aiReq.getMessages().add(new OpenAiReqDto.Message("user", userMsg));
        }

        // 3) OpenAI API 호출 (네가 이미 만든 서비스 메서드)
        OpenAiResDto aiRes = openAiService.getChatResponse(aiReq.getMessages());

        // 4) 어시스턴트 응답도 세션에 쌓아두기
        String assistantText = aiRes.getChoices().get(0).getMessage().getContent();
        aiReq.getMessages().add(
                new OpenAiReqDto.Message("assistant", assistantText)
        );

        // 5) 업데이트된 요청 DTO를 세션에 다시 저장
        session.setAttribute("aiReq", aiReq);

        return aiRes;
    }
}

