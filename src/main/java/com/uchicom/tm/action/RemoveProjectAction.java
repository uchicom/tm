package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.tm.window.TmFrame;

public class RemoveProjectAction extends AbstractAction {

	private TmFrame frame;
	public RemoveProjectAction(TmFrame frame) {
		this.frame = frame;
		putValue(NAME, "案件削除");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.remove();
	}

}
