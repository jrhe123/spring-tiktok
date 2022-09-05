package com.imooc.base;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {

	/*
	 * 1. define exchange
	 * 2. define queue
	 * 3. create exchange
	 * 4. create queue
	 * 5. bind exchange & queue
	 * 
	 * https://rabbitmq.com/getstarted.html
	 */
	public static final String EXCHANGE_MSG = "exchange_msg";
	public static final String QUEUE_SYS_MSG = "queue_sys_msg";
	
	@Bean(EXCHANGE_MSG)
	public Exchange exchange() {
		return ExchangeBuilder
				.topicExchange(EXCHANGE_MSG) // topic type
				.durable(true)				 // durable after reboot
				.build();
	}
	
	@Bean(QUEUE_SYS_MSG)
	public Queue queue() {
		return new Queue(QUEUE_SYS_MSG, true);
	}
	
	@Bean
	public Binding binding(
			@Qualifier(EXCHANGE_MSG) Exchange exchange,
			@Qualifier(QUEUE_SYS_MSG) Queue queue
			) {
		return BindingBuilder
				.bind(queue)
				.to(exchange)
				.with("sys.msg.*")	// route pattern
				.noargs();
		
		/**
		 * route pattern:
		 * 
		 * "*" only take one -> sys.msg.a
		 * "#" allow multiple -> sys.msg.a.b.c
		 */
	}
	
}
