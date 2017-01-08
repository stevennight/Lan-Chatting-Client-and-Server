package cn.sevaft.stevennight.groupchatting.server.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import cn.sevaft.stevennight.groupchatting.server.main.Main;

public class ListTranSocket {
	
	private int port = 8012;
	private String hostname = "239.0.0.250";
	private MulticastSocket socket;
	private InetAddress group;
	
	public ListTranSocket(){
		try {
			socket = new MulticastSocket(port);
			group = InetAddress.getByName(hostname);
			socket.joinGroup(group);
			
			Main.logger.info("Multicast Socket of list transportation is created.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Main.logger.warning("Multicast Socket of list transportation is fault to create. There is som details below :" + e.getMessage());
			System.exit(1);
		}
	}
	
	public boolean msgSend(int type,String hostname,String username){
		String msg = type + "," + hostname + "," + username;
		DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.getBytes().length,group,port);
		try {
			socket.send(packet);
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
}
