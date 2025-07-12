package com.example.be_prac.dto;

import com.example.be_prac.entity.Country;
import lombok.Getter;
import lombok.Setter;

/**
 * 국가 정보를 클라이언트와 주고받기 위한 DTO(Data Transfer Object)입니다.
 * 엔티티를 직접 노출하지 않고, 필요한 데이터만 담아 전달하는 역할을 합니다.
 */
@Getter
@Setter
public class CountryDto {
    /**
     * 국가 코드를 나타냅니다.
     * 클라이언트로부터 요청을 받을 때 "KR"과 같은 문자열을 받으면,
     * Spring이 자동으로 CountryCode.KR Enum 타입으로 변환해줍니다.
     */
    private String code;

    /**
     * 국가의 전체 이름(한글)입니다.
     * 이 값은 보통 서버에서 code를 기반으로 결정하여 클라이언트에게 응답으로 보내줄 때 사용됩니다.
     */
    private String name;

    /**
     * Country 엔티티 객체를 CountryDto 객체로 변환하는 정적 메소드입니다.
     * 서비스 계층에서 데이터베이스로부터 조회한 엔티티를 클라이언트에게 보낼 DTO로 변환할 때 유용합니다.
     * @param country 변환할 Country 엔티티 객체
     * @return 변환된 CountryDto 객체
     */
    public static CountryDto fromEntity(Country country) {
        CountryDto dto = new CountryDto();
        dto.setCode(country.getCode());
        dto.setName(country.getName());
        return dto;
    }
}
