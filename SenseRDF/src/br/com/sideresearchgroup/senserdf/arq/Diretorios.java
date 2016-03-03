/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq;

/**
 * @author Ayrton Nadgel
 *
 */
public enum Diretorios {
	
	DIRETORIO_VOCABULARIOS("vocabularios/"),
	DIRETORIO_CONVERSAO("conversao/"),
	DIRETORIO_CONFIGURACAO("configuracao/"),
	DIRETORIO_TEMP("temp/");
	
	private String nome;
	
	private Diretorios(String nome) {
		this.nome = nome;
	}
	
	public String nome() {
		return this.nome;
	}
	
	@Override
	public String toString() {
		return nome();
	}
}
