package hrjgd_Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class File_Transfer_Service {

	public File_Transfer_Service(String filepath,int port) {
		// TODO �Զ����ɵķ������
		try {
			DatagramSocket service = new DatagramSocket(port);
			DatagramPacket input = new DatagramPacket(new byte[1024],1024);
			service.receive(input);
			String filename = new String(input.getData(),0,input.getLength());
			System.out.println(filename);
			
			String rsp = "received";
			DatagramPacket output = new DatagramPacket(rsp.getBytes(),rsp.getBytes().length,input.getAddress(),input.getPort());
			service.send(output);
			
			DatagramPacket filesize = new DatagramPacket(new byte[512],512);
			service.receive(filesize);
			int realLen = 0;
			for(;realLen<filesize.getData().length && filesize.getData()[realLen]!=0;realLen++){
				
			}
			String filesizeStr = new String(filesize.getData(),0,realLen);
			int filelong = new Integer(filesizeStr);
			
			service.send(output);
			
			byte[] buf = new byte[filelong];
			int offset = 0;
			int perlen = 50*1024;
			int numsegment = filelong / perlen;
			int lastseglen = filelong % perlen;
			boolean sendcomplete = false;
			while(!sendcomplete){
				if(offset<numsegment){
					//�����һ��
					DatagramPacket fileinput = new DatagramPacket(buf,offset*perlen,perlen);
					System.out.println("���շֶ�");
					service.receive(fileinput);
					System.out.println("���շֶν���");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					service.send(output);
					System.out.println("������Ӧ����");
        			offset++;
				}else{
					DatagramPacket fileinput = new DatagramPacket(buf,offset*perlen,lastseglen);
					service.receive(fileinput);	
					service.send(output);
        			break;
				}
				
    			//DatagramPacket outputfile = new DatagramPacket(buf, buf.length,host,8015);
    			
			}
			
			File file = new File(filepath);
			FileOutputStream fos = new FileOutputStream(file);
			//fos.write(fileinput.getData(), 0, fileinput.getData().length);
			fos.write(buf, 0, buf.length);
			//service.send(output);
			fos.close();
			service.close();
			JOptionPane.showMessageDialog(null, filepath+"�Ѵ�����ϡ�");
		} catch (SocketException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	
	}

}
