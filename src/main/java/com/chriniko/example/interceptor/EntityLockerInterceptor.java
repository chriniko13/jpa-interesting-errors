package com.chriniko.example.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

@Aspect
@Component
public class EntityLockerInterceptor {

    private static final Map<String, ReadWriteLock> LOCK_MAPPINGS = new HashMap<String, ReadWriteLock>() {
        {
            put("Employees", new ReentrantReadWriteLock());
        }
    };

    @Pointcut(value = "@annotation(entityLocker)")
    void annotatedMethod(EntityLocker entityLocker) {
    }

    @Around(value = "annotatedMethod(entityLocker)")
    public Object around(ProceedingJoinPoint pjp, EntityLocker entityLocker) throws Throwable {

        String lockName = entityLocker.lockName();

        ReadWriteLock readWriteLock = getLock(lockName);
        EntityLockerType entityLockerType = entityLocker.lockerType();

        switch (entityLockerType) {
            case READ:
                return readLock(readWriteLock, pjpOperator(), pjp);

            case WRITE:
                return writeLock(readWriteLock, pjpOperator(), pjp);
        }

        throw new IllegalStateException();
    }

    private Function<ProceedingJoinPoint, Object> pjpOperator() {
        return pjp -> {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }

    private Object readLock(ReadWriteLock readWriteLock,
                            Function<ProceedingJoinPoint, Object> func,
                            ProceedingJoinPoint pjp) {
        readWriteLock.readLock().lock();
        try {
            return func.apply(pjp);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private Object writeLock(ReadWriteLock readWriteLock,
                             Function<ProceedingJoinPoint, Object> func,
                             ProceedingJoinPoint pjp) {
        readWriteLock.writeLock().lock();
        try {
            return func.apply(pjp);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private ReadWriteLock getLock(String lockName) {
        ReadWriteLock readWriteLock = LOCK_MAPPINGS.get(lockName);
        if (readWriteLock == null) {
            throw new IllegalStateException();
        }
        return readWriteLock;

    }

}
