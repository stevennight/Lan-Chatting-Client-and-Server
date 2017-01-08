package cn.sevaft.stevennight.groupchatting.server.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import cn.sevaft.stevennight.groupchatting.server.main.Main;

public class ChattingSocket implements Runnable {

	private int port = 8013;
	private MulticastSocket socket;
	private String hostname = "239.0.0.250";
	private InetAddress group;
	
	public ChattingSocket(){
		try {
			socket = new MulticastSocket(port);
			group = InetAddress.getByName(hostname);
			socket.joinGroup(group);
			
			Main.logger.info("Chatting socket is created.");
		} catch (IOException e) {
			Main.logger.warning("Some problems has been occurs when creating a multicast socket of chatting. Some details below: " + e.getMessage());
			System.exit(1);
		}
	}
	
	@Override
	public void run() {
		try {
			byte[] msg = new byte[1024];
			while(true){
				DatagramPacket packet = new DatagramPacket(msg, 1024);
				socket.receive(packet);
				System.out.println(new String(packet.getData(),0,packet.getData().length));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Main.logger.warning("Some problems occurs when receive the message from chatting. There are some details below: " + e.getMessage());
		}
	}
	
}
