package hu.ferencbalogh.actionmonitor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hu.ferencbalogh.actionmonitor.activemq.receiver.MessageReceiver;
import hu.ferencbalogh.actionmonitor.activemq.receiver.MessageReceiver.MessageListener;
import hu.ferencbalogh.actionmonitor.activemq.sender.MessageSender;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("hu.ferencbalogh.actionmonitor.activemq");
		
		MessageReceiver mr = ctx.getBean(MessageReceiver.class);
		mr.register(new MessageListener() {

			public void onMessage(String message) {
				System.out.println("message received: " + message);
			}
		});
		
		MessageSender ms = ctx.getBean(MessageSender.class);
		ms.sendMessage("buuuuu");
		Thread.sleep(2000);
		ms.sendMessage("ez... de amugy jo");
		Thread.sleep(1000);
		ms.sendMessage("es ize");
	}
}
