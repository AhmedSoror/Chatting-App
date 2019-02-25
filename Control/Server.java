package Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import Model.ServerModel;
import View.ServerFrame;

public class Server {
	
	static int port;
	static ServerModel model;
	public static void main(String[] args) {
		ServerFrame frame=new ServerFrame();
		
		frame.btn_port.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
					try {
						port=Integer.parseInt(frame.txt_port.getText());
						model= new ServerModel(port);
						frame.btn_joinServer.setEnabled(true);
						Thread t=new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									model.run();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "port number is taken");
					}
				
				
		/*		
				 try {
					port=Integer.parseInt(frame.txt_port.getText());
					model=new ServerModel(port);
					Thread t=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								model.run();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "port number is taken");
				}
				 catch(NumberFormatException e) {
					 JOptionPane.showMessageDialog(null, "Please enter a valid port number");
				 }
				 */
			}
		});
		
		frame.btn_joinServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int serverPort=Integer.parseInt(frame.txt_serverB.getText());
					model.connectToServer(serverPort);
				
				}
				catch(NumberFormatException e) {
					 JOptionPane.showMessageDialog(null, "Please enter a valid port number");
				}
				catch (IOException e) {
					JOptionPane.showMessageDialog(null, "No server on this port");
				}
				
				
			}
		});
		
	}
	
}
