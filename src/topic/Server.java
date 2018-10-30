package topic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

	ServerSocket serverSocket;
	private ArrayList<ClientToServer> clients;
	public static volatile LinkedBlockingQueue<Message> messages;

	public Server(int port, int time) {

		clients = new ArrayList<>();
		messages = new LinkedBlockingQueue<>();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		Thread server = new Thread() {

			public void run() {
				while (true) {

					try {
						Socket clientSocket = serverSocket.accept();
						clients.add(new ClientToServer(clientSocket));
						Thread.sleep(time);

					} catch (IOException | InterruptedException e) {
						System.out.println(e.getMessage());
					}

				}
			}

		};

		server.start();

		Thread messageHandle = new Thread() {

			public void run() {
				while (true) {
					try {

						for (int i = 0; i < messages.size(); i++) {
							Message msg = messages.take();
							sendToAll(msg);
						}

					} catch (IndexOutOfBoundsException | InterruptedException | IOException e) {
						System.out.println(e.getMessage());
					}
				}

			}

		};

		messageHandle.start();

	}

	public void sendToAll(Message message) throws IOException {
		for (ClientToServer index : clients)
			index.write(message);
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Not enough arguments");
			System.exit(-1);
		}

		Server server = new Server(Integer.parseInt(args[0]), 5000);

	}
}
