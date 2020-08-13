package com.scm.product.management.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ms-user-management", url = "http://localhost:8011/usermanagementservices")
public interface UserService {

    @GetMapping("/checkuserexists/{userid}")
    public boolean checkUserExists(@PathVariable("userid") Integer userId);
}
