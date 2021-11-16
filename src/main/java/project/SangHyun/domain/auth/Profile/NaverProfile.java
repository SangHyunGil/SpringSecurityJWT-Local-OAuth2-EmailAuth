package project.SangHyun.domain.auth.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverProfile {
    Response response;

    @Data
    public class Response {
        private String email;
    }
}
