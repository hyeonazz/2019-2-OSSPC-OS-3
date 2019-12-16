package com.ok.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.ok.ai.ScoreFrame;
import com.ok.ai.SettingsDialog;
import com.ok.ai.TetrisRenderer;
//import com.ok.window.Tetris;

public class TMain extends JFrame {
	
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;

	//private Tetris uc;
	private TSetting setkey;
	private TetrisRenderer ai;

	
	// ���� ���۸��� ���� ȭ�鿡 �̹����� ��� ���� Instance���̴�.
	private Image screenImage;
	private Graphics screenGraphic;

	// ��׶��� �̹��� ��ü ����
	//private Image background = new ImageIcon(Main.class.getResource("../images/IntroBackground.png")).getImage();
	private JLabel MainScreen = new JLabel(new ImageIcon(Main.class.getResource("../images/IntroBackground.png")));
	
	
	// �޴��� ��ü ����
	private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("../images/menuBar.png")));

	// �޴� �� ���� exit button ���� ��ü ����
	private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("../images/exitButtonBasic.png"));
	private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/exitButtonEntered.png"));
	private JButton exitButton = new JButton(exitButtonBasicImage);

	// SINGLE button ���� ��ü ����
	private Image singleImage = new ImageIcon(Main.class.getResource("../images/Start.png")).getImage();
	private ImageIcon singleBasicImage = new ImageIcon(Main.class.getResource("../images/Start.png"));
	private ImageIcon singleEnteredImage = new ImageIcon(Main.class.getResource("../images/StartRed.png"));
	private JButton singleBtn = new JButton(singleBasicImage);
	
	//Ranking button
	private Image rankingImage = new ImageIcon(Main.class.getResource("../images/Ranking.png")).getImage();
	private ImageIcon rankingBasicImage = new ImageIcon(Main.class.getResource("../images/Ranking.png"));
	private ImageIcon rankingEnteredImage = new ImageIcon(Main.class.getResource("../images/RankingRed.png"));
	private JButton rankBtn = new JButton(rankingBasicImage);

	// Normal button ���� ��ü ����
	private Image normalImage = new ImageIcon(Main.class.getResource("../images/NormalBasic.png")).getImage();
	private ImageIcon normalBasicImage = new ImageIcon(Main.class.getResource("../images/NormalBasic.png"));
	private ImageIcon normalEnteredImage = new ImageIcon(Main.class.getResource("../images/NormalEntered.png"));
	private JButton normalBtn = new JButton(normalBasicImage);

	// Hard button ���� ��ü ����
	private Image hardImage = new ImageIcon(Main.class.getResource("../images/HardBasic.png")).getImage();
	private ImageIcon hardBasicImage = new ImageIcon(Main.class.getResource("../images/HardBasic.png"));
	private ImageIcon hardEnteredImage = new ImageIcon(Main.class.getResource("../images/HardEntered.png"));
	private JButton hardBtn = new JButton(hardBasicImage);

	// Back button ���� ��ü ����
	private Image backImage = new ImageIcon(Main.class.getResource("../images/BackBasic.png")).getImage();
	private ImageIcon backBasicImage = new ImageIcon(Main.class.getResource("../images/BackBasic.png"));
	private ImageIcon backEnteredImage = new ImageIcon(Main.class.getResource("../images/BackEntered.png"));
	private JButton backBtn = new JButton(backBasicImage);

	// Setting button ���� ��ü ����
	private Image settingImage = new ImageIcon(Main.class.getResource("../images/Setting.png")).getImage();
	private ImageIcon settingBasicImage = new ImageIcon(Main.class.getResource("../images/Setting.png"));
	private ImageIcon settingEnteredImage = new ImageIcon(Main.class.getResource("../images/SettingRed.png"));
	private JButton settingBtn = new JButton(settingBasicImage);

	// Exit button ���� ��ü ����
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("../images/Exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("../images/ExitRed.png"));
	private JButton exitBtn = new JButton(exitBasicImage);

	private boolean isStartScreen = true;
	
	private boolean isSingleModeScreen = false;
	private boolean isNormalModeScrren = false;
	private boolean isHardModeScreen = false;
	private boolean isSettingModeScreen = false;
	
	public int[] key_setting = new int[11];
	
	// ���콺 �̺�Ʈ�� Ȱ���ϱ� ���� ���콺 x, y ��ǥ
	private int mouseX, mouseY;

	public TMain(int[] key)
	{
		this();
		this.key_setting = key;
	}
	
	public TMain() {
		
		
		setUndecorated(true); // �⺻ �޴��ٰ� ������ ����. -> ���ο� menuBar�� �ֱ� ���� �۾�
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false); // ȭ�� ũ�� ���� �Ұ���
		setLocationRelativeTo(null); // ȭ�� ���߾ӿ� �߰� ��.
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 255));
	
		setLayout(null); // ȭ�鿡 ��ġ�Ǵ� ��ư�̳� label�� �� �ڸ� �״�� ���� ��.

		
		// Menu bar exit ��ư ���� ó��
		exitButton.setBounds(1820, 0, 30, 30);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitButtonEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitButtonBasicImage);
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				dispose();
			}
		});
		add(exitButton);

		// �޴��� �̺�Ʈ
		menuBar.setBounds(0, 0, 1920, 30);
		menuBar.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { // ���콺 Ŭ�� �� x,y ��ǥ�� ����.
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() { // �޴��ٸ� �巡�� �Ҷ� ȭ���� ������� �ϴ� �̺�Ʈ
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY); // JFrame�� ��ġ�� �ٲ���
			}
		});
		add(menuBar);

		// Single ��ư ���� ó��
		singleBtn.setBounds(755, 350, 410, 110);
		singleBtn.setBorderPainted(false);
		singleBtn.setContentAreaFilled(false);
		singleBtn.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		singleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleBtn.setIcon(singleEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				singleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleBtn.setIcon(singleBasicImage);
				singleBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �̱� ��� ���� �̺�Ʈó�� �κ�
				normalModeScreen();
			}
		});
		add(singleBtn);
		//(rankBtn)
		rankBtn.setBounds(755, 800, 410, 110);
		rankBtn.setBorderPainted(false);
		rankBtn.setContentAreaFilled(false);
		rankBtn.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		rankBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rankBtn.setIcon(rankingEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				rankBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rankBtn.setIcon(rankingBasicImage);
				rankBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �̱� ��� ���� �̺�Ʈó�� �κ�
				String str="";
				int score=0;
				ScoreFrame sf = new ScoreFrame(str,score);
			}
		});
		add(rankBtn);

		// Hard ��ư ���� ó��
		hardBtn.setBounds(130, 340, 400, 100);
		hardBtn.setBorderPainted(false);
		hardBtn.setContentAreaFilled(false);
		hardBtn.setFocusPainted(false);
		hardBtn.setVisible(false);
		// hard Button �̺�Ʈ ó��
		hardBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				hardBtn.setIcon(hardEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				hardBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hardBtn.setIcon(hardBasicImage);
				hardBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �ϵ� ��� ���� �̺�Ʈó�� �κ�
			}
		});
		add(hardBtn);

		// Back ��ư ���� ó��
		backBtn.setBounds(130, 470, 400, 100);
		backBtn.setBorderPainted(false);
		backBtn.setContentAreaFilled(false);
		backBtn.setFocusPainted(false);
		backBtn.setVisible(false);
		// exit Button �̺�Ʈ ó��
		backBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				backBtn.setIcon(backEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backBtn.setIcon(backBasicImage);
				backBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �ڷ� ���� �̺�Ʈ ó���κ�
			}
		});
		add(backBtn);

		// Setting ��ư ���� ó��
		settingBtn.setBounds(755, 500, 410, 110);
		settingBtn.setBorderPainted(false);
		settingBtn.setContentAreaFilled(false);
		settingBtn.setFocusPainted(false);
		// Setting Button �̺�Ʈ ó��
		settingBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				settingBtn.setIcon(settingEnteredImage); // ���콺�� Setting ��ư�� �ö󰡸� �̹����� �ٲ���.
				settingBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				settingBtn.setIcon(settingBasicImage);
				settingBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Settingâ ȭ������ �Ѿ��
				SettingsDialog.showDialog(TMain.this, key_setting);
				//setkey = new TSetting(TMain.this);
			}
		});
		add(settingBtn);

		// exit ��ư ���� ó��
		exitBtn.setBounds(750, 650, 410, 110);
		exitBtn.setBorderPainted(false);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		exitBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitBtn.setIcon(exitEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitBtn.setIcon(exitBasicImage);
				exitBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exitBtn);
		
		MainScreen.setBounds(0,0, 1920, 1080);
		MainScreen.setVisible(true);
		add(MainScreen);
		
		key_setting = SettingsDialog.DEFAULTS;
	}
	

	public void paint(Graphics g) {
		// 1280X720��ŭ�� �̹����� ��������� screenImage�� �־���.
		//screenImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		//screenGraphic = screenImage.getGraphics(); // screenImage�� �̿��� �׷����� ����.
		//screenDraw(screenGraphic); // ��ũ���� �׷����� �׷���.
		//screenGraphic.drawImage(background, 0, 0, null);
		//g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.revalidate();
		this.repaint();
		//Toolkit.getDefaultToolkit().sync();
		g.drawImage(screenImage, 0, 0, null);
		//g.drawImage(background, 0, 0, null);
		//Toolkit.getDefaultToolkit().sync();
	}

	// ��׶��� �̹����� �׷��ش�.
	public void screenDraw(Graphics g) {
		//g.drawImage(MainScreen, 0, 0, null); // drawImage�� �߰��� ���� �ƴ϶� �ܼ��� ȭ�鿡 �̹����� ����� �� ���� �Լ��̴�.
		if(isNormalModeScrren == true)
		{
			//g.clearRect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		}
		paintComponents(g); // JLabel, ��ư ���� Main Frame�� �߰��� �͵��� �׷� �ִ� ������ ��.
		this.revalidate();
		this.repaint(); // paint �Լ��� �ٽ� �ҷ����� �Լ�. �� �� ������������ ���� �׷��ִ� ������ ��.
	}
	
	public void normalModeScreen() {
		dispose();
		ai = null;
		ai = new TetrisRenderer();
		//uc = new Tetris(1, key_setting);
	}
}
