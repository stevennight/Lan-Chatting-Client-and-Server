package hrjgd_Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
//import java.sql.Date;




import javax.net.ssl.SSLContext;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 * @date ������
 * @author abc
 *
 */

public class q  {
    private static socket sock;


    static String ipServer ;
    int deterCnt;
    int buttonCnt;

    public static JFrame signInFrame = new JFrame("QQ2035");
//  public static JFrame waitJFrame = new JFrame("QQ2035");
    public static JFrame ipFrame = new JFrame("QQ2035");
    public static JTextField ipText = new JTextField("172.16.145.90",15);
    public static  void gui() {

        //���������ip����
        createIPFrame();
    }

    /**
     * ����ȫ���û����������
     * 
     */
    public static JTextField countText = new JTextField("",15);

    public static void createIPFrame(){
        
    	ipFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	signInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	//�������������IP����
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setLayout( new GridLayout(2,1,8,8) );

        JButton ipButton = new JButton("              ��  ��              ");

        signInFrame.setLayout( new BorderLayout() );
        panel2.add(ipText);
        panel2.add(ipButton);

        panel.add( createTopLayout(),BorderLayout.NORTH );  
        panel.add( panel2,BorderLayout.CENTER );

        ipButton.addActionListener( new ActionListener(){
            //��ȡ����ip�¼�����    
            @Override
            public void actionPerformed(ActionEvent e) {
                ipServer = ipText.getText();
                sock = new socket(ipServer,8011); //��Ҫ����
                if(sock.sockjion){
                	ipFrame.dispose();
                    createsignInFrame();
                }else{
                	JOptionPane.showMessageDialog(ipFrame, "�����������ַ�Ƿ����󣬲��������롣���߱������Ѿ����ߡ�", "����", JOptionPane.ERROR_MESSAGE);
                }
                
            }

        }); 

        ipFrame.setContentPane(panel);
        ipFrame.setSize(440,335);
        ipFrame.setVisible(true);   
    }

    public static void createsignInFrame(){
        JPanel panel = new JPanel();
        //panel.setLayout( new BorderLayout() );
        signInFrame.setLayout( new BorderLayout() );

        panel.add( createTopLayout(),BorderLayout.NORTH );  

        panel.add( createCenterLayout(),BorderLayout.CENTER );

        signInFrame.setContentPane(panel);
        signInFrame.setSize(440,335);
        signInFrame.setVisible(true);   
    }

    public static void craetJFrameWait() {
        //��¼�ȴ�����
        JFrame waitFrame = new JFrame();
        JPanel waitPanel = new JPanel();
        waitFrame.setLayout( new BorderLayout() );
        waitFrame.setTitle("deng");

        waitPanel.add( createTopLayout(),BorderLayout.NORTH );  

        waitPanel.add( new JLabel("dsvfdf"),BorderLayout.CENTER );  

        waitFrame.setContentPane(waitPanel);
        waitFrame.setSize(440,335);
        waitFrame.setVisible(true);

        waitFrame.setVisible(false);    
    }
    public static JLabel createTopLayout(){
        JLabel label1 = new JLabel();
        return label1;
    }

    public static JPanel createCenterLayout(){

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout( new BorderLayout() );
        //icoHead.setBounds(100, 200, 300, 500);;
//      ͷ��
        JLabel label1 = new JLabel();

        centerPanel.add(label1,BorderLayout.WEST);

        centerPanel.add( createUserLayout(),BorderLayout.CENTER );


        centerPanel.add( createSignUpButtonLayout(),BorderLayout.SOUTH );

        return centerPanel;
    }

    public static JPanel createUserLayout(){
    	JPanel userLayout = new JPanel();
        userLayout.setLayout( new BorderLayout() );
        //countText
        userLayout.add(countText,BorderLayout.WEST);  
        return userLayout;
    }

    public static JPanel createSignUpButtonLayout(){
        JPanel buttonLayout = new JPanel();
        JButton sginInButton = new JButton("              ��   ��              ");
        buttonLayout.add(sginInButton);

        sginInButton.addActionListener( new ActionListener(){
            //��¼�¼�����    
            @Override
            public void actionPerformed(ActionEvent e) {        
            	
            	if(!sock.setusername(countText.getText())){
            		JOptionPane.showMessageDialog(signInFrame, "���������ֻ���ĸ������3��15λ����ʽû����Ļ������û����Ѿ�����", "����", JOptionPane.ERROR_MESSAGE);
            	}else{
            		//TODO ��¼������ʾ����  but û�ɹ� ,��¼���岻��������ʾ���п����о���2s����Գɹ���ת��������
//                  craetJFrameWait();          
	//              //������������
	                q client = new q();
	                JFrame chatHouseFrame = chatHouse();        
	                chatHouseFrame.setTitle("QQ������" + "����" + countText.getText() );
	                chatHouseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                signInFrame.dispose();
            	}
            	
//              ���ֹرշ���ֻ��ʹ�ô��ڲ��ɼ�,��Դ��û���ͷţ���ε���֮���ʹ��jvm��Դ�����˷�
//              signInFrame.setVisible(false);




            }
        });     
        return buttonLayout;
    }

