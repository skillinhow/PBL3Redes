package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JLabel;

public class Conexao extends Thread {

	private MulticastSocket ms;
	private int porta;
	private String grupo;
	private int id;
	private volatile static boolean coord;
	private volatile static int idCoord;
	private ThreadEscutaAtualizacao th;
	private Relogio rel;
	private Referencia ref;
	private JLabel o;

	public Conexao(Relogio relo, JLabel label) {
		this.grupo = "236.0.0.0";
		porta = 20141;
		rel = relo;
		coord = false;
		o = label;
		idCoord = 0;
		try {
			ms = new MulticastSocket(porta);
			ms.joinGroup(InetAddress.getByName(grupo));
			System.out.println("Multicast com grupo - " + InetAddress.getByName(grupo) + " e porta - " + porta);
			id = geraID();
			th = new ThreadEscutaAtualizacao(rel);
		} catch (IOException e) {
			e.printStackTrace();
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

	public void getCoord() {

	}

	@Override
	public void run() {
		while (true) {
			if (idCoord != 0) {
				o.setText("Cooord - " + idCoord);
			}
			try {
				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				ms.receive(pack);
				String x = new String(pack.getData());
				System.out.println("Recebi de " + pack.getAddress() + ":" + pack.getPort() + " esta mensagem -" + x);

				String[] y = x.trim().split("@");

				if (y[0].equals("ARP")) {
					System.out.println("Estado do coord - " + coord);
					if (coord == true && idCoord == id) {
						System.out.println("Eu to aqui e sou a referência");
						enviar("EUC@" + id);
					} else if (coord == true && idCoord != id) {
						System.out.println("Eu to aqi e já tem referencia!!");
						enviar(("EUR@" + id + "@" + idCoord));
					} else {
						enviar("EU@"+ id);
					}
				}

				else if (y[0].equals("EU")) {
					if (id != Integer.parseInt(y[1])) {
						System.out.println("Manda teu tempo ai namoral - ID - " + y[1]
								+ " Pra eu acertar meu relogio - ID - " + id);
						String msg = "ST@" + y[1] + "@" + id;
						enviar(msg);
					} else {
						System.out.println("Ouvi um eco");
					}

				}

				else if (y[0].equals("ST") && Integer.parseInt(y[1]) == id) {
					String i = rel.getHora();
					System.out.println("Mando sim brother - ID - " + id + " Minha hora é essa: " + i
							+ " Ouviu pivas - ID - " + y[2]);
					String msg = "MT@" + i + "@" + y[2] + "@" + y[1];
					enviar(msg);
				}

				else if (y[0].equals("MT") && Integer.parseInt(y[2]) == id) {
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
						coord = true;
						idCoord = Integer.parseInt(y[3]);
						th.start();
						rel.start();
						System.out.println("Vlw man o/");
					} else if (resp == 0) {
						System.out.println("Minha hora tava certa, mas vlw man o/");
					} else if (resp == 1) {
						System.out.println("Tua hora ta errada man t.t");
					}
				}

				else if (y[0].equals("COORD") && id == Integer.parseInt(y[1])) {
					System.out.println("Virei Coordenador");
					coord = true;
					idCoord = id;
					ref = new Referencia(rel, coord);
					// ref.enviar("Teste");
					ref.start();
				} else if (y[0].equals("EUR")) {
					coord = true;
					idCoord = Integer.parseInt(y[2]);
					th.start();
				}else if (y[0].equals("EUC")) {
					coord = true;
					idCoord = Integer.parseInt(y[1]);
					th.start();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Erro na escuta");
			}

			System.err.println("Rapaz cabô a thread");
		}
	}

}
