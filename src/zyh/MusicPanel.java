package zyh;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class MusicPanel extends JPanel {
	private JButton add, playbtn, stopbtn, deletebtn, deleteAllbtn, upbtn,
			downbtn;// ���š�ֹͣ��ɾ����ɾ��ȫ�������ϡ����°�ť
	private JTable table; // ������Ϣ��
	private Player player;

	public MusicPanel() {
		initCompont();
	}

	/** * ��ʼ������ */
	private void initCompont() {
		// ������ť����ʼֵ
		add = new JButton("����");
		playbtn = new JButton("����");
		stopbtn = new JButton("ֹͣ");
		deletebtn = new JButton("����ɾ��");

		deleteAllbtn = new JButton("ȫ��ɾ��");
		upbtn = new JButton(new ImageIcon("img/up.png"));
		downbtn = new JButton(new ImageIcon("img/down.png")); // ���밴ť�������
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFile();
			}
		}); // ������ť�������
		playbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player != null) {
					player.stop();
					player = null;
				}
				int rowNum = table.getSelectedRow();
				if (rowNum != -1) {
					player = new Player((String) table.getValueAt(rowNum, 1)
							+ "\\", (String) table.getValueAt(rowNum, 0));
					System.out.println((String) table.getValueAt(rowNum, 1)
							+ "\\" + (String) table.getValueAt(rowNum, 0));
					player.play();
				}
			}
		}); // ֹͣ��ť�������
		stopbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player != null) {
					player.stop();
					player = null;
				}
			}
		}); // ����ɾ����ť�������
		deletebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNum = table.getSelectedRow();
				if (rowNum != -1) {
					((DefaultTableModel) table.getModel()).removeRow(rowNum);
				}
			}
		}); // ɾ��ȫ����ť�������
		deleteAllbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = table.getRowCount() - 1; i >= 0; i--) {
					((DefaultTableModel) table.getModel()).removeRow(i);
				}
			}
		});
		downbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = table.getSelectedRow() + 1;
				if (n < table.getRowCount()) {
					table.setRowSelectionInterval(n, n);
				}
			}
		});
		upbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = table.getSelectedRow() - 1;
				if (n < -1) {
					n = table.getRowCount() - 1;
				}
				if (n >= 0) {
					table.setRowSelectionInterval(n, n);
				}
			}
		}); // ��Ӹ�����ť
		JPanel btnPanel = new JPanel();
		btnPanel.add(add);
		btnPanel.add(playbtn);
		btnPanel.add(stopbtn);
		btnPanel.add(deletebtn);
		btnPanel.add(deleteAllbtn);
		btnPanel.add(upbtn);
		btnPanel.add(downbtn);
		this.setLayout(new BorderLayout());
		this.add(btnPanel, BorderLayout.NORTH);
		Vector<String> tableContent = new Vector<String>(); // �������
		Vector<String> columnName = new Vector<String>();// ������Ϣ���������
		columnName.add("��������");
		columnName.add("���·��"); // ����table
		table = new JTable(tableContent, columnName);
		table.setSelectionBackground(Color.blue);
		table.setSelectionForeground(Color.LIGHT_GRAY);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.setSize(300, 210);
	}

	/** * ����ļ� */
	private void addFile() {
		JFileChooser fc = new JFileChooser(); // ����ѡ���ļ�����
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"mp3 or wav file", "mp3", "wav", "MP3", "WAV");
		fc.setFileFilter(filter);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);// ����ֻѡ���ļ�
		fc.setMultiSelectionEnabled(true);// ����ѡ�����ļ�
		int intRetVal = fc.showDialog(this, "��"); // ��ȡ�ļ�����ӵ�table
		if (intRetVal == JFileChooser.APPROVE_OPTION) {
			File[] file = fc.getSelectedFiles();
			String name;
			for (File var : file) {
				name = var.getName().toLowerCase();
				if (name.endsWith(".mp3") || name.endsWith(".wav")) {
					this.addMusicItem(var.getName(), var.getParentFile()
							.getAbsolutePath());
				}
			}
		}
	}

	/** * table��������������ļ�����name,�����ļ�·��path * @param name * @param path */
	private void addMusicItem(String name, String path) {
		Vector<String> rowData = new Vector<String>();
		rowData.add(name);
		rowData.add(path);
		// rowData.add(time);
		DefaultTableModel tabMod = (DefaultTableModel) table.getModel();
		tabMod.addRow(rowData);
	}
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jf.add(new MusicPanel());
		
		jf.pack();
		jf.setVisible(true);
	}

}
