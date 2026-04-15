package modelo.tiempo;
import java.io.*;

/**
 * Representa un instante de tiempo (fecha y hora) dentro de la simulación.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class DateTimeSimulado implements Comparable<DateTimeSimulado>, Serializable{

	private static final long serialVersionUID = 1L;

	/** El año de la fecha simulada. */
	private final int año;
	
	/** El mes de la fecha simulada. */
	private final int mes;
	
	/** El día de la fecha simulada. */
	private final int dia;
	
	/** La hora (formato 24h). */
	private final int hora;
	
	/** El minuto de la hora. */
	private final int minuto;
	
	/** El segundo del minuto. */
	private final int segundo;
	

	/**
	 * Instancia un nuevo objeto DateTimeSimulado capturando la fecha y hora 
	 * actuales de la aplicación a través del Singleton TiempoSimulado.
	 */
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
	 * Instancia un nuevo DateTimeSimulado especificando cada campo temporal.
	 * Se asegura de que, si la fecha es la de hoy, la hora introducida no 
	 * sea anterior al tiempo actual de la simulación.
	 *
	 * @param año el año
	 * @param mes el mes
	 * @param dia el día
	 * @param hora la hora
	 * @param minuto el minuto
	 * @param segundo el segundo
	 * @throws IllegalArgumentException si la hora, minuto o segundo especificados ya han pasado en el día de hoy.
	 */
	public DateTimeSimulado(int año, int mes, int dia, int hora, int minuto, int segundo) {
	    TiempoSimulado instancia = TiempoSimulado.getInstance();

//	    if (año < instancia.getAño()) {
//	        throw new IllegalArgumentException("El año no puede ser menor al actual (" + instancia.getAño() + ")");
//	    }
//
//	    if (año == instancia.getAño() && mes < instancia.getMes()) {
//	        throw new IllegalArgumentException("El mes no puede ser menor al actual en el mismo año");
//	    }
//
//	    if (año == instancia.getAño() && mes == instancia.getMes() && dia < instancia.getDia()) {
//	        throw new IllegalArgumentException("El día no puede ser anterior al actual");
//	    }
	    
//	    if (año == instancia.getAño() && mes == instancia.getMes() && dia == instancia.getDia()) {
//	        if (hora < instancia.getHora()) {
//	            throw new IllegalArgumentException("La hora ya ha pasado");
//	        }
//	        if (hora == instancia.getHora() && minuto < instancia.getMinuto()) {
//	            throw new IllegalArgumentException("El minuto ya ha pasado");
//	        }
//	        if (hora == instancia.getHora() && minuto == instancia.getMinuto() && segundo < instancia.getSegundo()) {
//	            throw new IllegalArgumentException("El segundo ya ha pasado");
//	        }
//	    }

	    // Si todas las comprobaciones pasan, asignamos los valores
	    this.año = año;
	    this.mes = mes;
	    this.dia = dia;
	    this.hora = hora;
	    this.minuto = minuto;
	    this.segundo = segundo;
	}
	
	/**
	 * Método estático que genera una nueva fecha sumando un número de días 
	 * a la fecha actual del sistema.
	 *
	 * @param dias número de días a avanzar.
	 * @return un nuevo objeto DateTimeSimulado con la fecha futura.
	 */
	public static DateTimeSimulado DateTimeDiasDespues(int dias) {
		DateTimeSimulado DateTime = new DateTimeSimulado();
		
		int diaPorMes = TiempoSimulado.getInstance().getDiasPorMes();
		int mesesPorAño = TiempoSimulado.getInstance().getMesesPorAño();
		
		int año = DateTime.getAño() + ((DateTime.getMes() + (DateTime.getDia() + dias)/diaPorMes)/mesesPorAño);
		int mes = (DateTime.getMes()-1 + (DateTime.getDia()-1 + dias)/diaPorMes)%mesesPorAño + 1;
		int dia = (DateTime.getDia()-1 + dias)%diaPorMes + 1;
		
				
		return new DateTimeSimulado(año, mes, dia, DateTime.getHora(), DateTime.getMinuto(), DateTime.getSegundo());
	}

	/**
	 * Devuelve una nueva fecha correspondiente a sumar un número indicado de días 
	 * a la fecha desde la que se invoca el método.
	 * @param dias número de días a sumar a esta fecha.
	 * @return un nuevo objeto DateTimeSimulado con la fecha calculada.
	 */
	public DateTimeSimulado diasDespuesDeFecha(int dias) {
		DateTimeSimulado DateTime = this;
		
		int diaPorMes = TiempoSimulado.getInstance().getDiasPorMes();
		int mesesPorAño = TiempoSimulado.getInstance().getMesesPorAño();
		
		int año = DateTime.getAño() + ((DateTime.getMes() + (DateTime.getDia() + dias)/diaPorMes)/mesesPorAño);
		int mes = (DateTime.getMes()-1 + (DateTime.getDia()-1 + dias)/diaPorMes)%mesesPorAño + 1;
		int dia = (DateTime.getDia()-1 + dias)%diaPorMes + 1;
		
				
		return new DateTimeSimulado(año, mes, dia, DateTime.getHora(), DateTime.getMinuto(), DateTime.getSegundo());
	}
	
	/**
	 * Obtiene el año.
	 *
	 * @return el año
	 */
	public int getAño() {
		return año;
	}

	/**
	 * Obtiene el mes.
	 *
	 * @return el mes
	 */
	public int getMes() {
		return mes;
	}

	/**
	 * Obtiene el día.
	 *
	 * @return el dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * Obtiene la hora.
	 *
	 * @return la hora
	 */
	public int getHora() {
		return hora;
	}

	/**
	 * Obtiene el minuto.
	 *
	 * @return el minuto
	 */
	public int getMinuto() {
		return minuto;
	}

	/**
	 * Obtiene el segundo.
	 *
	 * @return el segundo
	 */
	public int getSegundo() {
		return segundo;
	}

	/**
	 * Convierte la fecha actual a su equivalente absoluto en segundos.
	 * @return el total de segundos transcurridos en la simulación para esta fecha.
	 */
	public long dateTimeEnSegundos() {
		return TiempoSimulado.getInstance().dateTimeASegundos(this);
	}
	
	/**
	 * Método reservado para avanzar la fecha internamente (actualmente sin implementación).
	 * @param dias el número de días a avanzar.
	 */
	public void avanzarDias(int dias) {
		
	}
	
	/**
	 * Devuelve una representación textual detallada del objeto.
	 *
	 * @return cadena con el formato "Año: X-Mes: X-Dia: X-Hora: X-Minuto: X-Segundo: X"
	 */
	@Override
	public String toString() {
		return "Año: " + año + "-Mes: " + mes + "-Dia: " + dia + "-Hora: " + hora + "-Minuto: " + minuto
				+ "-Segundo: " + segundo;
	}
	
	/**
	 * Devuelve una representación textual corta de la fecha.
	 * @return cadena con el formato "dia/mes/año".
	 */
	public String toStringFecha() {
	    return dia + "/" + mes + "/" + año;
	}
 
	/**
	 * Compara cronológicamente este objeto con otro DateTimeSimulado.
	 * @param tiempoComparado la fecha con la que se quiere comparar.
	 * @return 0 si son exactamente iguales, -1 si esta fecha es anterior a la introducida, 
	 * 1 si esta fecha es posterior a la introducida.
	 */
	public int compareTo(DateTimeSimulado tiempoComparado){
		int ret;
		ret = Integer.compare(this.getAño(), tiempoComparado.getAño());
		if(ret != 0){
			return ret;
		} else{
			ret = Integer.compare(this.getMes(), tiempoComparado.getMes());
			if(ret != 0){
				return ret;
			} else{
				ret = Integer.compare(this.getDia(), tiempoComparado.getDia());
				if(ret != 0){
					return ret;
				} else{
					ret = Integer.compare(this.getHora(), tiempoComparado.getHora());
					if(ret != 0){
						return ret;
					} else{
						ret = Integer.compare(this.getMinuto(), tiempoComparado.getMinuto());
						if(ret != 0){
							return ret;
						} else{
							ret = Integer.compare(this.getSegundo(), tiempoComparado.getSegundo());
							return ret;
						}
					}
				}
			}
		}
	}
}
