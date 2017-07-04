package controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JLabel;

import util.Conexao;
import util.ConexaoDt;
import util.Relogio;

public class Controller {

	private Relogio rel;
	private Conexao con;
	private ConexaoDt ds;

	public Controller(JLabel label) {
		rel = new Relogio(label);
	}

	public void conectar(String ip) throws Exception {
		con = new Conexao(ip);
		con.start();
		int x = con.geraID();
//		int x = con.setaID(1);
		System.out.println("Quem ta ai?");
		ds = con.getDt();
		con.enviar("ARPR@" + x + "@" + ds.getPorta());
		System.err.println("Estou enviando isso - " + "ARPR@" + x + "@" + ds.getPorta());
		wait(1000);
		eleicao();

	}

	public void eleicao() {
		for (String aux : con.getLista()) {
			String[] aux2 = aux.split("@");

			if (Integer.parseInt(aux2[0]) > con.getID()) {
				ds.enviar("VOT", aux2[2], aux2[1]);
			} else {
				System.out.println("Nao tem ninguem maior!!");
			}

			System.out.println("ID do elemento - " + aux2[0]);
			System.out.println("IP do elemento - " + aux2[1]);
			System.out.println("Porta do elemento - " + aux2[2]);
		}
	}

	public void start() {
		rel.start();
	}

	public void setDrift(long aux) {
		rel.setMillis(aux);
	}

	public void stop() {
		System.out.println("Stop não implementado ainda XD");
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
	
	public void deuRuin(){
		
	}

}
