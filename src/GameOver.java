import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

public class GameOver extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel gameOverLabel;
	private JLabel scoreLabel;
	private JLabel tryAgainLabel;
	private Timer blinker;

	public GameOver() {
		blinker = new Timer(650, this);
		blinker.start();

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(ZmiorkaGame.FRAME_WIDTH,ZmiorkaGame.FRAME_HEIGHT));
		setForeground(Color.BLACK);
		setLayout(null);
		setFocusable(true);

		addScreenText();
		listenKeyboard();
		ZmiorkaGame.writeScore();
	}

	private void addScreenText() {

		gameOverLabel = new JLabel("Game over");
		gameOverLabel.setForeground(Color.WHITE);
		gameOverLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		gameOverLabel.setFont(new Font("Pixeled", Font.BOLD, 36));
		gameOverLabel.setBackground(Color.BLACK);
		gameOverLabel.setBounds(0, 100, 500, 100);
		add(gameOverLabel);

		scoreLabel = new JLabel("Your score is " + ZmiorkaGame.score);
																		
		scoreLabel.setBounds(137, 255, 226, 43);
		scoreLabel.setForeground(new Color(179, 179, 179));
		scoreLabel.setFont(new Font("Pixeled", Font.BOLD, 14));

		add(scoreLabel);

		tryAgainLabel = new JLabel("Press enter to try again");
		tryAgainLabel.setBounds(164, 390, 172, 14);
		tryAgainLabel.setForeground(Color.WHITE);
		tryAgainLabel.setFont(new Font("Pixeled", Font.BOLD, 8));

		add(tryAgainLabel);
	}

	private void listenKeyboard() {
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ZmiorkaFrame.jf.getContentPane().add(new ZmiorkaGame());
					ZmiorkaFrame.jf.pack();
					ZmiorkaFrame.jf.remove(GameOver.this);
					removeKeyListener(this);
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		blinkerTryAgainText();
	}
	private void blinkerTryAgainText() {
		if (tryAgainLabel.isVisible()) {
			tryAgainLabel.setVisible(false);
		} else {
			tryAgainLabel.setVisible(true);
		}
	}
}
