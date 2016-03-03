package br.com.sideresearchgroup.senserdf.resources;

public enum Idioma {
	PORTUGUES_BRASIL("pt", "Português"),
	INGLES("en", "English");
	
	private String sigla;
	
	private String nome;
	
	private Idioma(String sigla, String nome) {
		this.sigla = sigla;
		this.nome = nome;
	}

	public final String getSigla() {
		return sigla;
	}

	public final void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public final String getNome() {
		return nome;
	}

	public final void setNome(String nome) {
		this.nome = nome;
	}
}
