package com.uchicom.tm.entity;

import java.io.File;
import java.util.Vector;

public class Project {

	private long id;
	private String name;
	private Vector<Record> recordList = new Vector<>();
	private File file;

	public Project(File file) {
		String dirName = file.getName();
		int splitIndex = dirName.indexOf("_");
		String id = dirName.substring(0, splitIndex);
		String name = dirName.substring(splitIndex + 1);
		this.id = Long.parseLong(id);
		this.name = name;
		this.file = file;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}
	@Override
	public boolean equals(Object object) {
		if (object instanceof Project) {
			Project project = (Project) object;
			return project.id == id;
		}
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * recordListを取得します.
	 *
	 * @return recordList
	 */
	public Vector<Record> getRecordList() {
		return recordList;
	}

	/**
	 * recordListを設定します.
	 *
	 * @param recordList recordList
	 */
	public void setRecordList(Vector<Record> recordList) {
		this.recordList = recordList;
	}
	public void add(Record record) {
		recordList.add(record);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	public long calculateSum() {
		long sum = 0;
		for (Record record : recordList) {
			sum += record.getTime();
		}
		 return sum;
	}
}
