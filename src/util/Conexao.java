package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Conexao extends Thread {

    private MulticastSocket ms;
    private int porta;
    private String grupo;
    private int id;
    private volatile static boolean coord;
    private volatile static int idCoord;
    private Relogio rel;
    private Referencia ref;

<<<<<<< HEAD
	public Conexao(Relogio relo) {
		this.grupo = "230.0.0.0";
		porta = 50000;
		rel = relo;
		coord = false;
		try {
			ms = new MulticastSocket(porta);
			ms.joinGroup(InetAddress.getByName(grupo));
			//ms.setTimeToLive(10);
			System.out.println("Multicast com grupo - "+ InetAddress.getByName(grupo) +" e porta - "+ porta);
			id = geraID();
			System.out.println("Grupo criado com sucesso!!");
		} catch (IOException e) {
			System.out.println("Falha na cria��o do grupo Multicast");
		}
	}
=======
    public Conexao(Relogio relo) {
        this.grupo = "230.0.0.10";
        porta = 20141;
        rel = relo;
        coord = false;
        try {
            ms = new MulticastSocket(porta);
            ms.joinGroup(InetAddress.getByName(grupo));           
            System.out.println("Multicast com grupo - " + InetAddress.getByName(grupo) + " e porta - " + porta);
            id = geraID();            
        } catch (IOException e) {
            System.out.println("Falha na cria��o do grupo Multicast");
        }
    }
>>>>>>> 095e63bcc36b2263398c737fefd7043f9349c90a

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

<<<<<<< HEAD
	public String escuta(){
		byte buf[] = new byte[1024];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			ms.receive(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String x = new String(pack.getData());
		System.out.println("Recebi de " + pack.getAddress() + ":" + pack.getPort() + " esta mensagem -" + x);
		return x;
	}
	

	
	@Override
	public void run() {
		while (true) {
			try {
				String x = escuta();
=======
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
>>>>>>> 095e63bcc36b2263398c737fefd7043f9349c90a

                if (y[0].equals("ARP")) {
                    System.out.println("Estado do coord - " + coord);
                    if (coord == true) {
                        System.out.println("Eu to aqui e sou a refer�ncia");
                        enviar("EU@" + id + "@COORD");
                    } else {
                        System.out.println("Eu to aqi !!");
                        enviar(("EU@" + id));
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

<<<<<<< HEAD
				if (y[0].equals("ARP")) {
					System.out.println("Estado do coord - " + coord);
					if (coord == true) {
						System.out.println("Eu to aqui e sou a refer�ncia");
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
=======
                } 
                
                else if (y[0].equals("ST") && Integer.parseInt(y[1]) == id) {
                    String i = rel.getHora();
                    System.out.println("Mando sim brother - ID - " + id + " Minha hora � essa: " + i
                            + " Ouviu pivas - ID - " + y[2]);
                    String msg = "MT@" + i + "@" + y[2] + "@" + y[1];
                    enviar(msg);
                } 
                
                else if (y[0].equals("MT") && Integer.parseInt(y[2]) == id) {
                    String[] aux = y[1].split(":");
                    System.out.println("To comparando essa hora - " + y[1]);
                    int resp = rel.compareTo(y[1]);
                    System.out.println("Resposta da compara��o - " + resp);
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
                } 
                
                else if (y[0].equals("COORD")) {
                    System.out.println("Virei Coordenador");
                    coord = true;
                    ref = new Referencia(rel);
                    // ref.enviar("Teste");
                    ref.atualizarRel(coord);
                } 
                
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Erro na escuta");
            }
>>>>>>> 095e63bcc36b2263398c737fefd7043f9349c90a

            System.err.println("Rapaz cab� a thread");
        }
    }

}

