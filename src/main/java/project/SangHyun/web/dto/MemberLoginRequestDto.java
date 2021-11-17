package project.SangHyun.web.dto;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequestDto {
    private String email;
    private String password;
}
