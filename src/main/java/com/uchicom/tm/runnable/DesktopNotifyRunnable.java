// (c) 2017 uchicom
package com.uchicom.tm.runnable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class DesktopNotifyRunnable implements Runnable {

	private JFrame frame;
	private long span;

	public DesktopNotifyRunnable(JFrame frame, long span) {
		this.frame = frame;
		this.span = span;
	}

	/* (非 Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(span);
			if (!frame.isVisible() && frame.isDisplayable()) {
				SwingUtilities.invokeLater(() -> {
					frame.setVisible(true);
				});
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
