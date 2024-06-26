package net.mp3skater.main;

import net.mp3skater.main.elements.Element;
import net.mp3skater.main.io.Board;
import net.mp3skater.main.io.KeyHandler;
import net.mp3skater.main.io.Mouse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	final int FPS = 60;
	Thread gameThread;
	Board board = new Board();
	Mouse mouse = new Mouse();

	// List of Elements
	ArrayList<Element> elements = new ArrayList<>();

	// Booleans for the pause-function
	private boolean exPause = false; // To see if Pause has been changed
	private boolean isPause = false;

	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.black);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);

		// Implement KeyListener:
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);

		// Initialize Game
		Utils.setPieces(elements, 50);
	}
	public void launchGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {

		// GAME LOOP
		double drawInterval = 1_000_000_000d/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while(gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime-lastTime)/drawInterval;
			lastTime = currentTime;

			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	private void changePauseState() {
		isPause = !isPause;
	}
	private void update() {

		// Pause, when pause is being pressed
		if(KeyHandler.pausePressed && !exPause)
			changePauseState();
		exPause = KeyHandler.pausePressed;

		// Don't continue if Game paused
		if(isPause)
			return;

		// Insert UPDATE-code here:

		// For example:
		for(Element e : elements) {
			// Move all elements one pixel left-down every frame if they're not 100px away from the border
			if(e.getX() < WIDTH-100)
				e.setX(e.getX()+1);
			if(e.getY() < HEIGHT-100)
				e.setY(e.getY()+1);
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		// Board
		board.draw(g2);

		// Elements
		for(Element e : elements) {
			e.draw(g2);
		}

		// Set a font (example)
		g2.setColor(Color.white);
		Font font = new Font ("Courier New", Font.BOLD, 10);
		g2.setFont(font);

		// PAUSE (needs to be arranged to the center if you change WIDTH or HEIGHT)
		if(isPause) {
			g2.setColor(Color.blue);
			g2.setFont(new Font ("Courier New", Font.BOLD, 50));
			g2.drawString("Game Paused", 230, 310);
		}
	}
}