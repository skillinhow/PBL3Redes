package controller;

import javax.swing.JLabel;

import util.Conexao;
import util.Relogio;

public class Controller {

	private Relogio rel;
	private Conexao con;

	public Controller(JLabel label) {
		rel = new Relogio(label);
		con = new Conexao(rel);
	}

	public void conectar() throws Exception {
		if (!con.isAlive()) {
			System.err.println("taquisapoha");
			
			con.start();
		}
		
		System.out.println("Quem ta ai?");
		con.enviar("ARP@");
		// con.temGente();


		con = new Conexao(rel);
		con.start();                
		//System.err.println("Quem ta ai?");
		con.enviar("ARP@");                
	}

	public void start() {
		rel.start();
	}

	public void setDrift(long aux) {
		rel.setMillis(aux);
	}

	public void setSeg(int aux) {
		rel.setSeg(aux);
		rel.refresh();
	}

	public void setMin(int aux) {
		rel.setMin(aux);
		rel.refresh();
	}

	public void setHr(int aux) {
		rel.setHora(aux);
		rel.refresh();
	}
}

