package challenge.competer.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LogAspect {

    @Pointcut("execution(* challenge.competer.domain.*(..))")
    public void all() {}

//    @Pointcut("execution(* challenge.competer.domain.*Controller.*(..))")
//    public void controller() {}
//
//    @Pointcut("execution(* challenge.competer.domain.*Service.*(..))")
//    public void service() {}
//
//    @Pointcut("execution(* challenge.competer.domain.*Repository.*(..))")
//    public void repository() {}

}
