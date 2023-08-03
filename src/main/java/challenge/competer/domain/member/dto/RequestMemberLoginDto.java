package challenge.competer.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor
public class RequestMemberLoginDto {

    private String username;

    private String password;
}
