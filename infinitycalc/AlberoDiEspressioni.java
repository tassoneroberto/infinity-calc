package infinitycalc;

import java.util.Stack;
import java.util.StringTokenizer;

public class AlberoDiEspressioni {
	StringTokenizer st;
	private Nodo radice = null;

	private class Nodo {
		Nodo figlioDx, figlioSx;
	}

	private class NodoOperatore extends Nodo implements Comparable<NodoOperatore> {
		char operatore;

		public String toString() {
			return "" + operatore;
		}

		public int compareTo(NodoOperatore o) {
			if (this.operatore == o.operatore) {
				return 0;
			}
			if (o.operatore == '+' && this.operatore == '-') {
				return 0;
			}
			if (o.operatore == '-' && this.operatore == '+') {
				return 0;
			}
			if (o.operatore == '/' && this.operatore == 'x') {
				return 0;
			}
			if (o.operatore == 'x' && this.operatore == '/') {
				return 0;
			}
			if (o.operatore == 'x' && this.operatore == '%') {
				return 0;
			}
			if (o.operatore == '%' && this.operatore == '/') {
				return 0;
			}
			if (o.operatore == '%' && this.operatore == 'x') {
				return 0;
			}
			if (o.operatore == '/' && this.operatore == '%') {
				return 0;
			}
			if (o.operatore == '/' || o.operatore == '%' || o.operatore == 'x') {
				return 1;
			}
			return -1;
		}
	}

	private class NodoOperando extends Nodo {
		Numero operando;

		public String toString() {
			return operando.toString();
		}
	}

	public void build(String expr) {
		st = new StringTokenizer(expr, "$");
		radice = buildEspressione(st);
	}

	private Nodo buildEspressione(StringTokenizer st) {
		Stack<Nodo> operandi = new Stack<Nodo>();
		Stack<NodoOperatore> operatori = new Stack<NodoOperatore>();
		String next = st.nextToken();
		NodoOperando nodo = new NodoOperando();
		Numero opnd;
		if (!next.equals("(")) {
			opnd = new Numero(next);
			nodo.operando = new Numero(opnd);
			operandi.push(nodo);
		} else {
			Nodo n1 = buildEspressione(st);
			operandi.push(n1);
		}
		while (st.hasMoreTokens()) {
			char opr = st.nextToken().charAt(0);
			if (opr == ')') {
				while (!operatori.isEmpty()) {
					NodoOperatore albero = operatori.pop();
					Nodo a1 = operandi.pop();
					Nodo a2 = operandi.pop();
					albero.figlioSx = a1;
					albero.figlioDx = a2;
					operandi.push(albero);
				}
				return operandi.pop();
			}
			NodoOperatore operatore = new NodoOperatore();
			operatore.operatore = opr;
			if (operatori.isEmpty() || operatori.firstElement().compareTo(operatore) > 0) {
				operatori.push(operatore);
			} else {
				do {
					NodoOperatore albero = operatori.pop();
					Nodo a1 = operandi.pop();
					Nodo a2 = operandi.pop();
					albero.figlioSx = a1;
					albero.figlioDx = a2;
					operandi.push(albero);
				} while (!operatori.isEmpty() && operatori.firstElement().compareTo(operatore) <= 0);
				operatori.push(operatore);
			}
			next = st.nextToken();
			if (!next.equals("(")) {
				opnd = new Numero(next);
				nodo = new NodoOperando();
				nodo.operando = new Numero(opnd);
				operandi.push(nodo);
			} else {
				Nodo n1 = buildEspressione(st);
				operandi.push(n1);
			}
		}
		if (!operatori.isEmpty()) {
			do {
				NodoOperatore p1 = operatori.pop();
				Nodo a1 = operandi.pop();
				Nodo a2 = operandi.pop();
				p1.figlioSx = a1;
				p1.figlioDx = a2;
				operandi.push(p1);
			} while (!operatori.isEmpty());
		}
		return operandi.pop();
	}

	public String visitaSimmetrica() {
		StringBuilder sb = new StringBuilder();
		visitaSimmetrica(radice, sb);
		return sb.toString();
	}

