package com.uchicom.tm.runnable;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.uchicom.tm.entity.Project;

/**
 * 非同期スクリーンキャプチャクラス.
 * @author shigeki
 *
 */
public class ScreenCaptureRunnable implements Runnable {

	private long sleepTime;
	private boolean alive = true;
	private Robot robot;
	private Rectangle virtualBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	private File imageDir;
	private String prefix = "check_";
	private String suffix = ".png";
	private String type = "png";

	private Project project;

	/**
	 * 待機時間を指定するコンストラクタ.
	 * @param sleepTime 待機時間
	 */
	public ScreenCaptureRunnable(long sleepTime) {
		this.sleepTime = sleepTime;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (alive) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			if (!alive) break;
			BufferedImage image = robot.createScreenCapture(virtualBounds);
			try {
				File imageFile = new File(imageDir, prefix + System.currentTimeMillis() + suffix);
				if (!imageFile.exists()) {
					System.out.println(imageFile);
					if (!imageDir.exists()) break;
					imageFile.createNewFile();
				}
				if (!imageDir.exists()) break;
				ImageIO.write(image, type, imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 無限ループのブレーク条件設定
	 *
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public void setProject(Project project) {
		this.project = project;

	}
	public void setImageDir(File imageDir) {
		this.imageDir = imageDir;
		imageDir.mkdirs();
	}

}
