package filtro;

public abstract class Filtro {
	private boolean ordenAscendente;

	public Filtro(boolean ordenAscendente) {
		super();
		this.ordenAscendente = ordenAscendente;
	}
	
	public boolean isOrdenAscendente() {
	    return ordenAscendente;
	}
}
