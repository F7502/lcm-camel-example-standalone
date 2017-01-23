package de.ustutt.iaas.lcm.labs.standalone;

import org.apache.camel.Exchange;

public class DebugBean {
	
	public void process(Exchange exc) {
		Object o = exc.getIn().getBody();
		System.out.println("in.body: "+o);
		System.out.println("in.body.class: "+o.getClass().getName());
	}

}