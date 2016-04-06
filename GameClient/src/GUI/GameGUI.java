package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import Connections.Communication;
import GUI.InitGUI.Panel;

public class GameGUI extends JFrame implements Observer
{

	private GamePanel gamePanel;
	private JLabel result;
	private Communication communication;
	private int width;
	private int height;
	private byte[] board= new byte[144];
	private boolean end=false;
	
	public GameGUI(Communication communication)
	{
		this.gamePanel= new GamePanel();
		this.result=new JLabel();
		this.communication=communication;
		
		Toolkit tool=Toolkit.getDefaultToolkit();
		Dimension dim=tool.getScreenSize();
		this.width=12*50;
		this.height=12*40;
		setLocation(  (dim.width-width)/2, (dim.height-height)/2);
		setSize(width, height);
		setResizable(false);
		
	
		add(gamePanel,BorderLayout.CENTER);
		
		
	
	}
	
	
	class GamePanel extends JPanel
	{
		
		
		public GamePanel()
		{
			
			InputMap imap=this.getInputMap(JComponent.WHEN_FOCUSED);
			imap.put(KeyStroke.getKeyStroke("UP"), "UpAction");
			imap.put(KeyStroke.getKeyStroke("DOWN"), "DownAction");
			imap.put(KeyStroke.getKeyStroke("RIGHT"), "RightAction");
			imap.put(KeyStroke.getKeyStroke("LEFT"), "LeftAction");
			imap.put(KeyStroke.getKeyStroke(' '), "FireAction");
			ActionMap amap= this.getActionMap();
			amap.put("UpAction", new Action("up"));
			amap.put("DownAction", new Action("down"));
			amap.put("RightAction", new Action("right"));
			amap.put("LeftAction", new Action("left"));
			amap.put("FireAction", new Action("fire"));
			System.out.println("focus "+this.hasFocus());
			
		}
		@Override
		  protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    
		    // paint the background image and scale it to fill the entire space
		     BufferedImage ground = null;
		     BufferedImage bomba = null;
		     BufferedImage torpeda = null;
		     BufferedImage gus=null;
		     BufferedImage wall=null;
		     BufferedImage bomb=null;
		     BufferedImage result=null;
			try {
				
				ground =  ImageIO.read((getClass().getResource("/images/ground1.jpg")));
				bomba=ImageIO.read(getClass().getResource("/images/bomba2.jpg"));
				torpeda= ImageIO.read(getClass().getResource("/images/torpeda.jpg"));
				gus= ImageIO.read(getClass().getResource("/images/gus.jpg"));
				wall= ImageIO.read(getClass().getResource("/images/wall.jpg"));
				bomb= ImageIO.read(getClass().getResource("/images/bombaWyb.jpg"));
				result= ImageIO.read(getClass().getResource("/images/resultBig.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    if(!end)
		    {
		    	for(int j=0;j<12;j++)
		    	{
		    		for(int i=0;i<12;i++)
		    		{
		    		
		    			if(board[i+12*j]==0)
		    				g.drawImage(ground,i*50,37*j,null);
		    			else if(board[i+12*j]==1)
		    				g.drawImage(bomba,i*50,37*j,null);
		    			else if(board[i+12*j]==2)
		    				g.drawImage(torpeda,i*50,37*j,null);
		    			else if(board[i+12*j]==3)
		    				g.drawImage(gus,i*50,37*j,null);
		    			else if(board[i+12*j]==4)
		    				g.drawImage(wall,i*50,37*j,null);
		    			else if(board[i+12*j]==5)
		    				g.drawImage(bomb,i*50,37*j,null);
		    			
		    		}
		    	}
		    }
		    else
		    {
		    	g.drawImage(result,0,0,null);
		    }
		    
		   
		  }
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		
		board=(byte[]) arg1;
		if(new String(board).contains("wyniki"))
		{
			end=true;
			JTextArea area =new JTextArea();
			gamePanel.add(area,BorderLayout.NORTH);
			area.setText(new String(board));
			gamePanel.repaint();
			gamePanel.revalidate();
			
		}
		else
		{
			gamePanel.repaint();
			gamePanel.revalidate();
			gamePanel.setFocusable(true);
			
			
		}
		
		
	}
	class Action extends AbstractAction
	{
		private String action;
		public Action(String action)
		{
			this.action=action;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(action.equals("up"))
			{
				communication.send(communication.getHero()+"up");
			}
			else if(action.equals("down"))
			{
				communication.send(communication.getHero()+"down");
			}
			else if(action.equals("right"))
			{
				communication.send(communication.getHero()+"right");
			}
			else if(action.equals("left"))
			{
				communication.send(communication.getHero()+"left");
			}
			else if(action.equals("fire"))
			{
				communication.send(communication.getHero()+"fire");
			}
			
		}
		
	}

	
	
	

}
