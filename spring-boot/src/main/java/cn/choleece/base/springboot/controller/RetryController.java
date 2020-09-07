package cn.choleece.base.springboot.controller;

import cn.choleece.base.springboot.retry.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: controller of retry
 * @Date 2020-09-07 22:01
 **/
@RestController
@RequestMapping("/retry")
public class RetryController {
    @Resource
    private RetryService retryService;

    @GetMapping
    public String retry() {
        System.out.println(retryService.retryIfException());

        return "OK";
    }

}
