package com.scm.product.management.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallback implements UserClient{
    @Override
    public boolean checkUserExists(Integer userId) {
        log.info("User Service Down !! Fallback executed for user {}", userId);
        return false;
    }
}
