package hu.ferencbalogh.actionmonitor.entity;

import java.io.Serializable;

/**
 * <p><b>Action entity</b></p>
 * 
 * <p>Contains the following:</p>
 * 
 * <p><ol>
 * 	<li>Table name</li>
 *  <li>Action name (INSERT, UPDATE, DELETE)</li>
 *  <li>The old record (null if none)</li>
 *  <li>The new record (null if none)</li>
 * </ol></p> 
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class Action implements Serializable {
	private String table;
	private String action;
	private Record oldRecord;
	private Record newRecord;

	public Action() {
	}

	public Action(String table, String action) {
		this.table = table;
		this.action = action;
	}

	public Action(String table, String action, Record oldRecord, Record newRecord) {
		this.table = table;
		this.action = action;
		this.oldRecord = oldRecord;
		this.newRecord = newRecord;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Record getOldRecord() {
		return oldRecord;
	}

	public void setOldRecord(Record oldRecord) {
		this.oldRecord = oldRecord;
	}

	public Record getNewRecord() {
		return newRecord;
	}

	public void setNewRecord(Record newRecord) {
		this.newRecord = newRecord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((newRecord == null) ? 0 : newRecord.hashCode());
		result = prime * result + ((oldRecord == null) ? 0 : oldRecord.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
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
		Action other = (Action) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (newRecord == null) {
			if (other.newRecord != null)
				return false;
		} else if (!newRecord.equals(other.newRecord))
			return false;
		if (oldRecord == null) {
			if (other.oldRecord != null)
				return false;
		} else if (!oldRecord.equals(other.oldRecord))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

}
