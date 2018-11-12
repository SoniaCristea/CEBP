package p2p;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Server extends Thread{
	
	private volatile List<ServerToFromClient> clients;
	
	public Server() throws ClassNotFoundException{
		
		ServerSocket serverSocket = null;
		clients = new ArrayList<>();
		try {
			serverSocket = new ServerSocket(2222);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		while(true){
			 ServerToFromClient stc = null;
			 
			 try {
				stc = new ServerToFromClient(serverSocket.accept(),this);

				Thread t = new Thread(stc);
				t.start();
				clients.add(stc);
				
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public List<ServerToFromClient> getListClients(){
		return clients;
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException {
		Server ser = new Server();
		ser.start();
		
		
	}
	

}
