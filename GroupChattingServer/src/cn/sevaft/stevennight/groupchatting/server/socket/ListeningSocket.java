package cn.sevaft.stevennight.groupchatting.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.sevaft.stevennight.groupchatting.server.main.Main;

public class ListeningSocket implements Runnable{

	private int port = 8011;
	private ServerSocket ssocket;
	
	public ListeningSocket(){
		try {
			ssocket = new ServerSocket(port);
			
			Main.logger.info("Listening socket is created.");
		} catch (IOException e) {
			Main.logger.warning("Some problems has been occur when server socket is creating. Here is detail for it:" + e.getMessage());
			System.exit(1);
		}
	}
	
	@Override
	public void run() {
			try {
				while(true){
					Socket socket = ssocket.accept();
					Main.pool.execute(new UserSocket(socket));
				}
			} catch (IOException e) {
				Main.logger.warning("Some problems has been occur when socket is listening. Here is detail for it:" + e.getMessage());
				System.exit(1);
		}
	}
	
}
