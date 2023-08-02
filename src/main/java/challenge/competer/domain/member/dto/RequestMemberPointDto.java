package challenge.competer.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class RequestMemberPointDto {

    private Long point;

    public RequestMemberPointDto(Long point) {
        this.point = point;
    }
}
