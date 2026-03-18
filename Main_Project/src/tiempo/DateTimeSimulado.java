package tiempo;

public class DateTimeSimulado {

	private final int año;
	private final int mes;
	private final int dia;
	private final int hora;
	private final int minuto;
	private final int segundo;

	public DateTimeSimulado() {
		TiempoSimulado Instancia = TiempoSimulado.getInstance();
		año = Instancia.getAño();
		mes = Instancia.getMes();
		dia = Instancia.getDia();
		hora = Instancia.getHora();
		minuto = Instancia.getMinuto();
		segundo = Instancia.getSegundo();
	}

	/**
	 * @return el año
	 */
	public int getAnio() {
		return año;
	}

	/**
	 * @return el mes
	 */
	public int getMes() {
		return mes;
	}

	/**
	 * @return el dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * @return la hora
	 */
	public int getHora() {
		return hora;
	}

	/**
	 * @return el minuto
	 */
	public int getMinuto() {
		return minuto;
	}

	/**
	 * @return el segundo
	 */
	public int getSegundo() {
		return segundo;
	}

	@Override
	public String toString() {
		return "Año: " + año + "\nMes: " + mes + "\nDia: " + dia + "\nHora: " + hora + "\nMinuto: " + minuto
				+ "\nSegundo: " + segundo;
	}
}
