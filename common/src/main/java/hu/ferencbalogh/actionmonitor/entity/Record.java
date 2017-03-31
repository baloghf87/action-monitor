package hu.ferencbalogh.actionmonitor.entity;

import java.io.Serializable;

/**
 * <p><b>Record entity</b></p>
 * 
 * <p>Contains the following:</p>
 * 
 * <p><ol>
 * 	<li>id</li>
 *  <li>payload</li>
 *  <li>timestamp</li>
 * </ol></p> 
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class Record implements Serializable {
	private Integer id;
	private String payload;
	private Long timestamp;

	public Record() {
	}

	public Record(Integer id, String payload) {
		super();
		this.id = id;
		this.payload = payload;
	}

	public Record(Integer id, String payload, Long timestamp) {
		super();
		this.id = id;
		this.payload = payload;
		this.timestamp = timestamp;
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

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Record [id=" + id + ", payload=" + payload + ", timestamp=" + timestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

}
