// (c) 2013 uchicom
package com.uchicom.tm.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * shiwakeのレンダラーから流量.
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class TableCellRenderer extends DefaultTableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JTextField stringField = new JTextField();
    private JTextField integerField = new JTextField();
    private JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL);
    private Color progressColor = new Color(95, 195, 95);
    public TableCellRenderer() {
    	stringField.setBorder(null);
        integerField.setHorizontalAlignment(JTextField.RIGHT);
        integerField.setBorder(null);
        bar.setMinimum(0);
        bar.setValue(0);
        bar.setBorder(null);
        bar.setStringPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent returnComponent = null;
        int modelIndex = table.getColumnModel().getColumn(column).getModelIndex();
        Color color = null;
        switch (modelIndex) {
        case 0:
            stringField.setText((String)value);
            returnComponent = stringField;
            color = table.getForeground();
            break;
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        	if (value == null) {
        		if (column == 1 || column == 2 || column == 3) {
        			integerField.setText("DB検索中");
        		} else {
        			integerField.setText("");
        		}
        	} else {
        		integerField.setText(value.toString());
        	}

            returnComponent = integerField;
            color = table.getForeground();
            break;

        case 6:
        	color = progressColor;
        	if (value == null) {
        		bar.setValue(0);
        	} else {
        		Integer[] values = (Integer[]) value;
        		if (values.length == 2 && values[0] != null && values[1] != null) {
        			bar.setMaximum(values[0]);
        			bar.setValue(values[1]);
        			if (values[0] > 0) {
        				bar.setString(null);
        			} else {
        				bar.setString("対象外");
        			}
        			System.out.println(row + ":" + values[1]);
        		}
        	}
        	returnComponent = bar;
        	break;
            default :
                stringField.setText("");
                returnComponent = stringField;
        }

        //行高さ設定
        int rowHeight = returnComponent.getPreferredSize().height;
        if (rowHeight > table.getRowHeight(row)) {
            table.setRowHeight(row, rowHeight);
        }

        //配色
        if (isSelected && hasFocus) {
            returnComponent.setForeground(Color.WHITE);
        } else {
        	returnComponent.setForeground(color);
        }
        if (isSelected) {
        	returnComponent.setBackground(table.getSelectionBackground());
        } else {
            returnComponent.setBackground(table.getBackground());
        }

        return returnComponent;
    }

}
