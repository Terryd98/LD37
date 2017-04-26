package Terry.dev.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

	public static int mouseX = -1;
	public static int mouseY = -1;
	public static int mouseB = -1;

	public boolean[] keys = new boolean[5555];
	public boolean up, down, left, right, reload, space, shift, upArrow, downArrow, leftArrow, rightArrow, volUp, volDown, trap, esc, use, one, two;

	public void tick() {
		trap = keys[KeyEvent.VK_E];
		up = keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		reload = keys[KeyEvent.VK_R];
		space = keys[KeyEvent.VK_SPACE];
		shift = keys[KeyEvent.VK_SHIFT];
		upArrow = keys[KeyEvent.VK_UP];
		downArrow = keys[KeyEvent.VK_DOWN];
		leftArrow = keys[KeyEvent.VK_LEFT];
		rightArrow = keys[KeyEvent.VK_RIGHT];
		volUp = keys[KeyEvent.VK_PAGE_UP];
		volDown = keys[KeyEvent.VK_PAGE_DOWN];
		esc = keys[KeyEvent.VK_ESCAPE];
		use = keys[KeyEvent.VK_F];
		one = keys[KeyEvent.VK_1];
		two = keys[KeyEvent.VK_2];
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {

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