    /**
     * �������
     * @return
     */
    public static JFrame chatHouse(){
         JFrame chatHouseFrame = new JFrame("QQ������");
         JPanel chatMainPanel = new JPanel();
         chatMainPanel.setLayout( new BorderLayout() );

         chatMainPanel.add( craeteChatArea(),BorderLayout.WEST);

         //��Ϣ��ʾ������ʾ֪ͨ����棬��Ա�ŵ���Ϣ
         chatMainPanel.add( createInfoArea(),BorderLayout.CENTER );

         chatHouseFrame.setContentPane(chatMainPanel);
         chatHouseFrame.setSize(700,505);
         chatHouseFrame.setVisible(true);
         chatHouseFrame.setResizable(false);
         sock.start();
         return chatHouseFrame;
    }

    public static JPanel  craeteChatArea(){
        JPanel chatAreaPanel = new JPanel();
        chatAreaPanel.setLayout( new BorderLayout() );

        chatAreaPanel.add( createRecvArea(),BorderLayout.NORTH);
        chatAreaPanel.add( createSendArea(),BorderLayout.CENTER);
        chatAreaPanel.add( createSendButtonArea(),BorderLayout.SOUTH);
        return chatAreaPanel;
    }

    //������Ϣ��ʾ��
    public static JTextArea recviveArea = new JTextArea(20,40);
    //������Ϣ��ʾ��
    public static JTextArea sendArea = new JTextArea(3,40);
    //�����Ա��Ϣ��
    public static JTextArea infoOfMemberText = new JTextArea("����Ա:����\n"
                                                            + "����\n"
                                                            + "����\n"
                                                            + "����\n"
                                                            + "����\n"
                                                            + "����\n"
                                                            + "Ǯ��\n"
                                                            + "���\n",2,2);
    public static JButton sendImage = new JButton("����ͼƬ");
    public static JButton sendFile = new JButton("�����ļ�");

