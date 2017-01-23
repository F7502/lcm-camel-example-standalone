package de.ustutt.iaas.lcm.labs.standalone;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bean1 implements MessageListener {

	final Logger logger = LoggerFactory.getLogger(Bean1.class);

	public void onMessage(Message message) {
		logger.debug("received message. {}", message);
		if (message instanceof TextMessage) {
			try {
				// http://www.mkyong.com/java/jackson-tree-model-example/
				String body = ((TextMessage) message).getText();
				ObjectMapper mapper = new ObjectMapper();
				JsonNode article = mapper.readTree(body);
				String id = article.get(JSONConstants.FIELD_ART_ID).asText();
				String name = article.get(JSONConstants.FIELD_ART_NAME).asText();
				String cat = article.get(JSONConstants.FIELD_ART_CAT).asText();
				String producer = article.get(JSONConstants.FIELD_ART_PROD).asText();
				double weight = article.get(JSONConstants.FIELD_ART_WEIGHT).asDouble();
				double price = article.get(JSONConstants.FIELD_ART_PRICE).asDouble();

				logger.info("Received article '{}' ({}), produced by '{}', category '{}', weigth = {} and price = {}",
						new Object[] { name, id, producer, cat, weight, price });

			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}