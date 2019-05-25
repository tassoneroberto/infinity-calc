package infinitycalc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Funz extends Gui implements ActionListener, ChangeListener, KeyListener {
	private static final long serialVersionUID = 5074134301172963666L;
	private GRDPane grd;
	private Font angoloFont = new Font("Arial", Font.BOLD, 13);
	public static final int DEG = 0, GRAD = 1;
	// Tipo dell'angolo: DEG=Gradi, GRAD=Gradienti
	final public static int SIN = 1, COS = 2, TAN = 3, CTAN = 4, LOG = 5, SQRT = 6, EXP2 = 7, EXP3 = 8;
	// Tipo di funzione: sin, cos, ecc.

	// Booleane di controllo
	boolean cambioAvvenuto = false;
	// Vale true se il cambio di calcolatrice è andata a buon fine.
	private boolean risultato = false;
	// Vale true al termine di un operazione
	private boolean prosegui = true;
	// Vale false se il risultato nel display va azzerato, ad esempio dopo il
	// calcolo di sin, cos, etc.
	private boolean errore = false;
	// Vale true se è stata effettuata un'operazione errata, in particolare una
	// divisione per zero.

	// Costruttore della GUI FUNZIONALE.
	public Funz(Gestore gestore) {
		this.g = gestore;
		// Menù
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Modalità");
		fileMenu.setMnemonic(KeyEvent.VK_M);
		ButtonGroup bg = new ButtonGroup();
		JRadioButtonMenuItem modStand = new JRadioButtonMenuItem("Standard");
		JRadioButtonMenuItem modExpr = new JRadioButtonMenuItem("Espressione");
		JRadioButtonMenuItem modFunz = new JRadioButtonMenuItem("Funzionale");
		JRadioButtonMenuItem modCambio = new JRadioButtonMenuItem("Cambio Base");
		JSeparator separatore = new JSeparator();
		JMenuItem esci = new JMenuItem("Esci");
		fileMenu.add(modStand);
		fileMenu.add(modExpr);
		fileMenu.add(modFunz);
		fileMenu.add(modCambio);
		fileMenu.add(separatore);
		fileMenu.add(esci);
		modExpr.addChangeListener(this);
		modStand.addChangeListener(this);
		modFunz.addChangeListener(this);
		modCambio.addChangeListener(this);
		esci.addActionListener(this);
		bg.add(modStand);
		bg.add(modExpr);
		bg.add(modFunz);
		bg.add(modCambio);
		modFunz.setSelected(true); // Spunta su FUNZIONALE
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		// Tastiera
		keyPanel = new JPanel();
		keyPanel.setOpaque(false);
		keyPanel.setLayout(null);
		keyPanel.setBounds(DISTACCO_ORIZZ, 80 + DISTACCO_VERT, 400, 400);
		tasti = new JButton[21];
		for (int i = 0; i < 21; i++) {
			tasti[i] = new JButton(bn);
			tasti[i].setFont(keyFont);
			tasti[i].setPressedIcon(bnp);
			tasti[i].setRolloverIcon(bnr);
			tasti[i].setHorizontalTextPosition(AbstractButton.CENTER);
			tasti[i].setVerticalTextPosition(AbstractButton.CENTER);
			tasti[i].setBorder(null);
			tasti[i].setForeground(Color.white);
			tasti[i].addActionListener(this);
			keyPanel.add(tasti[i]);
		}
		int x = DISTACCO_ORIZZ;
		int y = DISTACCO_VERT;
		x = 4 * DISTACCO_ORIZZ;
		tasti[0].setText("C");
		tasti[0].setBounds(3 * widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[1].setText("Del");
		tasti[1].setBounds(4 * widthButton + x, 0 + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[2].setText("sin");
		tasti[2].setBounds(0 + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[3].setText("cos");
		tasti[3].setBounds(widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[4].setText("7");
		tasti[4].setBounds(2 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[5].setText("8");
		tasti[5].setBounds(3 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[6].setText("9");
		tasti[6].setBounds(4 * widthButton + x, heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[7].setText("tan");
		tasti[7].setBounds(0 + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[8].setText("ctan");
		tasti[8].setBounds(widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[9].setText("4");
		tasti[9].setBounds(2 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[10].setText("5");
		tasti[10].setBounds(3 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[11].setText("6");
		tasti[11].setBounds(4 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[12].setText("log₁₀");
		tasti[12].setBounds(0 + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[13].setText("√x");
		tasti[13].setBounds(widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[14].setText("1");
		tasti[14].setBounds(2 * widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[15].setText("2");
		tasti[15].setBounds(3 * widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[16].setText("3");
		tasti[16].setBounds(4 * widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[17].setText("x²");
		tasti[17].setBounds(0 + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[18].setText("x³");
		tasti[18].setBounds(widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[19].setText("0");
		tasti[19].setBounds(2 * widthButton + x, 4 * heightButton + y, 2 * widthButton + DISTACCO_ORIZZ, heightButton);
		tasti[19].setPressedIcon(bop);
		tasti[19].setRolloverIcon(bor);
		tasti[19].setIcon(bo);
		x += 2 * DISTACCO_ORIZZ;
		tasti[20].setText("±");
		tasti[20].setBounds(4 * widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		// Display
		display = new JTextField();
		display.setFont(display_font);
		display.setHorizontalAlignment(JTextField.RIGHT);
		display.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Color displayColor = new Color(204, 204, 204);
		display.setBackground(displayColor);
		display.setForeground(Color.black);
		display.setEditable(false);
		display.setAlignmentY(RIGHT_ALIGNMENT);
		display.setText("0");
		display.addKeyListener(this);
		display.requestFocusInWindow();
		BoundedRangeModel brm = display.getHorizontalVisibility();
		JScrollBar scrollbar = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollbar.setModel(brm);
		displayPane = new JPanel();
		displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
		displayPane.setBounds(DISTACCO_ORIZZ * 2, 20, 320, 60);
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		displayPane.setBorder(loweredbevel);
		displayPane.add(display);
		displayPane.add(scrollbar);
		// GRD
		grd = new GRDPane("Angolo");
		grd.setBounds(2 * DISTACCO_ORIZZ, 80 + 2 * DISTACCO_VERT, 3 * widthButton + 2 * DISTACCO_VERT, heightButton);
		// Pannello Principale
		setLayout(null);
		this.getContentPane().setBackground(Color.black);
		add(displayPane);
		add(grd);
		add(keyPanel);
		// Finestra GUI FUNZIONALE
		setTitle("Calcolatrice Funzionale");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		this.setLocation(350, 100);
		this.setSize(340, 415);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int xCenter = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int yCenter = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(xCenter, yCenter);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@SuppressWarnings("unused")
	public void stateChanged(ChangeEvent changEvent) {
		AbstractButton aButton = (AbstractButton) changEvent.getSource();
		ButtonModel aModel = aButton.getModel();
		if (aModel.isSelected() && !cambioAvvenuto) {
			if (aButton.getText() == "Standard") {
				Standard calc = new Standard(g);
				cambioAvvenuto = true;
				this.setVisible(false);
			}
			if (aButton.getText() == "Espressione") {
				Expr calc = new Expr(g);
				cambioAvvenuto = true;
				this.setVisible(false);
			}
			if (aButton.getText() == "Cambio Base") {
				Cambio calc = new Cambio(g);
				cambioAvvenuto = true;
				this.setVisible(false);
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("Esci")) {
			System.exit(0);
		}
		if (!errore) {
			try {
				int x = Integer.parseInt(s);
				prosegui = true;
				if (!risultato) {
					if (display.getText().equals("0"))
						display.setText("" + x);
					else {
						display.setText(display.getText() + x);
					}
				} else {
					display.setText("" + x);
					risultato = false;

				}

			} catch (Exception com) {
				if (prosegui) {
					if (s.equals("sin")) {
						String num = new String(display.getText());
						display.setText(g.risolviFunzione(num, SIN, grd.getTipo()));
						risultato = true;
						prosegui = false;
					}
					if (s.equals("cos")) {
						String num = new String(display.getText());
						display.setText(g.risolviFunzione(num, COS, grd.getTipo()));
						risultato = true;
						prosegui = false;
					}
					if (s.equals("tan")) {
						String num = new String(display.getText());
						display.setText(g.risolviFunzione(num, TAN, grd.getTipo()));
						risultato = true;
						prosegui = false;
					}
					if (s.equals("ctan")) {
						String num = new String(display.getText());
						display.setText(g.risolviFunzione(num, CTAN, grd.getTipo()));
						risultato = true;
						prosegui = false;
					}
					if (s.equals("log₁₀")) {
						String num = new String(display.getText());
						try {
							if (num.charAt(0) == '-')
								throw new ArithmeticException();
							display.setText(g.risolviFunzione(num, LOG));
							risultato = true;
							prosegui = true;
						} catch (ArithmeticException error) {
							errore = true;
							display.setText("Error");
						}
					}
					if (s.equals("√x")) {
						String num = new String(display.getText());
						try {
							display.setText(g.risolviFunzione(num, SQRT));

							risultato = true;
							prosegui = true;
						} catch (ArithmeticException error) {
							errore = true;
							display.setText("Error");
						}
					}
					if (s.equals("x²")) {
						String num = new String(display.getText());
						int funzione = 7;
						try {
							display.setText(g.risolviFunzione(num, funzione));
						} catch (OutOfMemoryError exception) {
							errore = true;
							display.setText("Error");
						}
						risultato = true;
						prosegui = true;
					}
					if (s.equals("x³")) {
						String num = new String(display.getText());
						int funzione = 8;
						try {
							display.setText(g.risolviFunzione(num, funzione));
						} catch (OutOfMemoryError exception) {
							errore = true;
							display.setText("Error");
						}
						risultato = true;
						prosegui = true;
					}

					if (s.equals("±")) {
						if (!risultato && !display.getText().equals("0")) {
							if (display.getText().charAt(0) != '-')
								display.setText('-' + display.getText());
							else {
								display.setText(display.getText().substring(1));
							}
						}
					}
				}
				if (s.equals("Del")) {
					if (!risultato) {
						String temp = display.getText();
						if (!temp.equals("0")) {
							if (temp.length() == 2 && temp.charAt(0) == ('-')) {
								display.setText("0");
							} else {
								if (temp.length() != 1)
									display.setText(temp.substring(0, temp.length() - 1));
								else {
									display.setText("0");
								}
							}
						}
					}
				}
			}
		}
		if (s.equals("C")) {
			expr = new StringBuilder(35);
			display.setText("0");
			risultato = false;
			errore = false;
			prosegui = true;
		}
		display.requestFocusInWindow();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int tasto = arg0.getKeyCode();
		String s = "" + arg0.getKeyChar();
		if (!errore) {
			try {
				int x = Integer.parseInt(s);
				prosegui = true;
				if (!risultato)
					if (display.getText().equals("0"))
						display.setText("" + x);
					else {
						display.setText(display.getText() + x);
					}
				else {
					display.setText("" + x);
					risultato = false;
				}
			} catch (Exception com) {
				if (!errore) {
					if (tasto == KeyEvent.VK_DELETE) {
						display.setText("0");
						prosegui = true;
						risultato = false;
						errore = false;
					}
				}
			}
			if (tasto == KeyEvent.VK_BACK_SPACE) {
				if (!risultato) {
					String temp = display.getText();
					if (!temp.equals("0")) {
						if (temp.length() == 2 && temp.charAt(0) == ('-')) {
							display.setText("0");
						} else {
							if (temp.length() != 1)
								display.setText(temp.substring(0, temp.length() - 1));
							else {
								display.setText("0");
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	// InnerClass per la gestione del tipo dell'angolo.
	private class GRDPane extends JPanel implements KeyListener {
		private static final long serialVersionUID = 218749784898274845L;
		private JLabel testo;
		private JComboBox<String> menuCambio;

		// Costruttore
		public GRDPane(String s) {
			this.setLayout(null);
			testo = new JLabel(s);
			testo.setForeground(Color.white);
			testo.setFont(angoloFont);
			testo.setBounds(75, 4, 100, 15);
			menuCambio = new JComboBox<String>();
			menuCambio.addItem("Gradi");
			menuCambio.addItem("Gradienti");
			menuCambio.addKeyListener(this);
			menuCambio.setSelectedIndex(0);
			menuCambio.setBounds(7, 22, 176, 20);
			add(testo);
			add(menuCambio);
			Border cornice = BorderFactory.createLoweredBevelBorder();
			this.setBorder(cornice);
			this.setOpaque(false);
		}

		public int getTipo() {
			return menuCambio.getSelectedIndex();
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			int tasto = arg0.getKeyCode();
			String s = "" + arg0.getKeyChar();
			if (!errore) {
				try {
					int x = Integer.parseInt(s);
					prosegui = true;
					if (!risultato)
						if (display.getText().equals("0"))
							display.setText("" + x);
						else {
							display.setText(display.getText() + x);
						}
					else {
						display.setText("" + x);
						risultato = false;
					}
				} catch (Exception com) {
					if (!errore) {
						if (tasto == KeyEvent.VK_DELETE) {
							display.setText("0");
							prosegui = true;
							risultato = false;
							errore = false;
						}
					}
				}
				if (tasto == KeyEvent.VK_BACK_SPACE) {
					if (!risultato) {
						String temp = display.getText();
						if (!temp.equals("0")) {
							if (temp.length() == 2 && temp.charAt(0) == ('-')) {
								display.setText("0");
							} else {
								if (temp.length() != 1)
									display.setText(temp.substring(0, temp.length() - 1));
								else {
									display.setText("0");
								}
							}
						}
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

	}
}