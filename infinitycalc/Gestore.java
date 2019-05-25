package infinitycalc;

import java.util.StringTokenizer;

public class Gestore {
	String mem = "0", ans = "0";
	// Serve per tenere salvato in memoria un numero.
	StringTokenizer st;
	// Viene utilizzato per salvare l'espressione prima dell'operazione.
	private AlberoDiEspressioni a = new AlberoDiEspressioni();

	// Costruisce un albero per le operazioni algebriche a partire
	// dall'espressione "st".
	public void setMem(String num) {
		mem = num.toString();
	}

	public String getMem() {
		return mem.toString();
	}

	public void setAns(String num) {
		ans = num.toString();
	}

	public String getAns() {
		return ans.toString();
	}

	public void setAlbero(String expr) {
		a.build(expr);
	}

	public String risolvi(String expr) {
		return risolviOperazione(expr);
	}

	public String risolvi(boolean risolvi) throws ArithmeticException {
		if (risolvi) {
			return a.valuta().toString();
		} else {
			try {
				a.semplifica();
			} catch (IllegalStateException e) {
			}

			return a.toString();
		}
	}

	private String risolviOperazione(String op) throws ArithmeticException {
		st = new StringTokenizer(op, "$");
		Numero num1 = new Numero(st.nextToken());
		char operatore = st.nextToken().charAt(0);
		Numero num2 = new Numero(st.nextToken());
		switch (operatore) {
		case '+': {
			return num1.somma(num2).toString();
		}
		case '-': {
			return num1.sottrai(num2).toString();
		}
		case 'x': {
			return num1.moltiplica(num2).toString();
		}
		case '*': {
			return num1.moltiplica(num2).toString();
		}
		case '/': {
			return num1.dividi(num2).toString();
		}
		default: {
			return num1.resto(num2).toString();
		}
		}
	}

	public boolean completamenteSemplificato() {
		return a.risultato();
	}

	public String risolviFunzione(String num, int f) throws ArithmeticException {
		Numero n = new Numero(num);
		String ris = new String();
		switch (f) {
		case Funz.LOG: {
			ris = n.Log().toString();
			break;
		}
		case Funz.SQRT: {
			ris = n.sqrt().toString();
			break;
		}
		case Funz.EXP2: {
			ris = n.moltiplica(n).toString();
			break;

		}
		case Funz.EXP3: {
			ris = n.moltiplica(n).moltiplica(n).toString();
			break;

		}
		}
		return ris;
	}

	public String risolviFunzione(String num, int funz, int tipoAngolo) {
		Numero n = new Numero(num);
		Numero fattoreDivisione;
		if (tipoAngolo == Funz.GRAD) {
			fattoreDivisione = new Numero("400");
		} else {
			fattoreDivisione = new Numero("360");
		}
		Numero ris = n.resto(fattoreDivisione);
		int angolo = Integer.parseInt(ris.toString());
		if (angolo < 0) {
			if (tipoAngolo == Funz.DEG) {
				angolo = 360 + angolo;
			} else
				angolo = 400 + angolo;
		}
		if ((tipoAngolo == Funz.DEG && angolo == 90) || (tipoAngolo == Funz.GRAD && angolo == 100))
			switch (funz) {
			case Funz.SIN:
				return "1.0";
			case Funz.COS:
				return "0.0";
			case Funz.TAN:
				return "INFINITY";
			case Funz.CTAN:
				return "0.0";
			}
		if ((tipoAngolo == Funz.DEG && angolo == 180) || (tipoAngolo == Funz.GRAD && angolo == 200))
			switch (funz) {
			case Funz.SIN:
				return "0.0";
			case Funz.COS:
				return "-1.0";
			case Funz.TAN:
				return "0";
			case Funz.CTAN:
				return "-INFINITY";
			}
		if ((tipoAngolo == Funz.DEG && angolo == 270) || (tipoAngolo == Funz.GRAD && angolo == 300))
			switch (funz) {
			case Funz.SIN:
				return "-1.0";
			case Funz.COS:
				return "0.0";
			case Funz.TAN:
				return "-INFINITY";
			case Funz.CTAN:
				return "0.0";
			}
		double angoloInRadianti;
		if (tipoAngolo == Funz.GRAD)
			angoloInRadianti = Math.PI * angolo / 200;
		else
			angoloInRadianti = Math.PI * angolo / 180;
		switch (funz) {
		case Funz.SIN:
			return "" + Math.sin(angoloInRadianti);
		case Funz.COS:
			return "" + Math.cos(angoloInRadianti);
		case Funz.TAN: {
			double sin = Math.sin(angoloInRadianti);
			double cos = Math.cos(angoloInRadianti);
			double risultato = sin / cos;
			return "" + risultato;
		}
		default/* CTAN */: {
			double sin = Math.sin(angoloInRadianti);
			double cos = Math.cos(angoloInRadianti);
			double risultato = cos / sin;
			return "" + risultato;
		}
		}
	}

	public String fromDecimal(Numero num, int base) {
		Numero n = new Numero(num);
		switch (base) {
		case Cambio.BIN:
			return daDacimale(num, 2);
		case Cambio.OCT:
			return daDacimale(num, 8);
		case Cambio.HEX:
			return daDacimale(num, 16);
		default:
			return n.toString();
		}
	}

	public Numero toDecimal(String n, int base) {
		switch (base) {
		case Cambio.BIN:
			return aDecimale(n, 2);
		case Cambio.OCT:
			return aDecimale(n, 8);
		case Cambio.HEX:
			return aDecimale(n, 16);
		default:
			return new Numero(n);
		}
	}

	private Numero aDecimale(String n, int base) {
		Numero fattore = new Numero("1");
		Numero ris = new Numero("0");
		for (int i = n.length() - 1; i >= 0; i--) {
			char fattore2 = n.charAt(i);
			int fatt = 0;
			if (fattore2 <= '9')
				fatt = fattore2 - 48;
			if (fattore2 >= 'A')
				fatt = fattore2 - 55;
			Numero temp = fattore.moltiplica(fatt);
			ris = ris.somma(temp);
			fattore = fattore.moltiplica(base);
		}
		return ris;
	}

	private String daDacimale(Numero num, int base) {
		StringBuilder str = new StringBuilder(30);
		Numero divisore = new Numero("" + base);
		Numero quoziente = num;
		Numero[] valori = { quoziente, Numero.ZERO };
		while (quoziente.compareTo(divisore) >= 0) {
			valori = quoziente.divisioneEResto(divisore);
			quoziente = valori[0];
			String temp = valori[1].toString();
			int x = Integer.parseInt(temp);
			if (x < 10)
				str.append(x);
			else {
				char c = (char) ('A' + x - 10);
				str.append(c);
			}
		}
		int x = Integer.parseInt(valori[0].toString());
		if (x < 10)
			str.append(x);
		else {
			char c = (char) ('A' + x - 10);
			str.append(c);
		}
		return str.reverse().toString();
	}
}
