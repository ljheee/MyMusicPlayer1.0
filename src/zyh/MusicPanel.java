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
			downbtn;// 播放、停止、删除、删除全部、向上。向下按钮
	private JTable table; // 歌曲信息表
	private Player player;

	public MusicPanel() {
		initCompont();
	}

	/** * 初始化界面 */
	private void initCompont() {
		// 各个按钮赋初始值
		add = new JButton("导入");
		playbtn = new JButton("试听");
		stopbtn = new JButton("停止");
		deletebtn = new JButton("单曲删除");

		deleteAllbtn = new JButton("全部删除");
		upbtn = new JButton(new ImageIcon("img/up.png"));
		downbtn = new JButton(new ImageIcon("img/down.png")); // 导入按钮点击设置
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFile();
			}
		}); // 试听按钮点击设置
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
		}); // 停止按钮点击设置
		stopbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player != null) {
					player.stop();
					player = null;
				}
			}
		}); // 单曲删除按钮点击设置
		deletebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNum = table.getSelectedRow();
				if (rowNum != -1) {
					((DefaultTableModel) table.getModel()).removeRow(rowNum);
				}
			}
		}); // 删除全部按钮点击设置
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
		}); // 添加各个按钮
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
		Vector<String> tableContent = new Vector<String>(); // 表格内容
		Vector<String> columnName = new Vector<String>();// 歌曲信息表格列名称
		columnName.add("歌曲名称");
		columnName.add("存放路径"); // 设置table
		table = new JTable(tableContent, columnName);
		table.setSelectionBackground(Color.blue);
		table.setSelectionForeground(Color.LIGHT_GRAY);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.setSize(300, 210);
	}

	/** * 添加文件 */
	private void addFile() {
		JFileChooser fc = new JFileChooser(); // 设置选入文件类型
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"mp3 or wav file", "mp3", "wav", "MP3", "WAV");
		fc.setFileFilter(filter);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设置只选择文件
		fc.setMultiSelectionEnabled(true);// 设置选择多个文件
		int intRetVal = fc.showDialog(this, "打开"); // 获取文件并添加到table
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

	/** * table的行中添加音乐文件名称name,音乐文件路径path * @param name * @param path */
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
