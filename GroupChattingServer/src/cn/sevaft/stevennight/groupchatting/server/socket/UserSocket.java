package cn.sevaft.stevennight.groupchatting.server.socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sevaft.stevennight.groupchatting.server.main.Main;

public class UserSocket implements Runnable {
	
	private int sleepTime = 5000;
	private String hostname;
	private String username;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	
	public UserSocket(Socket socket){
		this.socket = socket;
		
		Main.logger.info(socket.getInetAddress().getHostAddress()+" socket connected.");
	}

	@Override
	public void run() {
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			hostname = socket.getInetAddress().getHostAddress();
			//检查主机是否有多个连接。
			if(Main.userOnlineListClass.getList().containsKey(hostname)){
				bw.write("error: this host is connected.");
				bw.flush();
				throw new CustomException();
			}else{
				bw.write("success, give a username please.");
				bw.newLine();
				bw.flush();
			}
			
			//用户名处理
			String pattern = "^[0-9A-Za-z]+?$";
			Pattern r = Pattern.compile(pattern);
			boolean usernameChecked = false;
			while(!usernameChecked){
				username = br.readLine();
				Matcher m = r.matcher(username);
				String UsrnmResult;
				if(username.length()<3||username.length()>15){
					UsrnmResult = "error: illegal username length.";
				}else if(!m.find()){
					UsrnmResult = "error: username contain illegal character.";
				}else if(Main.userOnlineListClass.getList().containsValue(username)){
					UsrnmResult = "error: username already contains.";
				}else{
					UsrnmResult = "success, adding username to list now.";
					usernameChecked = true;
				}
				bw.write(UsrnmResult);
				bw.newLine();
				bw.flush();
			}

			if(Main.userOnlineListClass.addUser(hostname, username)){
				bw.write("success, added.Transporting the online list.");
				bw.newLine();
				bw.flush();
				
				//给用户传送列表
				br.readLine();
				DatagramSocket udpsocket = new DatagramSocket(0);
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(4096);
				ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
				os.flush();
				os.writeObject(Main.userOnlineListClass.getList());
				os.flush();
				//retrieves byte array
				byte[] sendBuf = byteStream.toByteArray();
				DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, InetAddress.getByName(hostname), socket.getPort());
				udpsocket.send(packet);
				os.close();
				udpsocket.close();
				
				//给所有在线用户广播用户变动。
				if(!Main.ltsocket.msgSend(1, hostname, username)){
					Main.logger.warning("Sending user online change failed.");
				}
				
				//心跳包
				while(true){
					bw.write("heat beated.");
					bw.newLine();
					bw.flush();
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						Main.logger.warning("Some problems has happen when heat beated send. Some details below: " + e.getMessage());
					}
				}
			}else{
				bw.write("error: can't add user info user online list.");
				bw.newLine();
				bw.flush();
			}
			
		} catch (IOException e) {
			Main.logger.warning("There are some problems to connecting users. There are some details below: " + e.getMessage());
		} catch (CustomException e) {
			Main.logger.warning("There are some problems when connect to user.");
		} finally{
			Main.userOnlineListClass.removeUser(hostname, username);
			if(!Main.ltsocket.msgSend(0, hostname, username)){
				Main.logger.warning("Sending user online change failed.");
			}
			
			try {
				bw.close();
				br.close();
				socket.close();
			} catch (IOException e) {
				Main.logger.warning("There are some problems when closing the stream. There are some details below: " + e.getMessage());
			}
		}
	}
	
}

class CustomException extends Exception{

	private static final long serialVersionUID = 1L;
	 
 }
