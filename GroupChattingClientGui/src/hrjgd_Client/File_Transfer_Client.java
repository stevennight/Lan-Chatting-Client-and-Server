package hrjgd_Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class File_Transfer_Client extends Thread {
		File file;
		int port;
		String hostname;
    	public File_Transfer_Client(File file,int port,String hostname) {
    		this.file = file;
    		this.port = port;
    		this.hostname=hostname;
    	}
		public void run(){
			try {
				System.out.println("传输开始");
    			DatagramSocket client = new DatagramSocket(0);
    			DatagramPacket input = new DatagramPacket(new byte[1024], 1024);
    			String filename = file.getName();
    			InetAddress host = InetAddress.getByName(hostname);
    			DatagramPacket outputfilename = new DatagramPacket(filename.getBytes(),filename.getBytes().length,host,port);
    			client.send(outputfilename);
    			
    			System.out.println("333333");
    			client.receive(input);
    			System.out.println("文件名已发送");
    			
    			FileInputStream fis = new FileInputStream(file);
    			Integer filelong = (int) file.length();
    			String filesizeSend = filelong.toString();
    			DatagramPacket filesize = new DatagramPacket(filesizeSend.getBytes(),filesizeSend.getBytes().length,host,port);
    			client.send(filesize);
    			
    			client.receive(input);
    			
    			byte buf[] = new byte[filelong];
    			fis.read(buf);
    			//分段发送
    			int offset = 0;
    			int perlen = 50*1024;
    			int numsegment = filelong / perlen;
    			int lastseglen = filelong % perlen;
    			boolean sendcomplete = false;
    			while(!sendcomplete){
    				if(offset<numsegment){
    					//非最后一段
    					DatagramPacket outputfile = new DatagramPacket(buf, offset*perlen, perlen, host, port);
    					try {
    						Thread.sleep(10);
    					} catch (InterruptedException e) {
    						// TODO 自动生成的 catch 块
    						e.printStackTrace();
    					}
    					client.send(outputfile);
            			client.receive(input);
            			System.out.println(offset);
            			offset++;
    				}else{
    					DatagramPacket outputfile = new DatagramPacket(buf, offset*perlen, lastseglen, host, port);
    					client.send(outputfile);
            			client.receive(input);
            			break;
    				}
    				
        			//DatagramPacket outputfile = new DatagramPacket(buf, buf.length,host,8015);
        			
    			}
    			
    			System.out.println("文件内容已发送");
    			client.close();
    					
    		} catch (SocketException e) {
    			// TODO 自动生成的 catch 块
    			e.printStackTrace();
    		} catch (UnknownHostException e) {
    			// TODO 自动生成的 catch 块
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO 自动生成的 catch 块
    			e.printStackTrace();
    		}
		}
	
}
