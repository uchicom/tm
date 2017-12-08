package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.tm.window.TmFrame;

/**
 * タスク設定アクション
 * @author shigeki
 *
 */
public class TaskConfigAction extends AbstractAction {

	TmFrame frame;
	public TaskConfigAction(TmFrame frame) {
		this.frame = frame;
		putValue(NAME, "設定");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.config();
	}

}
