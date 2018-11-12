package p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerToFromClient implements Runnable {

	private Socket client;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private Server ser;
	private String username;
	private LinkedBlockingQueue<String> queue;

	public ServerToFromClient(Socket client, Server ser) throws ClassNotFoundException {
		this.client = client;
		 queue = new LinkedBlockingQueue<>();
		this.ser = ser;
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			InputStream inp = client.getInputStream();
			in = new ObjectInputStream(inp);

			username = (String) in.readObject();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Conn established with " + username);
	}

	@Override
	public void run() {
		startQueueThread();
		while (true) {
			try {
				
				String mess = (String) in.readObject();
				System.out.println("Message received " + mess);
				String[] parse = mess.split(":");
				List<ServerToFromClient> clients = ser.getListClients();
				for(ServerToFromClient cli : clients){
					if(cli.username.equals(parse[0])){
						cli.putInQueue(parse[1]);
					}
				}

			} catch (ClassNotFoundException | IOException e) {
				System.out.println(e.getMessage());
			}

		}

	}

	private void startQueueThread() {
		Thread sendFromQueue = new Thread() {

			public void run() {
				while (true) {
					try {
						String messageToSend = queue.take();
						write(messageToSend);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					
				}
					
			}

		};
		sendFromQueue.start();
		
	}

	public void write(String msg) throws InterruptedException {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void putInQueue(String message){
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
