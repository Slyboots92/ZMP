import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;


public class Server extends Thread
{
	
	private DatagramSocket serSoc;
	private ArrayList<User> list;
	private byte receive[]= new byte[20];
	private byte send[]=new byte[144];
	private byte[] board= new byte[144];
	private int points=0;
	
	public Server()
	{
		try {
			this.serSoc= new DatagramSocket(4443);
			this.list= new ArrayList<User>();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<12;i++)
		{
			for(int j=0;j<12;j++)
			{
				
				board[i+12*j]=0;
			}
		}
		Random rand= new Random();
		for(int i=0;i<40;i++)
			board[Math.abs(rand.nextInt()%144)]=4;
		for(int i=0;i<10;i++)
			board[Math.abs(rand.nextInt()%142)+1]=5;
		
		board[0]=1;
		board[11]=2;
		board[143]=3;
		for(int i=0;i<144;i++)
		{
			if(board[i]==5)
				points++;
		}
		
		
	}
	private boolean checkPlayer(String login)
	{
		boolean flag=false;
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getLogin().equals(login))
				flag=true;
		}
		
		return flag;
	}
	private void clean(byte [] board)
	{
		for(int i=0;i<board.length;i++)
			board[i]=0;
		
	}
	private void send2All()
	{
		byte sendTemp[] = new byte[board.length];
		for(int j=0;j<board.length;j++)
			sendTemp[j]=board[j];
		for(int i=0;i<list.size();i++)
		{
			send=sendTemp;
			DatagramPacket sendPacket = new DatagramPacket(send,
					send.length, list.get(i).getIP(), list.get(i).getPort());       
			try {
				serSoc.send(sendPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
	}
	private void nextTurn(String rec)
	{
		int hero;
		hero=Integer.parseInt(rec.substring(0,1));
		int location;
		System.out.println(" szukam hero "+hero);
		for(location=0;location<144;location++)
		{
			
			if(board[location]==hero)
			{
				System.out.println("znalazlem "+ location);
				break;
			}
			
		}
		if(rec.contains("up"))
		{
			System.out.println("gora");
			if(location>=12&&(board[location-12]==0||board[location-12]==5))
			{
				if(board[location-12]==5)
				{
					list.get(hero-1).setPoints(list.get(hero-1).getPoints()+1);
					System.out.println("gracz nr "+hero+" ma"+list.get(hero-1).getPoints());
					points--;
				}
				board[location]=0;
				board[location-12]=(byte) hero;
			}
		}
		else if(rec.contains("down"))
		{
			System.out.println("dol");
			if(location+12<=143&&(board[location+12]==0||board[location+12]==5))
			{
				if(board[location+12]==5)
				{
					list.get(hero-1).setPoints(list.get(hero-1).getPoints()+1);
					System.out.println("gracz nr "+hero+" ma"+list.get(hero-1).getPoints()+1);
					points--;
				}
				board[location]=0;
				board[location+12]=(byte) hero;
			}
		}
		else if(rec.contains("right"))
		{
			System.out.println("prawo");
			if((location%12)+1<12&&(board[location+1]==0||board[location+1]==5))
			{
				if(board[location+1]==5)
				{
					list.get(hero-1).setPoints(list.get(hero-1).getPoints()+1);
					System.out.println("gracz nr "+hero+" ma"+list.get(hero-1).getPoints()+1);
					points--;
				}
				board[location]=0;
				board[location+1]=(byte) hero;
			}
		}
		else if(rec.contains("left"))
		{
			System.out.println("lewo");
			if((location%12)-1>=0&&(board[location-1]==0||board[location-1]==5))
			{
				if(board[location-1]==5)
				{
					list.get(hero-1).setPoints(list.get(hero-1).getPoints()+1);
					System.out.println("gracz nr "+hero+" ma "+list.get(hero-1).getPoints());
					points--;
				}
				board[location]=0;
				board[location-1]=(byte) hero;
			}
		}
	
			
		
		
	}
	public void run() {
		int players=1;
		while(players<4)
		{
			clean(receive);
			
			DatagramPacket packet= new DatagramPacket(receive, receive.length);
			try {
				serSoc.receive(packet);
				String result =new String(packet.getData());
				System.out.println(result);
				if(result.startsWith("login"))
				{
					if(!checkPlayer(result.substring(5)))
					{
						User user= new User(packet.getAddress(),
								result.substring(5),
								packet.getPort(),players);
						System.out.println(user.getLogin());
						list.add(user);
						send=("ok"+players).getBytes();
						players++;
						
					}
					else
					{
						send="no".getBytes();
					}
					DatagramPacket sendPacket = new DatagramPacket(send,
							send.length, packet.getAddress(), packet.getPort());       
					serSoc.send(sendPacket); 
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		DatagramPacket packet= new DatagramPacket(receive, receive.length);
		for(int i=0;i<receive.length;i++)
			receive[i]=0;
		while(points>0)
		{
			
			send2All();
			try {
				serSoc.receive(packet);
				nextTurn(new String(packet.getData()));
				System.out.println("pozostalo pkt "+points);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		send2All();
		for(int i=0;i<3;i++)
			System.out.println(list.get(i).getLogin());
		
		board=("wyniki:\n" +
				"gracz   "+list.get(0).getLogin()+"     "+ list.get(0).getPoints()+"\n"+
				"gracz   "+list.get(1).getLogin()+"     "+ list.get(1).getPoints()+"\n"+
				"gracz   "+list.get(2).getLogin()+"     "+ list.get(2).getPoints()+"\n").getBytes();
				
		send2All();
		
	}
	

}
