package topic;

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
	// private String username;
	private LinkedBlockingQueue<String> queue;
	private List<String> topics;

	public ServerToFromClient(Socket client, Server ser) throws ClassNotFoundException {
		this.client = client;
		queue = new LinkedBlockingQueue<>();
		this.ser = ser;
		
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			InputStream inp = client.getInputStream();
			in = new ObjectInputStream(inp);

			topics = (List<String>) in.readObject();
			
			System.out.println("Topic list received successfully");
			for ( Topic t : ser.queueOfTopics){
					if ( topics.contains(t.getHeader())){
						try {
							queue.put(t.getContent());
//							write(t.getContent());
						} catch (InterruptedException e) {
							System.out.println(e.getMessage());
						}
					}
				
				
			}
			

			
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		//System.out.println("Conn established with " + username);
	}

	@Override
	public void run() {
		startQueueThread();
		while (true) {
			try {

				Topic mess = (Topic) in.readObject();
				System.out.println("Message received " + mess.getContent());
				ser.queueOfTopics.put(mess);
				
				//TODO sa pui la fiecare client interesat de topic in queue
				for (ServerToFromClient cli : ser.getListClients()) {
					if (cli.topics.contains(mess.getHeader())) {
						cli.putInQueue(mess.getContent());
					}
					
				}

			} catch (ClassNotFoundException | IOException | InterruptedException e) {
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
			//System.out.println("Message : "+ msg + " had been successfully sent");
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void putInQueue(String message) {
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
