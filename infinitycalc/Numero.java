package infinitycalc;

public class Numero implements Comparable<Numero> {

	// Il numero viene gestito come una lista di interi. Ogni nodo conterrà un
	// numero di massimo nove cifre.

	// Costruttore del Nodo contenente il primo blocco di 9 cifre.
	private class Nodo {
		int numero;
		Nodo next;
		Nodo previous;

		private Nodo(int n) {
			numero = n;
		}
	}

	public final static Numero ZERO = new Numero("0");
	private final int MAX_NODO = 999999999;
	private boolean negativo = false;
	private Nodo testa = null;
	private Nodo coda = null;

	// Costruttore di default
	public Numero() {
	}

	// Costruttore di Numero a partire da una stringa
	public Numero(String numero) {
		if (numero.charAt(0) == '-') {
			negativo = true;
			numero = numero.substring(1);
		}
		int i = 0;
		int k = numero.length();
		// Calcola il numero di passi necessari a scandire l'intero numero.
		int limite = (int) Math.ceil((double) k / 9);
		while (i < limite) {
			String temp;
			if ((i + 1) * 9 < k) {
				temp = numero.substring(k - (9 * (i + 1)), k - (9 * i));
			} else {
				temp = numero.substring(0, k - (9 * i));
			}
			i++;
			Nodo n = new Nodo(Integer.parseInt(temp));
			addTesta(n);
		}
		this.trim();
	}

	// Costruttore di copia.
	public Numero(Numero n) {
		Nodo nodo = n.testa;
		while (nodo != null) {
			Nodo k = new Nodo(nodo.numero);
			nodo = nodo.next;
			addCoda(k);
		}
		this.negativo = n.negativo;
	}

	// Operazione di somma del numero this con il numero n. Sulla base dei segni
	// dei due numeri si decide se eseguire l'operazione di somma o di
	// differenza, operazioni demandate poi ai metodi privati.
	public Numero somma(Numero n) {
		Numero r = null;
		if (negativo) {
			if (n.negativo) {
				r = this.add(n);
				r.negativo = true;
			} else {
				r = n.sub(this);
			}
		} else {
			if (n.negativo) {
				r = this.sub(n);
			} else {
				r = this.add(n);
			}
		}
		return r;
	}

	// Operazione di sottrazione del numero this con il numero n. Si comporta
	// analogamente al metodo somma.
	public Numero sottrai(Numero n) {
		Numero r = null;
		if (negativo) {
			if (n.negativo) {
				r = n.sub(this);
			} else {
				r = this.add(n);
				r.negativo = true;
			}
		} else {
			if (n.negativo) {
				r = this.add(n);
			} else {

				r = this.sub(n);

			}
		}
		return r;
	}

	// Operazione di moltiplicazione tra il numero this e numero fattore. Si
	// implementa un "contatore" a cui si sommano man mano i risultati di ogni
	// singola operazione
	public Numero moltiplica(int fattore) {
		if (fattore == 0)
			return new Numero("0");
		if (fattore == 1)
			return new Numero(this);
		Nodo n = this.coda;
		Numero r = Numero.ZERO;
		int i = 0;
		while (n != null) {
			n = this.coda;
			while (n != null) {
				// Risultato temporaneo che andrà sommato al contatore r.
				Numero k = new Numero();
				// Aggiungiamo una serie di blocchi zero all'inizio del numero
				// temporaneo r, pari ai blocchi precedenti a quelli
				// considerati.
				for (int c = 0; c < i; c++) {
					Nodo nodo = new Nodo(0);
					k.addTesta(nodo);
				}
				long l1 = fattore;
				long l2 = n.numero;
				long ris = l1 * l2;
				long k1 = ris % (MAX_NODO + 1);
				long k2 = ris / (MAX_NODO + 1);
				Nodo nodo1 = new Nodo((int) k1);
				k.addTesta(nodo1);
				Nodo nodo2 = new Nodo((int) k2);
				k.addTesta(nodo2);
				r = r.somma(k);
				n = n.previous;
				i++;
			}
		}
		r.trim();
		return r;
	}

