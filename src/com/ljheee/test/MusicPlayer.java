package com.ljheee.test;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;

public class MusicPlayer extends JFrame {
	
	String filepath; // �����ļ�Ŀ¼
	String filename; // �����ļ�����
	AudioInputStream audioInputStream; // �ļ���
	AudioFormat audioFormat; // �ļ���ʽ
	SourceDataLine sourceDataLine; // ����豸
	boolean isStop = true; // ���Ʋ����߳�
	boolean hasStop = true; // �����߳�״̬
	List list; // �ļ��б�
	Label labelfilepath; // ����Ŀ¼��ʾ��ǩ
	Label labelfilename; // �����ļ���ʾ��ǩ

	public MusicPlayer() {
		// ���ô�������
		setLayout(new BorderLayout());
		setTitle("MP3���ֲ�����");
		setSize(350, 370);
		// �����˵���
		MenuBar menubar = new MenuBar();
		Menu menufile = new Menu("�ļ�");
		MenuItem menuopen = new MenuItem("��", new MenuShortcut(KeyEvent.VK_O)); // new
																				// MenuItem("��
		menufile.add(menuopen);
		
		menufile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});
		menubar.add(menufile);
		setMenuBar(menubar);
		// �ļ��б�
		list = new List(10); // new List(10);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// ˫��ʱ����
				if (e.getClickCount() == 2) {
					// ����ѡ�е��ļ�
					filename = list.getSelectedItem(); // "F:\\DJ
														// Music\\(DJ)������[1].mp3";//list.getSelectedItem();
					play();
				}
			}
		});

		add(list, "Center");
		// ��Ϣ��ʾ

		Panel panel = new Panel(new GridLayout(2, 1));
		labelfilepath = new Label("����Ŀ¼��");
		labelfilename = new Label("�����ļ���");
		panel.add(labelfilepath);
		panel.add(labelfilename);
		add(panel, "North");

		// ע�ᴰ��ر��¼�
		addWindowStateListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setVisible(true);
	}

	// ��
	private void open() {
		FileDialog dialog = new FileDialog(this, "Open", 0);
		dialog.setVisible(true);
		filepath = dialog.getDirectory();
		if (filepath != null) {
			labelfilepath.setText("����Ŀ¼��" + filepath);
			// ��ʾ�ļ��б�
			list.removeAll();
			File filedir = new File(filepath);
			File[] filelist = filedir.listFiles();
			for (File file : filelist) {
				String filename = file.getName().toLowerCase();
				if (filename.endsWith(".mp3") || filename.endsWith(".wav")) {
					list.add(filename);
				}
			}
		}
	}

	// ����
	private void play() {
		try {
			isStop = true;
			// ֹͣ�����߳�
			// �ȴ������߳�ֹͣ
			System.out.print("��ʼ���ţ�" + filename.toString());
			while (!hasStop) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {

				}
			}
			File file = new File(filepath + filename);
			labelfilename.setText("�����ļ���" + filename);
			// ȡ���ļ�������
			audioInputStream = AudioSystem.getAudioInputStream(file);
			if (audioInputStream != null) {
				audioFormat = audioInputStream.getFormat();
			}
			// ת��MP3�ļ�����
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16,
						audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
			}

			// ������豸
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			if (null == sourceDataLine) {
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();

			} else {
				sourceDataLine.stop();

				sourceDataLine.drain();
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();
			}

			// ���������߳̽��в���
			isStop = false;
			PlayThread playThread = new PlayThread(audioInputStream, sourceDataLine);
			Thread thread = new Thread(playThread);

			playThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new MusicPlayer();

	}
}
