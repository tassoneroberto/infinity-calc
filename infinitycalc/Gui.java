package infinitycalc;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class Gui extends JFrame {

	private static final long serialVersionUID = -1550330132516467992L;
	protected Gestore g;
	protected JPanel keyPanel;
	protected JButton[] tasti;
	protected Font keyFont = new Font("Arial", Font.BOLD, 17);
	protected JPanel displayPane;
	protected JTextField display;
	protected Font display_font = new Font("res/font/LCD", Font.BOLD, 30);
	protected int widthButton = 60;
	protected int heightButton = 50;
	protected final int DISTACCO_ORIZZ = 5;
	protected final int DISTACCO_VERT = 5;
	protected StringBuilder expr = new StringBuilder(35);

	// Immagini di sfondo dei bottoni: bn=bottone normale, bv=bottone verticale,
	// bo=bottone orizzontale.
	final ImageIcon bn = new ImageIcon(getClass().getClassLoader().getResource("res/images/bn.png"));
	final ImageIcon bv = new ImageIcon(getClass().getClassLoader().getResource("res/images/bv.png"));
	final ImageIcon bo = new ImageIcon(getClass().getClassLoader().getResource("res/images/bo.png"));
	final ImageIcon bnr = new ImageIcon(getClass().getClassLoader().getResource("res/images/bnr.png"));
	final ImageIcon bvr = new ImageIcon(getClass().getClassLoader().getResource("res/images/bvr.png"));
	final ImageIcon bor = new ImageIcon(getClass().getClassLoader().getResource("res/images/bor.png"));
	final ImageIcon bnp = new ImageIcon(getClass().getClassLoader().getResource("res/images/bnp.png"));
	final ImageIcon bvp = new ImageIcon(getClass().getClassLoader().getResource("res/images/bvp.png"));
	final ImageIcon bop = new ImageIcon(getClass().getClassLoader().getResource("res/images/bop.png"));
	final ImageIcon cr = new ImageIcon(getClass().getClassLoader().getResource("res/images/cr.png"));
	final ImageIcon cp = new ImageIcon(getClass().getClassLoader().getResource("res/images/cp.png"));

	public static void main(String argv[]) throws Exception {
		Gestore g = new Gestore();
		@SuppressWarnings("unused")
		Standard calc = new Standard(g);
	}
}
