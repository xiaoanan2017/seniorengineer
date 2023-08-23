package com.example.kafkamodule.controller;

import com.example.kafkamodule.producer.TestProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoaa
 * @date 2023/6/17 11:38
 **/
@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @Autowired
    TestProducer testProducer;

    @RequestMapping("/{id}")
    public SendResult send(@PathVariable(value = "id") Integer id) {
        SendResult sendResult = null;
        try {
            sendResult = testProducer.syncSend(id);
        } catch (Exception e) {
           log.error(e.getMessage(), e);
        }
        return sendResult;
    }
}
