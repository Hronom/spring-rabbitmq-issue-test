package com.github.hronom.spring.rabbitmq.issue.test.service;

import com.github.hronom.spring.rabbitmq.issue.test.properties.AppProperties;

import net.moznion.random.string.RandomStringGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(AppProperties.class)
public class ProducerService implements AutoCloseable {
    private final Log logger = LogFactory.getLog(getClass());

    private final String stringPattern =
        "Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!";
    private final RandomStringGenerator generator = new RandomStringGenerator();

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private RabbitTemplate template;

    @Scheduled(fixedDelay = 5000)
    public void doSomething() {
        logger.info("Generate random string...");
        String msg = generator.generateFromPattern(stringPattern);
        logger.info("Emit to \"" + appProperties.getQueueName() + "\" message: \"" + msg + "\"");
        template.convertAndSend(appProperties.getQueueName(), msg);
    }

    @Override
    public void close() throws Exception {
        String msg = "Closing " + this.toString();
        logger.info("Emit to \"" + appProperties.getQueueName() + "\" message: \"" + msg + "\"");
        template.convertAndSend(appProperties.getQueueName(), msg);
    }
}
