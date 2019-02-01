import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SplashScreen extends JPanel {
	
	private static final long serialVersionUID = 1L;

	SplashScreen() {
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(ZmiorkaGame.FRAME_WIDTH, ZmiorkaGame.FRAME_HEIGHT));
		setForeground(Color.BLACK);
		setLayout(null);
		setFocusable(true);
		
		JLabel splashScreenLabel = new JLabel(
				new ImageIcon(SplashScreen.class.getResource("/resources/splash_logo.png")));
		splashScreenLabel.setBounds(150, 150, 200, 200);
		add(splashScreenLabel);
		
		Thread thr = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					Thread.sleep(2500);
				} catch (InterruptedException e){
					e.printStackTrace();
				} finally {
					ZmiorkaFrame.jf.getContentPane().add(new MenuPanel());
					ZmiorkaFrame.jf.pack();
					ZmiorkaFrame.jf.remove(SplashScreen.this);
				}
			}
		});
		thr.start();
	}
}
