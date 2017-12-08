/**
 * (c) 2016 uchicom
 */
package com.uchicom.tm.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.uchicom.tm.Constants;
import com.uchicom.tm.action.AddProjectAction;
import com.uchicom.tm.action.EditProjectAction;
import com.uchicom.tm.action.RemoveProjectAction;
import com.uchicom.tm.action.StartAction;
import com.uchicom.tm.action.StopAction;
import com.uchicom.tm.action.TaskConfigAction;
import com.uchicom.tm.entity.Project;
import com.uchicom.tm.entity.Record;
import com.uchicom.tm.runnable.ScreenCaptureRunnable;
import com.uchicom.tm.table.ListTableModel;
import com.uchicom.tm.util.TimeUtil;

/**
 * ファイルの保管場所を、案件名をキーにして保管することができる。
 * 案件のフォルダを決定して、日付のフォルダを作成して、その中に、画像を格納する。
 * 最初は一分後にキャプチャして、その後は指定時間毎にキャプチャを実行する。
 * 新規作成や修正ようにダイアログを表示したほうがいいな
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class TmFrame extends JFrame {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	// 画面コンポーネント
	private JComboBox<Project> projectComboBox = new JComboBox<>();
	private JTextField taskTextField = new JTextField();
	private JLabel startLabel = new JLabel("----/--/-- --:--");
	private JLabel endLabel = new JLabel("----/--/-- --:--");
	private JLabel timeLabel = new JLabel("--:--:--");
	private JButton startButton = new JButton(new StartAction(this));
	private JButton stopButton = new JButton(new StopAction(this));
	private JLabel sumLabel = new JLabel();
	private JLabel selectedSumLabel = new JLabel();
	private JTable table;

	//データオブジェクト
	private Map<Project, Project> projectMap = new HashMap<>();
	private Date start;
	private Date end;
	private DefaultComboBoxModel<Project> comboBoxModel = new DefaultComboBoxModel<>();
	private List<Project> projectList = new ArrayList<>();
	private List<Record> rowList = new ArrayList<>();
	private ListTableModel model = new ListTableModel(rowList, 4);
	private ScreenCaptureRunnable runnable = new ScreenCaptureRunnable(1  * 1000);

	private Properties properties = new Properties();
	private NotifyFrame frame;
	private File baseDir;

	/**
	 * @throws HeadlessException
	 */
	public TmFrame() {
		super("作業時間管理");
		initComponents();
	}
	/**
	 *
	 */
	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initProperties();
		initJMenuBar();
		initProject();

		setWindowPosition(this, Constants.PROP_KEY_WINDOW_TM_POSITION);
		setWindowState(this, Constants.PROP_KEY_WINDOW_TM_STATE);
		//案件リスト
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10,5,10,5);
		panel.add(new JLabel("案件"), constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(projectComboBox, constraints);
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(new JLabel("作業"), constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(taskTextField, constraints);
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(new JLabel("開始"), constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		panel.add(startLabel, constraints);
		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(new JLabel("終了"), constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		panel.add(endLabel, constraints);
		constraints.gridy = 4;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(new JLabel("時間"), constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		panel.add(timeLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		panel.add(startButton, constraints);
		constraints.gridx = 2;
		panel.add(stopButton, constraints);


		TableColumnModel columnModel = new DefaultTableColumnModel();
		TableColumn tableColumn = new TableColumn(0);
		tableColumn.setIdentifier(0);
		tableColumn.setHeaderValue("タスク");
		tableColumn.setPreferredWidth(150);
		columnModel.addColumn(tableColumn);
		tableColumn = new TableColumn(1);
		tableColumn.setIdentifier(1);
		tableColumn.setHeaderValue("開始");
		tableColumn.setPreferredWidth(120);
		columnModel.addColumn(tableColumn);
		tableColumn = new TableColumn(2);
		tableColumn.setIdentifier(2);
		tableColumn.setHeaderValue("終了");
		tableColumn.setPreferredWidth(120);
		columnModel.addColumn(tableColumn);
		tableColumn = new TableColumn(3);
		tableColumn.setIdentifier(3);
		tableColumn.setHeaderValue("作業時間");
		tableColumn.setPreferredWidth(60);
		JTextField textField = new JTextField();
		textField.setEditable(false);
		tableColumn.setCellEditor(new DefaultCellEditor(textField));
		columnModel.addColumn(tableColumn);

		table = new JTable(model, columnModel);
		table.getSelectionModel().addListSelectionListener((e) -> {
			System.out.println(e);
			if (!e.getValueIsAdjusting()) {
			select();
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.add(sumLabel);
		southPanel.add(selectedSumLabel);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		//開始、終了ボタン
		//画面撮影
		stopButton.setEnabled(false);

		initView(getSelectedProject());
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent we) {
				if (start != null) {
					stop();
				}
			}
			@Override
			public void windowClosing(WindowEvent we) {
				if (TmFrame.this.getExtendedState() == JFrame.NORMAL) {
					// 画面の位置を保持する
					storeWindowPosition(TmFrame.this, Constants.PROP_KEY_WINDOW_TM_POSITION);
				} else {
					storeWindowState(TmFrame.this, Constants.PROP_KEY_WINDOW_TM_STATE);
				}
				storeProperties();
			}

		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent ce) {
				if (getExtendedState() == JFrame.NORMAL) {
					storeWindowPosition(TmFrame.this, Constants.PROP_KEY_WINDOW_TM_POSITION);
				}
			}
			@Override
			public void componentResized(ComponentEvent ce) {
				if (getExtendedState() == JFrame.NORMAL) {
					storeWindowPosition(TmFrame.this, Constants.PROP_KEY_WINDOW_TM_POSITION);
				}
			}
		});

		projectComboBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getItem() != null) {
					Project project = (Project) ie.getItem();
					initView(project);
				}

			}

		});

		setAlwaysOnTop(true); //常に全面に表示
		pack();
	}
	private void select() {
		System.out.println("select");
		int[] rows = table.getSelectedRows();
		List<Record> recordList = model.getRowList();
		long sum = 0;
		for (int i : rows) {
			sum += recordList.get(i).getTime();
		}
		selectedSumLabel.setText(TimeUtil.getDispElapsedTime(sum));
		System.out.println(selectedSumLabel.getText());
	}
	private void initView(Project project) {
		if (project == null) return;
		startLabel.setText("----/--/-- --:--");
		endLabel.setText("----/--/-- --:--");
		timeLabel.setText("--:--:--");
		model.setRowList(project.getRecordList());
		if (project.getRecordList() != null && project.getRecordList().size() > 0) {
			taskTextField.setText(project.getRecordList().get(project.getRecordList().size() - 1).getTaskName());
		} else {
			taskTextField.setText("");
		}
		sumLabel.setText(TimeUtil.getDispElapsedTime(project.calculateSum()));
	}
	private void initJMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("案件");
		menu.add(new JMenuItem(new AddProjectAction(this)));
		menu.add(new JMenuItem(new RemoveProjectAction(this)));
		menu.add(new JMenuItem(new EditProjectAction(this)));
		menu.addSeparator();
		menu.add(new JMenuItem(new TaskConfigAction(this)));
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}
	private void initProject() {
		baseDir = new File(properties.getProperty("dir"));
		File[] projectDirs = baseDir.listFiles();
		projectList.clear();

		for (File projectDir : projectDirs) {
			Project project = new Project(projectDir);
			initHistory(project, projectDir);
			projectMap.put(project, project);
			projectList.add(project);
		}
		projectList.sort(new Comparator<Project>() {
			@Override
			public int compare(Project arg0, Project arg1) {
				long ret = arg0.getFile().lastModified() - arg1.getFile().lastModified();
				if (ret > 0) {
					return -1;
				} else if (ret < 0) {
					return 1;
				} else {
					return 0;
				}
			}

		});
		comboBoxModel.removeAllElements();
		projectList.forEach((p)->{
			comboBoxModel.addElement(p);
		});
		projectComboBox.setModel(comboBoxModel);
	}

	/**
	 * 過去の作業データ生成.
	 * @param project
	 * @param projectDir
	 */
	private void initHistory(Project project, File projectDir) {
		for (File file : projectDir.listFiles()) {
			String name = file.getName();
			int splitIndex = name.indexOf("_");
			if (splitIndex < 0) {
				continue;
			}
			String taskName = name.substring(0, splitIndex);
			int splitIndex2 = name.indexOf("_", splitIndex + 1);
			if (splitIndex2 < 0) {
				continue;
			}
			String start = name.substring(splitIndex + 1, splitIndex2);
			Date startDate = null;
			if (!"".equals(start)) {
				startDate = new Date(Long.valueOf(start));
			}
			String end = name.substring(splitIndex2 + 1);
			Date endDate = null;
			if (!"".equals(end)) {
				endDate = new Date(Long.valueOf(end));
			}
			Record record = new Record(startDate, endDate, taskName);
			project.add(record);
		}
		project.getRecordList().sort(new Comparator<Record>() {

			@Override
			public int compare(Record arg0, Record arg1) {
				return arg0.compareTo(arg1);
			}

		});
	}

	/**
	 * @param gc
	 */
	public TmFrame(GraphicsConfiguration gc) {
		super(gc);
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public TmFrame(String title) throws HeadlessException {
		super(title);
	}

	/**
	 * @param title
	 * @param gc
	 */
	public TmFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

	/**
	 * 測定停止
	 */
	public void stop() {
		try {
			end = new Date();
			endLabel.setText(format.format(end));
			Project project = getSelectedProject();
			String taskName = getTaskName();
			Record record = new Record(start, end, taskName);
			project.add(record);
			sumLabel.setText(TimeUtil.getDispElapsedTime(project.calculateSum()));
			timeLabel.setText(record.getDispTime());
			frame.dispose();
			frame = null;
			model.fileRow();
//			runnable.setAlive(false);
			File recordDir = new File(baseDir, project.getId() + "_" + project.getName() + "/" + taskName + "_" + start.getTime());
			File renameDir = new File(baseDir, project.getId() + "_" + project.getName() + "/" + taskName + "_" + start.getTime() + "_" + end.getTime());
			recordDir.renameTo(renameDir);
		} finally {
			projectComboBox.setEnabled(true);
			taskTextField.setEnabled(true);
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			start = null;
			end = null;
			setAlwaysOnTop(true); //常に全面に表示
		}

	}

	/**
	 * 測定開始
	 */
	public void start() {
		if ("".equals(getTaskName())) {
			JOptionPane.showMessageDialog(this, "タスクを入力してください.");
			return;
		}
		try {
			start = new Date();
			endLabel.setText("----/--/-- --:--");
			timeLabel.setText("--:--:--");
			startLabel.setText(format.format(start));

			runnable.setAlive(true);
			Project project = getSelectedProject();
			runnable.setProject(project);
			File imageDir = new File(baseDir, project.getId() + "_" + project.getName() + "/" + getTaskName() + "_" + start.getTime());

			runnable.setImageDir(imageDir);
			frame = new NotifyFrame();
			frame.startNotify();
	//		Thread thread = new Thread(runnable);
	//		thread.setDaemon(true);
	//		thread.start();
		} finally {
			projectComboBox.setEnabled(false);
			taskTextField.setEnabled(false);
			stopButton.setEnabled(true);
			startButton.setEnabled(false);
			setAlwaysOnTop(false); //常に全面に表示
		}
	}

	/**
	 * 案件を追加します
	 */
	public void add() {
		String projectName = JOptionPane.showInputDialog("案件名");
		Project project = createProject(projectName);
		projectMap.put(project, project);
		projectList.add(project);

		projectList.sort(new Comparator<Project>() {

			@Override
			public int compare(Project arg0, Project arg1) {
				long ret = arg0.getFile().lastModified() - arg1.getFile().lastModified();
				if (ret > 0) {
					return -1;
				} else if (ret < 0) {
					return 1;
				} else {
					return 0;
				}
			}

		});
		comboBoxModel.removeAllElements();
		projectList.forEach((p)->{
			comboBoxModel.addElement(p);
		});
		taskTextField.setText("");
	}
	private File createDir(String dirName) {
		File dir = new File(properties.getProperty("dir"), dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	public void edit() {
		Project project = getSelectedProject();
		File dir = new File(properties.getProperty("dir"), project.getId() + "_" + project.getName());
		String projectName = JOptionPane.showInputDialog(this, "案件名", project.getName());
		if (projectName != null) {
			if ("".equals(projectName)) {
				JOptionPane.showMessageDialog(this, "案件名を入力してください");
			} else {
				File renameDir = new File(properties.getProperty("dir"), project.getId() + "_" + projectName);
				if (dir.renameTo(renameDir)) {
					project.setName(projectName);
				}
			}
		}
	}

	/**
	 * 案件名を登録します
	 * @param projectName
	 */
	private Project createProject(String projectName) {
		return new Project(createDir(System.currentTimeMillis() + "_" + projectName));
	}
	public void remove() {
		Project project = getSelectedProject();
		removeProject(project);

	}

	public void config() {

		Project task = comboBoxModel.getElementAt(projectComboBox.getSelectedIndex());
		TaskConfigDialog dialog = new TaskConfigDialog(this, task);
		dialog.pack();
		dialog.setVisible(true);
	}
	/**
	 * 案件を削除します、データは削除されます。
	 */
	private void removeProject(Project project) {
		int result = JOptionPane.showConfirmDialog(this, project.getName() + "案件を削除してもよろしいですか？\n削除したものは復元できません。", "情報", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			File projectDir = new File(baseDir, project.getId() + "_" + project.getName());
			deleteDir(projectDir);
			comboBoxModel.removeElement(project);
		}
	}
	private Project getSelectedProject() {
		return comboBoxModel.getElementAt(projectComboBox.getSelectedIndex());
	}
	private String getTaskName() {
		return taskTextField.getText();
	}
	private void deleteDir(File file) {
		File[] files = file.listFiles();
		for (File child : files) {
			if (child.isDirectory()) {
				deleteDir(child);
			} else {
				child.delete();
			}
		}
		file.delete();
	}

	/**
	 * 画面の位置をプロパティに設定する。
	 *
	 * @param frame
	 * @param key
	 */
	private void storeWindowPosition(JFrame frame, String key) {
		String value = frame.getLocation().x + Constants.PROP_SPLIT_CHAR + frame.getLocation().y + Constants.PROP_SPLIT_CHAR
				+ frame.getWidth() + Constants.PROP_SPLIT_CHAR + frame.getHeight() + Constants.PROP_SPLIT_CHAR;
		properties.setProperty(key, value);
	}
	/**
	 * 画面の位置をプロパティに設定する。
	 *
	 * @param frame
	 * @param key
	 */
	private void storeWindowState(JFrame frame, String key) {
		String value = frame.getState() + Constants.PROP_SPLIT_CHAR
				+ frame.getExtendedState();
		properties.setProperty(key, value);
	}

	/**
	 * 画面のサイズをプロパティから設定する。
	 *
	 * @param frame
	 * @param key
	 */
	public void setWindowPosition(JFrame frame, String key) {
		if (properties.containsKey(key)) {
			String initPoint = properties.getProperty(key);
			String[] points = initPoint.split(Constants.PROP_SPLIT_CHAR);
			if (points.length > 3) {
				frame.setLocation(Integer.parseInt(points[0]), Integer.parseInt(points[1]));
				frame.setPreferredSize(new Dimension(Integer.parseInt(points[2]), Integer.parseInt(points[3])));
			}
		}
	}
	public void setWindowState(JFrame frame, String key) {
		if (properties.containsKey(key)) {
			String initPoint = properties.getProperty(key);
			String[] points = initPoint.split(Constants.PROP_SPLIT_CHAR);
			if (points.length > 1) {
				frame.setState(Integer.parseInt(points[0]));
				frame.setExtendedState(Integer.parseInt(points[1]));
			}
		}
	}

	/**
	 *
	 */

	private void initProperties() {
		if (Constants.CONF_FILE.exists() && Constants.CONF_FILE.isFile()) {
			try (FileInputStream fis = new FileInputStream(Constants.CONF_FILE);) {
				properties.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void storeProperties() {
		try {
			if (!Constants.CONF_FILE.exists()) {
				Constants.CONF_FILE.getParentFile().mkdirs();
				Constants.CONF_FILE.createNewFile();
			}
			try (FileOutputStream fos = new FileOutputStream(Constants.CONF_FILE);) {
				properties.store(fos, Constants.APP_NAME + " Ver" + Constants.VERSION);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
