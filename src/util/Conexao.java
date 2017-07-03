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
	private ConexaoDt ds;
	private int porta;
	private String grupo;
	private int id;
	private ArrayList<String> lista;

	public Conexao(String grupo) {
		this.grupo = grupo;
		porta = 5000;
		lista = new ArrayList<>();
		try {
			ms = new MulticastSocket(porta);
			ms.joinGroup(InetAddress.getByName(grupo));
			ms.setTimeToLive(1);
			System.out.println("Grupo criado com sucesso!!");
			ds = new ConexaoDt();
			ds.start();
			System.out.println("Datagram criado com porta - " + ds.getPorta());
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
	
//	public int setaID(int id){
//		this.id = id;
//		InetAddress e;
//		try {
//			e = InetAddress.getLocalHost();
//			String eu = id + "@" + e.getHostAddress().toString() + "@" + ds.getPorta();
//			System.out.println(eu);
//			lista.add(eu);
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			System.out.println("Erro em gerar o ip local");
//		}
//		return id;
//	}

	public int geraID() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		System.out.println(sdf.format(new Date()));
		String[] u = (sdf.format(new Date())).split(":");
		int ID = (Integer.parseInt(u[0]) + Integer.parseInt(u[1]) + Integer.parseInt(u[2]))
				* new Random().nextInt(100000);
		System.out.println("ID - " + ID);
		id = ID;

		InetAddress e;
		try {
			e = InetAddress.getLocalHost();
			String eu = id + "@" + e.getHostAddress().toString() + "@" + ds.getPorta();
			System.out.println(eu);
			lista.add(eu);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			System.out.println("Erro em gerar o ip local");
		}
		return ID;
	}
	
	public ConexaoDt getDt(){
		return ds;
	}
	
	public ArrayList<String> getLista(){
		return lista;
	}
	
	public int getID(){
		return id;
	}

//	public int getPortaUDP() {
//		return ds.getPorta();
//	}

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

				System.out.println("Ouvi alguem chamando!!");
				if (y[0].equals("ARPR") && !y[1].equals(id + "")) {
					String ele = y[1] + "@" + pack.getAddress().toString().substring(1) + "@" + y[2];
					System.out.println("ELE - " + ele);
					int cont = 0;
					for (String string : lista) {
						String[] aux = string.split("@");
						if (y[1].equals(aux[0])) {
							cont++;
						}
					}
					if (cont == 0) {
						lista.add(ele);
					}
					System.out.println("Ele ja ta na lista");
					System.out.println("Eu to aqui tmb");
					enviar("ARPS@" + id + "@" + ds.getPorta());
				} else if (y[0].equals("ARPS") && !y[1].equals(id + "")) {
					String ele = y[1] + "@" + pack.getAddress().toString().substring(1) + "@" + y[2];
					System.out.println("ELE - " + ele);
					int cont = 0;
					for (String string : lista) {
						String[] aux = string.split("@");
						if (y[1].equals(aux[0])) {
							cont++;
						}
					}
					if (cont == 0) {
						lista.add(ele);
					}
					System.out.println("Ele ja ta na lista");
					System.out.println("Eu to aqui tmb");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Erro na escuta");
			}

			System.out.println("Lista de relogios");
			for (String string : lista) {
				System.out.println(string);
			}

		}
	}

}
