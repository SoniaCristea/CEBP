package topic;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionToServer {

	private Socket client;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	public ConnectionToServer(Socket client) {
		this.client = client;

		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			InputStream inp = client.getInputStream();
			in = new ObjectInputStream(inp);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		Thread read = new Thread() {

			public void run() {
				while (true) {
					try {
						Object message = in.readObject();

						Client.messages.put(message);

					} catch (InterruptedException | IOException | ClassNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
			}

		};

		read.start();

	}

	public void write(Object msg) throws IOException {
		out.flush();
		out.writeObject(msg.toString());

	}
}
