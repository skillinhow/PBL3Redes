package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class ConexaoDt extends Thread {

	private DatagramSocket ds;

	public ConexaoDt() {
		try {
			ds = new DatagramSocket();
			System.out.println("Datagram iniciado com a porta - " + ds.getLocalPort());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro em criar o Datagram");
		}
	}

	public void enviar(String mensagem, String porta, String ip) {

		byte[] bpkt = mensagem.getBytes();
		InetAddress Ip;
		try {
			Ip = InetAddress.getByName(ip);
			DatagramPacket depkt = new DatagramPacket(bpkt, bpkt.length, Ip, Integer.parseInt(porta));
			ds.send(depkt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro no envio do Datagram");
		}
	}

	@Override
	public void run() {
		byte[] dados = new byte[100];
		DatagramPacket drpkt = new DatagramPacket(dados, dados.length);
		try {
			ds.receive(drpkt);
			String x = new String(drpkt.getData());
			System.out.println("Recebi no Datagram - "+ x.trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro no recebimento do Datagram");
		}

	}

	public int getPorta() {
		return ds.getLocalPort();
	}

}
