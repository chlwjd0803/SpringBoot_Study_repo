package com.example.be_prac.dto;

import com.example.be_prac.entity.Passport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 여권 정보를 클라이언트와 주고받기 위한 DTO 입니다.
 */
@AllArgsConstructor
@ToString
@Getter
public class PassportReqDto {
    private Long id;
    private String passportNo;
    private String fullName;
    private String countryCode;

    // 여권 번호는 BL13$**** 형태로 만들어질것, 여기서 $는 알파벳 대문자, *은 0~9 범위의 정수
    public Passport toEntity() {
        if(this.fullName == null || this.countryCode == null) return null;
        if(this.passportNo == null || !this.passportNo.matches("BL13[A-Z]{2}[0-9]{4}")) return null;
        // toEntity 메소드는 이제 countryCode를 사용해야 합니다. (서비스 로직에서 처리 예정)
        // DTO는 데이터를 전달하는 역할에 집중하고, 엔티티 생성 로직은 서비스에서 처리하는 것이 더 좋습니다.
        // 여기서는 기본적인 생성자 호출만 남겨두거나, 혹은 서비스에서 직접 엔티티를 만드는 방식으로 변경할 수 있습니다.
        // 지금은 country_name 대신 countryCode.getName()을 사용하도록 수정할 수 있지만,
        // Country 엔티티와의 연관관계 매핑 후에는 로직이 변경될 것입니다.
        return new Passport(this.passportNo, this.fullName, null);
    }
}
