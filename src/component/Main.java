package component;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.GameState;

public class Main extends JPanel {

	private long oldTime;
	private Window window;

	/**
	 * main object maintains window, links user input and window/gamestate together
	 */
	public Main() {
		window = new Window(this);

		window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			/**
			 * passes keypress to the gamestate
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				GameState.getInstance().keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}

	/**
	 * updates the gamestate via. repainting
	 */
	public void update() {
		try {
			Thread.sleep(50);
			repaint();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * render method, simply redirects rendering to gamestate
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		GameState.getInstance().render(g);
	}

	/**
	 * main method just contains game loop
	 * 
	 * @param args
	 *            (not used)
	 */
	public static void main(String[] args) {
		Main main = new Main();

		while (true) {
			main.update();
		}
	}
}
