/**
 * @author Administrator
 *
 */
package com.cloudside.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

	@Value("${spring.rabbitmq.queue}")
    private String queueName;
	
    public Queue rabbitMqQueue(){
        return new Queue(queueName);
    }
}