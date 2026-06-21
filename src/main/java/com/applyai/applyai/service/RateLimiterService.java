package com.applyai.applyai.service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RateLimiterService {

    private final ConcurrentMap <Long, Bucket> userBuckets=new ConcurrentHashMap<>();

    public boolean tryConsume(Long userId){
        Bucket bucket = userBuckets.computeIfAbsent(userId, this::createNewBucket);
        return bucket.tryConsume(1);
    }
    private Bucket createNewBucket(Long userId) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofHours(1))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    
}
