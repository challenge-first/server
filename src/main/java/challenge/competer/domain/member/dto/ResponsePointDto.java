package challenge.competer.domain.member.dto;

import lombok.Getter;

@Getter
public class ResponsePointDto {

    private Long point;

    public ResponsePointDto(Long point) {
        this.point = point;
    }
}
