package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

	private Socket socket;
	private String username;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;

	public Client(String hostName, int port, String userName) throws IOException {
		socket = new Socket(hostName, port);
		this.username = userName;
	}

	@Override
	public void run() {
		
		
		BufferedReader systemIn = null;
		String input = "";
		
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(username); 
			out.flush();
			systemIn = new BufferedReader(new InputStreamReader(System.in));
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		Thread read = new Thread() {

			public void run() {
				while (true) {
					try {
						System.out.println(in.readObject().toString());
					} catch (ClassNotFoundException | IOException e) {
						System.out.println(e.getMessage());
					}
					
				}
					
			}

		};
		read.start();
		
		
		try {
			while ( (input = systemIn.readLine()) != null){
				
				out.writeObject(input);
				out.flush();
				
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				systemIn.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		

	}

	public static void main(String[] args) {	
		try {
			Client client1 = new Client(InetAddress.getLocalHost().getHostAddress(), 2222,
					"Darius");
			Thread t = new Thread(client1);
			t.start();
			
			
		} catch (NumberFormatException | IOException e) {
			System.out.println(e.getMessage());
		}

		

	}

}
