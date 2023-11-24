


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Trivial client for the date server.
 */
public class Client {
	private Socket palinSocket;
	private BufferedReader stdIn;
	private BufferedReader socketIn;
	private PrintWriter socketOut;
	
    public static void main(String[] args) throws IOException {
        
        Client dc = new Client("127.0.0.1", 9090);
        dc.communicate();
    }
    
	public Client(String serverName, int portNumber) {
		try {
			palinSocket = new Socket(serverName, portNumber);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			socketIn = new BufferedReader(new InputStreamReader(palinSocket.getInputStream()));
			socketOut = new PrintWriter((palinSocket.getOutputStream()), true);
		} catch (IOException e) {
			System.err.println(e.getStackTrace());
		}
	}


    
    public void communicate()  {
		try {
			
			while(true) {
				System.out.print("COMMANDS: DATE, TIME OR EXIT\nENTER SELECTION: ");
				String input = stdIn.readLine();
				
				socketOut.println(input);
				
				if (input.isEmpty()) {
					communicate();
				}
				
				if (input.equalsIgnoreCase("exit")) {
					break;
				}
				
				String response = socketIn.readLine();
				System.out.println("Server response: " + response);
			}
			
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			try {
                stdIn.close();
                socketIn.close();
                socketOut.close();
                palinSocket.close();
            } catch (IOException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
		

	}
}