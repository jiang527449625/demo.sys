package com.demo.sys.rabbit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MessageProduce {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 普通发送
     * @param routingKey
     * @param jsonObj
     */
    public void sendMessageMq(String routingKey,JSONObject jsonObj){
        rabbitTemplate.convertAndSend("amq.topic", routingKey,jsonObj.toString());
    }
}
