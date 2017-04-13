package Terry.dev.main.input;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class KeyBindings extends JFrame{

private static final int IFW=JComponent.WHEN_IN_FOCUSED_WINDOW;

	private static final long serialVersionUID = 1L;

	private JLabel comp = new JLabel();

	public KeyBindings() {
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke("W"), "move up");
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke("A"), "move left");
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke("D"), "move right");
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke("s"), "move down");
		
		add(comp);
	}
	
	
	
	public void main(String[] args) {
		
		new KeyBindings();
	}
}