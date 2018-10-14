package p2p;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionToServer {

	private Socket client;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private String uniqueID;

	public ConnectionToServer(Socket client, String uniqId) {
		this.uniqueID = uniqId;
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
						String message = in.readObject().toString();
						if (Client.messages.size() <= 10000) {
							
							Client.messages.put(message);
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
		String[] splits = ((String) msg).split("\\|\\|");
		System.out.println("Message sent to server " + splits[0]);
		out.flush();
		out.writeObject(msg.toString());

	}

	public String getUniqId() {
		return uniqueID;
	}
}
