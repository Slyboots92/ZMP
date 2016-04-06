import java.net.InetAddress;


public class User 
{
	
	private String login;
	private InetAddress IP;
	private int port;
	private int hero;
	private int points;
	public User(InetAddress IP,String login,int port,int hero)
	{
		this.setIP(IP);
		this.setLogin(login);
		this.setPort(port);
		this.hero=hero;
		
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
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
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	

}
