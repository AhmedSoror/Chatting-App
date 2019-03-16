package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Model.ServerModel3000.ThreadedServer;


public class ServerModel5000{
	
	
	static int clientNumber=0;
	ServerSocket ServerSocket;
	int port;
	static TreeSet<String>clientsName;
	static ArrayList<ThreadedServer> sockestList;
	static Socket server3000;
	static ClientModel toOtherServer;
//	static PrintWriter outToServer;
//	static BufferedReader inFromServer;
	
	public ServerModel5000(int port) throws IOException {
		this.port=port;
		ServerSocket=new ServerSocket(port);
		System.out.println("Server is running on port: "+port);
		clientsName=new TreeSet<String>();
		sockestList=new ArrayList<ServerModel5000.ThreadedServer>();
		
//		server3000=new Socket("LocalHost",3000);
		connectToOtherServer();
//		inFromServer=new BufferedReader(new InputStreamReader(server3000.getInputStream()));
		
	}
	
	public ServerSocket getServerSocket() {
		return ServerSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		ServerSocket = serverSocket;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void run() throws IOException {
		ExecutorService pool = Executors.newFixedThreadPool(20);
		int clientNumber = 0;
		while (true) {
			Socket connectionScoket=ServerSocket.accept(); 		
			Thread t=new Thread(new ThreadedServer(connectionScoket,clientNumber++));
			t.start();
		}
	}
	public static void connectToOtherServer() throws IOException {
		 toOtherServer = new ClientModel(3000);
		toOtherServer.sendMessage("$$Server2$$");
	}
	public static void main(String[] args) throws Exception {
		int port=5000;
		try {
			ServerModel5000 server=new ServerModel5000(port);
			server.run();
		}
		catch(Exception e) {
		}
	}


	static class ThreadedServer implements Runnable {
		private Socket socket;
		private int clientNumber;
		String clientName="Guest";
		BufferedReader inFromClient ;
		PrintWriter outToClient; 
		boolean firstConnection=true;  // if it is the first time to connect then client is sending his name
		
		
		
		public ThreadedServer(Socket socket, int clientNumber) throws IOException {
			this.socket = socket;
			this.clientNumber = clientNumber;
			inFromClient= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient= new PrintWriter(socket.getOutputStream(), true);
			System.out.println("New client #" + clientNumber + "connected at " + socket);
		}
		public void sendToClient(String msg) {
			outToClient.println(msg);
		}
		public String readFromClient() throws IOException {
			String msgIn=inFromClient.readLine();
			return msgIn;
		}
		public String getMemberList() {	
			return clientsName.toString();
		}
		public String recieverName(String clientSentence) {
			String name=null;
			if(clientSentence.charAt(0)=='~') { // ~ name ~
				name="";
				for(int i=1;i<clientSentence.length()&&clientSentence.charAt(i)!='~';i++) {
					name+=clientSentence.charAt(i);
				}
			}
			return name.trim();
		}
		public void run() {
			try {
				while(true) {
					String msgIn=readFromClient();
					System.out.println(msgIn);	
					if(msgIn.equalsIgnoreCase("bye")||msgIn.equalsIgnoreCase("quit")) {
						break;
					}
					if(firstConnection) {
						clientName=msgIn;
						if(clientsName.add(msgIn)) {
							sockestList.add(this);
							firstConnection=false;
							sendToClient("true,"+clientName);
						}
						else {
							sendToClient("name exists");
						}
					}
					else {
						if(msgIn.equalsIgnoreCase("GetMemberList")||(msgIn.toUpperCase().contains("MEMBER")&&msgIn.toUpperCase().contains("LIST"))) {
//							outToServer.println(msgIn);
//							String list=inFromServer.readLine();
							sendToClient(getMemberList()+"\n");
						}
						else {
							String toClient=recieverName(msgIn);
							 if(toClient!=null) {
								//////////////////////////////////////////////////////			send to client
								if(msgIn.charAt(msgIn.length()-1)=='0') {
									System.out.println("Client not found");
								}
								else {
									if(clientsName.contains(toClient)) {
										for(ThreadedServer se:sockestList) {
											if(se.clientName.equals(toClient)) {
												se.sendToClient(msgIn);
												break;
											}
										}
									}
									else {
										int timelive=Integer.parseInt(msgIn.charAt(msgIn.length()-1)+"")-1;
										String newMsg=msgIn.substring(0,msgIn.length()-1)+timelive;
										ServerModel5000.toOtherServer.sendMessage(newMsg);
//										outToServer.println(newMsg);
									}
								}
							
							
							///////////////////////////////////////////////////
							}
							else {
								sendToClient(msgIn.toUpperCase());
//								outToServer.println(msgIn);
								
							}
						}
						
					}
					
				}
			} catch (Exception e) {
//				System.out.println(clientNumber+" Closed the connection");
			} finally {
				try {
					socket.close();
				} 
				catch (IOException e) {
				}
				System.out.println("Connection with client # " + clientName + " closed");
				clientsName.remove(clientName);
				for(ThreadedServer se:sockestList) {
					if(se.clientName.equals(clientName)) {
						sockestList.remove(se);
						break;
					}
				}
			}
		}
		public void myrun() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(in.readLine().toUpperCase());
			} catch (Exception e) {
				System.out.println("Error handling client #" + clientNumber);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println("Connection with client # " + clientName+ " closed");
				clientsName.remove(clientName);
				for(ThreadedServer se:sockestList) {
					if(se.clientName.equals(clientName)) {
						sockestList.remove(se);
						break;
					}
				}
			}
		}
	}

}