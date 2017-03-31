package hu.ferencbalogh.actionmonitor.web.integration;

import hu.ferencbalogh.actionmonitor.web.Application;

public class ApplicationThread extends Thread {
	public ApplicationThread(Object startedSync, Object exitSync) {
		super(new Runnable() {
			@Override
			public void run() {
				try {
					Application.main(new String[] {});
				} catch (Exception e) {
					throw new Error(e);
				}

				synchronized (startedSync) {
					startedSync.notify();
				}

				synchronized (exitSync) {
					try {
						exitSync.wait();
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}
}
