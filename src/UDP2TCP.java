import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UDP2TCP {
	public static void main(String []args) {
		int port = 2063;
		
		if (args != null && args.length>0) {
			port = Integer.parseInt(args[0]);
		}
		
		List<Socket> tcpClients = Collections.synchronizedList(new ArrayList<Socket>());
		TCPServer tcpServer = new TCPServer(port, tcpClients);
		UDPServer udpServer = new UDPServer(port, tcpClients);
		new Thread(tcpServer).start();
		new Thread(udpServer).start();
	}
}