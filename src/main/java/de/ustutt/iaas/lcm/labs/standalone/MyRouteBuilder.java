package de.ustutt.iaas.lcm.labs.standalone;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;

import com.fasterxml.jackson.databind.JsonNode;

class MyRouteBuilder extends RouteBuilder {
		
	@Override
	public void configure() throws Exception {
		
		from("timer:foo?delay={{delaymillisecs}}&period={{periodmillisecs}}")
		.process(new Processor() {
					public void process(Exchange exchange) throws Exception {
							System.out.println("Invoked timer at " + new Date());
					}
				})
		.bean("foo")
		.noAutoStartup();

		// test route 1
		from("bla:queue:A")
		.log(LoggingLevel.INFO, "...msg...")
		.to("bla:topic:B")
		.bean("foo") // only sysout
		.to("log:bla")
		.bean("bean1") // JMS onMessage, unmarshal text into JsonNode
		.routeId("route1");
		
		// JSON text <-> JsonNode object
		DataFormat jsonFormat = new JacksonDataFormat(JsonNode.class);
		
		// test route 2
		from("bla:topic:B")
		.to("log:route2")
		//.unmarshal().json(JsonLibrary.Jackson, JsonNode.class, true)
		.unmarshal(jsonFormat)
			.description("JSON_text2node", "JSON text -> JsonNode object", null)
		.to("log:route2")
		.bean("debugBean")
			.description("debugBean", "prints information about the camel message body to sysout", null)
		//.marshal().json(JsonLibrary.Jackson, JsonNode.class, true)
		.marshal(jsonFormat)
			.description("JSON_node2text", "JsonNode object -> JSON text", null)
		.to("log:route2")
		.bean("debugBean")
		.to("bla:queue:C")
		// set route ID
		.routeId("route2");
		
		from("bla:queue:C")
		.to("log:route3")
		.choice()
		    .when().jsonpath("$[?(@.category == 'cat-A')]")
		    	.log("choice 1, cat-A")
			.when().jsonpath("$[?(@.category == 'cat-B')]")
				.log("choice 2, cat-B")
			.otherwise()
				.log("otherwise")
			.end()
		.to("log:route3")
		.routeId("route3");
		
	}

}