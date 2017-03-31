package hu.ferencbalogh.actionmonitor.database;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:/action-monitor.properties")
public abstract class AbstractSpringTest {
}
