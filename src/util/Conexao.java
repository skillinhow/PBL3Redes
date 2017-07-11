package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Conexao extends Thread {

	private MulticastSocket ms;
	private int porta;
	private String grupo;
	private int id;
	private volatile boolean coord;
	private Relogio rel;
	private Referencia ref;

	public Conexao(Relogio relo) {
		this.grupo = "230.0.0.1";
		porta = 5000;
		rel = relo;
		coord = false;
		try {
			ms = new MulticastSocket(porta);
			ms.joinGroup(InetAddress.getByName(grupo));
			ms.setTimeToLive(10);
			System.out.println("Multicast com grupo - "+ InetAddress.getByName(grupo) +" e porta - "+ porta);
			id = geraID();
			System.out.println("Grupo criado com sucesso!!");
		} catch (IOException e) {
			System.out.println("Falha na criação do grupo Multicast");
		}
	}

	public void enviar(String mensagem) throws Exception {

		byte buf[] = new byte[1024];
		buf = mensagem.getBytes();
		DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(grupo), porta);
		ms.send(pack);
	}

	public int geraID() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String[] u = (sdf.format(new Date())).split(":");
		int ID = (Integer.parseInt(u[0]) + Integer.parseInt(u[1]) + Integer.parseInt(u[2]))
				* new Random().nextInt(100000);
		System.out.println("ID - " + ID);
		id = ID;
		return ID;
	}

	public int getID() {
		return id;
	}

	// public int getPortaUDP() {
	// return ds.getPorta();
	// }

	@Override
	public void run() {
		while (true) {
			try {
				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				ms.receive(pack);
				String x = new String(pack.getData());
				System.out.println("Recebi de " + pack.getAddress() + ":" + pack.getPort() + " esta mensagem -" + x);

				String[] y = x.trim().split("@");

				if (y[0].equals("ARP")) {
					System.out.println("Estado do coord - " + coord);
					if (coord) {
						System.out.println("Eu to aqui e sou a referência");
						enviar("EU@" + id + "@COORD");
					} else {
						System.out.println("Eu to aqi !!");
						enviar(("EU@" + id));
					}
				} else if (y[0].equals("EU")) {
					if (id != Integer.parseInt(y[1])) {
						System.out.println("Manda teu tempo ai namoral - ID - " + y[1]
								+ " Pra eu acertar meu relogio - ID - " + id);
						String msg = "ST@" + y[1] + "@" + id;
						enviar(msg);
					} else {
						System.out.println("Ouvi um eco");
					}

				} else if (y[0].equals("ST") && Integer.parseInt(y[1]) == id) {
					String i = rel.getHora();
					System.out.println("Mando sim brother - ID - " + id + " Minha hora é essa: " + i
							+ " Ouviu pivas - ID - " + y[2]);
					String msg = "MT@" + i + "@" + y[2] + "@" + y[1];
					enviar(msg);
				} else if (y[0].equals("MT") && Integer.parseInt(y[2]) == id) {
					String[] aux = y[1].split(":");
					System.out.println("To comparando essa hora - " + y[1]);
					int resp = rel.compareTo(y[1]);
					System.out.println("Resposta da comparação - " + resp);
					if (resp == -1) {
						rel.setHora(Integer.parseInt(aux[0]));
						rel.setMin(Integer.parseInt(aux[1]));
						rel.setSeg(Integer.parseInt(aux[2]));
						rel.refresh();
						enviar("COORD@" + y[3]);						
						ref = new Referencia(rel);
						ref.start();
						rel.run();
						System.out.println("Vlw man o/");
					} else if (resp == 0) {
						System.out.println("Minha hora tava certa, mas vlw man o/");
					} else if (resp == 1) {
						System.out.println("Tua hora ta errada man t.t");
					}
				} else if (y[0].equals("COORD")) {
					System.out.println("Virei Coordenador");
					coord = true;
					ref = new Referencia(rel);
//					ref.enviar("Teste");
					ref.atualizarRel(coord);
				} else if (y[0].equals("EU") && y[2].equals("COORD")) {
					System.out.println("É tu quem manda aqui é? - ID -" + y[1]);
					ref = new Referencia(rel);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Erro na escuta");
			}

		}
	}

}
