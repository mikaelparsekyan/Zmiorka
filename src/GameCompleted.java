import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameCompleted extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public GameCompleted() {
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(ZmiorkaGame.FRAME_HEIGHT, ZmiorkaGame.FRAME_WIDTH));
		setLayout(null);
		setFocusable(true);
		
		addFinishText();
		
	}
	private void addFinishText(){
		JLabel finsishGameLabel = new JLabel("You finsih the game successfully!");
		finsishGameLabel.setForeground(Color.WHITE);
		finsishGameLabel.setFont(new Font("Pixeled", Font.BOLD, 14));
		finsishGameLabel.setBounds(35, 130, 429, 52);
		add(finsishGameLabel);
		
		JLabel trophyLabel = new JLabel(new ImageIcon(new ImageIcon(GameCompleted.class.getResource("/resources/trophy.png")).getImage()
				.getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
		trophyLabel.setBounds(200, 250, 100, 100);
		
		add(trophyLabel);
	}
}
