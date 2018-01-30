package component;

import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Window {
	public static final int WINDOW_WIDTH = 684;
	public static final int WINDOW_HEIGHT = WINDOW_WIDTH;

	private JFrame f;

	/**
	 * creates window
	 * 
	 * @param panel
	 *            panel to be contained within the window
	 */
	public Window(JPanel panel) {

		f = new JFrame();
		f.setTitle("Gomoku");
		f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 22);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		f.add(panel);
		f.setVisible(true);

	}

	/**
	 * adds a keylistener to the window
	 */
	public void addKeyListener(KeyListener keyListener) {
		f.addKeyListener(keyListener);
	}
}
