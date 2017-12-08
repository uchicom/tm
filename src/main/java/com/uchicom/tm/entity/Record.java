/**
 * (c) 2016 uchicom
 */
package com.uchicom.tm.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class Record implements Comparable<Record> {
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date startDate;
	Date endDate;
	long time;
	String taskName;
	/**
	 * startDateを取得します.
	 *
	 * @return startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * startDateを設定します.
	 *
	 * @param startDate startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * endDateを取得します.
	 *
	 * @return endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * endDateを設定します.
	 *
	 * @param endDate endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;

		if (startDate != null && endDate != null) {
			time = endDate.getTime() - startDate.getTime();
		}
	}

	/**
	 * taskNameを設定します.
	 *
	 * @param taskName taskName
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;

		if (startDate != null && endDate != null) {
			time = endDate.getTime() - startDate.getTime();
		}
	}

	/**
	 *
	 */
	public Record(Date startDate, Date endDate, String taskName) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskName = taskName;
		if (startDate != null && endDate != null) {
			time = endDate.getTime() - startDate.getTime();
		}
	}

	public long getTime() {
		return time;
	}
	public String getDispTime() {
		return (time / (1000 * 60 * 60)) + ":" + (time / (1000 * 60) % 60) + ":" + (time / (1000) % 60);
	}
	public String toString() {
		String formatStartDate = null;
		if (startDate != null) {
			formatStartDate = format.format(startDate);
		}
		String formatEndDate = null;
		if (endDate != null) {
			formatEndDate = format.format(endDate);
		}
		return taskName + ":" + formatStartDate + "~" + formatEndDate + "(" + getDispTime() + ")";

	}

	/* (非 Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Record record) {
		return startDate.compareTo(record.startDate);
	}
	public String getTaskName() {
		return taskName;
	}
	public String getDispStartDate() {
		if (startDate != null) {
			return format.format(startDate);
		} else {
			return null;
		}
	}
	public String getDispEndDate() {
		if (endDate != null) {
			return format.format(endDate);
		} else {
			return null;
		}
	}
}
