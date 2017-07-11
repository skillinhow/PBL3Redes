package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Referencia extends Thread {

	private MulticastSocket rf;
	private String grupo;
	private int porta;
	private Relogio rel;

	public Referencia(Relogio relo) {
		this.rel = relo;
		grupo = "230.0.0.136";
		porta = 6001;
		try {
			rf = new MulticastSocket(porta);
			rf.joinGroup(InetAddress.getByName(grupo));
			System.out.println("Grupo de atualização criado com sucesso!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enviar(String mensagem) throws Exception {

		byte buff[] = new byte[1024];
		buff = mensagem.getBytes();
		DatagramPacket pack = new DatagramPacket(buff, buff.length, InetAddress.getByName(grupo), porta);
		rf.send(pack);
	}

	public void atualizarRel(boolean coord) {
		System.out.println("Entoru no método de atuaçização com coord - " + coord);
		while (coord) {
			System.out.println("Mandando atualização");
			String msg = "HC@" + rel.getHora();
			try {
				enviar(msg);
				sleep(250);
				System.out.println(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Erro no envio da atualização da hora");
			}
		}
	}

	@Override
	public void run() {
		System.out.println("Escuta de atualização de horario pronta!!");
		while (true) {
			try {
				byte buff[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buff, buff.length);
				rf.receive(pack);
				String x = new String(pack.getData());
				System.out.println("Recebi de " + pack.getAddress() + ":" + pack.getPort() + " esta mensagem -" + x);

				String[] y = x.trim().split("@");

				if (y[0].equals("HC")) {
					String[] aux = y[1].split(":");
					System.out.println("To comparando essa hora - " + y[1]);
					int resp = rel.compareTo(y[1]);
					System.out.println("Resposta da comparação - " + resp);
					if (resp == -1) {
						rel.setHora(Integer.parseInt(aux[0]));
						rel.setMin(Integer.parseInt(aux[1]));
						rel.setSeg(Integer.parseInt(aux[2]));
						rel.refresh();
						System.out.println("Vlw man o/");
					} else if (resp == 0) {
						System.out.println("Minha hora tava certa, mas vlw man o/");
					} else if (resp == 1) {
						System.out.println("Tua hora ta errada man t.t");
					}
				}else if (y[0].equals("ARP")) {
					System.out.println("Ta travando assim");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Erro na escuta da Referência");
			}
		}
	}

}
