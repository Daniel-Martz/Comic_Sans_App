package tiempo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase refactorizada siguiendo el patrón Singleton.
 * Permite gestionar un tiempo simulado con velocidad variable.
 */
public class TiempoSimulado {
    

    private static TiempoSimulado instance;
    private static long MS_POR_DIA = 24L * 60 * 60 * 1000;
    
    private long milisegundosSimulados = 0;
    private long ultimoTiempoReal;
    private double velocidad;
    private int diasPorMes;
    private int mesesPorAño;

    private TiempoSimulado() {
        this.velocidad = 1;
        this.diasPorMes = 30;
        this.mesesPorAño = 12;
        this.ultimoTiempoReal = System.currentTimeMillis();
    }
    
    public static TiempoSimulado getInstance() {
        if (instance == null) {
            instance = new TiempoSimulado();
        }
        return instance;
    }

    public void iniciar(double velocidadInicial, int diasMes, int mesesAño) {
        this.velocidad = velocidadInicial;
        this.diasPorMes = diasMes;
        this.mesesPorAño = mesesAño;
        this.ultimoTiempoReal = System.currentTimeMillis();
    }

    public void actualizar() {
        long ahora = System.currentTimeMillis();
        long diferenciaReal = ahora - ultimoTiempoReal;
        this.milisegundosSimulados += (long) (diferenciaReal * velocidad);
        this.ultimoTiempoReal = ahora;
    }
    
	public void avanzarDias(int dias) {
		if (dias < 0) {
	        throw new IllegalArgumentException("No se puede avanzar un número negativo de días.");
	    }
		actualizar();
		this.milisegundosSimulados += (long) dias * MS_POR_DIA;
	}

    // --- Getters y Setters de instancia ---

    public void setVelocidad(double nuevaVelocidad) {
        this.velocidad = nuevaVelocidad;
    }

    private long getSegundosTotales() {
        actualizar();
        return milisegundosSimulados / 1000;
    }

    public int getSegundo() { return (int) (getSegundosTotales() % 60); }
    public int getMinuto() { return (int) ((getSegundosTotales() / 60) % 60); }
    public int getHora() { return (int) ((getSegundosTotales() / 3600) % 24); }

    public int getDia() {
        long diasTotales = (getSegundosTotales() / 86400); 
        return (int) ((diasTotales % diasPorMes)+ 1); //El +1 es para que empiece en 1 y no en 0
    }

    public int getMes() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = (diasTotales / diasPorMes);
        return (int) ((mesesTotales % mesesPorAño)+1);//El +1 es para que empiece en 1 y no en 0
    }

    public int getAño() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int) ((mesesTotales / mesesPorAño)+1);//El +1 es para que empiece en 1 y no en 0
    }

    /**
	 * @return the mesesPorAño
	 */
	public int getMesesPorAño() {
		return mesesPorAño;
	}
	
	/**
	 * @return the diasPorMes
	 */
	public int getDiasPorMes() {
		return diasPorMes;
	}
	
	public long dateTimeASegundos(DateTimeSimulado dt) {
	    long totalDias = (long)(dt.getAño() - 1) * mesesPorAño * diasPorMes
	                   + (long)(dt.getMes() - 1) * diasPorMes
	                   + (dt.getDia() - 1);

	    return totalDias * 86400
	         + dt.getHora() * 3600
	         + dt.getMinuto() * 60
	         + dt.getSegundo();
	}
	
	@Override
    public String toString() {
        return "Año: " + getAño() + "\nMes: " + getMes() + "\nDia: " + getDia() + 
               "\nHora: " + getHora() + "\nMinuto: " + getMinuto() + "\nSegundo: " + getSegundo();
    }
	
	//Metodo para fraccionar dos fechas en sus meses
	public List<String> periodoAMeses(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin) {
	    List<String> meses = new ArrayList<>();
	    
	    if (periodoInicio == null || periodoFin == null) {
	        return meses;
	    }
	    
	    int mes = periodoInicio.getMes();
	    int año = periodoInicio.getAño();
	    
	    while (año < periodoFin.getAño() || (año == periodoFin.getAño() && mes <= periodoFin.getMes())) {
	        meses.add(mes + "/" + año);
	        mes++;
	        if (mes > mesesPorAño) {
	            mes = 1;
	            año++;
	        }
	    }
	    return meses;
	}
}