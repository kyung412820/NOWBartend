package com.nowbartend.global.aop.aspect;

import com.nowbartend.global.aop.annotation.DistributedLock;
import com.nowbartend.global.exception.DistributedLockException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = getLockKey(joinPoint, distributedLock.key());
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();
        TimeUnit timeUnit = distributedLock.unit();

        RLock lock = redissonClient.getLock(key);

        try {
            boolean available = lock.tryLock(waitTime, leaseTime, timeUnit);

            if (!available) {
                throw new DistributedLockException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            return joinPoint.proceed();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        // 메서드 인자 가져오기
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 파라미터 이름 가져오기
        String[] parameterNames = signature.getParameterNames();

        // Spring Expression Language 로 파싱하여 읽어옴
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        Object request = context.lookupVariable("request");

        // SpEL로 request.date 존재하면 가져오기
        if (request != null && hasField(request, "date")) {
            LocalDateTime date = parser.parseExpression("#request.date").getValue(context, LocalDateTime.class);

            if (date != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
                String formattedDate = date.format(formatter);
                context.setVariable("formattedDate", formattedDate);
            }
        }

        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }

    public boolean hasField(Object object, String fieldName) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }

        return false;
    }
}