	// Operazione di moltiplicazione tra il Numero this e il numero n. Si
	// implementa un "contatore" a cui si sommano man mano i risultato di ogni
	// singola operazione.
	public Numero moltiplica(Numero n) {
		Nodo n1 = n.coda;
		Nodo n2 = this.coda;
		Numero r = Numero.ZERO;
		int i = 0;
		int j = 0;
		while (n1 != null) {
			j = 0;
			n2 = this.coda;
			while (n2 != null) {
				// Risultato temporaneo che andrà sommato al contatore r
				Numero k = new Numero();
				// Aggiungiamo una serie di blocchi zero all'inizio del numero
				// temporaneo r, pari alla somma dei blocchi dei due fattori
				// precedenti ai blocchi considerati.
				for (int c = 0; c < i + j; c++) {
					Nodo nodo = new Nodo(0);
					k.addTesta(nodo);
				}
				long l1 = n1.numero;
				long l2 = n2.numero;
				long ris = l1 * l2;
				long k1 = ris % (MAX_NODO + 1);
				long k2 = ris / (MAX_NODO + 1);
				Nodo nodo1 = new Nodo((int) k1);
				k.addTesta(nodo1);
				Nodo nodo2 = new Nodo((int) k2);
				k.addTesta(nodo2);
				r = r.somma(k);
				n2 = n2.previous;
				j++;
			}
			n1 = n1.previous;
			i++;
		}
		if (this.negativo != n.negativo)
			r.negativo = true;
		else
			r.negativo = false;
		r.trim();
		return r;
	}

	// Operazione di divisione tra il numero this e il numero n. La divisione
	// viene eseguita come fosse una divisione "in colonna".
	public Numero dividi(Numero n) {
		if (n.equals(ZERO)) {
			throw new ArithmeticException();
		}
		int compare = this.compareTo(n);
		Numero r;
		if (compare < 0) {
			return new Numero("0");
		}
		if (compare == 0) {
			r = new Numero("1");
		} else {
			StringBuilder sb = new StringBuilder(18);
			String s1 = this.toStringU();
			String s2 = n.toStringU();
			String numero = s1.substring(0, s2.length());
			Numero num1 = new Numero(numero);
			int i = -1;
			while (s2.length() + i < s1.length()) {
				i++;
				String[] s = num1.div(n);
				sb.append(s[0]);
				if (s2.length() + i < s1.length()) {
					numero = s[1] + s1.charAt(s2.length() + i);
					num1 = new Numero(numero);
				}
			}
			r = new Numero(sb.toString());
		}
		if (this.negativo != n.negativo)
			r.negativo = true;
		else
			r.negativo = false;
		r.trim();
		return r;
	}

	// Operazione di resto tra il numero this e il numero n. Si definisce resto
	// di una divisione quel numero pari alla differenza tra il dividendo e il
	// prodotto tra quoziente e divisore. Per calcolarlo possiamo calcolare il
	// resto della divisione senza considerare i segni. Il modulo avrà poi lo
	// stesso segno del dividendo.
	public Numero resto(Numero n) {
		if (n.equals(ZERO)) {
			throw new ArithmeticException();
		}
		int compare = this.compareTo(n);
		if (compare < 0) {
			return new Numero(this);
		}
		if (compare == 0) {
			return new Numero("0");
		}
		String s1 = this.toStringU();
		String s2 = n.toStringU();
		String numero = s1.substring(0, s2.length());
		Numero num1 = new Numero(numero);
		int i = -1;
		while (s2.length() + i < s1.length()) {
			i++;
			String[] s = num1.div(n);
			if (s2.length() + i < s1.length()) {
				numero = s[1] + s1.charAt(s2.length() + i);
			} else {
				numero = s[1];
			}
			num1 = new Numero(numero);
		}
		num1.negativo = this.negativo;
		return num1;

	}

	// Operazione di divisione e
	public Numero[] divisioneEResto(Numero divisore) {
		Numero[] valori = new Numero[2];

		if (divisore.equals(ZERO)) {
			throw new ArithmeticException();
		}
		int compare = this.compareTo(divisore);
		if (compare < 0) {
			valori[0] = Numero.ZERO;
			valori[1] = this;
			return valori;
		}
		if (compare == 0) {
			valori[0] = new Numero("1");
			valori[1] = Numero.ZERO;
			return valori;
		}
		String s1 = this.toStringU();
		String s2 = divisore.toStringU();
		String numero = s1.substring(0, s2.length());
		StringBuilder sb = new StringBuilder(18);
		Numero num1 = new Numero(numero);
		int i = -1;
		while (s2.length() + i < s1.length()) {
			i++;
			String[] s = num1.div(divisore);
			sb.append(s[0]);
			if (s2.length() + i < s1.length()) {
				numero = s[1] + s1.charAt(s2.length() + i);
			} else {
				numero = s[1];
			}
			num1 = new Numero(numero);
			valori[1] = num1;
		}
		valori[0] = new Numero(sb.toString());
		return valori;
	}

