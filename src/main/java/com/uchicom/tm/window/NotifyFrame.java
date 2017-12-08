// (c) 2017 uchicom
package com.uchicom.tm.window;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.uchicom.tm.action.CloseAction;
import com.uchicom.tm.runnable.DesktopNotifyRunnable;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class NotifyFrame extends JFrame {
	/** 通知までの時間 */
	private long sleep = 10 * 1000;//30 * 60 * 1000;

	/** 通知非同期処理 */
	private DesktopNotifyRunnable desktopNotifyRunnable = new DesktopNotifyRunnable(this, sleep);

	/**
	 * @throws HeadlessException
	 */
	public NotifyFrame() {
		initComponents();
	}

	/**
	 * 初期化
	 */
	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.insets = new Insets(10, 10, 10, 10);
		JLabel label = new JLabel("計測中");
		panel.add(label, constraints);
		constraints.gridx = 1;
		JButton button = new JButton(new CloseAction(this));
		label.setForeground(Color.RED);
		button.setBackground(Color.ORANGE);
		panel.setBackground(Color.WHITE);
		panel.add(button, constraints);
		getContentPane().add(panel);
		setUndecorated(true);//タイトルが消える
		setAlwaysOnTop(true); //常に全面に表示
		setFocusableWindowState(false); //画面を選択してもフォーカスが移動しない、作業中も移動しない
		pack();
	}

	/**
	 * デスクトップ通知処理
	 */
	public void startNotify() {
		//過去の通知は終了する
		//過去の通知は破棄して新規作成
		Thread thread = new Thread(desktopNotifyRunnable);
		thread.setDaemon(true);
		thread.start();
	}

}
