package hu.ferencbalogh.actionmonitor.database.util;

import java.util.Date;

public class Record {
	private Integer id;
	private String payload;
	private Date timestamp;
	
	public Record() {
	}
	
	public Record(Integer id, String payload) {
		super();
		this.id = id;
		this.payload = payload;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
