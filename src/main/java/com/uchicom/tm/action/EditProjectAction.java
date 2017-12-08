package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.tm.window.TmFrame;

public class EditProjectAction extends AbstractAction {

	private TmFrame frame;
	public EditProjectAction(TmFrame frame) {
		this.frame = frame;
		putValue(NAME, "案件編集");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.edit();
	}

}
