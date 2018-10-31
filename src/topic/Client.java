package topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

	private ConnectionToServer server;
	protected static volatile LinkedBlockingQueue<Message> messages;
	private Socket socket;
	private MessageTypes type;

	public Client(String hostName, int port, MessageTypes msgType) throws IOException {
		socket = new Socket(hostName, port);
		messages = new LinkedBlockingQueue<>();
		server = new ConnectionToServer(socket);
		this.type = msgType;

		Thread messageHandling = new Thread() {
			public void run() {
				while (true) {

					try {
						Message message = messages.take();
						
						System.out.println(message.getContent().toString());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};

		messageHandling.start();
	}

	private ConnectionToServer getServer() {
		return server;
	}

	private void sendMsg(Message msg) throws IOException, InterruptedException {

		server.write(msg);

	}

	public static void main(String[] args) throws InterruptedException {

		if (args.length < 1) {
			System.out.println("Not enough arguments");
			System.exit(-1);
		}

		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			Client client1 = new Client(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(args[0]),
					MessageTypes.STRING);
			System.out.println("Enter message:  ");
			while (true) {

				String msg = "";
				msg += buf.readLine();
				Message mess = new Message(msg, MessageTypes.STRING, 6000);
				client1.sendMsg(mess);
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println(e.getMessage());
		}

	}

}
