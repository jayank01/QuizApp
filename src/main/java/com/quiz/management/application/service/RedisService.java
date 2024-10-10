package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> redisTemplate;

    public <T> T get(String key, Class<T> entity) throws JsonProcessingException {
        var object = redisTemplate.opsForValue().get(key);
        if(object == null) return null;
        return objectMapper.convertValue(object,entity);
    }

    public void set(String key, Object object, Long ttl) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key,object,ttl, TimeUnit.SECONDS);
    }
}
