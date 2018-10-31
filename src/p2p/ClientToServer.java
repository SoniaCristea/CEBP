package p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientToServer {

	private Socket client = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private String uniqId;

	public ClientToServer(Socket clientSocket) {
		client = clientSocket;

		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			InputStream inp = client.getInputStream();
			in = new ObjectInputStream(inp);
			uniqId = in.readObject().toString();

			System.out.println(uniqId);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error in getting I/O stream");
			System.out.println(e.getMessage());
		}

		Thread read = new Thread() {

			public void run() {
				while (true) {
					try {
						String message = in.readObject().toString();
						if (Server.messages.size() <= Server.maxNoOfMsg) {
							System.out.println(message.toString());

								Server.messages.put(message);
							
						}

					} catch (InterruptedException | IOException | ClassNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
			}

		};

		read.start();

	}

	public void write(Object msg) throws IOException {
		System.out.println("Message sent to client " + msg);
		out.writeObject(msg.toString());
		out.flush();
	}

	/**
	 * @return
	 */
	public String getUniqueID() {
		return uniqId;
	}

}