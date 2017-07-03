package util;

import javax.swing.JLabel;

public class Relogio extends Thread {

	private int hora, min, seg;
	private JLabel lbl;
	private long millis;
	private String rel;

	public Relogio(JLabel label) {
		hora = 00;
		min = 00;
		seg = 00;
		lbl = label;
		millis = 1000;
		rel = hora+":"+min+":"+seg;
		lbl.setText(rel);
	}

	@Override
	public void run() {
		while (true) {
			try {
				seg += 1;
				Thread.sleep(millis);
				rel = hora+":"+min+":"+seg;
				lbl.setText(rel);
//				System.out.println("Drift na contagem - "+ millis);
				if (seg > 60) {
					min += 1;
					seg = 0;
					rel = hora+":"+min+":"+seg;
					lbl.setText(rel);
				}else if (min > 60) {
					hora += 1;
					min = 0;
					rel = hora+":"+min+":"+seg;
					lbl.setText(rel);
				} else if (hora > 23) {
					hora = 0;
					rel = hora+":"+min+":"+seg;
					lbl.setText(rel);
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
		this.millis = millis;
	}
	
	public void refresh(){
		rel = hora+":"+min+":"+seg;
		lbl.setText(rel);
	}
	
}
