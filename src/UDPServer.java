import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

public class UDPServer implements Runnable {

    protected DatagramSocket socket = null;
    protected boolean listen = true;
    protected List<Socket> clients;
    protected int port;

    public UDPServer(int port, List<Socket> clients) {
        this.port = port;
        this.clients = clients;
    }

    public void run() {
    	System.out.println("UDP Server started.") ;
        byte[] buf = new byte[10000];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
        DataOutputStream out = null;
        Socket client = null;
        while (listen) {
            // receive request
            try {
				socket.receive(packet);
            } catch (SocketTimeoutException e) { 
            	continue;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
            
            for (int i = clients.size()-1 ; i >-1; i--) {
    			client = clients.get(i);
				try {
					out = new DataOutputStream(client.getOutputStream());
	    			out.write(buf, packet.getOffset(), packet.getLength());
				} catch (IOException e) {
					clients.remove(i);
					System.out.println("client is disconnected.") ;
//					e.printStackTrace();
				}
    		}
        }
        for (int i = clients.size()-1 ; i >-1; i--) {
			client = clients.get(i);
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        socket.close();
        System.out.println("UDP Server stopped.") ;
    }
}