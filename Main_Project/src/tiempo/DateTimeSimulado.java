package tiempo;

/**
 * The Class DateTimeSimulado.
 */
public class DateTimeSimulado {

	/** El año. */
	private final int año;
	
	/** El mes. */
	private final int mes;
	
	/** El dia. */
	private final int dia;
	
	/** El hora. */
	private final int hora;
	
	/** El minuto. */
	private final int minuto;
	
	/** El segundo. */
	private final int segundo;
	

	/**
	 * Instancia un nuevo date time simulado, con la fecha actual de la apliación.
	 */
	public DateTimeSimulado() {
		TiempoSimulado Instancia = TiempoSimulado.getInstance();
		año = Instancia.getAño();
		mes = Instancia.getMes();
		dia = Instancia.getAño();
		hora = Instancia.getAño();
		minuto = Instancia.getAño();
		segundo = Instancia.getAño();
	}

	/**
	 * instancia un nuevo date time simulado, pero campo por campo, asegurandose de que los campos 
	 * sean válidos (no sean menores que el tiempo actual)
	 *
	 * @param año el año
	 * @param mes el mes
	 * @param dia el dia
	 * @param hora la hora
	 * @param minuto el minuto
	 * @param segundo el segundo
	 */
	public DateTimeSimulado(int año, int mes, int dia, int hora, int minuto, int segundo) {
	    TiempoSimulado instancia = TiempoSimulado.getInstance();

	    if (año < instancia.getAño()) {
	        throw new IllegalArgumentException("El año no puede ser menor al actual (" + instancia.getAño() + ")");
	    }

	    if (año == instancia.getAño() && mes < instancia.getMes()) {
	        throw new IllegalArgumentException("El mes no puede ser menor al actual en el mismo año");
	    }

	    if (año == instancia.getAño() && mes == instancia.getMes() && dia < instancia.getDia()) {
	        throw new IllegalArgumentException("El día no puede ser anterior al actual");
	    }
	    
	    if (año == instancia.getAño() && mes == instancia.getMes() && dia == instancia.getDia()) {
	        if (hora < instancia.getHora()) {
	            throw new IllegalArgumentException("La hora ya ha pasado");
	        }
	        if (hora == instancia.getHora() && minuto < instancia.getMinuto()) {
	            throw new IllegalArgumentException("El minuto ya ha pasado");
	        }
	        if (hora == instancia.getHora() && minuto == instancia.getMinuto() && segundo < instancia.getSegundo()) {
	            throw new IllegalArgumentException("El segundo ya ha pasado");
	        }
	    }

	    // Si todas las comprobaciones pasan, asignamos los valores
	    this.año = año;
	    this.mes = mes;
	    this.dia = dia;
	    this.hora = hora;
	    this.minuto = minuto;
	    this.segundo = segundo;
	}
	
	/**
	 * Date time dias despues.
	 *
	 * @param dias the dias
	 * @return the date time simulado
	 */
	static DateTimeSimulado DateTimeDiasDespues(int dias) {
		DateTimeSimulado DateTime = new DateTimeSimulado();
		
		int diaPorMes = TiempoSimulado.getInstance().getDiasPorMes();
		int mesesPorAño = TiempoSimulado.getInstance().getMesesPorAño();
		
		int año = DateTime.getAño() + ((DateTime.getMes() + (DateTime.getDia() + dias)/diaPorMes)/mesesPorAño);
		int mes = (DateTime.getMes()-1 + (DateTime.getDia()-1 + dias)/diaPorMes)%mesesPorAño + 1;
		int dia = (DateTime.getDia()-1 + dias)%diaPorMes + 1;
		
				
		return new DateTimeSimulado(año, mes, dia, DateTime.getHora(), DateTime.getMinuto(), DateTime.getSegundo());
	}
	
	/**
	 * Gets the año.
	 *
	 * @return el año
	 */
	public int getAño() {
		return año;
	}

	/**
	 * Gets the mes.
	 *
	 * @return el mes
	 */
	public int getMes() {
		return mes;
	}

	/**
	 * Gets the dia.
	 *
	 * @return el dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * Gets the hora.
	 *
	 * @return la hora
	 */
	public int getHora() {
		return hora;
	}

	/**
	 * Gets the minuto.
	 *
	 * @return el minuto
	 */
	public int getMinuto() {
		return minuto;
	}

	/**
	 * Gets the segundo.
	 *
	 * @return el segundo
	 */
	public int getSegundo() {
		return segundo;
	}

	public long dateTimeEnSegundos() {
		return TiempoSimulado.getInstance().dateTimeASegundos(this);
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Año: " + año + "\nMes: " + mes + "\nDia: " + dia + "\nHora: " + hora + "\nMinuto: " + minuto
				+ "\nSegundo: " + segundo;
	}
}
