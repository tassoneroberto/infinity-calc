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
import javax.swing.JFrame;
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

class Standard extends Gui implements ActionListener, ChangeListener, KeyListener {
	private static final long serialVersionUID = 5074134301172963666L;

	// Booleane di controllo.
	boolean cambioAvvenuto = false;
	// Vale true se il cambio di calcolatrice è andata a buon fine.
	private boolean operatore = false;
	// Vale true appena è stato selezionato un operatore oppure è stato premuto
	// MS.
	private boolean primaOperazione = false;
	// Vale true quando un operatore è già stato selezionato.
	private boolean errore = false;

	// Vale true se è stata effettuata un'operazione errata, in particolare una
	// divisione per zero.

	// Costruttore della GUI STANDARD.
	public Standard(Gestore gestore) {
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
		modStand.setSelected(true); // Spunta su STANDARD
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		// Tastiera
		keyPanel = new JPanel();
		keyPanel.setOpaque(false);
		keyPanel.setLayout(null);
		keyPanel.setBounds(DISTACCO_ORIZZ, 80 + DISTACCO_VERT, 400, 400);
		tasti = new JButton[23];
		for (int i = 0; i < 23; i++) {
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
		tasti[0].setText("Del");
		tasti[0].setBounds(0 + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[1].setText("C");
		tasti[1].setBounds(widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[2].setText("CE");
		tasti[2].setBounds(2 * widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[3].setText("MR");
		tasti[3].setBounds(3 * widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[4].setText("MS");
		tasti[4].setBounds(4 * widthButton + x, 0 + y, widthButton, heightButton);
		if (!g.getMem().equals("0"))
			tasti[4].setForeground(Color.RED);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[5].setText("7");
		tasti[5].setBounds(0 + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[6].setText("8");
		tasti[6].setBounds(widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[7].setText("9");
		tasti[7].setBounds(2 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[8].setText("/");
		tasti[8].setBounds(3 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[9].setText("%");
		tasti[9].setBounds(4 * widthButton + x, heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[10].setText("4");
		tasti[10].setBounds(0 + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[11].setText("5");
		tasti[11].setBounds(widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[12].setText("6");
		tasti[12].setBounds(2 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[13].setText("x");
		tasti[13].setBounds(3 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[14].setText("±");
		tasti[14].setBounds(4 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[15].setText("1");
		tasti[15].setBounds(0 + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[16].setText("2");
		tasti[16].setBounds(widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[17].setText("3");
		tasti[17].setBounds(2 * widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[18].setText("+");
		tasti[18].setBounds(3 * widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[19].setText("=");
		tasti[19].setIcon(bv);
		tasti[19].setRolloverIcon(bvr);
		tasti[19].setPressedIcon(bvp);
		tasti[19].setBounds(4 * widthButton + x, 3 * heightButton + y, widthButton, 2 * heightButton + DISTACCO_VERT);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[20].setText("0");
		tasti[20].setIcon(bo);
		tasti[20].setRolloverIcon(bor);
		tasti[20].setPressedIcon(bop);
		tasti[20].setBounds(0 + x, 4 * heightButton + y, 2 * widthButton + DISTACCO_ORIZZ, heightButton);
		x += DISTACCO_ORIZZ + DISTACCO_ORIZZ;
		tasti[21].setText("Ans");
		tasti[21].setFont(new Font("Arial", Font.BOLD, 15));
		tasti[21].setBounds(2 * widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[22].setText("-");
		tasti[22].setBounds(3 * widthButton + x, 4 * heightButton + y, widthButton, heightButton);
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
		// Pannello Principale
		setLayout(null);
		this.getContentPane().setBackground(Color.black);
		add(displayPane);
		add(keyPanel);
		// Finestra GUI STANDARD
		this.setTitle("Calcolatrice Standard");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		this.setSize(340, 415);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int xCenter = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int yCenter = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(xCenter, yCenter);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	// Cambia tipologia di Calcolatrice: Standard, Espressione, Funzionale,
	// Cambio Base.
	@SuppressWarnings("unused")
	public void stateChanged(ChangeEvent changEvent) {
		AbstractButton aButton = (AbstractButton) changEvent.getSource();
		ButtonModel aModel = aButton.getModel();
		if (aModel.isSelected() && !cambioAvvenuto) {
			if (aButton.getText() == "Espressione") {
				Expr calc = new Expr(g);
				cambioAvvenuto = true;
				this.setVisible(false);
			}
			if (aButton.getText() == "Funzionale") {
				Funz calc = new Funz(g);
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
		try {
			int x = Integer.parseInt(s);
			if (!errore) {
				if (!operatore)
					if (display.getText().equals("0"))
						display.setText("" + x);
					else {
						display.setText(display.getText() + x);
					}
				else {
					display.setText("" + x);
					operatore = false;
				}
			}
		} catch (Exception com) {
			if (!errore) {
				if (s.equals("-") || s.equals("+") || s.equals("*") || s.equals("x") || s.equals("/")
						|| s.equals("%")) {
					if (operatore) {
						expr = new StringBuilder();
						primaOperazione = false;
					}
					if (primaOperazione) {
						expr.append(display.getText());
						try {
							display.setText(g.risolvi(expr.toString()));
						} catch (ArithmeticException exc) {
							display.setText("ERROR");
							errore = true;
						}
						expr = new StringBuilder(35);
					}
					expr.append(display.getText() + "$" + s + "$");
					primaOperazione = true;
					operatore = true;
				}
				if (s.equals("=")) {
					if (primaOperazione) {
						expr.append(display.getText());
						try {
							display.setText(g.risolvi(expr.toString()));
						} catch (ArithmeticException exc) {
							display.setText("ERROR");
							errore = true;
						}
						operatore = true;
						expr = new StringBuilder(35);
						primaOperazione = false;
						if (!errore)
							g.setAns(display.getText());
					}
				}
				if (s.equals("Ans")) {
					display.setText(g.getAns());
				}
				if (s.equals("±")) {
					if (!display.getText().equals("0")) {
						if (display.getText().charAt(0) != '-')
							display.setText('-' + display.getText());
						else {
							display.setText(display.getText().substring(1));
						}
					}
				}
				if (s.equals("MR")) {
					display.setText(g.getMem());
				}
				if (s.equals("MS")) {
					g.setMem(display.getText());
					if (display.getText().equals("0"))
						tasti[4].setForeground(Color.WHITE);
					else
						tasti[4].setForeground(Color.RED);
					operatore = true;
				}
				if (s.equals("Del")) {
					if (!operatore) {
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
			if (s.equals("C")) {
				expr = new StringBuilder(35);
				display.setText("0");
				operatore = false;
				errore = false;
			}
			if (s.equals("CE")) {
				display.setText("0");
				operatore = false;
				errore = false;
			}
		}
		display.requestFocusInWindow();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int tasto = arg0.getKeyCode();
		String s = "" + arg0.getKeyChar();
		try {
			int x = Integer.parseInt(s);
			if (!errore) {
				if (!operatore)
					if (display.getText().equals("0"))
						display.setText("" + x);
					else {
						display.setText(display.getText() + x);
					}
				else {
					display.setText("" + x);
					operatore = false;
				}
			}
		} catch (Exception e) {
			if (s.equals("-") || s.equals("+") || s.equals("*") || s.equals("x") || s.equals("/") || s.equals("%")) {
				if (operatore) {
					expr = new StringBuilder();
					primaOperazione = false;
				}
				if (primaOperazione) {
					expr.append(display.getText());
					try {
						display.setText(g.risolvi(expr.toString()));
					} catch (ArithmeticException exc) {
						display.setText("ERROR");
						errore = true;
					}
					expr = new StringBuilder(35);
				}
				expr.append(display.getText() + "$" + s + "$");
				primaOperazione = true;
				operatore = true;
			}
			if (s.equals("=") || tasto == KeyEvent.VK_ENTER) {
				if (primaOperazione) {
					expr.append(display.getText());
					try {
						display.setText(g.risolvi(expr.toString()));
					} catch (ArithmeticException exc) {
						display.setText("ERROR");
						errore = true;
					}
					operatore = true;
					expr = new StringBuilder(35);
					primaOperazione = false;
					if (!errore)
						g.setAns(display.getText());
				}
			}
			if (tasto == KeyEvent.VK_DELETE) {
				display.setText("0");
				operatore = false;
				errore = false;
			}
			if (tasto == KeyEvent.VK_ESCAPE) {
				expr = new StringBuilder(35);
				display.setText("0");
				operatore = false;
				errore = false;
			}

			if (tasto == KeyEvent.VK_BACK_SPACE) {
				if (!operatore) {
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