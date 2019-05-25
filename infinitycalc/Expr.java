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

public class Expr extends Gui implements ActionListener, ChangeListener, KeyListener {
	private static final long serialVersionUID = 5074134301172963666L;
	private JPanel displayExprPane;
	private JTextField displayExpr;
	private Font displayExpr_font = new Font("res/font/LCD", Font.BOLD, 18);

	// Booleane e contatori di controllo.
	boolean cambioAvvenuto = false;
	// Vale true se il cambio di calcolatrice è andata a buon fine.
	private boolean operatore = false;
	// Vale true appena è stato selezionato un operatore oppure è stato premuto
	// MS.
	private boolean errore = false;
	// Vale true se è stata effettuata un'operazione errata, in particolare una
	// divisione per zero.
	private boolean caricato = false;
	// Vale true se l'albero è stato già caricato nel gestore.
	private int parentesi = 0;
	// Serve ad evitare che si inseriscano parentesi chiuse a cui non
	// corrisponda alcuna parentesi aperta.
	boolean parentesiChiusa = false;
	// Serve ad impedire l'immissione di numeri in seguito alla chiusura di una
	// parentesi.
	boolean parentesiAperta = false;
	// Serve ad indicare quando è stata appena aperta una parentesi
	boolean uguale = false;

	// Indica se è stato premuto uguale in modo da poter cancellare la riga
	// contenente l'espressione.

