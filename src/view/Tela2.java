package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import util.Conexao;
import util.Relogio;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;

import controller.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

public class Tela2 {

	private JFrame frame;
	private JTextField hr, min, seg, drift;
	private Relogio rel;
	private Conexao con;
	private Controller control;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela2 window = new Tela2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tela2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 489, 287);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel relogio = new JLabel("New label");
		JLabel lblCoord = new JLabel("Coord - ");
		relogio.setHorizontalAlignment(SwingConstants.CENTER);
		relogio.setFont(new Font("Tahoma", Font.PLAIN, 62));
		relogio.setBounds(67, 24, 366, 75);
		frame.getContentPane().add(relogio);
		control = new Controller(relogio, lblCoord);

		hr = new JTextField();
		hr.setBounds(53, 110, 89, 20);
		frame.getContentPane().add(hr);
		hr.setColumns(10);

		min = new JTextField();
		min.setBounds(211, 110, 89, 20);
		frame.getContentPane().add(min);
		min.setColumns(10);

		seg = new JTextField();
		seg.setBounds(380, 110, 86, 20);
		frame.getContentPane().add(seg);
		seg.setColumns(10);

		ButtonHandller btmH = new ButtonHandller();

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(btmH);
		btnStart.setBounds(267, 183, 89, 23);
		frame.getContentPane().add(btnStart);

		JLabel lblHora = new JLabel("Hora - ");
		lblHora.setBounds(10, 113, 46, 14);
		frame.getContentPane().add(lblHora);

		JLabel lblMinuto = new JLabel("Minuto - ");
		lblMinuto.setBounds(152, 113, 52, 14);
		frame.getContentPane().add(lblMinuto);

		JLabel lblSegundo = new JLabel("Segundo - ");
		lblSegundo.setBounds(310, 113, 60, 14);
		frame.getContentPane().add(lblSegundo);

		drift = new JTextField();
		drift.setBounds(53, 184, 86, 20);
		frame.getContentPane().add(drift);
		drift.setColumns(10);

		JLabel lblDtift = new JLabel("Dtift -");
		lblDtift.setBounds(10, 187, 46, 14);
		frame.getContentPane().add(lblDtift);

		JButton btnSetDrift = new JButton("Set Drift");
		btnSetDrift.addActionListener(btmH);
		btnSetDrift.setBounds(152, 183, 89, 23);
		frame.getContentPane().add(btnSetDrift);

		JButton btnSetHora = new JButton("Set Hora");
		btnSetHora.addActionListener(btmH);
		btnSetHora.setBounds(53, 133, 89, 23);
		frame.getContentPane().add(btnSetHora);

		JButton btnSetMinuto = new JButton("Set Min");
		btnSetMinuto.addActionListener(btmH);
		btnSetMinuto.setBounds(211, 133, 89, 23);
		frame.getContentPane().add(btnSetMinuto);

		JButton btnSetSegundo = new JButton("Set Seg");
		btnSetSegundo.addActionListener(btmH);
		btnSetSegundo.setBounds(377, 133, 89, 23);
		frame.getContentPane().add(btnSetSegundo);

		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(btmH);
		btnConectar.setBounds(377, 183, 89, 23);
		frame.getContentPane().add(btnConectar);

		lblCoord.setBounds(211, 223, 46, 14);
		frame.getContentPane().add(lblCoord);
	}

	private class ButtonHandller implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("Start".equals(e.getActionCommand())) {
				control.start();
			} else if ("Set Drift".equals(e.getActionCommand())) {
				if ("".equals(drift.getText())) {
					JOptionPane.showMessageDialog(null, "Insira um valor de Drift!!");
				} else {
					long aux = Integer.parseInt(drift.getText()) * 1000;
					control.setDrift(aux);
				}
			} else if ("Set Hora".equals(e.getActionCommand())) {
				if ("".equals(hr.getText())) {
					JOptionPane.showMessageDialog(null, "Insira um valor da Hora!!");
				} else {
					int aux = Integer.parseInt(hr.getText());
					if (aux <= 23 && aux >= 0) {
						control.setHr(aux);
					}else{
						JOptionPane.showMessageDialog(null, "Inserir um valor válido!!");
					}
				}
			} else if ("Set Min".equals(e.getActionCommand())) {
				if ("".equals(min.getText())) {
					JOptionPane.showMessageDialog(null, "Insira um valor de Minutos!!");
				} else {
					int aux = Integer.parseInt(min.getText());
					if (aux >= 0 && aux <= 59  ) {
						control.setMin(aux);
					}else{
						JOptionPane.showMessageDialog(null, "Inserir um valor válido!!");
					}
					
				}

			} else if ("Set Seg".equals(e.getActionCommand())) {
				if ("".equals(seg.getText())) {
					JOptionPane.showMessageDialog(null, "Insira um valor de Segundos!!");
				} else {
					int aux = Integer.parseInt(seg.getText());
					if (aux <= 59 && aux >= 0) {
						control.setSeg(aux);
					}else{
						JOptionPane.showMessageDialog(null, "Inserir um valor válido!!");
					}
					
				}
			} else if ("Conectar".equals(e.getActionCommand())) {
				try {
					control.conectar();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println("Erro no envio");
				}
			}

		}

	}

	private void main() {
		// TODO Auto-generated method stub
		Tela2 t = new Tela2();
	}
}