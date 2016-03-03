/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq;

import br.com.sideresearchgroup.senserdf.conversor.Dataset;

/**
 * @author Ayrton
 *
 */
public final class ValidadorCorrespondencia {
	
	private Dataset dataset;
	
	private Metadado metadado;
	
	private String prefixoVocabulario;
	
	private String termo;
	
	private boolean adicionarVocabulario;
	
	public ValidadorCorrespondencia(Dataset dataset, Metadado metadado) {
		this.dataset = dataset;
		this.metadado = metadado;
	}

	public final String getMetadado() {
		return metadado.getMetadado();
	}

	public final String getPrefixoVocabulario() {
		return metadado.getPrefixoVocabulario();
	}

	public final String getTermo() {
		return metadado.getConstrutor();
	}

	public int getIdDominio() {
		return dataset.getDominio().getId();
	}
	
	public int getIdDataset() {
		return dataset.getId();
	}
	
	public void setResultadoValidacao(String prefixoVocabulario, String termo) {
		this.prefixoVocabulario = prefixoVocabulario;
		this.termo = termo;
	}
	
	public boolean isValido() {
		if (prefixoVocabulario == null || prefixoVocabulario.trim().equals("")) {
			return false;
		} 
		
		if (termo == null || termo.trim().equals("")) {
			return false;
		}
		
		return true; 
	}
	
	public String getVocabularioValidado() {
		return this.prefixoVocabulario;
	}
	
	public String getTermoValidado() {
		return this.termo;
	}

	public boolean isAdicionarOntologia() {
		return adicionarVocabulario;
	}

	public void setAdicionarVocabulario(boolean adicionarVocabulario) {
		this.adicionarVocabulario = adicionarVocabulario;
	}
}
