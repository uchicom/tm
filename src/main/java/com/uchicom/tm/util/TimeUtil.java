// (c) 2017 uchicom
package com.uchicom.tm.util;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class TimeUtil {

	public static String getDispElapsedTime(long time) {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("作業時間合計:")
		.append(time / (60 * 60 * 1000)).append("時間")
		.append(time % (60 * 60 * 1000) / (60 * 1000)).append("分")
		.append(time % (60 * 1000) / (1000)).append("秒(")
		.append(time / (60 * 1000)).append("分)");
		return strBuff.toString();
	}
}
