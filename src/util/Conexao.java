package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Conexao extends Thread {

	private MulticastSocket ms;
	private int porta;
	private String grupo;

	public Conexao(String grupo) {
		this.grupo = grupo;
		porta = 5000;
		try {
			ms = new MulticastSocket(porta);
			ms.joinGroup(InetAddress.getByName(grupo));
			ms.setTimeToLive(1);
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
		System.out.println("Pacote tecnicamente enviado");
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Inicio da thread");
				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				ms.receive(pack);
				String x = new String(pack.getData());
				System.out.println("Recebi de " +pack.getAddress() +":"+ pack.getPort()+ " esta mensagem -" + x);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Erro na escuta");
			}

		}
	}

}
