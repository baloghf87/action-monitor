package hu.ferencbalogh.actionmonitor.web.integrationtest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hu.ferencbalogh.actionmonitor.database.util.RecordDao;
import hu.ferencbalogh.actionmonitor.entity.Record;
import hu.ferencbalogh.actionmonitor.web.integrationtest.util.ApplicationThread;
import hu.ferencbalogh.actionmonitor.web.integrationtest.util.IntegrationTestContext;
import hu.ferencbalogh.actionmonitor.web.integrationtest.util.WebsocketClientThread;
/**
 * <p>End-to-end test: manipulate database and assert output on websocket</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class FromDbToWebsocketTest {

	private static final String WEBSOCKET_URL = "ws://localhost:8080/action-monitor";
	private static final String WEBSOCKET_TOPIC = "/actions";

	private static final int TIMEOUT = 2;
	private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
	private static final int TEST_RECORD_ID = 1234;

	private static final Object APPLICATION_EXIT_SYNC = new Object();
	private static final Object APPLICATION_STARTED_SYNC = new Object();
	private static final Object WEBSOCKET_EXIT_SYNC = new Object();
	private static final Object WEBSOCKET_CONNECT_SYNC = new Object();
	private static final BlockingQueue<String> WEBSOCKET_QUEUE = new ArrayBlockingQueue<>(1);

	private static RecordDao recordDao;
	private static AnnotationConfigApplicationContext ctx;

	@BeforeClass
	public static void setup() throws InterruptedException {
		startApplication();
		createContextAndDAO();
		connectToWebSocket();
	}

	@AfterClass
	public static void teardown() {
		closeWebsocket();
		closeApplication();
		closeContext();
	}

	@Test
	public void testInsertWithoutId() throws InterruptedException {
		recordDao.insert(new Record(null, ""));
		assertTrue(pollWebsocket().startsWith("Table: ACTIONS, Action: INSERT:, Old: null, New: Record [id="));
	}

	@Test
	public void testInsertWithId() throws InterruptedException {
		delete(TEST_RECORD_ID);

		recordDao.insert(new Record(TEST_RECORD_ID, "test"));
		assertTrue(pollWebsocket().startsWith(
				"Table: ACTIONS, Action: INSERT:, Old: null, New: Record [id=" + TEST_RECORD_ID + ", payload=test"));
	}

	@Test
	public void testUpdate() throws InterruptedException {
		testInsertWithId();

		recordDao.update(new Record(TEST_RECORD_ID, "example"));
		String updateNotification = pollWebsocket();
		String expectedUpdateStart = "Table: ACTIONS, Action: UPDATE:, Old: Record [id=" + TEST_RECORD_ID
				+ ", payload=test";
		assertTrue(updateNotification.startsWith(expectedUpdateStart));
		String actualUpdateEnd = updateNotification.substring(expectedUpdateStart.length());
		assertTrue(actualUpdateEnd.contains("], New: Record [id=" + TEST_RECORD_ID + ", payload=example, "));

		delete(TEST_RECORD_ID);
		recordDao.update(new Record(TEST_RECORD_ID, "example"));
		assertNull(pollWebsocket());
	}

	@Test
	public void testDelete() throws InterruptedException {
		testInsertWithId();

		String deleteNotification = delete(TEST_RECORD_ID);
		String expectedDeleteStart = "Table: ACTIONS, Action: DELETE:, Old: Record [id=" + TEST_RECORD_ID;
		assertTrue(deleteNotification.startsWith(expectedDeleteStart));
		String actualDeleteEnd = deleteNotification.substring(expectedDeleteStart.length());
		assertTrue(actualDeleteEnd.endsWith("], New: null"));

		assertNull(delete(TEST_RECORD_ID));
	}

	private static void startApplication() throws InterruptedException {
		ApplicationThread applicationThread = new ApplicationThread(APPLICATION_STARTED_SYNC, APPLICATION_EXIT_SYNC);
		applicationThread.start();
		waitFor(APPLICATION_STARTED_SYNC);
	}

	private static void createContextAndDAO() {
		ctx = new AnnotationConfigApplicationContext(IntegrationTestContext.class);
		recordDao = ctx.getBean(RecordDao.class);
		assertNotNull(recordDao);
	}

	private static void connectToWebSocket() throws InterruptedException {
		WebsocketClientThread websocketClientThread = new WebsocketClientThread(WEBSOCKET_URL, WEBSOCKET_TOPIC,
				WEBSOCKET_QUEUE, WEBSOCKET_CONNECT_SYNC, WEBSOCKET_EXIT_SYNC);
		websocketClientThread.start();
		waitFor(WEBSOCKET_CONNECT_SYNC);
	}

	private static String pollWebsocket() throws InterruptedException {
		return WEBSOCKET_QUEUE.poll(TIMEOUT, TIMEOUT_UNIT);
	}

	private String delete(int id) throws InterruptedException {
		recordDao.delete(id);
		return pollWebsocket();
	}

	private static void closeApplication() {
		notify(APPLICATION_EXIT_SYNC);
	}

	private static void closeWebsocket() {
		notify(WEBSOCKET_EXIT_SYNC);
	}

	private static void closeContext() {
		ctx.close();
	}

	private static void notify(Object sync) {
		synchronized (sync) {
			sync.notify();
		}
	}

	private static void waitFor(Object sync) throws InterruptedException {
		synchronized (sync) {
			sync.wait();
		}
	}

}
