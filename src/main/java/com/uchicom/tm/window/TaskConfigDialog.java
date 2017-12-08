package com.uchicom.tm.window;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.uchicom.tm.action.FileSelectAction;
import com.uchicom.tm.entity.Project;

public class TaskConfigDialog extends JDialog {
	/** タスク名 */
	private JTextField nameTextField = new JTextField();
	/** ファイルパス */
	private JTextField filePath = new JTextField();
	/** 開始日 */
	private JTextField startDate = new JTextField();
	/** 終了日 */
	private JTextField endDate = new JTextField();
	/** 画面キャプチャ間隔[msec] */
	private JTextField captureTime = new JTextField();

	private Project task;
	public TaskConfigDialog(TmFrame frame, Project task) {
		super(frame, "タスク設定");
		this.task = task;
		initComponents();
	}
	private void initField() {
		nameTextField.setText(task.getName());
	}
	private void initComponents() {
		initField();
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;

		//タスク名
		panel.add(new JLabel("タスク名"), constraints);
		constraints.gridx = 1;
		panel.add(nameTextField, constraints);
		//データ格納フォルダ
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(new JLabel("保存先"), constraints);
		constraints.gridx = 1;
		panel.add(new JButton(new FileSelectAction(this)));
		panel.add(filePath, constraints);
		//開始日
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(new JLabel("開始日"), constraints);
		constraints.gridx = 1;
		panel.add(startDate, constraints);
		//終了日
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(new JLabel("終了日"), constraints);
		constraints.gridx = 1;
		panel.add(endDate, constraints);
		//キャプチャ間隔
		constraints.gridx = 0;
		constraints.gridy = 4;
		panel.add(new JLabel("キャプチャ間隔[msec]"), constraints);
		constraints.gridx = 1;
		panel.add(captureTime, constraints);
		getContentPane().add(new JScrollPane(panel));
	}

	public void selectDir() {


		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				return false;
			}

			@Override
			public String getDescription() {
				// TODO 自動生成されたメソッド・スタブ
				return null;
			}

		});
//		String path = config.getProperty(TASK + task.getId() + PATH);
//		if (path != null) {
//			File file = new File(path);
//			if (file.exists() && file.isDirectory()) {
//				chooser.setSelectedFile(file);
//			}
//		}
//		int result = chooser.showDialog(this, "設定");
//		if (JFileChooser.CANCEL_OPTION != result) {
//			File selectedFile = chooser.getSelectedFile();
//			try {
//				config.setProperty(TASK + task.getId() + PATH, selectedFile.getCanonicalPath());
//			} catch (IOException e) {
//				// TODO 自動生成された catch ブロック
//				e.printStackTrace();
//			}
//		}
	}
}
