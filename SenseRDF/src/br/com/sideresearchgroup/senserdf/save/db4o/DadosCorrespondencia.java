/**
 * 
 */
package br.com.sideresearchgroup.senserdf.save.db4o;

/**
 * @author Ayrton
 *
 */
final class DadosCorrespondencia {
	
	private String metadado;
	
	private String prefixoVocabulario;
	
	private String termo;
	
	private double confianca;
	
	private int idDominio;

	public final String getMetadado() {
		return metadado;
	}

	public final void setMetadado(String metadado) {
		this.metadado = metadado;
	}

	public final String getPrefixoVocabulario() {
		return prefixoVocabulario;
	}

	public final void setPrefixoVocabulario(String prefixoVocabulario) {
		this.prefixoVocabulario = prefixoVocabulario;
	}

	public final String getTermo() {
		return termo;
	}

	public final void setTermo(String termo) {
		this.termo = termo;
	}

	public final double getConfianca() {
		return confianca;
	}

	public final void setConfianca(double confianca) {
		this.confianca = confianca;
	}

	public final int getIdDominio() {
		return idDominio;
	}

	public final void setIdDominio(int idDominio) {
		this.idDominio = idDominio;
	}
}
