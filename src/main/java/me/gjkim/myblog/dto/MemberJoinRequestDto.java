package me.gjkim.myblog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import jakarta.validation.constraints.Pattern;
import me.gjkim.myblog.domain.Member;
import me.gjkim.myblog.domain.Role;
import me.gjkim.myblog.utils.RegexUtil;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberJoinRequestDto {
    @NotBlank(message = "이메일 입력 필수")
    @Pattern(regexp = RegexUtil.EMAIL_REGEXP, message = "유효하지 않은 이메일입니다")
    private String username;

    @NotBlank(message = "비밀번호 입력 필수")
    @Pattern(regexp = RegexUtil.PASSWORD_REGEXP, message = "유효하지 않은 비밀번호입니다")
    private String password;

    @NotBlank(message = "권한 입력 필수")
    private String role; //ADMIN, USER, GUEST

    public Member toEntity(String encryptPassword) {
        return Member.builder()
                .username(this.username)
                .password(encryptPassword)
                .role(Role.valueOf(role))
                .build();
    }

    public void checkJoinUser(String secret) {
        if (role.equals(Role.ADMIN.name())) {
            return;
        }
    }
}