package com.example.kafkamodule.producer;

import com.alibaba.fastjson.JSON;
import com.example.kafkamodule.message.DemoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;


/**
 * @author xiaoaa
 * @date 2023/6/17 11:38
 **/
@Component
@Slf4j
public class TestProducer {


    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 异步发生消息
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public SendResult syncSend(Integer id) throws ExecutionException, InterruptedException {

        DemoMessage message = new DemoMessage(id);
        SendResult<Object, Object> sendResult = kafkaTemplate.send(DemoMessage.TOPIC, message).get();
        log.info(JSON.toJSONString(sendResult));
        return sendResult;
    }

}
