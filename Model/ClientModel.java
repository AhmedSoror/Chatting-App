package Model;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientModel {
	public Socket ClientSocket ;
	public int port;
	public DataOutputStream outToServer;
	public BufferedReader inFromServer;
	public String chat="";
	
	public ClientModel(int port) throws UnknownHostException, IOException {
		this.port=port;
		ClientSocket=new Socket("localhost",port);
		outToServer = new DataOutputStream(ClientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
		
	}
	public Socket getClientSocket() {
		return ClientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		ClientSocket = clientSocket;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public void sendMessage(String msg) throws IOException {
		outToServer.writeBytes(msg+ '\n');
	}
	public String readMessage() throws IOException {
		return (inFromServer.readLine());
	}
	
	public void close() throws IOException {
		sendMessage("quit");
		ClientSocket.close();
	}
	
	public void chat() throws IOException {
		Scanner sc=new Scanner(System.in);
		String msg=sc.next();
		while(true) {
			if(msg.equalsIgnoreCase("quit")||msg.equalsIgnoreCase("bye")) {
				close();
				break;
			}
			sendMessage(msg);
			System.out.println(readMessage());
			msg=sc.next();
			
		}
	}
	public void recieveMessages() throws IOException {
		while(true) {
			String msgIn=readMessage();
			chat+=msgIn+"\n";
			
		}
		
	}
	public static void main(String argv[]) throws Exception {
		Scanner sc=new Scanner(System.in);
		System.out.print("please enter a port: ");
		int port=sc.nextInt();
		ClientModel c=new ClientModel(port);
		c.chat();
		
		
	}
}