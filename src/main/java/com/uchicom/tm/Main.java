package com.uchicom.tm;

import javax.swing.SwingUtilities;

import com.uchicom.tm.window.TmFrame;


public class Main {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(()-> {
			TmFrame frame = new TmFrame();
			frame.setVisible(true);
		});

	}

}