	private void visitaSimmetrica(Nodo n, StringBuilder sb) {
		String s = new String();
		if (n != null) {
			if (n instanceof NodoOperatore) {
				sb.append("(");
			}
			visitaSimmetrica(n.figlioDx, sb);
			if (n instanceof NodoOperando) {
				s = n.toString();
				if (s.charAt(0) == '-')
					sb.append("(");
			}
			sb.append(n);
			if (n instanceof NodoOperando) {
				if (s.charAt(0) == '-')
					sb.append(")");
			}
			visitaSimmetrica(n.figlioSx, sb);
			if (n instanceof NodoOperatore) {
				sb.append(")");
			}
		}
	}

	public Numero valuta() {
		Numero n = valuta(radice);
		NodoOperando nodo = new NodoOperando();
		nodo.operando = n;
		radice = nodo;
		return n;
	}

	private Numero valuta(Nodo radice) throws ArithmeticException {
		if (radice instanceof NodoOperando) {
			return ((NodoOperando) radice).operando;
		}
		Numero val1 = valuta(radice.figlioDx);
		Numero val2 = valuta(radice.figlioSx);
		char op = ((NodoOperatore) radice).operatore;
		switch (op) {
		case '+': {
			return val1.somma(val2);
		}
		case '-': {
			return val1.sottrai(val2);
		}
		case 'x': {
			return val1.moltiplica(val2);
		}
		case '/': {
			return val1.dividi(val2);
		}
		default: {
			return val1.resto(val2);
		}
		}
	}

	public boolean risultato() {
		// il metotodo ritorner� true se l'espressione � stata semplificata
		// completamente, cio� se all'interno dell'albero � presente un solo
		// nodo, equivalente al risultato. In nessun'altro caso � possibile
		// che la radice dell'albero sia un operando
		return radice instanceof NodoOperando;

	}

	public void semplifica() throws IllegalStateException {
		if (radice instanceof NodoOperando) {
			throw new IllegalStateException();
		}
		if (radice.figlioDx instanceof NodoOperatore) {
			semplifica(radice.figlioDx, radice, 0);
		} else {
			if (radice.figlioSx instanceof NodoOperando) {
				NodoOperando risultato = new NodoOperando();
				char op = ((NodoOperatore) radice).operatore;
				Numero val1 = ((NodoOperando) radice.figlioDx).operando;
				Numero val2 = ((NodoOperando) radice.figlioSx).operando;
				switch (op) {
				case '+': {
					risultato.operando = val1.somma(val2);
					break;
				}
				case '-': {
					risultato.operando = val1.sottrai(val2);
					break;
				}
				case 'x': {
					risultato.operando = val1.moltiplica(val2);
					break;
				}
				case '*': {
					risultato.operando = val1.moltiplica(val2);
					break;
				}
				case '/': {
					risultato.operando = val1.dividi(val2);
					break;
				}
				default: {
					risultato.operando = val1.resto(val2);
					break;
				}
				}
				radice = risultato;
			} else {
				semplifica(radice.figlioSx, radice, 1);
				return;
			}
		}
	}

	private void semplifica(Nodo figlio, Nodo padre, int direzione) {
		// direzione � =0 se � il figlio destro,=1 se � figlio sinistro
		if (figlio.figlioDx instanceof NodoOperatore) {
			semplifica(figlio.figlioDx, figlio, 0);
			return;
		} else {
			if (figlio.figlioSx instanceof NodoOperatore) {
				semplifica(figlio.figlioSx, figlio, 1);
				return;
			} else {
				NodoOperando risultato = new NodoOperando();
				char op = ((NodoOperatore) figlio).operatore;
				Numero val1 = ((NodoOperando) figlio.figlioDx).operando;
				Numero val2 = ((NodoOperando) figlio.figlioSx).operando;
				switch (op) {
				case '+': {
					risultato.operando = val1.somma(val2);
					break;
				}
				case '-': {
					risultato.operando = val1.sottrai(val2);
					break;
				}
				case 'x': {
					risultato.operando = val1.moltiplica(val2);
					break;
				}
				case '/': {
					risultato.operando = val1.dividi(val2);
					break;
				}
				default: {
					risultato.operando = val1.resto(val2);
					break;
				}
				}
				if (direzione == 0) {
					padre.figlioDx = risultato;
				} else {
					padre.figlioSx = risultato;
				}
				return;
			}
		}
	}

	public String toString() {
		return visitaSimmetrica();
	}
}
