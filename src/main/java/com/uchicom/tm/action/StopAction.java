/**
 * (c) 2016 uchicom
 */
package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.uchicom.tm.window.TmFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class StopAction extends AbstractAction {

	private TmFrame frame;
	/**
	 *
	 */
	public StopAction(TmFrame frame) {
		this.frame = frame;
		putValue(NAME, "終了");
	}

	/**
	 * @param name
	 */
	public StopAction(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * @param name
	 * @param icon
	 */
	public StopAction(String name, Icon icon) {
		super(name, icon);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.stop();
	}

}
