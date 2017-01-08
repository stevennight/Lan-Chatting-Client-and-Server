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
 * @date 第七天
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

        //输入服务器ip窗体
        createIPFrame();
    }

    /**
     * 定义全局用户名框，密码框
     * 
     */
    public static JTextField countText = new JTextField("",15);

    public static void createIPFrame(){
        
    	ipFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	signInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	//建立输入服务器IP窗体
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setLayout( new GridLayout(2,1,8,8) );

        JButton ipButton = new JButton("              连  接              ");

        signInFrame.setLayout( new BorderLayout() );
        panel2.add(ipText);
        panel2.add(ipButton);

        panel.add( createTopLayout(),BorderLayout.NORTH );  
        panel.add( panel2,BorderLayout.CENTER );

        ipButton.addActionListener( new ActionListener(){
            //获取主机ip事件监听    
            @Override
            public void actionPerformed(ActionEvent e) {
                ipServer = ipText.getText();
                sock = new socket(ipServer,8011); //需要更改
                if(sock.sockjion){
                	ipFrame.dispose();
                    createsignInFrame();
                }else{
                	JOptionPane.showMessageDialog(ipFrame, "请检查服务器地址是否有误，并重新输入。或者本主机已经在线。", "错误", JOptionPane.ERROR_MESSAGE);
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
        //登录等待窗口
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
//      头像
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
        JButton sginInButton = new JButton("              进   入              ");
        buttonLayout.add(sginInButton);

        sginInButton.addActionListener( new ActionListener(){
            //登录事件监听    
            @Override
            public void actionPerformed(ActionEvent e) {        
            	
            	if(!sock.setusername(countText.getText())){
            		JOptionPane.showMessageDialog(signInFrame, "请输入数字或字母并长度3到15位。格式没错误的话就是用户名已经存在", "错误", JOptionPane.ERROR_MESSAGE);
            	}else{
            		//TODO 登录窗体显示两秒  but 没成功 ,登录窗体不能完整显示，有卡死感觉。2s后可以成功跳转至主窗体
//                  craetJFrameWait();          
	//              //聊天室主窗口
	                q client = new q();
	                JFrame chatHouseFrame = chatHouse();        
	                chatHouseFrame.setTitle("QQ聊天室" + "——" + countText.getText() );
	                chatHouseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                signInFrame.dispose();
            	}
            	
//              这种关闭方法只是使得窗口不可见,资源并没有释放，多次调试之后会使得jvm资源大量浪费
//              signInFrame.setVisible(false);




            }
        });     
        return buttonLayout;
    }

    /**
     * 聊天界面
     * @return
     */
    public static JFrame chatHouse(){
         JFrame chatHouseFrame = new JFrame("QQ聊天室");
         JPanel chatMainPanel = new JPanel();
         chatMainPanel.setLayout( new BorderLayout() );

         chatMainPanel.add( craeteChatArea(),BorderLayout.WEST);

         //信息显示区，显示通知，广告，成员信的信息
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

    //接收信息显示框
    public static JTextArea recviveArea = new JTextArea(20,40);
    //发送信息显示框
    public static JTextArea sendArea = new JTextArea(3,40);
    //聊天成员信息框
    public static JTextArea infoOfMemberText = new JTextArea("管理员:张特\n"
                                                            + "张三\n"
                                                            + "李四\n"
                                                            + "王五\n"
                                                            + "马六\n"
                                                            + "赵七\n"
                                                            + "钱八\n"
                                                            + "孙九\n",2,2);
    public static JButton sendImage = new JButton("发送图片");
    public static JButton sendFile = new JButton("发送文件");

    public static JPanel createRecvArea(){
        JPanel recviveAreaPanel = new JPanel();
        JScrollPane recviveScroll = new JScrollPane(recviveArea);

        //接收信息框竖直滚动条自动出现
        recviveScroll.setVerticalScrollBarPolicy( 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        recviveArea.setEditable(false);
        recviveArea.setLineWrap(true); //超出显示长度自动换行

        recviveAreaPanel.add(recviveScroll);
        return recviveAreaPanel;
    }

    public static JPanel createSendArea(){
        JPanel sendAreaPanel = new JPanel();
        JScrollPane sendScroll = new JScrollPane(sendArea);

        //接收信息框竖直滚动条自动出现
        sendScroll.setVerticalScrollBarPolicy( 
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        sendArea.setLineWrap(true); //超出显示长度自动换行
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

        //这一句对应于QQ聊天界面的小广告条
        SendButtonAreaPanel.add( new JLabel("powered by stevennight & my three classmate"),BorderLayout.WEST );
        SendButtonAreaPanel.add( craetSendButton(),BorderLayout.EAST );
        return SendButtonAreaPanel;
    }

    public static JPanel craetSendButton(){
        //聊天区关闭和发送按钮面板
        JPanel buttonPanel = new JPanel();
        JButton close = new JButton("关闭(C)");
        JButton send = new JButton("发送(S)");
        buttonPanel.add(close);
        buttonPanel.add(send);

        send.addActionListener( new ActionListener(){
            //发送事件监听    
            @Override
            public void actionPerformed(ActionEvent e) {

            	new Thread(new speak(sendArea.getText())).start();
                sendArea.setText(null);
            }

        });

        close.addActionListener( new ActionListener(){
            //关闭事件监听    
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
					  
				    //要过滤的文件  
				    public boolean accept(File f) {  
				        //显示的文件类型  
				        if (f.isDirectory()) {  
				            return true;  
				        }  
				        //显示满足条件的文件     
				        return f.getName().endsWith(".jpg") || f.getName().endsWith(".jpge") ||  f.getName().endsWith(".png") ||  f.getName().endsWith(".bmp");  
				    }  
				  
				    /**   
				     * 这就是显示在打开框中   
				     */  
				    public String getDescription() {  
				  
				        return "*.jpg,*.jpge,*.png,*.bmp";  
				    }
				};  
				choose.setAcceptAllFileFilterUsed(false);
				choose.setFileFilter(filter);
				choose.showOpenDialog(null);
				if(JOptionPane.showConfirmDialog(null, "确定发送以下文件吗？"+choose.getSelectedFile().getPath(),"确定",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
					//发送图片
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
				if(JOptionPane.showConfirmDialog(null, "确定发送以下文件吗？"+choose.getSelectedFile().getPath(),"确定",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
					//发送文件
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

        infoOfMemberPanel.add( new JLabel("聊天成员"),BorderLayout.NORTH);
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
    	JLabel infoLabel = new JLabel("给你发送文件：");
    	JLabel filenameLabel = new JLabel(filename+",大小："+fileSize);
    	info.add(usernameLabel);
    	info.add(infoLabel);
    	info.add(filenameLabel);
    	
    	JButton recvBtn = new JButton("接收文件");
    	JButton cancelBtn = new JButton("取消接收");
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
