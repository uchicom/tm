package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.tm.window.TaskConfigDialog;

public class FileSelectAction extends AbstractAction {

	private TaskConfigDialog dialog;
	public FileSelectAction(TaskConfigDialog dialog) {
		this.dialog = dialog;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.selectDir();
	}

}
