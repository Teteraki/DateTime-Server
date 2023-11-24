import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The client can send three commands: DATE, TIME, or EXIT.
 * The server keeps the connection with the client open and responds accordingly with sending
 * the client the DATE or TIME information or to close the connection with the client, when
 * they request.
 */
public class Server {
	
	// Initialize a server socket.
    private ServerSocket serverSocket;


    public Server() {
        try {
            serverSocket = new ServerSocket(9090);
            System.out.println("Server is now running.");

            while (true) {
            	// Listens for a connection to be made to this socket and accepts it.
                Socket aSocket = serverSocket.accept();
                
                // Create a new Thread Object and start it.
                new Thread(() -> handleClient(aSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            
        } finally {
        	
        	/*
        	 * Ensure that certain cleanup operations are performed, regardless of whether 
        	 * an exception is thrown or not.
        	 */
        	
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    System.out.println("Server closed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles communication with a connected client.
     * Performs operations based on client input: DATE, TIME, or EXIT
     * @param socket
     */
    private void handleClient(Socket socket) {
        try (
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String line;

            while ((line = socketInput.readLine()) != null) {
                // Check if the client wants to end the communication
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }

                if (!line.isEmpty()) {
                	if (line.equalsIgnoreCase("date")) {
                		 socketOutput.println(line.toUpperCase() + ": " + getCurrentDate());
                	} else if (line.equalsIgnoreCase("time")) {
                		socketOutput.println(line.toUpperCase() + ": " + getCurrentTime());
                	} else {
                		socketOutput.println("INVALID COMMAND");
                	}
                }
            }

            // Close the socket for this client
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Set the format for date and return it.
     * @return currentDate
     */
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Set the format for time and return it.
     * @return currentTime
     */
    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(Calendar.getInstance().getTime());
    }
    
    /**
     * Entry point for Server
     * @param args
     */
    public static void main(String[] args) {
        new Server();
    }
}
