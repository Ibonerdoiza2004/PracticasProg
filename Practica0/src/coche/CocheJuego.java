package coche;

public class CocheJuego extends coche{
	
	protected JLabelCoche lCoche = new JLabelCoche();

	@Override
	public void setPosX(double posX) {
		super.setPosX(posX);
		lCoche.setBounds((int)posX, (int)posY, 100, 100);
	}



	@Override
	public void setPosY(double posY) {
		super.setPosY(posY);
		lCoche.setBounds((int)posX, (int)posY, 100, 100);
	}



	@Override
	public void mueve(double tiempoDeMovimiento) {
		super.mueve(tiempoDeMovimiento);
		lCoche.setBounds((int)posX, (int)posY, 100, 100);
	}
	public String toString() {
		return super.toString();
	}
	
}
