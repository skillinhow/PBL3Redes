package util;

import java.io.IOException;
import static java.lang.Thread.sleep;
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
<<<<<<< HEAD
<<<<<<< HEAD
		grupo = "230.0.0.1";
		porta = 20141;
=======
		grupo = "230.0.0.11";
		porta = 20142;
>>>>>>> 095e63bcc36b2263398c737fefd7043f9349c90a
=======
		grupo = "230.0.0.11";
		porta = 20142;
>>>>>>> 5592ecd153ec69017de20e75dcae93950bcef06e
		try {
			rf = new MulticastSocket(porta);
			rf.joinGroup(InetAddress.getByName(grupo));
			System.out.println("Grupo de atualização criado com sucesso!!");
		} catch (IOException e) {			
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
		System.out.println("Entrou no método de atualização com coord - " + coord);
                
		if (coord == true) {
			//System.out.println("Mandando atualização");
			String msg = "HC@" + rel.getHora();
			try {
				enviar(msg);                               
				sleep(250);                                
				//System.out.println(msg); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Erro no envio da atualização da hora");
			}
                       
		}
	}
	


	@Override
	public void run() {
		
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

