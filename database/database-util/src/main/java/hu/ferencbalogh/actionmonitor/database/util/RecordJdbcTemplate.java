package hu.ferencbalogh.actionmonitor.database.util;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import hu.ferencbalogh.actionmonitor.entity.Record;

/**
 * <p>Implementation of {@link RecordDao} using a {@link JdbcTemplate}</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class RecordJdbcTemplate implements RecordDao {

	@Value("${hsql.tablename}")
	private String tableName;

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void insert(Record record) {
		if (record.getId() != null) {
			String sql = "insert into " + tableName + " (id, payload) values (?, ?)";
			jdbcTemplate.update(sql, record.getId(), record.getPayload());
		} else {
			String sql = "insert into " + tableName + " (payload) values (?)";
			jdbcTemplate.update(sql, record.getPayload());
		}
	}

	public void update(Record record) {
		String sql = "update " + tableName + " SET payload = ? WHERE id = ?";
		jdbcTemplate.update(sql, record.getPayload(), record.getId());
	}

	public void delete(int id) {
		String sql = "delete from " + tableName + " WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

}
