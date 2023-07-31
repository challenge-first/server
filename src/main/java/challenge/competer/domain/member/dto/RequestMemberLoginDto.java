package challenge.competer.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class RequestMemberLoginDto {

    private String username;

    private String password;
}
