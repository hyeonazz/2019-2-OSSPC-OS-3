package com.ok.ai;
/*

This program was written by Jacob Jackson. You may modify,
copy, or redistribute it in any way you wish, but you must
provide credit to me if you use it in your own program.

*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import com.ok.main.Main;
import com.ok.main.TMain;

import java.util.*;
import java.util.prefs.Preferences;

import javax.imageio.*;

@SuppressWarnings("serial")
public class TetrisRenderer extends Component implements KeyListener, ActionListener
{
	public static Main m = new Main();
	public static final String VERSION = "1.1";
	public static int num=0;
	public static JFrame frame = new JFrame("Tetris");

	public static JButton keyButton;
	public static JButton newButton;
	public static JButton homeButton;
	
	private static final int W = -180;
	private static final int H = Tetris.PIXEL_H + 100;
	

	private static int[] keyPresses = new int[1000];
	private static int keyPos = 0;
	
	public Tetris game;
	private Timer timer;
	private Timer painter;
	public TMain main;
	
	private Object aiLock = new Object();

	private boolean down;
	private boolean left;
	private boolean right;
	private long moveTime;
	private boolean onDas;
	private int[] settings;	
	public static final int MARATHON = 1;
	
	private static final String GAME_TYPE_SETTING = "game_type";
	
	private ImageIcon backgroundImage = new ImageIcon(Main.class.getResource("../images/test0101.png")); //background image
	public JButton background = new JButton(backgroundImage);
	public int gameType;

	Thread thread;
	
	public Dimension getMinimunSize(){
		return new Dimension(1000,720);
	}
	public TetrisRenderer()
	{
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setResizable(true); //Resize the Game screen using the mouse

	
		newButton =new JButton (new ImageIcon(Main.class.getResource("../images/button-newgame.png")));
		newButton.setBorderPainted(false);
		newButton.setContentAreaFilled(false);
		newButton.setFocusPainted(false);
		newButton.setSize(newButton.getPreferredSize());
		newButton.setLocation(W / 2 - newButton.getWidth() / 2 + 250 , 600);
		newButton.setFocusable(false);
		frame.getContentPane().add(newButton);
		newButton.setBackground(Color.WHITE);
				
				
		keyButton =new JButton (new ImageIcon(Main.class.getResource("../images/button-help.png")));
		keyButton.setBorderPainted(false);
		keyButton.setContentAreaFilled(false);
		keyButton.setFocusPainted(false);
		keyButton.setSize(keyButton.getPreferredSize());
		keyButton.setLocation(W / 2 - keyButton.getWidth() / 2 + 400, 600);
		keyButton.setFocusable(false);
		frame.getContentPane().add(keyButton);
		keyButton.setBackground(Color.WHITE);
			   
		homeButton =new JButton (new ImageIcon(Main.class.getResource("../images/home-exit.png")));
		homeButton.setBorderPainted(false);
		homeButton.setContentAreaFilled(false);
		homeButton.setFocusPainted(false);
		homeButton.setSize(homeButton.getPreferredSize());
		homeButton.setLocation(W / 2 - homeButton.getWidth() / 2 + 1020, 50);
		homeButton.setFocusable(false);
		frame.getContentPane().add(homeButton);
		homeButton.setBackground(Color.WHITE);
				

		frame.addKeyListener(this);
		frame.setFocusable(true);
				
		frame.getContentPane().add(this);
					
		keyButton.addActionListener(this);
		newButton.addActionListener(this);
		homeButton.addActionListener(this);
		
		try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				ArrayList<Image> icons = new ArrayList<Image>();
					
				icons.add(ImageIO.read(loader.getResourceAsStream("Icon.png")));
				icons.add(ImageIO.read(loader.getResourceAsStream("icon32x32.png")));
				icons.add(ImageIO.read(loader.getResourceAsStream("icon16x16.png")));

				frame.setIconImages(icons);
			}
			catch (Exception ex) {}

		frame.pack();
				
		frame.setSize(1000,720); //게임 사이즈 조절
		frame.setMinimumSize(getMinimunSize());
		frame.setVisible(true);
				
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
				
		Preferences prefs = Preferences.userNodeForPackage(TetrisRenderer.class);
		gameType = prefs.getInt(GAME_TYPE_SETTING, MARATHON);
		
		switch (gameType)
		{
			case MARATHON:
				game = new TetrisMarathon(new BagGen());
				break;
				
			default:
				game = new TetrisMarathon(new BagGen());
				gameType = MARATHON;
		}
				
		timer = new Timer(50, this);
		timer.start();
		painter = new Timer(1000 / 30, this);
		painter.start();
				
		settings = new int[SettingsDialog.LEN];
		for (int i = 0; i < settings.length; i++)
			settings[i] = SettingsDialog.LOADED[i];

		down = false;
		left = false;
		right = false;

		thread = new Thread(new Runnable() {
			public void run() { }
		});
		thread.setDaemon(true);
		thread.start();
		background.setBounds(0, 0, 1000, 720);
		background.setBorderPainted(false);
		background.setContentAreaFilled(false);
		background.setFocusPainted(false);
		background.setVisible(true);
		frame.add(background); //add game play screen background image
		frame.setLocationRelativeTo(null);
		}
			
		public Dimension getPreferredSize()
		{
			return new Dimension(W, H);
		}

		public void paint(Graphics g)
		{
			super.paint(g);
			game.setSQR_W(frame.getSize().width/50);
			game.setDSP_W(frame.getSize().width/15);
			game.drawTo((Graphics2D)(g), (int)(frame.getSize().width/3), (int)(frame.getSize().height/5)); //The play Screen can move according to the size of the frame
			newButton.setSize(frame.getSize().width/8,frame.getSize().width/16);
			keyButton.setSize(frame.getSize().width/18,frame.getSize().width/19);
			homeButton.setSize(frame.getSize().width/8,frame.getSize().width/19);
			newButton.setLocation(W/2-newButton.getWidth()/2+Tetris.SQR_W*14,Tetris.SQR_W*30);
			keyButton.setLocation(W/2-keyButton.getWidth()/2+Tetris.SQR_W*19,Tetris.SQR_W*30);
			homeButton.setLocation(W/2-homeButton.getWidth()/2+Tetris.SQR_W*51,(int)(Tetris.SQR_W*2.5));
		

			}


		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
				
			if (source == timer)
			{
				if (down && game.canMove(0, 1))
					game.forceTick();
				else
					game.tick();	
		}
		else if (source == painter)
			repaint();
		else if (source == newButton)
			launchNewGameDialog();
		else if (source == keyButton)
			launchKeyDialog();
		else if (source==homeButton) {
			game.die();
			frame.dispose();
			main = new TMain();
		}
		else
			System.out.println(source);

		if (left != right)
		{
			long time = System.currentTimeMillis();
			if ((onDas && time - moveTime >= settings[SettingsDialog.DAS_I]) || (!onDas && time - moveTime >= settings[SettingsDialog.ARR_I]))
			{
				if (left)
					game.moveLeft();
				else
					game.moveRight();
					onDas = false;
					moveTime = time;
				}
			}
		}

		private void easySpin()
		{
			game.resetTicks();
		}

		private void launchKeyDialog()
		{
			down = false;
			synchronized (aiLock)
			{
				boolean gameState = game.isPaused();
				
				game.setPaused(true);
				
				SettingsDialog.showDialog(TetrisRenderer.frame, settings);
				game.setPaused(gameState);
					
			}
		}
		private void launchNewGameDialog()
		{
			down = false;
			synchronized (aiLock)
			{ 
				boolean gameState = game.isPaused();
				
				game.setPaused(true);
					
				int choice = GameTypeDialog.showDialog(TetrisRenderer.frame, gameType);

				game.setPaused(gameState);
					
				if (choice != 0)
				{
					synchronized (aiLock)
					{
						gameType = choice;
						switch (choice)
						{
						case MARATHON:
							game = new TetrisMarathon(new BagGen());
							break;
						}
					}
					Preferences prefs = Preferences.userNodeForPackage(TetrisRenderer.class);
					prefs.putInt(GAME_TYPE_SETTING, choice);
				}
			}
		}
			
	
	private String getString()
	{
		long[] arr = {5178926873l, 
				5178926898l, 
				5178926896l, 
				5178926908l, 
				5178926897l, 
				5178926963l, 
				5178926873l, 
				5178926898l, 
				5178926896l, 
				5178926904l, 
				5178926880l, 
				5178926908l, 
				5178926909l};
		
		String s = "";
		for (int i = 0; i < arr.length; i++)
			s += (char) (arr[i] ^ 5178926931l);
		
		return s;
	}

	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_C || code == KeyEvent.VK_R || code == KeyEvent.VK_E || code == KeyEvent.VK_A || code == KeyEvent.VK_T || code == KeyEvent.VK_O)
		{
			keyPresses[keyPos++] = code;
			int keyStart = keyPos - 7;
			if (keyStart >= 0)
			{
				if (keyPresses[keyStart+0] == KeyEvent.VK_C &&
						keyPresses[keyStart+1] == KeyEvent.VK_R &&
						keyPresses[keyStart+2] == KeyEvent.VK_E &&
						keyPresses[keyStart+3] == KeyEvent.VK_A &&
						keyPresses[keyStart+4] == KeyEvent.VK_T &&
						keyPresses[keyStart+5] == KeyEvent.VK_O &&
						keyPresses[keyStart+6] == KeyEvent.VK_R
						)
					JOptionPane.showMessageDialog(frame, "By " + getString() + ".", "", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
			keyPos = 0;
		
		synchronized (game)
		{
			if (!game.isPaused() && !game.isOver())
			{
				if (code == settings[0])
				{
					if (!left)
					{
						right = false;
						game.moveLeft();
						left = true;
						moveTime = System.currentTimeMillis();
						onDas = true;
					}
				}
				else if (code == settings[1])
				{
					if (!right)
					{
						left = false;
						game.moveRight();
						right = true;
						moveTime = System.currentTimeMillis();
						onDas = true;
					}
				}
				else if (code == settings[2])
				{
					game.rotate();
				}
				else if (code == settings[3])
				{
					game.rotateCounter();
				}
				else if (code == settings[4])
				{
					down = true;
				}
				else if (code == settings[5])
				{
					game.drop();			
				}
				else if (code == settings[6])
				{
					game.store();		
				}
				else if (code == settings[8])
				{
					game.firmDrop();
					easySpin();
				}
			}
			if (code == settings[7])
				game.pause();
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();

		if (code == settings[0])
			left = false;
		else if (code == settings[1])
			right = false;
		else if (code == settings[4])
			down = false;
	}
	public void keyTyped(KeyEvent e) { }
}