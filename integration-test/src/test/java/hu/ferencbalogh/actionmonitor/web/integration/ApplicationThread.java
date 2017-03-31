package hu.ferencbalogh.actionmonitor.web.integration;

import hu.ferencbalogh.actionmonitor.web.Application;

/**
 * <p>
 * Thread that starts the {@link Application}, notifies when it is started, then
 * waits until it gets notified to exit
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class ApplicationThread extends Thread {
	private Object startedSync;
	private Object exitSync;

	/**
	 * Construct a new ApplicationThread
	 * 
	 * @param startedSync
	 *            gets notified when started
	 * @param exitSync
	 *            application exits when notifying
	 */
	public ApplicationThread(Object startedSync, Object exitSync) {
		this.startedSync = startedSync;
		this.exitSync = exitSync;
	}

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
}
