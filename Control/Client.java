package Control;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JList;
import javax.swing.JOptionPane;

import Model.ClientModel;
import Model.ServerModel3000;
import View.ClientFrame;

public class Client {
	static ClientModel model;
	static String clientName;
	static boolean firstName = true;
	static boolean connected=false;
	public static String removeBrackets(String msg) {
		String s="";
		for(int i=0;i<msg.length();i++) {
			if(msg.charAt(i)=='['||msg.charAt(i)==']')
				continue;
			s+=msg.charAt(i)+"";
		}
		return s;
	}
	
	public static String[] removeServers(String[] arr) {
		LinkedList<String> list=new LinkedList<>();
		for(int i=0;i<arr.length;i++) {
			if(arr[i].contains("$$"))
				continue;
			list.add(arr[i]);
		}
		return list.toArray(new String[list.size()]);
		
	}
	public static String removeReciever(String msg) { // msg= ~reciever~!sender!body
		int i=0;
		for(i=1;i<msg.length();i++) {
			if(msg.charAt(i)=='~')
				break;
		}
		i++;
		String s=msg.substring(i+1);
		s.replace("!", ":");
		return s;
	}
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
												if(msg==null||msg.length()==0)
													continue;
												StringTokenizer st=new StringTokenizer(msg,",");
												if(!connected&&st.nextToken().equalsIgnoreCase("true")) {
													connected=true;
													String name=st.nextToken();
													msg="connected to server on port "+port+"0";
													frame.btn_send.setText("Send");
													clientName=name;
													firstName=false;
													frame.setTitle(name);
												}
												if (msg.charAt(0) == '[' && msg.charAt(msg.length() - 1) == ']') {
//													msg = msg.substring(1, msg.length() - 1);//*****************************
													msg=removeBrackets(msg);
													String[] member_server = msg.split(",");
													System.out.println("Client 96: "+Arrays.toString(member_server));
													String[] members = removeServers(member_server);
													frame.list_members.setListData(members);

												} else {
//													if(connected) {
//														frame.txt_display.setText(frame.txt_display.getText()+"\n"+msg.substring(0,msg.length()-1));	
//													}
//													else {
														try {
															System.out.println(clientName+" 98 "+msg);
															String msgMod = removeReciever(msg);
															System.out.println(clientName+" 100 "+msgMod);
															frame.txt_display.setText(frame.txt_display.getText() + "\n"+ msgMod.substring(0, msgMod.length() - 1));
														} catch (Exception e) {
															frame.txt_display.setText(frame.txt_display.getText()+"\n"+msg.substring(0,msg.length()-1));	
														}
//													}
												}
												
											} catch (Exception e) {
//												e.printStackTrace();
//												System.out.println("client 101");
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
								if(firstName) {
									model.sendMessage(msg);
//									firstName =false;
								}
								else {
									String reciever=(String) frame.list_members.getSelectedValue();
//									System.out.println(reciever);
									msg="~"+reciever+"~"+"!"+clientName+"!"+msg;
									model.sendMessage(msg+"2");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,"Please connect to server first","Error",JOptionPane.WARNING_MESSAGE);
							}
						}
					});
					frame.btn_getMembers.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							String msg="get member list";
							try {
								model.sendMessage(msg);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,"Please connect to server first","Error",JOptionPane.WARNING_MESSAGE);
							}
							
						}
					});
					frame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							if(connected) {
								try {
									model.sendMessage("BYE"+"\n");
									model.close();
								} 
								catch (Exception ex) {
									System.out.println("closing problem");
								}
							}
							System.exit(0);
							frame.dispose();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
