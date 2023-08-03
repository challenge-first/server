package challenge.competer.global.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LogTraceImplTest {

    @Autowired
    LogTrace logTrace;

    @Test
    @DisplayName("로깅 내역 확인")
    void logging() {

        TraceStatus traceStatus1 = logTrace.begin("Controller.read");
        TraceStatus traceStatus2 = logTrace.begin("Service.read");
        TraceStatus traceStatus3 = logTrace.begin("Repository.read");

        logTrace.end(traceStatus3);
        logTrace.end(traceStatus2);
        logTrace.end(traceStatus1);

        TraceStatus traceStatus4 = logTrace.begin("Controller.read");
        TraceStatus traceStatus5 = logTrace.begin("Service.read");
        TraceStatus traceStatus6 = logTrace.begin("Repository.read");

        logTrace.end(traceStatus6);
        logTrace.end(traceStatus5);
        logTrace.end(traceStatus4);
    }

    @Test
    @DisplayName("예외 로깅 내역 확인")
    void loggingEx() {

        TraceStatus traceStatus1 = logTrace.begin("Controller.read");
        TraceStatus traceStatus2 = logTrace.begin("Service.read");

        logTrace.exception(traceStatus2, new NullPointerException());
        logTrace.exception(traceStatus1, new NullPointerException());
    }
}