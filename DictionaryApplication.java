/*
 * Tu Dien Giao Dien Do Hoa
 * @author Nguyen Quang Huy
 * @version 1.0
 * @since 14-10-2018
 */
package dictionary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javazoom.jl.player.Player;

public class DictionaryApplication extends JFrame {
	
	/*
	 * Khai báo các biến 
	 */
	private ArrayList<Dictionary> list = new ArrayList<>();
	private final String filename = "data/dictionaries.txt";
	private JPanel contentPane, panelExplain, panelTarget;
	private JList<String> jList;
	private JTextField targetField, wtarget0, wtarget, wexplain;
	private JTextArea explainArea;
	private DefaultListModel<String> model;
	private JScrollPane scrollList, scrollExplain;
	private JButton btnAdd, btnEdit, btnDel, btnPronun, btnSave;
	private Player playmp3;
	private int modifyCheck = 0;
	
	/*
	 * Hàm đọc file rồi lưu từ vào arraylist
	 */
	public void insertFromFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				Dictionary newWord = new Dictionary();
				String[] cell = line.split("\\t");
				newWord.setWord_target(cell[0].trim());
				newWord.setWord_explain(cell[1].trim());
				list.add(newWord);
				line = br.readLine();
			}
		} catch (Exception e) {}
	}
	
	/*
	 * Hàm in từ ra jList
	 */
	public void refreshList() {
		model = new DefaultListModel<String>();
		for (Dictionary element: list) {
			model.addElement(element.getWord_target());
			jList.setModel(model);
		}
	}
	
	/*
	 * Hàm tìm từ hàng loạt
	 */
	public void dictionarySearcher(String text) {
		model = new DefaultListModel<String>();
		for (Dictionary element: list) {
			if (element.getWord_target().startsWith(text)) {
				model.addElement(element.getWord_target());
				jList.setModel(model);
			}
		}

	}
	
	/*
	 * Hàm tra từ
	 */
	public void dictionaryLookUp() {
		explainArea.setText(null);
		for (Dictionary element: list) {
			if (element.getWord_target().equals(targetField.getText()))
				explainArea.setText(element.getWord_explain());
		}
	}

	/*
	 * Hàm đọc file âm thanh
	 */
	public void getAudio(String name) {
		File file = new File("data/" + name + ".mp3");
		if (file.exists()) {
			try (BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file))) {
				playmp3 = new Player(bi);
				playmp3.play();
			} catch (Exception e) {}	
		}
		else
			JOptionPane.showMessageDialog(contentPane, "Chương trình chưa cập nhật phát âm cho từ này, mong bạn thông cảm.");
	}
	
	/**
	 * Create the frame.
	 */
	public DictionaryApplication() {

		/*
		 * Khởi tạo các kiểu dữ liệu
		 */
		contentPane = new JPanel();
		scrollList = new JScrollPane();
		scrollExplain = new JScrollPane();
		scrollExplain.setEnabled(false);
		explainArea = new JTextArea();
		jList = new JList<String>();
		panelExplain = new JPanel();
		panelTarget = new JPanel();
		targetField = new JTextField();
		btnAdd = new JButton("THÊM");
		btnEdit = new JButton("SỬA");
		btnDel = new JButton("XÓA");
		btnPronun = new JButton("PHÁT ÂM");
		btnSave = new JButton("SAVE");
		wtarget0 = new JTextField();
		wtarget = new JTextField();
		wexplain = new JTextField();

		/*
		 * Đọc file và in dữ liệu mặc định
		 */
		insertFromFile();
		refreshList();		
		
		/*
		 * Nhập và tìm kiếm tự động
		 */
		targetField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String text = targetField.getText();
				text += arg0.getKeyChar();
				if (targetField.getText().isEmpty()) refreshList();
				dictionarySearcher(text);
				dictionaryLookUp();
			}
		});
		
		/*
		 * Lựa chọn từ trong jList
		 */
		jList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				for (Dictionary element: list) {
					if (!jList.isSelectionEmpty()) {
						if (jList.getSelectedValue().equals(element.getWord_target())) {
							targetField.setText(element.getWord_target());
							explainArea.setText(element.getWord_explain());
							wtarget0.setText(element.getWord_target());
							wtarget.setText(element.getWord_target());
							wexplain.setText(element.getWord_explain());
							
						}
					}
				}
			}
		});

		/*
		 * Thiết lập nút thêm từ
		 */
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] add = {"Nhập từ cần thêm:", wtarget, "Nhập nghĩa của từ:", wexplain};
				int option = JOptionPane.showConfirmDialog(contentPane, add, "THÊM TỪ", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION)
				{
					String wt = wtarget.getText();
					String we = wexplain.getText();
					Dictionary newWord = new Dictionary();
					newWord.setWord_target(wt);
					newWord.setWord_explain(we);
					list.add(newWord);
					JOptionPane.showMessageDialog(contentPane, "TỪ ĐÃ ĐƯỢC THÊM");
					modifyCheck++;
					refreshList();
				}
			}
		});
		
		/*
		 * Thiết lập nút sửa từ
		 */
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] edit = {"Nhập từ cần sửa:", wtarget0, "Nhập từ sửa mới:", wtarget, "Nghĩa mới của từ là:", wexplain};
				int option = JOptionPane.showConfirmDialog(contentPane, edit, "SỬA TỪ", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					String wto = wtarget0.getText();
					String wt = wtarget.getText();
					String we = wexplain.getText();
					for (Dictionary element: list) {
						if (element.getWord_target().equals(wto)) {
							element.setWord_target(wt);
							element.setWord_explain(we);
						}
					}
					JOptionPane.showMessageDialog(contentPane, "TỪ ĐÃ ĐƯỢC SỬA");
					modifyCheck++;
					refreshList();
				}
			}
		});
		
		/*
		 * Thiết lập nút xóa từ
		 */
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] del = {"Nhập từ cần xóa:", wtarget};
				int option = JOptionPane.showConfirmDialog(contentPane, del, "XÓA TỪ", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					String wt = wtarget.getText();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getWord_target().equals(wt))
							list.remove(i);
					}
					JOptionPane.showMessageDialog(contentPane, "TỪ ĐÃ ĐƯỢC XÓA");
					modifyCheck++;
					refreshList();
				}
			}
		});
		
		/*
		 * Thiết lập nút lưu vào file
		 */
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int option = JOptionPane.showConfirmDialog(contentPane, "Bạn có muốn lưu dữ liệu vào file không?", "LƯU VÀO FILE", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (modifyCheck == 0) {
						JOptionPane.showMessageDialog(contentPane, "Bạn chưa có thay đổi nào.");
					}
					else {
							try (PrintWriter pw = new PrintWriter(new FileWriter(filename))){
								pw.println("TỪ\tĐIỂN");
								for (Dictionary element: list) {
									pw.println(element.getWord_target() + "\t" + element.getWord_explain());
								}
							} catch (Exception e) {}
						JOptionPane.showMessageDialog(contentPane, "ĐÃ LƯU");
					}
				}
			}
		});
		
		/*
		 * Thiết lập nút phát âm
		 */
		btnPronun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (Dictionary element: list) {
					if (element.getWord_explain().equals(explainArea.getText()))
						getAudio(element.getWord_target());
				}
			}
		});
		
		
		/*
		 * Thiết lập đồ họa
		 */
		setTitle("DICTIONARY ENGLISH-VIETNAMESE");
		setFont(new Font("Helvetica World", Font.PLAIN, 14));
		setBackground(Color.WHITE);
		scrollList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollList.setBounds(10, 66, 200, 394);
		contentPane.add(scrollList);
		scrollList.setViewportView(jList);
		jList.setBorder(new EmptyBorder(2, 2, 2, 2));
		jList.setFont(new Font("Helvetica World", Font.BOLD, 19));
		panelTarget.setBackground(Color.LIGHT_GRAY);
		panelTarget.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "NH\u1EACP T\u1EEA C\u1EA6N TRA C\u1EE8U", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(112, 128, 144)));
		panelTarget.setBounds(4, 14, 212, 53);
		contentPane.add(panelTarget);
		panelTarget.setLayout(null);
		targetField.setBounds(6, 16, 200, 30);
		panelTarget.add(targetField);
		targetField.setColumns(10);
		targetField.setFont(new Font("Helvetica World", Font.PLAIN, 18));
		panelExplain.setBackground(Color.LIGHT_GRAY);
		panelExplain.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u0110\u1ECANH NGH\u0128A", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(112, 128, 144)));
		panelExplain.setBounds(220, 87, 264, 136);
		contentPane.add(panelExplain);
		panelExplain.setLayout(null);
		scrollExplain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollExplain.setBounds(10, 23, 246, 106);
		panelExplain.add(scrollExplain);
		scrollExplain.setViewportView(explainArea);
		explainArea.setLineWrap(true);
		explainArea.setWrapStyleWord(true);
		explainArea.setEditable(false);
		explainArea.setFont(new Font("Helvetica World", Font.ITALIC, 30));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panelExplain.setForeground(Color.LIGHT_GRAY);
		btnAdd.setBounds(233, 226, 78, 17);
		contentPane.add(btnAdd);
		btnEdit.setBounds(314, 226, 78, 17);
		contentPane.add(btnEdit);
		btnDel.setBounds(395, 226, 78, 17);
		contentPane.add(btnDel);
		btnDel.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnDel.setForeground(new Color(112, 128, 144));
		btnDel.setFont(new Font("Helvetica World", Font.BOLD, 10));
		btnAdd.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnAdd.setFont(new Font("Helvetica World", Font.BOLD, 10));
		btnAdd.setForeground(new Color(112, 128, 144));
		btnEdit.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnEdit.setForeground(new Color(112, 128, 144));
		btnEdit.setFont(new Font("Helvetica World", Font.BOLD, 10));
		btnPronun.setFont(new Font("Helvetica World", Font.BOLD, 10));
		btnPronun.setForeground(new Color(112, 128, 144));
		btnPronun.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnPronun.setBounds(384, 66, 100, 17);
		contentPane.add(btnPronun);
		btnSave.setForeground(new Color(112, 128, 144));
		btnSave.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnSave.setFont(new Font("Helvetica World", Font.BOLD, 10));
		btnSave.setBounds(233, 247, 240, 17);
		contentPane.add(btnSave);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryApplication frame = new DictionaryApplication();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
