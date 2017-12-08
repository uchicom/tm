// (c) 2012 uchicom
package com.uchicom.tm.table;

import java.text.ParseException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.uchicom.tm.entity.Record;

/**
 * shiwakeとpodofから流用
 * @author uchicom: Shigeki Uchiyama
 */
public class ListTableModel extends DefaultTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ.
	 * @param rowList
	 * @param columnCount
	 */
	public ListTableModel(List<Record> rowList, int columnCount) {
		this.rowList = rowList;
		this.columnCount = columnCount;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Record dto = rowList.get(row);
		String viewString = null;
		switch (col) {
		case 0:
			viewString = dto.getTaskName();
			break;
		case 1:
			return dto.getDispStartDate();
		case 2:
			return dto.getDispEndDate();
		case 3:
			return dto.getDispTime();
		default:
			viewString = "";
		}
		return viewString;
	}

	@Override
	public void setValueAt(Object obj, int row, int col) {
		String value = obj.toString();
		Record record = rowList.get(row);
		switch (col) {
		case 0:
			record.setTaskName(value);
			break;
		case 1:
			try {
				record.setStartDate(Record.format.parse(value));

		         fireTableCellUpdated(row, 3);
			} catch (ParseException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				record.setEndDate(Record.format.parse(value));
		         fireTableCellUpdated(row, 3);
			} catch (ParseException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			break;
		}
	}
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		if (rowList != null) {
			return rowList.size();
		} else {
			return 0;
		}
	}

	public void addRow(Record dto) {
	     rowList.add(dto);
         fireTableRowsInserted(rowList.size() - 1, rowList.size() -1);
	}
	public void fileRow() {
        fireTableRowsInserted(rowList.size() - 1, rowList.size() -1);
	}

	/** データ格納リスト */
	private List<Record> rowList;

	/** 列最大数 */
	private int columnCount = 0;

    public List<Record> getRowList() {
        return rowList;
    }
    public void setRowList(List<Record> rowList) {
        this.rowList = rowList;
        fireTableStructureChanged();
    }
}
