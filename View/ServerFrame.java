package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JComboBox;

public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txt_msg;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 582, 527);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		txt_msg = new JTextField();
		txt_msg.setColumns(10);
		
		JButton btn_send = new JButton("Join");
		
		JScrollPane scrollPane = new JScrollPane();
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JButton btn_port = new JButton("Port");
		
		JList list_members = new JList();
		
		JButton btn_getMembers = new JButton("Get members");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btn_port))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
								.addComponent(txt_msg, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(list_members, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btn_getMembers, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(btn_send, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_port))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(list_members, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btn_getMembers)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txt_msg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_send))
					.addContainerGap())
		);
		
		JTextArea txt_display = new JTextArea();
		txt_display.setEditable(false);
		scrollPane.setViewportView(txt_display);
		contentPane.setLayout(gl_contentPane);
	}
}
