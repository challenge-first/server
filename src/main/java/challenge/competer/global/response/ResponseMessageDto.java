package challenge.competer.global.response;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ResponseMessageDto {

    private String message;
    private int statusCode;
    private String statusMessage;
}
