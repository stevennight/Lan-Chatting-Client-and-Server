package cn.sevaft.stevennight.groupchatting.server.data;

import java.util.HashMap;
import java.util.Map;

import cn.sevaft.stevennight.groupchatting.server.main.Main;

public class UserOnlineList {
	
	private Map<String,String> userOnlineList = new HashMap<String, String>();
	
	public UserOnlineList(){
		Main.logger.info("User Online List is created.");
	}
	
	public Map<String,String> getList(){
		return userOnlineList;
	}
	
	public boolean addUser(String hostname,String username){
		synchronized(userOnlineList){
			if(userOnlineList.containsKey(hostname)||userOnlineList.containsValue(username)){
				return false; //用户列表中已经存在该主机地址或者用户名。
			}else{
				userOnlineList.put(hostname, username);
				return true;
			}
		}
	}
	
	public int removeUser(String hostname,String username){
		synchronized(userOnlineList){
			if(userOnlineList.containsKey(hostname)){
				if(userOnlineList.get(hostname).equals(username)){
					userOnlineList.remove(hostname);
					return 1; //成功删除。
				}else{
					return 0; //在线用户列表中，主机名所对应的用户名和删除请求中的用户名不一致。
				}
			}else{
				return -1; //在线用户列表中不存在该主机记录
			}
		}
	}
	
	public void temp(){
		userOnlineList.put("aaa", "aaaaa");
		userOnlineList.put("bbbbb", "bbbbb");
		System.out.println(userOnlineList.toString());
	}
}