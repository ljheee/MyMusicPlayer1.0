package zyh;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Player {
	private String path;// 文件路径
	private String name;// 文件名称
	private AudioFormat audioFormat;// 播放格式
	private AudioInputStream audioInputStream;// 音乐播放输入流
	private SourceDataLine sourceDataLine;// 播放设备
	private boolean isStop = false;// 播放停止标志
	// 创建对象时需要传入播放路径及文件名称 *

	public Player(String path, String name) {
		this.path = path;
		this.name = name;
	}

	/** * 播放音乐 */
	public void play() {
		File file = new File(path + name);
		try { // 获取音乐播放流
			audioInputStream = AudioSystem.getAudioInputStream(file); // 获取播放格式
			audioFormat = audioInputStream.getFormat();
			/*
			 * System.out.println("取样率："+ audioFormat.getSampleRate()); Map map
			 * = audioFormat.properties(); Iterator it =
			 * map.entrySet().iterator(); while(it.hasNext()) { Map.Entry m =
			 * (Entry) it.next();
			 * System.out.println(m.getKey()+":"+m.getValue()); }
			 */
			// 其它格式音乐文件处理
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						audioFormat.getSampleRate(), 16, audioFormat
								.getChannels(), audioFormat.getChannels() * 2,
						audioFormat.getSampleRate(), audioFormat.isBigEndian());
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
						audioInputStream);
			} // 打开输出设备
			DataLine.Info dataLineInfo = new DataLine.Info(
					SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start(); // 启动播放线程
			new Thread() {
				@Override
				public void run() {
					try {
						int n = 0;
						byte tempBuffer[] = new byte[320];
						while (n != -1) {
							// 停止播放入口，如果isStop被置为真，结束播放
							if (isStop)
								break; // 将音乐输入流的数据读入tempBuffer缓存
							n = audioInputStream.read(tempBuffer, 0,
									tempBuffer.length);
							if (n > 0) {
								// 将缓存数据写入播放设备，开始播放
								sourceDataLine.write(tempBuffer, 0, n);
							}
						}
						audioInputStream.close();
						sourceDataLine.drain();
						sourceDataLine.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			throw new RuntimeException();
		}
	}

	/** * 停止播放 */
	public void stop() {
		try {
			isStop = true;
			audioInputStream.close();
			sourceDataLine.drain();
			sourceDataLine.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}