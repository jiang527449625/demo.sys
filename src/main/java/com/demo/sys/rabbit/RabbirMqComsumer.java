package com.demo.sys.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class RabbirMqComsumer {

    ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    @RabbitListener(queues = "${rabbitmq.departure.task.queues.demo}", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(String message) {
//		String msg=new String(message);
        log.info("revice message from rabbitMq {}", message);
        pool.execute(new SendMessage(message));
    }

    static class SendMessage implements Runnable {
        String message;

        public SendMessage(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println("=================="+message);
        }
    }

}
