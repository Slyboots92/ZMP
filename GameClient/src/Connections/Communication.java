package Connections;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;

public class Communication extends Observable implements Runnable
{
	private boolean isConnected;
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	public DatagramSocket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(DatagramSocket clientSocket) {
		this.clientSocket = clientSocket;
	}
	public byte[] getSendData() {
		return sendData;
	}
	public void setSendData(byte[] sendData) {
		this.sendData = sendData;
	}
	public byte[] getReceiveData() {
		return receiveData;
	}
	public void setReceiveData(byte[] receiveData) {
		this.receiveData = receiveData;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	private DatagramSocket clientSocket ;
	private byte[] sendData = new byte[20];  
	private byte[] receiveData = new byte[144];  
	private String login;
	private InetAddress IP;
	private int port;
	private String temp;
	private int hero;
	public Communication(String IP, int port,String login)
	{
		this.isConnected=false;
		this.login=login;
		this.port=port;
		
		
		try {
			this.clientSocket= new DatagramSocket();
			this.IP=InetAddress.getByName(IP);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			 
		
			
		//	
	
	}
	public void send(String message)
	{
		sendData= (message).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IP,port); 
		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
	}
	@Override
	public void run() {
		send("login"+login);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length,
				IP,port);
		try {
			clientSocket.receive(receivePacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		temp = new String(receivePacket.getData());
		System.out.println(temp);
		if(temp.contains("ok"))
		{
			isConnected=true;
			hero=Integer.parseInt(temp.substring(2,3));
		}
		else
			isConnected=false;
		
		setChanged();
		notifyObservers(isConnected);
		
		
		
		
		while(isConnected)
		{
			try {
				clientSocket.receive(receivePacket);
				setChanged();
				notifyObservers(receiveData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		
		
		
		
	}
	public int getHero() {
		return hero;
	}
	public void setHero(int hero) {
		this.hero = hero;
	}
	public InetAddress getIP() {
		return IP;
	}
	public void setIP(InetAddress iP) {
		IP = iP;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	
	
	

}
