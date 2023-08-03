package challenge.competer.global.log;

import challenge.competer.domain.member.dto.RequestMemberPointDto;
import challenge.competer.domain.transaction.dto.RequestTransactionDto;
import challenge.competer.global.auth.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Aspect
public class LogAspect {

    private final LogTrace logTrace;

    @Pointcut("execution(* challenge.competer.domain..*(..))")
    public void all() {}


    @Pointcut("execution(* challenge.competer.domain.member.controller.MemberController.addPoint(..))")
    public void point() {}

    @Pointcut("execution(* challenge.competer.domain.transaction.controller.TransactionController.*(..))")
    public void transaction() {}

    @Before("point() && args(pointDto,memberDetails,..)")
    public void logAddPoint(RequestMemberPointDto pointDto, MemberDetails memberDetails) {
        log.info("[{}] point = {}", memberDetails.getUsername(), pointDto.getPoint());
    }

    @Before("transaction() && args(transactionDto,memberDetails,..)")
    public void logTransaction(RequestTransactionDto transactionDto, MemberDetails memberDetails) {
        log.info("[{}] transaction = {}", memberDetails.getUsername(),transactionDto.getPrice());
    }

    @Around("all()")
    public Object logAll(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
