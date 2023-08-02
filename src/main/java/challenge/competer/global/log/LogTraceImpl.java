package challenge.competer.global.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogTraceImpl implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        
        TraceId traceId = traceIdHolder.get();
        Long startTime = System.currentTimeMillis();
        
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        
        return new TraceStatus(traceId, startTime, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();

        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        }

        if (traceId != null) {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - status.getStartTime();

        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    elapsedTime);
        }

        if (e != null) {
            log.info("[{}] {}{} time={} ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    elapsedTime,
                    e.toString());
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        traceIdHolder.set(traceId.createPreviousId());

        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++) {
            if (i != level - 1) {
                sb.append("| ");
            }

            if (i == level - 1) {
                sb.append("|" + prefix);
            }
        }

        return sb.toString();
    }
}