	public Numero Log() {
		if (this.equals(ZERO))
			throw new ArithmeticException();
		int cifre = 0;
		Nodo n = coda;
		while (n.previous != null) {
			cifre += 9;
			n = n.previous;
		}
		cifre += Math.log10(n.numero);
		return new Numero("" + (cifre));
	}

	public Numero sqrt() {
		if (this.equals(ZERO))
			return Numero.ZERO;
		if (this.negativo) {
			throw new ArithmeticException();
		}
		StringBuilder sb = new StringBuilder(15);
		Numero cifre = this.Log();
		String numS = this.toString();
		int index;
		int numero1;
		Numero numero;
		if (cifre.EPari()) {
			String t = numS.substring(0, 1);
			numero1 = Integer.parseInt(t);
			numero = new Numero(t);
			index = 1;
		} else {
			String t = numS.substring(0, 2);
			numero1 = Integer.parseInt(t);
			numero = new Numero(t);
			index = 2;
		}
		int ris = (int) Math.sqrt(numero1);
		sb.append(ris);
		Numero risultato = new Numero(sb.toString());
		risultato = risultato.moltiplica(risultato);
		while (index < numS.length()) {
			numero = numero.sottrai(risultato);
			numero = numero.moltiplica(100);
			String t = numS.substring(index, index + 2);
			numero = numero.somma(new Numero(t));
			index = index + 2;
			Numero r = new Numero(sb.toString());
			r = r.moltiplica(2);
			String rS = r.toString();
			int prossimo = prossimoNum(numero, rS);
			String c = rS + prossimo;
			risultato = new Numero(c);
			risultato = risultato.moltiplica(prossimo);
			sb.append(prossimo);
		}
		return new Numero(sb.toString());
	}

	public boolean EPari() {
		Nodo n = coda;
		return n.numero % 2 == 0;
	}

	public int prossimoNum(Numero n, String divisore) {
		int x = 1;
		boolean procedi = true;
		do {
			String s = divisore + x;
			Numero div = new Numero(s);
			div = div.moltiplica(x);
			if (div.compareTo(n) > 0) {
				procedi = false;
			}
			x++;
		} while (procedi);
		return x - 2;
	}

