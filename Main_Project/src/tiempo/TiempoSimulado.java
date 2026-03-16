package tiempo;

public class TiempoSimulado {

    private static long milisegundosSimulados = 0;
    private static long ultimoTiempoReal;

    private static double velocidad;

    private static int diasPorMes;
    private static int mesesPorAnio;

    public static void iniciar(double velocidadInicial, int diasMes, int mesesAnio) {
        velocidad = velocidadInicial;
        diasPorMes = diasMes;
        mesesPorAnio = mesesAnio;
        ultimoTiempoReal = System.currentTimeMillis();
    }

    public static void actualizar() {
        long ahora = System.currentTimeMillis();
        long diferenciaReal = ahora - ultimoTiempoReal;

        milisegundosSimulados += (long)(diferenciaReal * velocidad);

        ultimoTiempoReal = ahora;
    }

    public static void setVelocidad(double nuevaVelocidad) {
        velocidad = nuevaVelocidad;
    }

    private static long getSegundosTotales() {
        return milisegundosSimulados / 1000;
    }

    public static int getSegundo() {
        return (int)(getSegundosTotales() % 60);
    }

    public static int getMinuto() {
        return (int)((getSegundosTotales() / 60) % 60);
    }

    public static int getHora() {
        return (int)((getSegundosTotales() / 3600) % 24);
    }

    public static int getDia() {
        long diasTotales = getSegundosTotales() / 86400;
        return (int)(diasTotales % diasPorMes);
    }

    public static int getMes() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int)(mesesTotales % mesesPorAnio);
    }

    public static int getAnio() {
        long diasTotales = getSegundosTotales() / 86400;
        long mesesTotales = diasTotales / diasPorMes;
        return (int)(mesesTotales / mesesPorAnio);
    }

    public static String getFechaHora() {
        return String.format(
            "Año %d Mes %d Día %d %02d:%02d:%02d",
            getAnio(),
            getMes(),
            getDia(),
            getHora(),
            getMinuto(),
            getSegundo()
        );
    }
}