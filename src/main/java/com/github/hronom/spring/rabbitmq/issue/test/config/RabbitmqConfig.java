package com.github.hronom.spring.rabbitmq.issue.test.config;

import com.github.hronom.spring.rabbitmq.issue.test.properties.AppProperties;
import com.github.hronom.spring.rabbitmq.issue.test.properties.RabbitProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({AppProperties.class, RabbitProperties.class})
public class RabbitmqConfig {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
            rabbitProperties.getHostname(),
            rabbitProperties.getPort()
        );
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyTimeout(TimeUnit.SECONDS.toMillis(3));
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReplyTimeout(-1);
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue(appProperties.getQueueName(), false);
    }
}