	@Override
	public int compareTo(Numero n) {
		Nodo n1 = this.testa;
		Nodo n2 = n.testa;
		while (n1 != null && n2 != null) {
			n1 = n1.next;
			n2 = n2.next;
		}
		if (n1 == null && n2 != null)
			return -1;
		if (n2 == null && n1 != null)
			return 1;
		n1 = this.testa;
		n2 = n.testa;
		while (n1 != null) {
			if (n1.numero < n2.numero)
				return -1;
			if (n1.numero > n2.numero)
				return 1;
			n1 = n1.next;
			n2 = n2.next;
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Numero)) {
			return false;
		}
		Numero n = (Numero) obj;
		Nodo n1 = this.testa;
		Nodo n2 = n.testa;
		while (n1 != null && n2 != null) {
			if (n1.numero != n2.numero)
				return false;
			n1 = n1.next;
			n2 = n2.next;
		}
		if (!(n1 == null && n2 == null)) {
			return false;
		}
		return true;
	}

	public String toString() {
		if (testa == coda && testa.numero == 0) {
			return "0";
		}
		StringBuilder s = new StringBuilder();
		if (negativo) {
			s.append("-");
		}
		Nodo n = testa;
		while (n != null) {
			if (n.previous != null) {
				int cifre;
				if (n.numero == 0) {
					cifre = 1;
				} else {
					cifre = (int) Math.log10(n.numero) + 1;
				}
				for (int i = cifre; i < 9; i++) {
					s.append("0");
				}
			}
			s.append(n.numero);
			n = n.next;
		}
		return s.toString();
	}

	private void addTesta(Nodo n) {
		if (testa == null) {
			coda = n;
		} else {
			testa.previous = n;
		}
		n.next = testa;
		n.previous = null;
		testa = n;
	}

	private void addCoda(Nodo n) {
		if (coda == null) {
			testa = n;
		} else {
			coda.next = n;
		}
		n.next = null;
		n.previous = coda;
		coda = n;
	}

	private void trim() {// Questo metodo rimuove tutti i blocchi nulli inseriti
		// in testa al numero
		while (testa.numero == 0 && testa != coda) {
			testa = testa.next;
			testa.previous.next = null;
			testa.previous = null;

		}
	}

	private Numero add(Numero n) {
		Numero r = new Numero();
		Nodo n1 = coda;
		Nodo n2 = n.coda;
		int riporto = 0;
		while (n1 != null && n2 != null) {
			int ris = n1.numero + n2.numero + riporto;
			riporto = 0;
			if (ris > MAX_NODO) {
				riporto = 1;
				ris = ris - MAX_NODO - 1;
			}
			Nodo t = new Nodo(ris);
			r.addTesta(t);
			n1 = n1.previous;
			n2 = n2.previous;
		}
		while (n1 != null) {
			int ris = n1.numero + riporto;
			riporto = 0;
			if (ris > MAX_NODO) {
				riporto = 1;
				ris = 0;
			}
			Nodo nodo = new Nodo(ris);
			r.addTesta(nodo);
			n1 = n1.previous;
		}
		while (n2 != null) {
			int ris = n2.numero + riporto;
			riporto = 0;
			if (ris > MAX_NODO) {
				riporto = 1;
				ris = 0;
			}
			Nodo nodo = new Nodo(ris);
			r.addTesta(nodo);
			n2 = n2.previous;
		}
		if (riporto == 1) {
			Nodo nodo = new Nodo(1);
			r.addTesta(nodo);
		}
		return r;
	}

	private Numero sub(Numero n) {
		Numero r = new Numero();
		Nodo n1;
		Nodo n2;
		int compare = this.compareTo(n);
		if (compare > 0) {
			n1 = coda;
			n2 = n.coda;
		} else {
			if (compare < 0) {
				n1 = n.coda;
				n2 = coda;
				r.negativo = true;
			} else {
				return new Numero("0");
			}
		}
		int riporto = 0;
		while (n1 != null && n2 != null) {
			int ris = n1.numero - riporto - n2.numero;
			riporto = 0;
			if (ris < 0) {
				ris = ris + MAX_NODO + 1;
				riporto = 1;
			}
			Nodo nodo = new Nodo(ris);
			r.addTesta(nodo);
			n1 = n1.previous;
			n2 = n2.previous;
		}
		while (n1 != null) {
			int ris = n1.numero - riporto;
			riporto = 0;
			if (ris < 0) {
				riporto = 1;
				ris = MAX_NODO;
			}
			Nodo nodo = new Nodo(ris);
			r.addTesta(nodo);
			n1 = n1.previous;
		}
		r.trim();
		return r;
	}

	private String[] div(Numero n) {

		String[] valori = new String[2];
		/*
		 * Questo � il vettore che viene restituito. La prima posizione del vettore
		 * contiene il quoziente il secondo il resto. Quest'ultimo servir� per calcolare
		 * il prossimo passo della divisione
		 */
		if (this.compareTo(n) < 0) {
			valori[0] = "0";
			valori[1] = this.toString();
		} else {
			int quoziente = 1;
			Numero num = new Numero(this);
			num = num.sub(n);
			while (num.compareTo(n) >= 0) {
				quoziente++;
				num = num.sub(n);
			}
			valori[0] = "" + quoziente;
			valori[1] = num.toString();
		}
		return valori;
	}

	private String toStringU() {
		if (testa == coda && testa.numero == 0) {
			return "0";
		}
		StringBuilder s = new StringBuilder();
		Nodo n = testa;
		while (n != null) {
			if (n.previous != null) {
				int cifre;
				if (n.numero == 0) {
					cifre = 1;
				} else {
					cifre = (int) Math.log10(n.numero) + 1;
				}
				for (int i = cifre; i < 9; i++) {
					s.append("0");
				}
			}
			s.append(n.numero);
			n = n.next;
		}
		return s.toString();
	}

}
