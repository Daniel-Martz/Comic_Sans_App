package tiempo;

/**
 * Clase refactorizada siguiendo el patrón Singleton.
 * Permite gestionar un tiempo simulado con velocidad variable.
 */
public class TiempoSimulado {
    
    // 1. La instancia única se mantiene estática
    private static TiempoSimulado instance;

    // 2. Los atributos pasan a ser de INSTANCIA (sin static)
    private long milisegundosSimulados = 0;
    private long ultimoTiempoReal;
    private double velocidad;
    private int diasPorMes;
    private int mesesPorAño;

    // 3. El constructor privado inicializa los valores por defecto
    private TiempoSimulado() {
        this.velocidad = 1;
        this.diasPorMes = 30;
        this.mesesPorAño = 12;
        this.ultimoTiempoReal = System.currentTimeMillis();
    }
    
    // 4. Método para obtener la instancia única
    public static TiempoSimulado getInstance() {
        if (instance == null) {
            instance = new TiempoSimulado();
        }
        return instance;
    }

    // 5. Los métodos de lógica pasan a ser de INSTANCIA (sin static)
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
        long diasTotales = getSegundosTotales() / 86400;
        return (int) (diasTotales % diasPorMes);
    }

    public int getMes() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int) (mesesTotales % mesesPorAño);
    }

    public int getAño() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int) (mesesTotales / mesesPorAño);
    }

    @Override
    public String toString() {
        return "Año: " + getAño() + "\nMes: " + getMes() + "\nDia: " + getDia() + 
               "\nHora: " + getHora() + "\nMinuto: " + getMinuto() + "\nSegundo: " + getSegundo();
    }
}