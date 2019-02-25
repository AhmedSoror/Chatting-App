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
	public JTextField txt_serverB;
	public JTextField txt_port;
	
	public JButton btn_joinServer; 
	public JButton btn_port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
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
		setBounds(100, 100, 264, 307);
		contentPane = new JPanel();
		setVisible(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		txt_serverB = new JTextField();
		txt_serverB.setColumns(10);
		
		btn_joinServer = new JButton("Join server");
		btn_joinServer.setEnabled(false);
		
		txt_port = new JTextField();
		txt_port.setColumns(10);
		
		 btn_port = new JButton("Start Server");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(62)
					.addComponent(btn_joinServer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(79))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(txt_serverB, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
						.addComponent(txt_port, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
					.addGap(23))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(61)
					.addComponent(btn_port, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(72))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addComponent(txt_port)
					.addGap(18)
					.addComponent(btn_port, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
					.addGap(38)
					.addComponent(txt_serverB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btn_joinServer, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
					.addGap(61))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
