package topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client implements Runnable {

	private Socket socket;
	//private String username;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	List<String> topics;

	public Client(String hostName, int port, List<String> topics) throws IOException {
		socket = new Socket(hostName, port);
		this.topics = topics;
	}

	@Override
	public void run() {
		
		
		BufferedReader systemIn = null;
		String input = "";
		
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(topics); 
			out.flush();
			systemIn = new BufferedReader(new InputStreamReader(System.in));
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		Thread read = new Thread() {

			public void run() {
				while (true) {
					try {
						System.out.println("Message: "+in.readObject().toString()+" had been successfully received");
					} catch (ClassNotFoundException | IOException e) {
						System.out.println(e.getMessage());
					}
					
				}
					
			}

		};
		read.start();
		
		
		try {
			while ( (input = systemIn.readLine()) != null){
				
				//TODO message has to be written in the form header:content
				//split message , crate topic object and sent on the stream
				
				String[] splits = input.split(":");
				Topic t = new Topic(splits[0], splits[1],30000);
				out.writeObject(t);
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
			List<String> topic = new ArrayList<String>();
			topic.add("Nature");
//			topic.add("Food");
			topic.add("School");
			Client client1 = new Client(InetAddress.getLocalHost().getHostAddress(), 2222,
					topic);
			Thread t = new Thread(client1);
			t.start();
			
			
		} catch (NumberFormatException | IOException e) {
			System.out.println(e.getMessage());
		}

		

	}

}
