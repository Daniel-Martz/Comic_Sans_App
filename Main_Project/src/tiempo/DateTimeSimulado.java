package tiempo;

public class DateTimeSimulado {

	private final int año;
	private final int mes;
	private final int dia;
	private final int hora;
	private final int minuto;
	private final int segundo;

	public DateTimeSimulado() {
		año = TiempoSimulado.getAño();
		mes = TiempoSimulado.getMes();
		dia = TiempoSimulado.getDia();
		hora = TiempoSimulado.getHora();
		minuto = TiempoSimulado.getMinuto();
		segundo = TiempoSimulado.getSegundo();
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
