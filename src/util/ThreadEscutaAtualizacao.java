package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.xml.bind.annotation.XmlInlineBinaryData;

public class ThreadEscutaAtualizacao extends Thread {

	private MulticastSocket ea;
	private Relogio rel;
	private String grupo;
	private int porta;

	public ThreadEscutaAtualizacao(Relogio relo) {
		rel = relo;
		grupo = "230.0.0.11";
		porta = 20142;
		try {
			ea = new MulticastSocket(porta);
			ea.joinGroup(InetAddress.getByName(grupo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte buff[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buff, buff.length);
				ea.receive(pack);
				String x = new String(pack.getData());
				System.out.println("Recebi de " + pack.getAddress() + ":" + pack.getPort() + " esta mensagem -" + x);

				String[] y = x.trim().split("@");

				if (y[0].equals("HC")) {
					String[] aux = y[1].split(":");
					// System.out.println("To comparando essa hora - " + y[1]);
					int resp = rel.compareTo(y[1]);

					if (resp == -1) {
						rel.setHora(Integer.parseInt(aux[0]));
						rel.setMin(Integer.parseInt(aux[1]));
						rel.setSeg(Integer.parseInt(aux[2]));
						rel.refresh();
						// System.out.println("Vlw man o/");
					} else if (resp == 0) {
						// System.out.println("Minha hora tava certa, mas vlw
						// man o/");
					} else if (resp == 1) {
						// System.out.println("Tua hora ta errada man t.t");
					}
				} else if (y[0].equals("ARP")) {
					System.out.println("Ta travando assim");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Erro na escuta da Referência");
			}

		}
	}

}
