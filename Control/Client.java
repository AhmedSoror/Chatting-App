package Control;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import Model.ClientModel;
import Model.ServerModel;
import View.ClientFrame;

public class Client {
	static ClientModel model;
	static boolean connected=false;
	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame frame= new ClientFrame();
					
					frame.btn_port.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								int port=Integer.parseInt(frame.txt_port.getText());
								model=new ClientModel(port);
								Thread th=new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										while(true) {
											try {
												String msg=model.readMessage();
												StringTokenizer st=new StringTokenizer(msg,",");
												if(!connected&&st.nextToken().equalsIgnoreCase("true")) {
													connected=true;
													String name=st.nextToken();
													msg="connected to server on port "+port;
													frame.btn_send.setText("Send");
													frame.setTitle(name);
												}
												frame.txt_display.setText(frame.txt_display.getText()+"\n"+msg);
												
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}
								});
								th.start();
							}
							catch(Exception ex) {
								JOptionPane.showMessageDialog(null,"Please enter a correct port number","Error",JOptionPane.WARNING_MESSAGE);
							}
							
						}
					});
					frame.btn_send.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							String msg=frame.txt_msg.getText();
							try {
								model.sendMessage(msg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,"Please connect to server first","Error",JOptionPane.WARNING_MESSAGE);
							}
							
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
