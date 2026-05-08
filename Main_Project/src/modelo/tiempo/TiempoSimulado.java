package modelo.tiempo;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Clase refactorizada siguiendo el patrón Singleton.
 * Permite gestionar un tiempo simulado con velocidad variable y
 * soporta persistencia mediante serialización personalizada.
 *
 * @author Matteo Artuñedo
 * @author Rodrigo Diaz
 * @author Daniel Martinez
 * @version 1.2
 */
public class TiempoSimulado implements Serializable {

    /** Identificador único para la serialización de la clase. */
    private static final long serialVersionUID = 1L;

    /** Instancia única de la clase (Singleton). */
    private static TiempoSimulado instance;

    /** Constante que define los milisegundos que tiene un día real. */
    private static long MS_POR_DIA = 24L * 60 * 60 * 1000;

    /** Acumulador de milisegundos transcurridos en la simulación. */
    private long milisegundosSimulados = 0;

    /** Almacena la última vez en tiempo real que se actualizó el sistema. */
    private long ultimoTiempoReal;

    /** Factor de velocidad de la simulación (1.0 equivale a tiempo real). */
    private double velocidad;

    /** Número de días que componen un mes en el sistema. */
    private int diasPorMes;

    /** Número de meses que componen un año en el sistema. */
    private int mesesPorAño;

    /**
     * Constructor privado para evitar instanciación externa.
     * Inicializa los valores por defecto del calendario y la velocidad.
     */
    private TiempoSimulado() {
        this.velocidad = 1;
        this.diasPorMes = 30;
        this.mesesPorAño = 12;
        this.ultimoTiempoReal = System.currentTimeMillis();
    }

    /**
     * Obtiene la instancia única de TiempoSimulado.
     *
     * @return La instancia global.
     */
    public static TiempoSimulado getInstance() {
        if (instance == null) {
            instance = new TiempoSimulado();
        }
        return instance;
    }

    /**
     * Configura los parámetros iniciales de la simulación temporal.
     *
     * @param velocidadInicial multiplicador de velocidad del tiempo
     * @param diasMes cantidad de días por mes
     * @param mesesAño cantidad de meses por año
     */
    public void iniciar(double velocidadInicial, int diasMes, int mesesAño) {
        this.velocidad = velocidadInicial;
        this.diasPorMes = diasMes;
        this.mesesPorAño = mesesAño;
        this.ultimoTiempoReal = System.currentTimeMillis();
    }

    /**
     * Actualiza el contador de milisegundos simulados basándose en el tiempo real transcurrido.
     */
    public void actualizar() {
        long ahora = System.currentTimeMillis();
        long diferenciaReal = ahora - ultimoTiempoReal;
        this.milisegundosSimulados += (long) (diferenciaReal * velocidad);
        this.ultimoTiempoReal = ahora;
    }

    /**
     * Adelanta el reloj de la simulación un número específico de días.
     *
     * @param dias número de días a avanzar
     * @throws IllegalArgumentException si el número de días es negativo
     */
    public void avanzarDias(int dias) {
        if (dias < 0) {
            throw new IllegalArgumentException("No se puede avanzar un número negativo de días.");
        }
        actualizar();
        this.milisegundosSimulados += (long) dias * MS_POR_DIA;
    }

    /**
     * Obtiene el factor de velocidad actual de la simulación.
     *
     * @return velocidad (1.0 equivale a tiempo real)
     */
    public double getVelocidad() {
        return this.velocidad;
    }

    /**
     * Cambia la velocidad de transcurso del tiempo simulado.
     *
     * @param nuevaVelocidad nuevo factor de velocidad
     */
    public void setVelocidad(double nuevaVelocidad) {
        this.velocidad = nuevaVelocidad;
    }

    /**
     * Calcula el total de segundos transcurridos en la simulación.
     *
     * @return segundos totales acumulados
     */
    private long getSegundosTotales() {
        actualizar();
        return milisegundosSimulados / 1000;
    }

    /**
     * Metodo para conseguir el segundo actual
     * @return El segundo actual del minuto (0-59).
     */
    public int getSegundo() {
        return (int) (getSegundosTotales() % 60);
    }

    /**
     * Metodo para conseguir el minuto actual
     * @return El minuto actual de la hora (0-59).
     */
    public int getMinuto() {
        return (int) ((getSegundosTotales() / 60) % 60);
    }

    /**
     * Metodo para conseguir la hora actual
     * @return La hora actual del día (0-23).
     */
    public int getHora() {
        return (int) ((getSegundosTotales() / 3600) % 24);
    }

    /**
     * Metodo para conseguir el dia acutal
     * @return El día actual del mes.
     */
    public int getDia() {
        long diasTotales = getSegundosTotales() / 86400;
        return (int) ((diasTotales % diasPorMes) + 1);
    }

    /**
     * Metodo para conseguir el mes actual
     * @return El mes actual del año.
     */
    public int getMes() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int) ((mesesTotales % mesesPorAño) + 1);
    }

    /**
     * Metodo para conseguir el año actual
     * @return El año actual 
     */
    public int getAño() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int) ((mesesTotales / mesesPorAño) + 1);
    }

    /**
     * Metodo para conseguir los años
     * @return número de meses por año
     */
    public int getMesesPorAño() {
        return mesesPorAño;
    }

    /**
     * Metodo para conseguir los meses
     * @return número de días por mes
     */
    public int getDiasPorMes() {
        return diasPorMes;
    }

    /**
     * Convierte un objeto DateTimeSimulado a segundos absolutos.
     *
     * @param dt fecha simulada
     * @return segundos transcurridos desde el inicio
     */
    public long dateTimeASegundos(DateTimeSimulado dt) {
        long totalDias = (long) (dt.getAño() - 1) * mesesPorAño * diasPorMes
                + (long) (dt.getMes() - 1) * diasPorMes
                + (dt.getDia() - 1);

        return totalDias * 86400
                + dt.getHora() * 3600
                + dt.getMinuto() * 60
                + dt.getSegundo();
    }

    /**
     * Devuelve la representación textual de la fecha y hora actuales.
     *
     * @return fecha y hora simuladas
     */
    @Override
    public String toString() {
        return "Año: " + getAño() + "\nMes: " + getMes() + "\nDia: " + getDia()
                + "\nHora: " + getHora() + "\nMinuto: " + getMinuto() + "\nSegundo: " + getSegundo();
    }

    /**
     * Genera la lista de meses comprendidos entre dos fechas.
     *
     * @param periodoInicio fecha inicial
     * @param periodoFin fecha final
     * @return lista de meses en formato mes/año
     */
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

    /**
     * Serializa el objeto y guarda las variables necesarias.
     *
     * @param oos flujo de salida de objetos
     * @throws IOException si ocurre un error de escritura
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeLong(MS_POR_DIA);
    }

    /**
     * Deserializa el objeto y restaura la instancia singleton.
     *
     * @param ois flujo de entrada de objetos
     * @throws IOException si ocurre un error de lectura
     * @throws ClassNotFoundException si no se encuentra una clase durante la deserialización
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        MS_POR_DIA = ois.readLong();
        instance = this;
    }
}
