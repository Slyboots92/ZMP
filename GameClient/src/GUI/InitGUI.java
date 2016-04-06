package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Connections.Communication;



public class InitGUI extends JFrame 
{
	private int width;
	private int height;
	private JTextField IpField;
	private JTextField portField;
	private JTextField loginField;
	private Panel panel;
	private JButton start;
	private JLabel lab4 ;

	public InitGUI(int width,int height)
	{
		
		this.width=width;
		this.height=height;
		this.panel= new Panel();
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		Toolkit tool=Toolkit.getDefaultToolkit();
		Dimension dim=tool.getScreenSize();
		setBounds((dim.width-width)/2, (dim.height-height)/2, width, height);
		setResizable(false);
		add(panel,BorderLayout.CENTER);
		setVisible(true);
	
		
	
	}
	
	
	class Panel extends JPanel
	{
		
		public Panel()
		{
			setLayout(null);
			

			
			start= new JButton("rozpocznij");
			IpField= new JTextField();
			portField= new JTextField();
			loginField= new JTextField();
			JLabel lab1= new JLabel("podaj IP");
			JLabel lab2 = new JLabel("podaj port");
			JLabel lab3 = new JLabel("podaj login");
			lab4= new JLabel();
			add(lab1);
			add(lab2);
			add(lab3);
			add(lab4);
			add(IpField);
			add(portField);
			add(loginField);
			add(start);
			lab1.setForeground(new Color(0xffffff));
			lab2.setForeground(new Color(0xffffff));
			lab3.setForeground(new Color(0xffffff));
			lab4.setForeground(new Color(0xffffff));
			lab1.setBounds(0, 0, 80, 20);
			lab2.setBounds(0, 30, 80, 20);
			lab3.setBounds(0, 60, 80, 20);
			lab4.setBounds(0, 230, 200, 20);
			IpField.setBounds(100, 0, 100, 20);
			portField.setBounds(100,30,100,20);
			loginField.setBounds(100,60,100,20);
			start.setBounds(0, 251, 200, 20);
			start.addActionListener(new StartListener());
			IpField.setText("localhost");
			portField.setText("4443");
			loginField.setText("test");
			
			
		}
		@Override
		  protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    // paint the background image and scale it to fill the entire space
		     BufferedImage img = null;
			try {
				
				img =  ImageIO.read(getClass().getResource("/images/background.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		  }
		
	}
	class StartListener implements ActionListener, Observer
	{
		private Communication communication;
		@Override
		public void actionPerformed(ActionEvent e) {
				communication = new 
					Communication(IpField.getText(),
								Integer.parseInt(portField.getText()),
								loginField.getText());
			Thread thread = new Thread(communication);
			thread.start();
			communication.addObserver(this);
					
			
		}

		@Override
		public void update(Observable o, Object arg) {
			if(communication.isConnected())
			{
				lab4.setText("zalogowano");
				GameGUI gui=new GameGUI(communication);
				communication.addObserver(gui);
				communication.deleteObserver(this);
				
				
				gui.setVisible(true);
				setVisible(false);
			}
			else
				lab4.setText("login zajety");
			
		}
		
		
		
	}
	
	

}
