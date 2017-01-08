package hrjgd_Client;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.ObjectOutputStream.PutField;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.spec.RC2ParameterSpec;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
public class xuebaclient {
		
	Socket client = null;
	
	public static void main (String args[]){	
		
		
		q.gui();
		
		
	}	
}


/***********************************TCP Socket功能*******************************************/




class socket extends Thread{           
		// TODO 自动生成的构造函数存根
	String username = null;
	Socket sock = null;
	boolean sockjion = false;
	boolean joinin ; 																// ui可根据此值进行判定，如果为true则登录通过，如果为flase则返回继续输入用户名并显示原因；	
	BufferedReader systemin=null;
	BufferedWriter pw=null;
	BufferedReader br = null;
	int UDP_PORT  ;
	Map<String, String> list=null;
	// 登录错误显示输出
	
	
	/**********************************************socket创建构造函数***************************************/
	public socket (String host,int port) {
		try {
			sock = new Socket(host,port);
			UDP_PORT = sock.getLocalPort();
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String joinerror = br.readLine();
			if(joinerror.equals("error: this host is connected.")){
				System.out.println(joinerror);                        //需要更改，删除				
			}else if (joinerror.equals("success, give a username please.")){
				sockjion= true;
				System.out.println(joinerror);     					 //需要更改，删除
			}			
			
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	/*************************TCP线程*********************************************/
	
	
	
	  public void run() {
		  
		  String result;
		try {
			
			result = br.readLine();
			
			  if(result.equals("success, added.Transporting the online list.")){
				 
				  
				  	DatagramSocket udpsock = new DatagramSocket(UDP_PORT);		  		
				    byte[] recvBuf = new byte[4096];
				    DatagramPacket packet = new DatagramPacket(recvBuf,recvBuf.length);
				    pw.write("i am coming");
				    pw.newLine();
				    pw.flush();
				    udpsock.receive(packet);
				    //int byteCount = packet.getLength();    
				    ByteArrayInputStream byteStream = new
				                                ByteArrayInputStream(recvBuf);
				    ObjectInputStream is = new  ObjectInputStream(new BufferedInputStream(byteStream));
				   this.setmap((Map<String, String>) is.readObject());  
				    outuserlist outframe = new outuserlist(list);
				    updataonline onlineupdata = new updataonline(list);
				    onlineupdata.start();
				    
				    is.close();			    
				    udpsock.close();
				    
				    sendFilelisten sendfilelisten = new sendFilelisten(list);
				    sendfilelisten.start();
				    Sendfile sendfile = new Sendfile();
				    sendfile.start();
				    listen listen = new listen(list);
				    listen.start();
				    
				    /******************测试服务端有没关闭***************************/
				    while(true){        
				    	pw.write("学霸你还在吗");
					    pw.newLine();
					    pw.flush();
					    Thread.sleep(5000);
				    }
				    
				    
				    
				    
				    
				    
				    			
					
					
					
			  }
			  else if(result.equals("error: can't add user info user online list.")){
				  System.exit(1);	 //需要更改
			  }
		  } catch (IOException e1) {
			// TODO 自动生成的 catch 块
			  JOptionPane.showMessageDialog(null, "连接断开。","错误",JOptionPane.ERROR_MESSAGE);
			  System.exit(1);
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		  
	  }
	
	  
	  
	  
	  
	  /**************************************如果joinin为true，则进入聊天，flase则显示用户名输入错误并重新输入****************************************************/
	  
		public boolean setusername(String username){
			try {	
				pw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
				pw.write(username);
				pw.newLine();
				pw.flush();
				String str = br.readLine();
				if(str.equals("success, adding username to list now.")){
					
					this.username = username;
					System.out.println(str);                                           //需要更改,setText()
					//pw.close();
					//br.close();
					joinin = true;
					
				}else if (str.equals("error: username contain illegal character.")){
					System.out.println(str);                                             //需要更改，输出到Jtext中
					joinin = false;										 
				}else if (str.equals("error: username already contains.")){
					System.out.println(str);                                             //需要更改，输出到Jtext中
					joinin = false;
				}
				else if (str.equals("error: illegal username length.")){
					System.out.println(str);                                             //需要更改，输出到Jtext中
					joinin = false;
				}				
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return joinin;
		}	
		
    /************************取出用户列表map **************************************/
		Map<String, String> getmap(){
			return list;		
		}
	/************************设置用户列表map**********************************************/	
		void setmap(Map<String, String> list){
			this.list = list;
		}
		
		
		
		
		
		
		

		
}





/**************************************在线列表更变*****************************************/

class updataonline extends Thread  {
	String updata;
	Map<String, String> list;
	public updataonline(Map<String, String> list) {
		this.list=list;
	}
	public void run(){
		
		try {
			InetAddress grop = InetAddress.getByName("239.0.0.250");
			MulticastSocket udpdata = new MulticastSocket(8012);
			udpdata.joinGroup(grop);
			while (true) {
			DatagramPacket input = new DatagramPacket(new byte[512], 512);
			udpdata.receive(input);
			this.updata = new String(input.getData(),0,input.getData().length);
			String updata[] = this.updata.split(",");
			if(updata[0].equals("0")){
				list.remove(updata[1]);							
			}else {
				list.put(updata[1], updata[2]);		
			}
			outuserlist ot = new outuserlist(list);
							
			}
			
			
		
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}


/*******************************************打印出用户列表********************************************/
class outuserlist{
	
	public outuserlist(Map<String, String> list) {       
		// TODO 自动生成的构造函数存根
		q.infoOfMemberText.setText("");
		Collection<String> coll = list.values();
		Iterator<String> it= coll.iterator();
		it = coll.iterator();                             //需要更改，输出每个用户名setText()
		while (it.hasNext()) {
			q.infoOfMemberText.append(it.next()+"\r\n");
		}		
	}	
}













/*********************************组播接收功能**************************************/


class listen extends Thread{  
	Map<String, String> list;
	public listen(Map<String, String> list) {
		// TODO 自动生成的构造函数存根
		this.list = list;
	}
	public void run() {
		try {
			MulticastSocket rsp = new MulticastSocket(8013);
			InetAddress grop = InetAddress.getByName("239.0.0.250");
			rsp.joinGroup(grop);
			String str = null;
			String check = null;
			while (true) {
				DatagramPacket input = new DatagramPacket(new byte[1024], 1024);
				rsp.receive(input);
				str = new String(input.getData(),0,input.getLength());
				String ip = input.getAddress().getHostAddress();
				String msg = list.get(ip)+":"+str+"\r\n";
				q.recviveArea.append(msg);
			}			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}	
}

/********************************组播发送功能******************************************/

class speak extends Thread{      	
	
	
	String str;
	public speak(String str) {
		// TODO 自动生成的构造函数存根
		this.str=str;
	}
	
	
	
	public void run() {
		try {
			MulticastSocket rsq = new MulticastSocket();
			InetAddress grop = InetAddress.getByName("239.0.0.250");
			rsq.joinGroup(grop);
			DatagramPacket output = null;
				output= new DatagramPacket(str.getBytes(), str.getBytes().length,grop,8013);
				rsq.send(output);								
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}
} 

/***************************组播发送文件通知************************************/
class snedFileNoti extends Thread{      	
	
	
	File f;
	private String str;
	public snedFileNoti(File f) {
		// TODO 自动生成的构造函数存根
		this.f=f;
	}
	
	
	
	public void run() {
		try {
			
			MulticastSocket rsq = new MulticastSocket();
			InetAddress grop = InetAddress.getByName("239.0.0.250");
			rsq.joinGroup(grop);
			
			String str = f.getPath() + "," + f.getName() + "," + f.length();
			System.out.println(str);
			DatagramPacket output = null;
				output= new DatagramPacket(str.getBytes(), str.getBytes().length,grop,8014);
				rsq.send(output);								
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}
}

/*********************************组播接收功能**************************************/


class sendFilelisten extends Thread{  
	Map<String, String> list;
	public sendFilelisten(Map<String, String> list) {
		// TODO 自动生成的构造函数存根
		this.list = list;
	}
	public void run() {
		try {
			MulticastSocket rsp = new MulticastSocket(8014);
			InetAddress grop = InetAddress.getByName("239.0.0.250");
			rsp.joinGroup(grop);		
			String str = null;
			String check = null;
			while (true) {
				DatagramPacket input = new DatagramPacket(new byte[1024], 1024);
				rsp.receive(input);
				str = new String(input.getData(),0,input.getData().length);
				String strrec[] = str.split(",");
				
				String filePath = strrec[0];
				String fileName = strrec[1];
				String fileSize = strrec[2];
				String hostname = input.getAddress().getHostAddress();
				
				q.createFileTransInfo(hostname,list.get(hostname),filePath,fileName,fileSize);
			}			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}	
}

/*************************发送文件*******************************/
class Sendfile extends Thread{
	
	public Sendfile(){
		
	}
	
	public void run(){
		try {
			DatagramSocket socket = new DatagramSocket(8015);
			
			while(true){
				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
				socket.receive(packet);
				int realLen = 0;
				for(;realLen<packet.getData().length && packet.getData()[realLen]!=0;realLen++){
					
				}
				String filepath = new String(packet.getData(),0,realLen);
				File_Transfer_Client ftc = new File_Transfer_Client(new File(filepath),packet.getPort(),packet.getAddress().getHostAddress());
				ftc.start();
			}
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
}

/**************************接收文件****************************/
class Recvfile extends Thread{
	
	String hostname;
	String filepath;
	String savePath;
	
	public Recvfile(String hostname,String filepath,String savepath) {
		this.hostname = hostname;
		this.filepath = filepath;
		this.savePath = savepath;
	}
	
	public void run(){
		try {
			DatagramSocket socket = new DatagramSocket();
			String sendcontent = filepath;
			DatagramPacket packet = new DatagramPacket(sendcontent.getBytes(), sendcontent.getBytes().length,InetAddress.getByName(hostname),8015);
			socket.send(packet);
			
			int port = socket.getLocalPort();
			socket.close();
			
			new File_Transfer_Service(savePath,port);
			
			
			
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