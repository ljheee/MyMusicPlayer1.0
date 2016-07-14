package zyh;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Player {
	private String path;// �ļ�·��
	private String name;// �ļ�����
	private AudioFormat audioFormat;// ���Ÿ�ʽ
	private AudioInputStream audioInputStream;// ���ֲ���������
	private SourceDataLine sourceDataLine;// �����豸
	private boolean isStop = false;// ����ֹͣ��־
	// ��������ʱ��Ҫ���벥��·�����ļ����� *

	public Player(String path, String name) {
		this.path = path;
		this.name = name;
	}

	/** * �������� */
	public void play() {
		File file = new File(path + name);
		try { // ��ȡ���ֲ�����
			audioInputStream = AudioSystem.getAudioInputStream(file); // ��ȡ���Ÿ�ʽ
			audioFormat = audioInputStream.getFormat();
			/*
			 * System.out.println("ȡ���ʣ�"+ audioFormat.getSampleRate()); Map map
			 * = audioFormat.properties(); Iterator it =
			 * map.entrySet().iterator(); while(it.hasNext()) { Map.Entry m =
			 * (Entry) it.next();
			 * System.out.println(m.getKey()+":"+m.getValue()); }
			 */
			// ������ʽ�����ļ�����
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						audioFormat.getSampleRate(), 16, audioFormat
								.getChannels(), audioFormat.getChannels() * 2,
						audioFormat.getSampleRate(), audioFormat.isBigEndian());
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
						audioInputStream);
			} // ������豸
			DataLine.Info dataLineInfo = new DataLine.Info(
					SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start(); // ���������߳�
			new Thread() {
				@Override
				public void run() {
					try {
						int n = 0;
						byte tempBuffer[] = new byte[320];
						while (n != -1) {
							// ֹͣ������ڣ����isStop����Ϊ�棬��������
							if (isStop)
								break; // �����������������ݶ���tempBuffer����
							n = audioInputStream.read(tempBuffer, 0,
									tempBuffer.length);
							if (n > 0) {
								// ����������д�벥���豸����ʼ����
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

	/** * ֹͣ���� */
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