package ProjectSnake;

import java.applet.*;
import java.awt.*;

public class Project extends Applet implements Runnable {

	Thread t;
	int direction;
	final int RIGHT = 6;
	final int LEFT = 4;
	final int UP = 8;
	final int DOWN = 2;

	final int PAUSE = 5;
	final int CRASH = 3;
	final int PLAY = 1;
	final int START = 0;

	int x[] = new int[800];
	int y[] = new int[800];
	boolean flag = false;
	int length;
	int level = 500;
	int width;
	int height;
	int gameState = START;
	int targetX, targetY;
	int points = 0;
	int highest = 0;

	Image offScreen;
	Graphics g;

	public void init() {
		requestFocus();
		for (int i = 0; i < 800; i++) {
			y[i] = -100;
			x[i] = -100;
		}

		setSize(800, 800);
		width = size().width;
		height = size().height;
		offScreen = createImage(width, height);
		g = offScreen.getGraphics();

		for (int i = 0; i < length; i++) {
			int r = 100 - (10 * i);
			x[i] = r;
			y[i] = 100;
		}

		randomizetarget();
	}

	public void start() {
		t = new Thread(this);
		t.start();
	}

	public void checkCrashes() {

		if (x[0] < 0 || x[0] > width - 10 || y[0] < 0 || y[0] > height - 10) {
			gameState = CRASH;
			repaint();
		}

		for (int i = 1; i < length; i++) {
			if (x[0] + 10 > x[i] && x[0] < x[i] + 10 && y[0] + 10 > y[i]
					&& y[0] < y[i] + 10) {
				gameState = CRASH;
				repaint();
			}
		}

	}

	void randomizetarget() {
		targetX = (int) (Math.random() * (width - 10));
		targetY = (int) (Math.random() * (height - 10));
		targetX = (int) (10 * (Math.floor(targetX / 10)));
		targetY = (int) (10 * (Math.floor(targetY / 10)));
	}

	public void run() {
		while (true) {
			if (gameState == PLAY) {
				int yChange = 0;
				int xChange = 0;

				switch (direction) {
				case LEFT: {
					xChange = -10;
					break;
				}
				case RIGHT: {
					xChange = 10;
					break;
				}
				case DOWN: {
					yChange = 10;
					break;
				}
				case UP: {
					yChange = -10;
					break;
				}
				}

				for (int i = length - 1; i > 0; i--) {
					x[i] = x[i - 1];
					y[i] = y[i - 1];
				}

				x[0] += xChange;
				y[0] += yChange;

				checkCrashes();
				checktarget();
			}

			repaint();
			try {
				Thread.sleep(level);
			} catch (InterruptedException ioe) {
			}

		}

	}

	public boolean keyDown(Event e, int key) {
		switch (key) {
		case Event.LEFT: {
			if (y[0] != y[1])
				direction = LEFT;
			break;
		}

		case Event.RIGHT: {
			if (y[0] != y[1])
				direction = RIGHT;
			break;
		}
		case Event.UP: {
			if (x[0] != x[1])
				direction = UP;
			break;
		}
		case Event.DOWN: {
			if (x[0] != x[1])
				direction = DOWN;
			break;
		}

		case Event.F1: {
			level = 100;
			newGame();
			break;
		}
		case Event.F2: {
			level = 50;
			newGame();
			break;
		}
		case Event.F3: {
			level = 25;
			newGame();
			break;
		}
		case Event.ENTER: {
			if (flag == true && gameState == START)
			{
				flag = false;
				break;
			}
			else if (gameState == PAUSE) {
				gameState = PLAY;
				break;
			}
			if (gameState == CRASH && highest >= 500)
			{
				gameState = START;
				break;
			}
			gameState = PAUSE;
			break;
		}
		case Event.ESCAPE: {
			if (flag == true && gameState == START)
				t.stop();
		}
		}
		return true;
	}

	void checktarget() {
		if (x[0] + 10 > targetX && x[0] < targetX + 10 && y[0] + 10 > targetY
				&& y[0] < targetY + 10) {
			if (level > 50)
				points += 10;
			else if (level > 25)
				points += 25;
			else if (level <= 25)
				points += 50;
			if (length < 99)
				++length;
			randomizetarget();

			if (level >= 50)
				level -= 5;
			else if (level >= 25)
				level -= 2;
			else if (level > 0 && level < 25)
				level -= 1;
		}

	}

	void newGame() {
		for (int a = 0; a < 800; a++) {
			y[a] = -100;
			x[a] = -100;
		}
		length = 10;
		points = 0;
		for (int i = 0; i < length; i++) {
			int r = 100 - (10 * i);
			x[i] = r;
			y[i] = 100;
		}

		direction = RIGHT;
		randomizetarget();
		gameState = PLAY;
	}

