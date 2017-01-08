package cn.sevaft.stevennight.groupchatting.server.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cn.sevaft.stevennight.groupchatting.server.data.UserOnlineList;
import cn.sevaft.stevennight.groupchatting.server.socket.ChattingSocket;
import cn.sevaft.stevennight.groupchatting.server.socket.ListTranSocket;
import cn.sevaft.stevennight.groupchatting.server.socket.ListeningSocket;

public class Main {
	
	public static Logger logger;
	public static ExecutorService pool;
	public static int poolSize = 100000;
	public static UserOnlineList userOnlineListClass;
	public static ListTranSocket ltsocket;
	
	public static void main(String[] args){
		
		logger = Logger.getLogger(Main.class.getName());
		pool = Executors.newFixedThreadPool(poolSize);
		//创建用户在线列表。
		userOnlineListClass = new UserOnlineList();
		//userOnlineListClass.temp();
		//启动各种socket
		ltsocket = new ListTranSocket();
		pool.execute(new ChattingSocket());
		pool.execute(new ListeningSocket());
	}
}
