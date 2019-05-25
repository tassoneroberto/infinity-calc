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

class Cambio extends Gui implements ActionListener, ChangeListener, KeyListener {
	private static final long serialVersionUID = 5074134301172963666L;
	private Font cambioFont = new Font("Arial", Font.BOLD, 13);
	public static final int BIN = 0, OCT = 1, DEC = 2, HEX = 3;
	// Costanti che indicano le basi: BIN=2, OCT=8, DEC=10, HEX=16
	CambioPane input, output;
	// Pannelli per la scelta della base di input e di output.

	// Booleane di controllo
	boolean cambioAvvenuto = false;
	// Vale true se il cambio di calcolatrice è andata a buon fine.
	boolean risultato = false;
	// Vale true se il numero nel display è un risultato e quindi alla pressione
	// di un tasto numerico, questo va azzerato.
	boolean nuovoNumero = true;
	// Vale true quando per la conversione si utilizza il numero nel display,
	// false se si utilizza il numero in memoria cioè quello utilizzato nella
	// conversione precedente.
	Numero mem = new Numero("0");

	// Numero salvato in memoria per le conversioni.

	// Costruttore della GUI CAMBIO BASE
	public Cambio(Gestore gestore) {
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
		modCambio.setSelected(true); // Spunta su CAMBIO BASE
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		// Tastiera
		keyPanel = new JPanel();
		keyPanel.setOpaque(false);
		keyPanel.setLayout(null);
		keyPanel.setBounds(DISTACCO_ORIZZ, 143, 400, 400);
		tasti = new JButton[18];
		for (int i = 0; i < 18; i++) {
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
		tasti[0].setText("0");
		tasti[1].setText("1");
		tasti[2].setText("2");
		tasti[3].setText("3");
		tasti[4].setText("4");
		tasti[5].setText("5");
		tasti[6].setText("6");
		tasti[7].setText("7");
		tasti[8].setText("8");
		tasti[9].setText("9");
		tasti[10].setText("A");
		tasti[11].setText("B");
		tasti[12].setText("C");
		tasti[13].setText("D");
		tasti[14].setText("E");
		tasti[15].setText("F");
		tasti[16].setText("Del");
		tasti[17].setText("CE");
		tasti[16].setBounds(0 + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[7].setBounds(widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[8].setBounds(2 * widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[9].setBounds(3 * widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[10].setBounds(4 * widthButton + x, 0 + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[11].setBounds(5 * widthButton + x, 0 + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[17].setBounds(0 + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[4].setBounds(widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[5].setBounds(2 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[6].setBounds(3 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[12].setBounds(4 * widthButton + x, heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[13].setBounds(5 * widthButton + x, heightButton + y, widthButton, heightButton);
		x = DISTACCO_ORIZZ;
		y += DISTACCO_VERT;
		tasti[0].setBounds(0 + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[1].setBounds(widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[2].setBounds(2 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[3].setBounds(3 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[14].setBounds(4 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);
		x += DISTACCO_ORIZZ;
		tasti[15].setBounds(5 * widthButton + x, 2 * heightButton + y, widthButton, heightButton);

		// Disabilitatori dei bottoni di default (base 10). Lascia attivi solo i
		// numeri da 0 a 9.
		for (int i = 2; i < 10; i++)
			tasti[i].setEnabled(true);
		for (int i = 10; i < 16; i++) {
			tasti[i].setEnabled(false);
			tasti[i].setDisabledIcon(bn);

		}
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
		displayPane.setBounds(DISTACCO_ORIZZ * 2, 20, 385, 60);
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		displayPane.setBorder(loweredbevel);
		displayPane.add(display);
		displayPane.add(scrollbar);
		// Bottone per la Conversione
		JButton ChangeKey = new JButton(bn);
		ChangeKey.setBounds(173, 90, widthButton, heightButton);
		ChangeKey.setFont(keyFont);
		ChangeKey.setPressedIcon(cp);
		ChangeKey.setRolloverIcon(cr);
		ChangeKey.setHorizontalTextPosition(AbstractButton.CENTER);
		ChangeKey.setVerticalTextPosition(AbstractButton.CENTER);
		ChangeKey.setBorder(null);
		ChangeKey.setForeground(Color.white);
		ChangeKey.setText(">");
		ChangeKey.addActionListener(this);
		// Pannelli di Input e Output
		input = new CambioPane("Input", true);
		input.setBounds(DISTACCO_ORIZZ * 2, 90, 150, 50);
		output = new CambioPane("Output", false);
		output.setBounds(244, 90, 150, 50);
		// Pannello Principale
		setLayout(null);
		this.getContentPane().setBackground(Color.black);
		add(displayPane);
		add(input);
		add(output);
		add(ChangeKey);
		add(keyPanel);
		// Finestra GUI FUNZIONALE
		setTitle("Convertitore di base");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		this.setLocation(350, 100);
		this.setSize(405, 363);
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
			if (aButton.getText() == "Funzionale") {
				Funz calc = new Funz(g);
				cambioAvvenuto = true;
				this.setVisible(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		display.requestFocusInWindow();
		String s = e.getActionCommand();
		if (s.equals("Esci")) {
			System.exit(0);
		}
		String regex = "[0-9[A-F]]";
		if (s.matches(regex)) {
			char x = s.charAt(0);
			if (!risultato)
				if (display.getText().equals("0"))
					display.setText("" + x);
				else {
					display.setText(display.getText() + x);
				}
			else {
				display.setText("" + x);
				risultato = false;
				nuovoNumero = true;
			}
		} else {

			if (s.equals(">")) {
				risultato = true;
				if (nuovoNumero) {
					mem = g.toDecimal(display.getText().toUpperCase(), input.getIndex());
					nuovoNumero = false;
				}
				display.setText(g.fromDecimal(mem, output.getIndex()));
			}
			if (s.equals("CE")) {
				nuovoNumero = true;
				display.setText("0");
				risultato = false;
			}

			if (s.equals("Del")) {
				if (!risultato) {
					String temp = display.getText();
					if (!temp.equals("0")) {
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

	@Override
	public void keyPressed(KeyEvent arg0) {
		int tasto = arg0.getKeyCode();
		char c = arg0.getKeyChar();
		int base = input.getIndex();
		if (tasto == KeyEvent.VK_ENTER) {
			risultato = true;
			if (nuovoNumero) {
				mem = g.toDecimal(display.getText().toUpperCase(), input.getIndex());
				nuovoNumero = false;

			}
			display.setText(g.fromDecimal(mem, output.getIndex()));
		}
		if (tasto == KeyEvent.VK_DELETE) {
			display.setText("0");
			risultato = false;
			nuovoNumero = true;
		}

		if (tasto == KeyEvent.VK_BACK_SPACE) {
			if (!risultato) {
				String temp = display.getText();
				if (!temp.equals("0")) {
					if (temp.length() != 1)
						display.setText(temp.substring(0, temp.length() - 1));
					else {
						display.setText("0");
					}

				}
			}
		}

		if (base == 0 && (c > '1' || c < '0')) {
			return;
		}
		if (base == 1 && (c > '7' || c < '0')) {
			return;
		}
		if (base == 2 && (c > '9' || c < '0')) {
			return;
		}

		if (base == 3 && (!(c >= '0' && c <= '9') && !(c >= 'a' && c <= 'f') && !(c >= 'A' && c <= 'F'))) {
			return;
		}
		if (!risultato)
			if (display.getText().equals("0")) {
				display.setText("" + c);

			} else {
				display.setText(display.getText() + c);
			}
		else {
			display.setText("" + c);
			risultato = false;
			nuovoNumero = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	// InnerClass per la gestione della base di Input e di Output
	private class CambioPane extends JPanel implements ActionListener {
		private static final long serialVersionUID = -7893541502054976332L;
		private JLabel testo;
		private JComboBox<String> menuCambio;

		public int getIndex() {
			return menuCambio.getSelectedIndex();
		}

		// Costruttore
		public CambioPane(String s, boolean ascoltatore) {
			super();
			testo = new JLabel(s);
			testo.setBounds(50, 4, 100, 15);
			testo.setForeground(Color.white);
			testo.setFont(cambioFont);
			menuCambio = new JComboBox<String>();
			menuCambio.setBounds(7, 22, 136, 20);
			menuCambio.addItem("Binario");
			menuCambio.addItem("Ottale");
			menuCambio.addItem("Decimale");
			menuCambio.addItem("Esadecimale");
			menuCambio.setSelectedIndex(2);
			add(testo);
			add(menuCambio);
			Border cornice = BorderFactory.createLoweredBevelBorder();
			this.setBorder(cornice);
			this.setLayout(null);
			this.setOpaque(false);
			if (ascoltatore)
				menuCambio.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			display.requestFocusInWindow();
			JComboBox<String> cambio = (JComboBox<String>) arg0.getSource();
			int input = cambio.getSelectedIndex();
			display.setText("0");
			mem = new Numero("0");
			risultato = false;
			nuovoNumero = true;
			if (input == 0) {
				for (int i = 2; i < 16; i++) {
					tasti[i].setEnabled(false);
					tasti[i].setDisabledIcon(bn);
				}
			}
			if (input == 1) {
				for (int i = 2; i < 8; i++)
					tasti[i].setEnabled(true);

				for (int i = 8; i < 16; i++) {
					tasti[i].setEnabled(false);
					tasti[i].setDisabledIcon(bn);
				}

			}
			if (input == 2) {
				for (int i = 2; i < 10; i++)
					tasti[i].setEnabled(true);

				for (int i = 10; i < 16; i++) {
					tasti[i].setEnabled(false);
					tasti[i].setDisabledIcon(bn);
				}

			}
			if (input == 3) {
				for (int i = 2; i < 16; i++)
					tasti[i].setEnabled(true);
			}
		}

	}
}