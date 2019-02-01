import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static Timer timer;
	private static JLabel startTextLabel;
	private JLabel zmiorkaTextLabel;

	public MenuPanel() {
		timer = new Timer(700, this);
		timer.start();//timer for blinking text
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(ZmiorkaGame.FRAME_HEIGHT, ZmiorkaGame.FRAME_WIDTH));
		setLayout(null);
		setVisible(true);
		setFocusable(true);
		addKeyListener(new keyboardListener());
		
		addStartText();
	}
	
	private void addStartText(){
		zmiorkaTextLabel = new JLabel(new ImageIcon(new ImageIcon(MenuPanel.class.getResource("resources/zmiorka_text_logo.png")).getImage()
				.getScaledInstance(400, 180, Image.SCALE_DEFAULT)));
		zmiorkaTextLabel.setBounds(10, 90, 480, 182);
		add(zmiorkaTextLabel);
		
		startTextLabel = new JLabel("Press enter to start game");
		startTextLabel.setForeground(Color.WHITE);
		startTextLabel.setVisible(true);
		startTextLabel.setFont(new Font("Pixeled", Font.PLAIN, 12));
		startTextLabel.setBounds(113, 350, 274, 27);
		add(startTextLabel);
	}

	private void blinkingStartText() {
		if (startTextLabel.isVisible() == true) {
			startTextLabel.setVisible(false);
		} else {
			startTextLabel.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		blinkingStartText();
	}

	public void startGame() {
		ZmiorkaFrame.jf.add(new ZmiorkaGame());
		ZmiorkaFrame.jf.pack();
		ZmiorkaFrame.jf.remove(MenuPanel.this);
	}

	class keyboardListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent event) {

			if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("CLICKED?");
				startGame();
				removeKeyListener(this);
			}
		}
	}
}
