import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ZmiorkaGame extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ImageIcon apple_imgIcon;
	private static ImageIcon gold_apple_imgIcon;
	private static ImageIcon snake_imgIcon;
	private static ImageIcon snake_head_imgIcon;

	private static Timer timer;
	private static JLabel scoreLabel;
	private static JLabel aimLabel;
	private static JLabel tipLabel;

	private static final int ONE_DOT = 20;
	private static final int DELAY = 120;// ms
	private static final int MAX_SCORE = 150;

	public static final int FRAME_HEIGHT = 500;
	public static final int FRAME_WIDTH = 500;

	private static final int GOLD_APPLE_MIN_TIME = 2000;// ms
	private static final int GOLD_APPLE_MAX_TIME = 3600; // ms

	private static int[] x_coordinates = new int[154];// max snake x length
	private static int[] y_coordinates = new int[154];// max snake y length

	private static int gold_apple_count = 0;
	private static int gold_apple_timer;

	private static int snake_length;
	public static int score;
	private static int goal;

	private static int appleX = 0;
	private static int appleY = 0;

	private static int gold_appleX = 0;
	private static int gold_appleY = 0;

	private static int direction; // 1up 2right 3down 4left

	private static File score_file = new File("src/resources/score.txt");

	private static boolean isGameOver = false;
	private static boolean isKeyPressed = true;
	private static boolean isGoldAppleVisible = false;
	private static boolean isGoldAppleThreadWork = false;

	public ZmiorkaGame() {
		launchSnakeGame();
	}

	private void launchSnakeGame() {
		addKeyListener(new keyChecker());
		setBackground(Color.BLACK);
		setLayout(new CardLayout());
		setFocusable(true);
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLayout(null);
		loadImg();
		addGame();
	}

	private void addGame() {

		setParams();
		addSnake();
		addApple();
		addTipText();

		aimLabel = new JLabel();
		if (goal > 0) {
			aimLabel.setText("Goal: " + goal);
		} else {
			aimLabel.setText("Goal: -");
		}
		timer = new Timer(DELAY, this);
		timer.start();

		addLabels();
	}

	private void setParams() {
		readGoal();
		isGameOver = false;
		direction = 2; // right start
		snake_length = 3;
		gold_apple_count = 0;
		score = 0;
	}

	private void addLabels() {

		aimLabel.setFont(new Font("Pixeled", Font.BOLD, 8));
		aimLabel.setBounds(10, 11, 70, 15);
		aimLabel.setForeground(Color.WHITE);
		aimLabel.setBackground(Color.WHITE);
		aimLabel.setVisible(true);

		scoreLabel = new JLabel("Score: " + score);
		scoreLabel.setFont(new Font("Pixeled", Font.BOLD, 8));
		scoreLabel.setBounds(430, 11, 70, 15);
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setBackground(Color.WHITE);
		scoreLabel.setVisible(true);

		add(scoreLabel);
		add(aimLabel);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		doDrawing(graphics);
	}

	private void doDrawing(Graphics graphics) {
		if (isGameOver == false) {
			if (isGoldAppleVisible) {
				graphics.drawImage(gold_apple_imgIcon.getImage(), gold_appleX, gold_appleY, ONE_DOT, ONE_DOT, this);
			}
			graphics.drawImage(apple_imgIcon.getImage(), appleX, appleY, ONE_DOT, ONE_DOT, this);

			for (int z = 0; z < snake_length; z++) {
				if (z == 0) {
					graphics.drawImage(snake_head_imgIcon.getImage(), x_coordinates[z], y_coordinates[z], ONE_DOT,
							ONE_DOT, this);
				} else if (z < snake_length) {
					graphics.drawImage(snake_imgIcon.getImage(), x_coordinates[z], y_coordinates[z], ONE_DOT, ONE_DOT,
							this);
				}
			}
		} else {
			gameEnd();
		}
	}

	private static void loadImg() {
		apple_imgIcon = new ImageIcon(ZmiorkaGame.class.getResource("/resources/apple.png"));
		gold_apple_imgIcon = new ImageIcon(ZmiorkaGame.class.getResource("/resources/apple_gold.png"));
		snake_imgIcon = new ImageIcon(ZmiorkaGame.class.getResource("/resources/snake.png"));
		snake_head_imgIcon = new ImageIcon(ZmiorkaGame.class.getResource("/resources/snake_head.png"));
	}

	private void addSnake() {
		direction = 2;
		for (int i = 0; i < snake_length; i++) {
			x_coordinates[i] = 100 - i * ONE_DOT; // startup coordinates
			y_coordinates[i] = 100;
		}
	}

	private static void addApple() {
		appleX = (int) (Math.random() * ((FRAME_WIDTH / ONE_DOT) - 1)) * ONE_DOT;
		appleY = (int) (Math.random() * ((FRAME_HEIGHT / ONE_DOT) - 1)) * ONE_DOT;
	}

	private static void addGoldApple() {
		if (!isGoldAppleThreadWork) {
			Random random = new Random();
			
			gold_apple_timer = random.nextInt(GOLD_APPLE_MAX_TIME - GOLD_APPLE_MIN_TIME) + GOLD_APPLE_MIN_TIME;
			
			gold_appleX = (int) (Math.random() * ((FRAME_WIDTH / ONE_DOT) - 1)) * ONE_DOT;
			gold_appleY = (int) (Math.random() * ((FRAME_HEIGHT / ONE_DOT) - 1)) * ONE_DOT;
			Thread gold_apple_time = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						isGoldAppleThreadWork = true;
						Thread.sleep(gold_apple_timer);
						System.out.println(gold_apple_timer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						isGoldAppleThreadWork = false;
						isGoldAppleVisible = false;
					}
				}
			});
			gold_apple_time.start();
		}
	}

	private static void checkFood() {
		if ((x_coordinates[0] == appleX) && (y_coordinates[0] == appleY)) {
			gold_apple_count++;
			if (gold_apple_count == 5) {
				isGoldAppleVisible = true;
				gold_apple_count = 0;
				addGoldApple();
			}
			addLength(1);
			remakeSnake();
			addApple();
			updateScore(1);
		} else if ((x_coordinates[0] == gold_appleX) && (y_coordinates[0] == gold_appleY) && isGoldAppleVisible) {
			if (score < MAX_SCORE - score) {
				addLength(3);
				updateScore(3);
			}
			isGoldAppleVisible = false;
			remakeSnake();
		}
	}

	private void moveSnake() {
		remakeSnake();

		for (int k = snake_length; k > 0; k--) {
			if ((k > 4) && x_coordinates[0] == x_coordinates[k] && y_coordinates[0] == y_coordinates[k]) {
				isGameOver = true;
			}
		}
		if (direction == 4) {
			isKeyPressed = false;
			if (x_coordinates[0] <= 0) {
				x_coordinates[0] = FRAME_WIDTH - ONE_DOT;
			} else {
				x_coordinates[0] -= ONE_DOT;
				addKeyListener(new keyChecker());
			}
		}
		if (direction == 2) {
			isKeyPressed = false;
			if (x_coordinates[0] >= FRAME_WIDTH - ONE_DOT) {
				x_coordinates[0] = 0;
			} else {
				x_coordinates[0] += ONE_DOT;
				addKeyListener(new keyChecker());
			}
		}
		if (direction == 1) {
			isKeyPressed = false;
			if (y_coordinates[0] <= 0) {
				y_coordinates[0] = FRAME_WIDTH - ONE_DOT;
			} else {
				y_coordinates[0] -= ONE_DOT;
				addKeyListener(new keyChecker());
			}
		}
		if (direction == 3) {
			isKeyPressed = false;
			if (y_coordinates[0] >= FRAME_WIDTH - ONE_DOT) {
				y_coordinates[0] = 0;
			} else {
				y_coordinates[0] += ONE_DOT;
				addKeyListener(new keyChecker());
			}
		}
	}

	private static void updateScore(int update) {
		score += update;
		scoreLabel.setText("Score: " + score);
	}

	private static void addLength(int length) {
		snake_length += length;
	}

	private static void remakeSnake() {
		for (int i = snake_length; i > 0; i--) {
			x_coordinates[i] = x_coordinates[(i - 1)];
			y_coordinates[i] = y_coordinates[(i - 1)];
			if (x_coordinates[i + 1] == appleX && y_coordinates[i + 1] == appleY) {
				addApple();
				System.out.println("otgore e normal");
			}
		}
	}

	private void gameEnd() {
		timer.stop();
		ZmiorkaFrame.jf.getContentPane().add(new GameOver());
		ZmiorkaFrame.jf.pack();
		ZmiorkaFrame.jf.remove(this);
	}

	private void addTipText() {
		tipLabel = new JLabel("Use SHIFT to increase your speed");
		tipLabel.setForeground(Color.WHITE);
		tipLabel.setFont(new Font("Pixeled", Font.PLAIN, 8));
		tipLabel.setBounds(270, 475, 230, 14);
		add(tipLabel);

		Thread tipThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					remove(tipLabel);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		tipThread.start();
	}

	private static void setSpeed(int speed) {
		timer.setDelay(speed);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		checkFood();
		moveSnake();
		repaint();
		checkIfGameCompleted();
	}

	public static void writeScore() {
		if (score > goal) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(score_file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			writer.println(score);
			writer.close();
		}
	}

	private void checkIfGameCompleted() {
		if (score == MAX_SCORE - 1) {
			timer.stop();
			
			ZmiorkaFrame.jf.getContentPane().add(new GameCompleted());
			ZmiorkaFrame.jf.pack();
			ZmiorkaFrame.jf.remove(this);
			removeKeyListener(new keyChecker());
		}
	}

	private static void readGoal() {
		Scanner reader = null;
		try {
			reader = new Scanner(score_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		goal = reader.nextInt();
		reader.close();
	}

	class keyChecker extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent event) {
			if (!isKeyPressed) {
				if (event.getKeyCode() == KeyEvent.VK_UP && (direction != 3)) {
					direction = 1;
					isKeyPressed = true;
				} else if (event.getKeyCode() == KeyEvent.VK_DOWN && (direction != 1)) {
					direction = 3;
					isKeyPressed = true;
				} else if (event.getKeyCode() == KeyEvent.VK_LEFT && (direction != 2)) {
					direction = 4;
					isKeyPressed = true;
				} else if (event.getKeyCode() == KeyEvent.VK_RIGHT && (direction != 4)) {
					direction = 2;
					isKeyPressed = true;
				} else if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
					setSpeed(60);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				setSpeed(DELAY);
			}
		}
	}
}
