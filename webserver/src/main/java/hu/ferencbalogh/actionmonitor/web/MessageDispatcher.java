package hu.ferencbalogh.actionmonitor.web;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageDispatcher {
	@Autowired
	private SimpMessagingTemplate template;

	@PostConstruct
	public void send(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (template != null) {
						System.out.println("JEE");
						sendMessage(System.currentTimeMillis() + "");
					} else {
						System.out.println("BAH");
					}
				}
			}
		}).start();
	}

	public void sendMessage(String message) {
		template.convertAndSend("/actions", message);
	}
}