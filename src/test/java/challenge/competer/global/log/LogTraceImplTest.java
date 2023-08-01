package challenge.competer.global.log;

import org.junit.jupiter.api.Test;

class LogTraceImplTest {

    LogTraceImpl logTrace = new LogTraceImpl();

    @Test
    void logging() {

        TraceStatus traceStatus1 = logTrace.begin("Controller.read");
        TraceStatus traceStatus2 = logTrace.begin("Service.read");
        TraceStatus traceStatus3 = logTrace.begin("Repository.read");

        logTrace.end(traceStatus3);
        logTrace.end(traceStatus2);
        logTrace.end(traceStatus1);
    }
}