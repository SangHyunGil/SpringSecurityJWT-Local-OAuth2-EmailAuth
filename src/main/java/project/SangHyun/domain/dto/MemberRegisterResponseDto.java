package project.SangHyun.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRegisterResponseDto {
    private Long id;
    private String email;

    @Builder
    public MemberRegisterResponseDto(Long id, String email, String provider) {
        this.id = id;
        this.email = email;
    }
}
