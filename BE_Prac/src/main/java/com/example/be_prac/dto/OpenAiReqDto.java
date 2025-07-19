package com.example.be_prac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// 해당 어노테이션은 Getter, Setter, ToString, equals(), hashCode()를 모두 만들어준답니다.
// 아래 구성은 OpenAI에서 요구하는 JSON형식과 동일하게 제작.
@Data
@AllArgsConstructor
public class OpenAiReqDto {
    private String model; // 이용할 모델을 설정.
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
