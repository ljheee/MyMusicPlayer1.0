package com.ljheee.test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

public class PlayThread extends Thread {

	byte tempBuffer[] = new byte[320];
	boolean isStop;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;

	public PlayThread(AudioInputStream audioInputStream, SourceDataLine sourceDataLine) {
		this.audioInputStream = audioInputStream;
		this.sourceDataLine = sourceDataLine;
	}

	public void run() {

		try {
			int cnt = 0;
			boolean hasStop = false;

			while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {

				if (isStop)
					break;
				if (cnt > 0) {// 写入缓存数据
					sourceDataLine.write(tempBuffer, 0, cnt);
				}
			}
			// Block等待临时数据被输出为空 sourceDataLine.drain(); sourceDataLine.close();
			// hasStop = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
