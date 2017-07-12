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
	private boolean coord;

	public Referencia(Relogio relo, boolean c) {
		this.rel = relo;
		grupo = "230.0.0.11";
		porta = 20142;
		coord = c;
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
	
	

	public String getGrupo() {
		return grupo;
	}

	public int getPorta() {
		return porta;
	}

	@Override
	public void run() {
		System.out.println("Entrou no método de atualização com coord - " + coord);
        
		while (coord == true) {
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

}