	public void update(Graphics gra) {
		paint(gra);
	}

	public void paint(Graphics g2)

	{
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.setColor(new Color(36, 63, 137));
		g.setFont(new Font("Serif", 1, 36));

		if (gameState == PLAY) {
			g.setColor(new Color(36, 63, 137));

			g.drawString("SCORE: " + points, 10, 30);

			g.setColor(new Color(244, 29, 16));
			g.drawRect(targetX, targetY, 10, 10);
			g.setColor(new Color(249, 122, 108));
			g.fillRect(targetX + 1, targetY + 1, 9, 9);

			for (int i = 0; i < length; i++) {
				g.setColor(new Color(36, 63, 137));
				g.drawRect(x[i], y[i], 10, 10);
				g.setColor(new Color(186, 212, 239));
				g.fillRect((x[i]) + 1, (y[i]) + 1, 9, 9);
			}

		}

		if (gameState == START) {
			if (flag == true)
			{
				g.drawString("Do you want to revenge to your father of sins?", 100, 200);
				g.drawString("Enter/Escape", 100, 240);
			}
			else
			{
				g.setColor(new Color(36, 63, 137));
				g.drawString("S N A K E", 200, 150);
				g.drawString("I've watched you in a coma for years.", 100, 200);
				g.drawString("It's time for you to stand your ground.", 100, 240);
				g.drawString("Me? To know who I am, just beat the red ...", 100, 280);
				g.drawString("- F1: EASY", 200, 360);
				g.drawString("- F2: NORMAL", 200, 390);
				g.drawString("- F3: HARD", 200, 420);
				g.drawString("Use arrow keys to move, enter to pause.", 30, 575);
			}
		}

		if (gameState == CRASH) {
			if (points > highest)
				highest = points;
			if (highest >= 500)
			{
				g.drawString("My sins never die.", 100, 200);
				g.drawString("Every time you fail, I feel the pain.", 100, 240);
				g.drawString("Sins brought me here, and punished me.", 100, 280);
				g.drawString("It's my kind of hell which suffers me.", 100, 320);
				g.drawString("I'm your father, the wall that protects you -", 100, 360);
				g.drawString("- bumps you and makes you in a coma...", 100, 400);
				g.drawString("Now, rest in peace. My son.", 100, 480);
				flag = true;
			}
			else
			{
				g.setColor(new Color(36, 63, 137));
				g.drawString("Y O U  A R E  I N  A  C O M A .", 200, 150);
				g.drawString("- F1: EASY", 200, 360);
				g.drawString("- F2: NORMAL", 200, 390);
				g.drawString("- F3: HARD", 200, 420);
				g.drawString("Use arrow keys to move, enter to pause.", 30, 575);
				g.drawString("SCORE: " + points, 10, 30);
				g.drawString("HIGH SCORE: " + highest, 10, 60);
			}
			if (highest < 100)
			{
				g.drawString("You know what?", 100, 200);
				g.drawString("Seeing you fail like that is unbearable.", 100, 240);
			}
			else if (highest >= 100 && highest < 250) {
				g.drawString("You step up quite fast.", 100, 200);
				g.drawString("If you can keep it up, ", 100, 240);
				g.drawString("I won't be in pain anymore...", 100, 280);
			}
			else if (highest >= 250 && highest < 500) {
				g.drawString("It's a better feeling now.", 100, 200);
				g.drawString("But still, I can feel the pain,", 100, 240);
				g.drawString("even my body have already lost...", 100, 280);
			}
		}
		if (gameState == PAUSE) {
			g.setColor(new Color(36, 63, 137));
			g.drawString("SCORE: " + points, 10, 30);
			g.setColor(new Color(244, 29, 16));
			g.drawRect(targetX, targetY, 10, 10);
			g.setColor(new Color(249, 122, 108));
			g.fillRect(targetX + 1, targetY + 1, 9, 9);

			for (int i = 0; i < length; i++) {
				g.setColor(new Color(36, 63, 137));
				g.drawRect(x[i], y[i], 10, 10);
				g.setColor(new Color(186, 212, 239));
				g.fillRect((x[i]) + 1, (y[i]) + 1, 9, 9);
			}

			g.drawString("Just rest...", 150, 250);
			g.drawString("Continue your trial when you're ready.", 150, 290);
		}
		g.setColor(new Color(36, 63, 137));
		g.drawRect(0, 0, width - 1, height - 1);
		g2.drawImage(offScreen, 0, 0, width, height, this);
	}

}
