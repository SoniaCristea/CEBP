package topic;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientToServer {

	private Socket client = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	public ClientToServer(Socket clientSocket) {
		client = clientSocket;

		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			InputStream inp = client.getInputStream();
			in = new ObjectInputStream(inp);

		} catch (IOException e) {
			System.out.println("Error in getting I/O stream");
			System.out.println(e.getMessage());
		}

		Thread read = new Thread() {

			public void run() {
				while (true) {
					try {
						Object message = in.readObject();
						Server.messages.put(message);

					} catch (InterruptedException | IOException | ClassNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
			}

		};

		read.start();

	}

	public void write(Object msg) throws IOException {
		System.out.println("Message: "+msg+" sent to clients");
		out.writeObject(msg.toString());
		out.flush();
	}


}