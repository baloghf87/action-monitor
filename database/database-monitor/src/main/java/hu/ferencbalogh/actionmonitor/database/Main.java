package hu.ferencbalogh.actionmonitor.database;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(DatabaseMonitorConfiguration.class);
		DatabaseMonitor dm = ctx.getBean(DatabaseMonitor.class);
	}
}
