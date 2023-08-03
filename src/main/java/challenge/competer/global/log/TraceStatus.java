package challenge.competer.global.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public class TraceStatus {

    private TraceId traceId;
    private Long startTime;
    private String message;
}
