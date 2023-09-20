package coche;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaJuego extends JFrame {
	JPanel pPrincipal = new JPanel();
	JPanel pBotonera = new JPanel();
	JButton acelerar = new JButton();
	JButton frenar = new JButton();
	JButton izquierda = new JButton();
	JButton derecha = new JButton();
	public VentanaJuego() throws HeadlessException {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(800, 600);
		setVisible(true);
		acelerar.setText("Acelerar");
		acelerar.setFocusable(false);
		frenar.setText("Frenar");
		frenar.setFocusable(false);
		izquierda.setText("Girar izq.");
		izquierda.setFocusable(false);
		derecha.setText("Girar der.");
		derecha.setFocusable(false);
		pPrincipal.setLayout(null);
		pBotonera.add(acelerar);
		pBotonera.add(frenar);
		pBotonera.add(izquierda);
		pBotonera.add(derecha);
		
		add(pPrincipal, BorderLayout.CENTER);
		add(pBotonera, BorderLayout.SOUTH);
	}
	public static void main(String[] args) {
		VentanaJuego ventana = new VentanaJuego();
		CocheJuego coche = new CocheJuego();
		ventana.pPrincipal.add(coche.lCoche);
		ventana.acelerar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				coche.acelera(5);
				System.out.println("Velocidad: "+coche.getMiVelocidad());
				
			}
		});
		ventana.frenar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				coche.acelera(-5);
				System.out.println("Velocidad: "+coche.getMiVelocidad());
			}
		});
		ventana.izquierda.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				coche.gira(-10);
				if (coche.miDireccionActual<-180) {
					coche.miDireccionActual = coche.miDireccionActual + 360;
				}
				coche.lCoche.setRotacion((int)coche.miDireccionActual);
				System.out.println("Angulo: "+coche.getMiDireccionActual());
			}
		});
		ventana.derecha.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				coche.gira(10);
				if (coche.miDireccionActual>180) {
					coche.miDireccionActual = coche.miDireccionActual - 360;
				}
				coche.lCoche.setRotacion((int)coche.miDireccionActual);
				System.out.println("Angulo: " + coche.getMiDireccionActual());
			}
		});
		ventana.setFocusable(true);
		ventana.addKeyListener(new KeyAdapter() {
			
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_UP) {
					coche.acelera(5);
					System.out.println("Velocidad: "+coche.getMiVelocidad());
				}
				if(e.getKeyCode()==KeyEvent.VK_DOWN) {
					coche.acelera(-5);
					System.out.println("Velocidad: "+coche.getMiVelocidad());
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT) {
					coche.gira(-10);
					if (coche.miDireccionActual<-180) {
						coche.miDireccionActual = coche.miDireccionActual + 360;
					}
					coche.lCoche.setRotacion((int)coche.miDireccionActual);
					System.out.println("Angulo: "+coche.getMiDireccionActual());
				}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
					coche.gira(10);
					if (coche.miDireccionActual>180) {
						coche.miDireccionActual = coche.miDireccionActual - 360;
					}
					coche.lCoche.setRotacion((int)coche.miDireccionActual);
					System.out.println("Angulo: " + coche.getMiDireccionActual());
				}
				
			}
		});
		Thread hiloMueve = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(ventana.isVisible()) {
					double alto = ventana.pPrincipal.getHeight();
					double ancho = ventana.pPrincipal.getWidth();
					if (coche.posY<0 || coche.posY + 100 > alto) {    //Esta parte calcula el nuevo ángulo después del rebote
						coche.miDireccionActual = -coche.miDireccionActual;
						coche.lCoche.setRotacion((int)coche.miDireccionActual);
						System.out.println("Angulo: "+coche.getMiDireccionActual());
					}
					if (coche.posX<0 || coche.posX+100 > ancho) {
						if(coche.miDireccionActual<0) {
							coche.miDireccionActual = -180-coche.miDireccionActual;
						}else {
							coche.miDireccionActual = 180-coche.miDireccionActual;
						}
						coche.lCoche.setRotacion((int)coche.miDireccionActual);
						System.out.println("Angulo: "+coche.getMiDireccionActual());
					}
					coche.mueve(0.04);
					ventana.repaint();
					try {
	           			Thread.sleep(40);
	           		} catch (InterruptedException e) {
	           			System.out.println("Error");
	           		}
				}
				
			}
		});
		hiloMueve.start();
		
	}
	
	
}