	// Costruttore della GUI ESPRESSIONE.
	public Expr(Gestore gestore) {
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
		modExpr.setSelected(true); // Spunta su ESPRESSIONE
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		// Tastiera
		keyPanel = new JPanel();
		keyPanel.setOpaque(false);
		keyPanel.setLayout(null);
		keyPanel.setBounds(DISTACCO_ORIZZ, 80 + DISTACCO_VERT + 50, 400, 400);
		tasti = new JButton[25];
		for (int i = 0; i < 25; i++) {
			tasti[i] = new JButton(bn);
			tasti[i].setFont(keyFont);
			tasti[i].setPressedIcon(bnp);
			tasti[i].setRolloverIcon(bnr);
			tasti[i].setHorizontalTextPosition(AbstractButton.CENTER);
			tasti[i].setVerticalTextPosition(AbstractButton.CENTER);
			tasti[i].setBorder(null);
			tasti[i].setForeground(Color.white);
			keyPanel.add(tasti[i]);
			tasti[i].addActionListener(this);
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
		tasti[19].setText("-");
		tasti[19].setBounds(4 * widthButton + x, 3 * heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[20].setText("0");
		tasti[20].setBounds(0 + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[21].setText("SE");
		tasti[21].setBounds(widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[22].setText("=");
		tasti[22].setBounds(2 * widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[23].setText("(");
		tasti[23].setBounds(3 * widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[24].setText(")");
		tasti[24].setBounds(4 * widthButton + x, 4 * heightButton + y, widthButton, heightButton);
		// Display Principale
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
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		display.setBorder(loweredbevel);
		display.addKeyListener(this);
		displayPane = new JPanel();
		JScrollBar scrollBarDisplayNumero = new JScrollBar(JScrollBar.HORIZONTAL);
		BoundedRangeModel brm = (display.getHorizontalVisibility());
		scrollBarDisplayNumero.setModel(brm);
		displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
		displayPane.setBounds(DISTACCO_ORIZZ + 5, DISTACCO_VERT * 4 + 50, 320, 60);
		displayPane.add(display);
		displayPane.add(scrollBarDisplayNumero);
		// Display Espressione
		displayExpr = new JTextField();
		displayExpr.setFont(displayExpr_font);
		displayExpr.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		displayExpr.setBackground(displayColor);
		displayExpr.setForeground(Color.black);
		displayExpr.setEditable(false);
		displayExpr.setBorder(loweredbevel);
		displayExprPane = new JPanel();
		JScrollBar scrollBarDisplayExpr = new JScrollBar(JScrollBar.HORIZONTAL);
		BoundedRangeModel brm2 = (displayExpr.getHorizontalVisibility());
		scrollBarDisplayExpr.setModel(brm2);
		displayExprPane.setLayout(new BoxLayout(displayExprPane, BoxLayout.Y_AXIS));
		displayExprPane.setBounds(DISTACCO_ORIZZ + 5, DISTACCO_VERT * 4, 320, 50);
		displayExprPane.add(displayExpr);
		displayExprPane.add(scrollBarDisplayExpr);
		// Pannello Principale
		setLayout(null);
		this.getContentPane().setBackground(Color.black);
		add(displayExprPane);
		add(displayPane);
		add(keyPanel);
		// Finestra GUI ESPRESSIONE
		setTitle("Calcolatrice di Espressioni");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		this.setLocation(350, 100);
		this.setSize(340, 465);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int xCenter = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int yCenter = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(xCenter, yCenter);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		display.requestFocusInWindow();

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
			if (!errore) {
				int x = Integer.parseInt(s);
				if (uguale) {
					displayExpr.setText("");
					uguale = false;
					caricato = false;
				}
				if (!operatore && !parentesiAperta)
					if (display.getText().equals("0"))
						display.setText("" + x);
					else {
						display.setText(display.getText() + x);
					}
				else {
					display.setText("" + x);
					operatore = false;
					parentesiAperta = false;
				}
			}
		} catch (Exception com) {
			if (!errore) {
				if (s.equals("±")) {
					if (!operatore && !display.getText().equals("0")) {
						if (display.getText().charAt(0) != '-')
							display.setText('-' + display.getText());
						else {
							display.setText(display.getText().substring(1));
						}
					}
				}
				if (s.equals("CE")) {
					display.setText("0");
					operatore = false;
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
				if (s.equals("(")) {
					if (uguale) {
						displayExpr.setText("");
						display.setText("0");
						uguale = false;
					}
					if (!parentesiChiusa) {
						displayExpr.setText(displayExpr.getText() + '(');
						parentesi++;
						expr.append("$(");
						parentesiAperta = true;
						operatore = false;
					}
				}
				if (s.equals(")")) {
					if (parentesi > 0) {
						if (!parentesiChiusa) {
							displayExpr.setText(displayExpr.getText() + display.getText() + ')');
							expr.append("$" + display.getText() + "$)");
						} else {
							displayExpr.setText(displayExpr.getText() + ')');
							expr.append("$)");
						}
						parentesi--;
						parentesiChiusa = true;
						operatore = false;
						display.setText("0");
					}
				}

				if (s.equals("MR")) {
					display.setText(g.getMem());
					operatore = false;
				}
				if (s.equals("MS")) {
					g.setMem(display.getText());
					if (display.getText().equals("0"))
						tasti[4].setForeground(Color.WHITE);
					else
						tasti[4].setForeground(Color.RED);
					operatore = true;
				}
				if (s.equals("=")) {
					if (!uguale && !parentesiChiusa) {
						displayExpr.setText(displayExpr.getText() + display.getText());
						for (int i = 0; i < parentesi; i++)
							displayExpr.setText(displayExpr.getText() + ")");
						expr.append("$" + display.getText());
					}
					try {
						if (!caricato && !uguale) {
							g.setAlbero(expr.toString());
							caricato = true;
						}
						display.setText(g.risolvi(true));
					} catch (Exception exc) {
						display.setText("ERROR");
						errore = true;
					}
					expr = new StringBuilder(35);
					operatore = true;
					uguale = true;
					caricato = false;
					parentesiChiusa = false;
				}
				if (s.equals("SE")) {
					if (!caricato && !uguale) {
						if (!parentesiChiusa) {
							displayExpr.setText(displayExpr.getText() + display.getText());
							expr.append("$" + display.getText());
						}
						g.setAlbero(expr.toString());
						expr = new StringBuilder(35);
						caricato = true;
					}
					try {
						String ris = g.risolvi(false);
						displayExpr.setText(ris);
						if (g.completamenteSemplificato()) {
							if (ris.charAt(0) == '(') {
								ris = ris.substring(1, ris.length() - 1);
								displayExpr.setText(ris);
							}
							display.setText(ris);
						}
					} catch (ArithmeticException exc) {
						display.setText("ERROR");
						errore = true;
					}
					operatore = true;
					uguale = true;
					parentesiChiusa = false;
				}
				if (s.equals("-") || s.equals("+") || s.equals("x") || s.equals("/") || s.equals("%")) {
					if (!operatore || uguale) {
						if (uguale) {
							displayExpr.setText("");
							uguale = false;
							caricato = false;
						}
						if (parentesiChiusa) {
							displayExpr.setText(displayExpr.getText() + s);
							expr.append("$" + s);
							parentesiChiusa = false;
						} else {
							String k = display.getText();
							if (k.charAt(0) == '-') {
								k = "(" + k + ")";
							}
							displayExpr.setText(displayExpr.getText() + k + s);
							expr.append("$" + display.getText() + "$" + s);
						}
					} else {
						displayExpr.setText(displayExpr.getText().substring(0, displayExpr.getText().length() - 1) + s);
						String temp = expr.substring(0, expr.length() - 1);
						expr = new StringBuilder(35);
						expr.append(temp + s);
					}
					operatore = true;
				}
			}
		}
		if (s.equals("C")) {
			expr = new StringBuilder(35);
			display.setText("0");
			displayExpr.setText("");
			operatore = false;
			errore = false;
			parentesiChiusa = false;
		}
		display.requestFocusInWindow();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int tasto = arg0.getKeyCode();
		String s = "" + arg0.getKeyChar();
		try {
			if (!errore) {
				int x = Integer.parseInt(s);
				if (uguale) {
					displayExpr.setText("");
					uguale = false;
					caricato = false;
				}
				if (!operatore && !parentesiAperta)
					if (display.getText().equals("0"))
						display.setText("" + x);
					else {
						display.setText(display.getText() + x);
					}
				else {
					display.setText("" + x);
					operatore = false;
					parentesiAperta = false;
				}
			}
		} catch (Exception com) {
			if (!errore) {

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
				if (s.equals("(")) {
					if (uguale) {
						displayExpr.setText("");
						display.setText("0");
						uguale = false;
					}
					if (!parentesiChiusa) {
						displayExpr.setText(displayExpr.getText() + '(');
						parentesi++;
						expr.append("$(");
						operatore = false;
					}
				}
				if (s.equals(")")) {
					if (parentesi > 0) {
						if (!parentesiChiusa) {
							displayExpr.setText(displayExpr.getText() + display.getText() + ')');
							expr.append("$" + display.getText() + "$)");
						} else {
							displayExpr.setText(displayExpr.getText() + ')');
							expr.append("$)");
						}
						parentesi--;
						parentesiChiusa = true;
						operatore = false;
						display.setText("0");
					}
				}

				if (tasto == KeyEvent.VK_ENTER || s.equals("=")) {
					if (!uguale && !parentesiChiusa) {
						displayExpr.setText(displayExpr.getText() + display.getText());
						expr.append("$" + display.getText());
					}
					try {
						if (!caricato && !uguale) {
							g.setAlbero(expr.toString());
							caricato = true;
						}
						display.setText(g.risolvi(true));
					} catch (Exception exc) {
						display.setText("ERROR");
						errore = true;
					}
					expr = new StringBuilder(35);
					operatore = true;
					uguale = true;
					caricato = false;
					parentesiChiusa = false;
				}

				if (s.equals("-") || s.equals("+") || s.equals("x") || s.equals("/") || s.equals("%")) {
					if (!operatore || uguale) {
						if (uguale) {
							displayExpr.setText("");
							uguale = false;
							caricato = false;
						}
						if (parentesiChiusa) {
							displayExpr.setText(displayExpr.getText() + s);
							expr.append("$" + s);
							parentesiChiusa = false;
						} else {
							String k = display.getText();
							if (k.charAt(0) == '-') {
								k = "(" + k + ")";
							}
							displayExpr.setText(displayExpr.getText() + k + s);
							expr.append("$" + display.getText() + "$" + s);
						}
					} else {
						displayExpr.setText(displayExpr.getText().substring(0, displayExpr.getText().length() - 1) + s);
						String temp = expr.substring(0, expr.length() - 1);
						expr = new StringBuilder(35);
						expr.append(temp + s);
					}
					operatore = true;
				}
			}
		}
		if (tasto == KeyEvent.VK_DELETE) {
			expr = new StringBuilder(35);
			display.setText("0");
			displayExpr.setText("");
			operatore = false;
			errore = false;
			parentesiChiusa = false;
		}
		display.requestFocusInWindow();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
}