// (c) 2017 uchicom
package com.uchicom.tm.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.tm.window.NotifyFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class CloseAction extends AbstractAction {

	private NotifyFrame frame;
	public CloseAction(NotifyFrame frame) {
		this.frame = frame;
		putValue(NAME, "閉じる");
	}
	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		frame.startNotify();
	}

}
