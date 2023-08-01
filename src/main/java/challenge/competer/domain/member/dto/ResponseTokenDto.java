package challenge.competer.domain.member.dto;


import lombok.Getter;

@Getter
public class ResponseTokenDto {

    private String headerName;
    private String accessTokenValue;

    public ResponseTokenDto(String headerName, String accessTokenValue) {
        this.headerName = headerName;
        this.accessTokenValue = accessTokenValue;
    }
}
