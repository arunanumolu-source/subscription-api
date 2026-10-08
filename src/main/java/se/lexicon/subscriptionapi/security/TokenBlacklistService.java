package se.lexicon.subscriptionapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long remainingExpirationMillis) {

        if (remainingExpirationMillis <= 0) {
            return;
        }

        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + token,
                "blacklisted",
                Duration.ofMillis(remainingExpirationMillis)
        );
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(BLACKLIST_PREFIX + token)
        );
    }
}