    public static JPanel createRecvArea(){
        JPanel recviveAreaPanel = new JPanel();
        JScrollPane recviveScroll = new JScrollPane(recviveArea);

        //������Ϣ����ֱ�������Զ�����
        recviveScroll.setVerticalScrollBarPolicy( 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        recviveArea.setEditable(false);
        recviveArea.setLineWrap(true); //������ʾ�����Զ�����

        recviveAreaPanel.add(recviveScroll);
        return recviveAreaPanel;
    }

    public static JPanel createSendArea(){
        JPanel sendAreaPanel = new JPanel();
        JScrollPane sendScroll = new JScrollPane(sendArea);

        //������Ϣ����ֱ�������Զ�����
        sendScroll.setVerticalScrollBarPolicy( 
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        sendArea.setLineWrap(true); //������ʾ�����Զ�����
        sendAreaPanel.setLayout(new BorderLayout());

        sendAreaPanel.add( createAssLabel() ,BorderLayout.CENTER);
        sendAreaPanel.add(sendScroll,BorderLayout.SOUTH);
        return sendAreaPanel;   
    }

    public static JLabel createAssLabel(){
        JLabel assLabel = new JLabel();
        return assLabel;
    }

    public static JPanel createSendButtonArea(){
        JPanel SendButtonAreaPanel = new JPanel();
        SendButtonAreaPanel.setLayout( new BorderLayout() );

        //��һ���Ӧ��QQ��������С�����
        SendButtonAreaPanel.add( new JLabel("powered by stevennight & my three classmate"),BorderLayout.WEST );
        SendButtonAreaPanel.add( craetSendButton(),BorderLayout.EAST );
        return SendButtonAreaPanel;
    }

    public static JPanel craetSendButton(){
        //�������رպͷ��Ͱ�ť���
        JPanel buttonPanel = new JPanel();
        JButton close = new JButton("�ر�(C)");
        JButton send = new JButton("����(S)");
        buttonPanel.add(close);
        buttonPanel.add(send);

        send.addActionListener( new ActionListener(){
            //�����¼�����    
            @Override
            public void actionPerformed(ActionEvent e) {

            	new Thread(new speak(sendArea.getText())).start();
                sendArea.setText(null);
            }

        });

        close.addActionListener( new ActionListener(){
            //�ر��¼�����    
            @Override
            public void actionPerformed(ActionEvent e) {            
                System.exit(0);
            }

        });

        return buttonPanel; 
    }

    public static JPanel  createInfoArea(){
        JPanel infoPanel = new JPanel();
        JPanel sendPanel = new JPanel();
        infoPanel.setLayout( new GridLayout(2,1) );
        sendPanel.setLayout(new GridLayout(1,2));
        
        sendPanel.add(sendImage);
        sendPanel.add(sendFile);
        infoPanel.add(sendPanel);
        infoPanel.add( createInfoOfMemberArea() );

        sendImage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choose = new JFileChooser();
				FileFilter filter = new FileFilter() {  
					  
				    //Ҫ���˵��ļ�  
				    public boolean accept(File f) {  
				        //��ʾ���ļ�����  
				        if (f.isDirectory()) {  
				            return true;  
				        }  
				        //��ʾ�����������ļ�     
				        return f.getName().endsWith(".jpg") || f.getName().endsWith(".jpge") ||  f.getName().endsWith(".png") ||  f.getName().endsWith(".bmp");  
				    }  
				  
				    /**   
				     * �������ʾ�ڴ򿪿���   
				     */  
				    public String getDescription() {  
				  
				        return "*.jpg,*.jpge,*.png,*.bmp";  
				    }
				};  
				choose.setAcceptAllFileFilterUsed(false);
				choose.setFileFilter(filter);
				choose.showOpenDialog(null);
				if(JOptionPane.showConfirmDialog(null, "ȷ�����������ļ���"+choose.getSelectedFile().getPath(),"ȷ��",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
					//����ͼƬ
					snedFileNoti sfnoti = new snedFileNoti(choose.getSelectedFile());
					sfnoti.start();
				}
			}
        	
        });
        
        sendFile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choose = new JFileChooser();
				choose.showOpenDialog(null);
				if(JOptionPane.showConfirmDialog(null, "ȷ�����������ļ���"+choose.getSelectedFile().getPath(),"ȷ��",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
					//�����ļ�
					//File_Transfer_Client ftc = new File_Transfer_Client(choose.getSelectedFile());
					//ftc.start();
					snedFileNoti sfnoti = new snedFileNoti(choose.getSelectedFile());
					sfnoti.start();
				}
			}
        	
        });
        
        return infoPanel;
    }

    public static JPanel createInfoOfMemberArea(){
        JPanel infoOfMemberPanel = new JPanel();
        infoOfMemberPanel.setLayout(new BorderLayout() );

        JScrollPane infoScroll = new JScrollPane(infoOfMemberText);
        infoScroll.setVerticalScrollBarPolicy( 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        infoOfMemberText.setEditable(false);

        infoOfMemberPanel.add( new JLabel("�����Ա"),BorderLayout.NORTH);
        infoOfMemberPanel.add(infoScroll);
        return infoOfMemberPanel;
    }
    
    public static JFrame createFileTransInfo(final String hostname, String username,final String filePath, String filename, String fileSize){
    	final JFrame FileTransInfo = new JFrame();
    	
    	JPanel info = new JPanel();
    	JPanel button = new JPanel();
    	
    	info.setLayout(new GridLayout(3,1));
    	button.setLayout(new GridLayout(1,2));
    	
    	JLabel usernameLabel = new JLabel(username);
    	JLabel infoLabel = new JLabel("���㷢���ļ���");
    	JLabel filenameLabel = new JLabel(filename+",��С��"+fileSize);
    	info.add(usernameLabel);
    	info.add(infoLabel);
    	info.add(filenameLabel);
    	
    	JButton recvBtn = new JButton("�����ļ�");
    	JButton cancelBtn = new JButton("ȡ������");
    	button.add(recvBtn);
    	button.add(cancelBtn);
    	
    	recvBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//JOptionPane.showMessageDialog(null, filename);
				JFileChooser choose = new JFileChooser();
				choose.setDialogType(JFileChooser.SAVE_DIALOG);
				choose.showOpenDialog(null);
				new Recvfile(hostname, filePath,choose.getSelectedFile().getPath()).start();
				FileTransInfo.dispose();
			}
    		
    	});
    	cancelBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FileTransInfo.dispose();
			}
    		
    	});
    	
    	FileTransInfo.setLayout(new GridLayout(2,1));
    	FileTransInfo.add(info);
    	FileTransInfo.add(button);
    	FileTransInfo.setSize(300, 300);
    	FileTransInfo.setVisible(true);
    	
    	return FileTransInfo;
    }

}
