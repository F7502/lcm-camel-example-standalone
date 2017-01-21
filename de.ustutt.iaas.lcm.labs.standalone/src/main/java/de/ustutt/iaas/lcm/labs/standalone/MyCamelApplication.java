package de.ustutt.iaas.lcm.labs.standalone;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;
import org.apache.camel.main.MainSupport;

/**
 * taken from
 * http://camel.apache.org/running-camel-standalone-and-have-it-keep-running.
 * html
 */
public class MyCamelApplication {

	private Main main;

	public static void main(String[] args) throws Exception {
		MyCamelApplication myApp = new MyCamelApplication();
		myApp.boot();
	}

	public void boot() throws Exception {
		// create a Main instance
		main = new Main();
		// bind MyBean into the registry
		main.bind("foo", new MyBean());
		// add routes
		main.addRouteBuilder(new MyRouteBuilder());
		// add event listener
		main.addMainListener(new Events());

		PropertiesComponent pc = new PropertiesComponent("my.properties");
		main.bind("properties", pc);

		// run until you terminate the JVM
		System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
		main.run();
	}

	public static class Events extends MainListenerSupport {

		@Override
		public void afterStart(MainSupport main) {
			System.out.println("MyCamelApplication with Camel is now started!");
		}

		@Override
		public void beforeStop(MainSupport main) {
			System.out.println("MyCamelApplication with Camel is now being stopped!");
		}
	}

}
