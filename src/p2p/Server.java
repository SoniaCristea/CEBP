package p2p;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

	ServerSocket serverSocket;
	public volatile static LinkedBlockingQueue<Object> messages;
	private ArrayList<ClientToServer> clients;
	protected static final int maxNoOfMsg = 1000;
	private volatile static boolean hasClientConnections = false;

	public Server(int port, int time) {

		messages = new LinkedBlockingQueue<>();
		clients = new ArrayList<>();
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
						hasClientConnections = true;
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

						String message = messages.take().toString();
						String[] split = message.split("\\|\\|");
						sendToOne(split[0], split[1]);
						System.out.println(message.toString());
					} catch (InterruptedException | IndexOutOfBoundsException | IOException e) {
						System.out.println(e.getMessage());
					}
				}

			}

		};

		messageHandle.start();

	}

	public void sendToOne(String id, Object message) throws IndexOutOfBoundsException, IOException {
		for (ClientToServer cl : clients) {
			if (cl.getUniqueID().equals(id)) {
				
				System.out.println("Message sent to " + id + " . Message content: " + message);
				cl.write(message);
			}
		}
		// clients.get(index).write(message);
	}

	public void sendToAll(Object message) throws IOException {
		for (ClientToServer index : clients)
			index.write(message);
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Not enough arguments");
			System.exit(-1);
		}

		Server server = new Server(Integer.parseInt(args[0]), 5000);

		while (!Server.hasClientConnections) {
			//
		}
		// server.sendToAll("Hallo!:)");

	}
}
