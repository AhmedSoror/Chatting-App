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

import Control.Client;
import Model.ServerModel5000.ThreadedServer;


public class ServerModel3000{
	
	
	static int clientNumber=0;
	ServerSocket ServerSocket;
	int port;
	static TreeSet<String>clientsName;
	static ArrayList<ThreadedServer> sockestList;
	static PrintWriter pw;
	static ClientModel toOtherServer;
	static boolean server5000Connected;
	public ServerModel3000(int port) throws IOException {
		this.port=port;
		ServerSocket=new ServerSocket(port);
		System.out.println("Server is running on port: "+port);
		clientsName=new TreeSet<String>();
		sockestList=new ArrayList<ServerModel3000.ThreadedServer>();
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
		 toOtherServer = new ClientModel(5000);
		toOtherServer.sendMessage("$$Server$$");
		server5000Connected=true;
	}
	public static void main(String[] args) throws Exception {
		int port=3000;
		try {
			ServerModel3000 server=new ServerModel3000(port);
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
		public void sendToClient(Socket connectionSocket,String msg) throws IOException {
			PrintWriter pw=new PrintWriter(connectionSocket.getOutputStream(),true);
			pw.println(msg);
		}
		public String readFromClient() throws IOException {
			String msgIn=inFromClient.readLine();
			return msgIn;
		}
		public String getMemberOtherServer() {
			String s="";
			if(server5000Connected) {
				try {
					toOtherServer.sendMessage("getMemberList");
					s=toOtherServer.readMessage();
					System.out.println("S3000 113 : "+s);
					s=toOtherServer.readMessage();
					System.out.println("S3000 115 : "+s);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return ","+s;
		}
		public String getMemberList() {	
			String s=getMemberOtherServer();
			return clientsName.toString()+s;
		}
		public String getServerList() {	
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
					System.out.println("s3000 143: "+msgIn);	
					
					if(msgIn.equalsIgnoreCase("bye")||msgIn.equalsIgnoreCase("quit")) {
						break;
					}
					if(firstConnection) {
						clientName=msgIn;
						if(clientsName.add(msgIn)) {
							sockestList.add(this);
							firstConnection=false;
							if (msgIn.equalsIgnoreCase("$$Server2$$")) {
								ServerModel3000.connectToOtherServer();
							}
							sendToClient("true,"+clientName);
						}
						else {
							sendToClient("name exists");
						}
					}
					else {
						if(msgIn.equalsIgnoreCase("GetMemberList")||(msgIn.toUpperCase().contains("MEMBER")&&msgIn.toUpperCase().contains("LIST"))) {
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
										String newMsg=msgIn.substring(0,msgIn.length()-1)+""+timelive;
										ServerModel3000.toOtherServer.sendMessage(newMsg);
//										outToServer.println(newMsg);
									}
								}
							
							
							///////////////////////////////////////////////////
							}
//							 */
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
				System.out.println("Connection with client  " + clientName + " closed 218");
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
				System.out.println("Error handling client " + clientName);
			} finally {
				try {
					socket.close();
				} catch (Exception e) {
				}
				System.out.println("Connection with client  " + clientName + " closed 240");
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
