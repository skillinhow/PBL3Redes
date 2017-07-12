package util;

import javax.swing.JLabel;

public class Relogio extends Thread implements Comparable<String> {

	private int hora, min, seg;
	private JLabel lbl;
	private long millis;
	private String rel;

	public Relogio(JLabel label) {
		hora = 0;
		min = 0;
		seg = 0;
		lbl = label;
		millis = 1000;
		rel = hora + ":" + min + ":" + seg;
		lbl.setText(rel);
	}

	@Override
	public void run() {
		while (true) {
			try {
				seg += 1;
				Thread.sleep(millis);
				rel = hora + ":" + min + ":" + seg;
				lbl.setText(rel);
				
				if (seg > 59) {
                                        seg = 0;
					min += 1;					
					rel = hora + ":" + min + ":" + seg;
					lbl.setText(rel);
                                        if(min > 59){
                                        hora += 1;
					min = 0;
					rel = hora + ":" + min + ":" + seg;
					lbl.setText(rel);
                                        if(hora>23)
                                            hora = 0;
					rel = hora + ":" + min + ":" + seg;
					lbl.setText(rel);
                                        }
                                        
				} 

			} catch (InterruptedException e) {
				System.out.println("Erro no delay do contador");
			}
		}
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setSeg(int seg) {
		this.seg = seg;
	}

	public void setMillis(long millis) {
		this.millis += millis;
	}

	public long getDrift() {
		return millis;
	}

	public void refresh() {
		rel = hora + ":" + min + ":" + seg;
		lbl.setText(rel);
	}

	public String getHora() {
		return hora + ":" + min + ":" + seg;

	}

	public boolean comparaHora(String hora) {
		String[] hrec = hora.split(":");
		if (Integer.parseInt(hrec[2]) < seg) {
			if (Integer.parseInt(hrec[1]) < min) {
				if (Integer.parseInt(hrec[0]) < this.hora) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public int compareTo(String arg0) {
		String[] aux = arg0.split(":");
		// System.out.println("Hora atual - " + hora + ":" + min + ":" + seg);

		// Caso a hora passada por argumento seja maior que a minha
		if (Integer.parseInt(aux[0]) == hora) {
			if (Integer.parseInt(aux[1]) == min) {
				if (Integer.parseInt(aux[2]) > seg) {
					return -1;
				}
			} else if (Integer.parseInt(aux[1]) > min) {
				return -1;
			}
		} else if (Integer.parseInt(aux[0]) > hora) {
			return -1;
		}

		// Caso a hora passada por argumento seja igual a minha
		if (Integer.parseInt(aux[0]) == hora && Integer.parseInt(aux[1]) == min && Integer.parseInt(aux[2]) == seg) {
			return 0;
		}

		// Caso a hora passada por argumento seja menor que a minha
		if (Integer.parseInt(aux[0]) == hora) {
			if (Integer.parseInt(aux[1]) == min) {
				if (Integer.parseInt(aux[2]) < seg) {
					return 1;
				}
			} else if (Integer.parseInt(aux[1]) < min) {
				return 1;
			}
		} else if (Integer.parseInt(aux[0]) < hora) {
			return 1;
		}

		return 2;

	}

	public int compareTo2(String arg0) {

		String[] aux = arg0.split(":");
		System.out.println("Hora atual - " + hora + ":" + min + ":" + seg);

		// Caso a hora passada por argumento seja maior que a minha
		if (Integer.parseInt(aux[0]) == hora) {
			if (Integer.parseInt(aux[1]) == min) {
				if (Integer.parseInt(aux[2]) > seg) {
					return -1;
				}
			} else if (Integer.parseInt(aux[1]) > min) {
				return -1;
			}
		} else if (Integer.parseInt(aux[0]) > hora) {
			return -1;
		}

		// Caso a hora passada por argumento seja igual a minha.
		if (Integer.parseInt(aux[0]) == hora && Integer.parseInt(aux[1]) == min && Integer.parseInt(aux[2]) == seg) {
			return 0;
		}

		// Caso a hora passada por argumento seja menor que a minha
		if (Integer.parseInt(aux[0]) == hora) {
			if (Integer.parseInt(aux[1]) == min) {
				if (Integer.parseInt(aux[2]) < seg) {
					return 1;
				}
			} else if (Integer.parseInt(aux[1]) < min) {
				return 1;
			}
		} else if (Integer.parseInt(aux[0]) < hora) {
			return 1;
		}

		return 2;
	}

}
