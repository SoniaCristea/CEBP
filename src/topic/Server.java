
package topic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

  ServerSocket serverSocket;
  private ArrayList<ClientToServer> clients;
  public static volatile LinkedBlockingQueue<Message> messages;
  protected static LinkedList<Message> messageHistory;
  protected static int timeout;

  public Server(int port, int timeout) {

    clients = new ArrayList<>();
    messages = new LinkedBlockingQueue<>();
    messageHistory = new LinkedList<>();
    this.timeout = timeout;

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
            Thread.sleep(10000);

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

              messageHistory.add(msg);
              sendToAll(msg);
              printHistory();

              new java.util.Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                  removeMessageFromHistory(msg);

                }
              }, Server.this.timeout);

            }

          } catch (IndexOutOfBoundsException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
          }
        }

      }

    };

    messageHandle.start();

  }

  protected void printHistory() {

    for (Message m : messageHistory) {
      System.out.println("g" + m.getContent());
    }
    System.out.println();

  }

  protected void removeMessageFromHistory(Message msg) {

    Iterator<Message> itr = messageHistory.iterator();

    while (itr.hasNext()) {
      Message message = itr.next();

      if (message.equals(msg)) {
        itr.remove();
      }
    }

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

    Server server = new Server(Integer.parseInt(args[0]), 10000);

  }
}
