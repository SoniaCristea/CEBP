package topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

	private ConnectionToServer server;
	protected static volatile LinkedBlockingQueue<Object> messages;
	private Socket socket;
	

	public Client(String hostName, int port) throws IOException {
		socket = new Socket(hostName, port);
		messages = new LinkedBlockingQueue<>();
		server = new ConnectionToServer(socket);

		Thread messageHandling = new Thread() {
			public void run() {
				while (true) {
					try {

						Object message = messages.take();
						System.out.println(message.toString());
						
					} catch (InterruptedException e) {
					}
				}
			}
		};

		messageHandling.start();
	}

	private ConnectionToServer getServer() {
		return server;
	}

	private void sendMsg(Object msg) throws IOException {
		server.write(msg);
	}

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Not enough arguments");
			System.exit(-1);
		}

		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			Client client1 = new Client(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(args[0]));
			System.out.println("Enter message:  ");
			while (true) {
				String mess = "";
				mess += buf.readLine();
				client1.sendMsg(mess);
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println(e.getMessage());
		}

	}

}
