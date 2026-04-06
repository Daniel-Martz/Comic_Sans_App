package filtro;

public abstract class Filtro {
	protected boolean ordenAscendente;

	public Filtro(boolean ordenAscendente) {
		super();
		this.ordenAscendente = ordenAscendente;
	}
	
	public boolean isOrdenAscendente() {
	    return ordenAscendente;
	}
	
	public void limpiarFiltro() {
		ordenAscendente = false;
	}
	
}
