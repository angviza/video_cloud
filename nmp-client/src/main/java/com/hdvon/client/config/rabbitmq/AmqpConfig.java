/*package com.hdvon.client.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hdvon.client.config.ClientConfig;
import com.hdvon.client.config.SystemConstant;

*//**
 * 
 * 创建消息生产者
 * 
 * @author wanshaojian
 *//*
@Configuration
public class AmqpConfig {

	@Autowired
	private ClientConfig clientConfig;

	*//**
	 * 连接rabbitmq
	 * 
	 * @return
	 *//*
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(clientConfig.getMqHost() + ":" + clientConfig.getMqPort());
		connectionFactory.setUsername(clientConfig.getMqUsername());
		connectionFactory.setPassword(clientConfig.getMqPassword());
		*//**
		 * 对于每一个RabbitTemplate只支持一个ReturnCallback。 对于返回消息，模板的mandatory属性必须被设定为true，
		 * 它同样要求CachingConnectionFactory的publisherReturns属性被设定为true。
		 * 如果客户端通过调用setReturnCallback(ReturnCallback
		 * callback)注册了RabbitTemplate.ReturnCallback，那么返回将被发送到客户端。 这个回调函数必须实现下列方法： void
		 * returnedMessage(Message message, intreplyCode, String replyText,String
		 * exchange, String routingKey);
		 *//*
		// connectionFactory.setPublisherReturns(true);
		*//**
		 * 同样一个RabbitTemplate只支持一个ConfirmCallback。
		 * 对于发布确认，template要求CachingConnectionFactory的publisherConfirms属性设置为true。
		 * 如果客户端通过setConfirmCallback(ConfirmCallback
		 * callback)注册了RabbitTemplate.ConfirmCallback，那么确认消息将被发送到客户端。 这个回调函数必须实现以下方法：
		 * void confirm(CorrelationData correlationData, booleanack);
		 *//*
		connectionFactory.setPublisherConfirms(clientConfig.getMqPublisherconfirms());

		return connectionFactory;
	}


	*//**
	 * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*”
	 * 只会匹配到“audit.irs” 默认：, durable = true, autoDelete = false
	 * 
	 * @return
	 *//*
	@Bean
	TopicExchange contractTopicExchangeDurable() {
		TopicExchange contractTopicExchange = new TopicExchange(SystemConstant.CONTRACT_TOPIC);
		return contractTopicExchange;
	}

	*//**
	 * 处理路由键。需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键
	 * “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog
	 * 
	 * @return
	 *//*
	@Bean
	DirectExchange contractDirectExchange() {
		DirectExchange contractDirectExchange = new DirectExchange(SystemConstant.CONTRACT_DIRECT);
		return contractDirectExchange;
	}

	@Bean
	Queue queueCamera() {
		Queue queue = new Queue(SystemConstant.QUEUE_ES_CAMERA, true);
		return queue;
	}

	@Bean
	Queue queueCameraGroup() {
		Queue queue = new Queue(SystemConstant.QUEUE_ES_CAMERA_GROUP, true);
		return queue;
	}

	@Bean
	Queue queueUserCamera() {
		Queue queue = new Queue(SystemConstant.QUEUE_ES_USER_CAMERA, true);
		return queue;
	}

	@Bean
	Queue queuePlanCamera() {
		Queue queue = new Queue(SystemConstant.QUEUE_ES_PLAN_CAMERA, true);
		return queue;
	}

	*//**
	 * 绑定queue和exchange
	 * 
	 * @param queuePlanCamera
	 * @param exchange
	 * @return
	 *//*
	@Bean
	Binding bindingExchangeCamera(Queue queueCamera, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(queueCamera).to(exchange).with(SystemConstant.QUEUE_ES_CAMERA);
		return binding;
	}

	*//**
	 * 绑定queue和exchange
	 * 
	 * @param queuePlanCamera
	 * @param exchange
	 * @return
	 *//*
	@Bean
	Binding bindingExchangeCameraGroup(Queue queueCameraGroup, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(queueCameraGroup).to(exchange).with(SystemConstant.QUEUE_ES_CAMERA_GROUP);
		return binding;
	}

	*//**
	 * 绑定queue和exchange
	 * 
	 * @param queuePlanCamera
	 * @param exchange
	 * @return
	 *//*
	@Bean
	Binding bindingExchangeUserCamera(Queue queueUserCamera, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(queueUserCamera).to(exchange).with(SystemConstant.QUEUE_ES_USER_CAMERA);
		return binding;
	}

	*//**
	 * 绑定queue和exchange
	 * 
	 * @param queuePlanCamera
	 * @param exchange
	 * @return
	 *//*
	@Bean
	Binding bindingExchangePlanCamera(Queue queuePlanCamera, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(queuePlanCamera).to(exchange).with(SystemConstant.QUEUE_ES_PLAN_CAMERA);
		return binding;
	}
}
*/