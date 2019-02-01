import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ZmiorkaFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public static ZmiorkaFrame jf;
	
	ZmiorkaFrame(){
		makeGame();
	}
	private void makeGame(){
		add(new SplashScreen());
		setTitle("Zmiorka");
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(ZmiorkaFrame.class.getResource("/resources/zmiorka_icon.jpg")).getImage());
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				jf = new ZmiorkaFrame();
				jf.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosed(e);
						ZmiorkaGame.writeScore();
					}
				});
				jf.setVisible(true);
			}
		});
	}	
}
