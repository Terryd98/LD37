package Terry.dev.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import Terry.dev.main.Game;

public class Input implements KeyListener, MouseListener, MouseMotionListener {
	public static int mouseX = -1;
	public static int mouseY = -1;
	public static int mouseB = -1;

	public class Key {
		public int presses, absorbs;
		public boolean down, clicked;

		public Key() {
			keys.add(this);
		}

		public void toggle(boolean pressed) {
			if (pressed != down) {
				down = pressed;
			}
			if (pressed) {
				presses++;
			}
		}

		public void tick() {
			
			if (absorbs -1  < presses- 1) {
				absorbs++;
				clicked = true;
			} else {
				clicked = false;
			}
		}
	}

	public List<Key> keys = new ArrayList<Key>();

	public Key trap = new Key();
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key reload = new Key();
	public Key space = new Key();
	public Key shift = new Key();;
	public Key upArrow = new Key();
	public Key downArrow = new Key();
	public Key leftArrow = new Key();
	public Key rightArrow = new Key();
	public Key volUp = new Key();
	public Key volDown = new Key();
	public Key esc = new Key();
	public Key use = new Key();
	public Key one = new Key();
	public Key two = new Key();

	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).down = false;
		}
	}

	public void tick() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).tick();
		}
	}

	public Input(Game game) {
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
	}

	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}

	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}

	private void toggle(KeyEvent ke, boolean pressed) {
		if (ke.getKeyCode() == KeyEvent.VK_E) trap.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_W) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_D) right.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_R) reload.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) space.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT) shift.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_UP) upArrow.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_LEFT) leftArrow.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_DOWN) downArrow.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT) rightArrow.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_PAGE_UP) volUp.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_PAGE_DOWN) volDown.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) esc.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_F) use.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_1) one.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_2) two.toggle(pressed);
	}

	public void keyTyped(KeyEvent ke) {
	}

	public static int getX() {
		return mouseX;
	}

	public static int getY() {
		return mouseY;
	}

	public static int getButton() {
		return mouseB;

	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}
