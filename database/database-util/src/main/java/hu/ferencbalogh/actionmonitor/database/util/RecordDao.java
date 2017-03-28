package hu.ferencbalogh.actionmonitor.database.util;

import javax.sql.DataSource;

public interface RecordDao {

	   public void setDataSource(DataSource ds);
	   
	   public void insert(Record record);
	   
	   public void update(Record record);
	   
	   public void delete(int id);
}
