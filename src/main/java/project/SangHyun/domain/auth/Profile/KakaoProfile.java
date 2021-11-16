package project.SangHyun.domain.auth.Profile;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoProfile {
    KakaoAccount kakao_account;

    @Data
    public class KakaoAccount {
        private String email;
    }
}
