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
				return false; //�û��б����Ѿ����ڸ�������ַ�����û�����
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
					return 1; //�ɹ�ɾ����
				}else{
					return 0; //�����û��б��У�����������Ӧ���û�����ɾ�������е��û�����һ�¡�
				}
			}else{
				return -1; //�����û��б��в����ڸ�������¼
			}
		}
	}
	
	public void temp(){
		userOnlineList.put("aaa", "aaaaa");
		userOnlineList.put("bbbbb", "bbbbb");
		System.out.println(userOnlineList.toString());
	}
}