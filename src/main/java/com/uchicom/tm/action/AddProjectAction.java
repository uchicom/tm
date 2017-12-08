package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.tm.window.TmFrame;

public class AddProjectAction extends AbstractAction {

	private TmFrame frame;
	public AddProjectAction(TmFrame frame) {
		this.frame = frame;
		putValue(NAME, "案件追加");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.add();
	}

}
