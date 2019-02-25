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


public class ServerModel{
	
	
	static int clientNumber=0;
	ServerSocket ServerSocket;
	int port;
	static TreeSet<String>clientsName;
	static ArrayList<ThreadedServer> sockestList;
	
	public ServerModel(int port) throws IOException {
		this.port=port;
		ServerSocket=new ServerSocket(port);
		System.out.println("Server is running on port: "+port);
		clientsName=new TreeSet<String>();
		sockestList=new ArrayList<ServerModel.ThreadedServer>();
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
	public static void main(String[] args) throws Exception {
		Scanner sc=new Scanner(System.in);
		System.out.println("please eneter a port number:");
		int port=sc.nextInt();
		ServerModel server=new ServerModel(port);
		server.run();
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
			return name;
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
							sendToClient(getMemberList());
						}
						else {
							String toClient=recieverName(msgIn);
							if(toClient!=null) {
								for(ThreadedServer se:sockestList) {
									if(se.clientName.equals(toClient)) {
										se.sendToClient(msgIn);
										break;
									}
								}
							}
							else {
								sendToClient(msgIn.toUpperCase());
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
				System.out.println("Connection with client # " + clientNumber + " closed");
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
				System.out.println("Connection with client # " + clientNumber + " closed");
			}
		}
	}

}