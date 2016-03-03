package br.com.sideresearchgroup.senserdf.arq;

import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioService;

public class Metadado implements Comparable<Metadado>, Validate{
	private String metadado = null;
	private String construtor = null;
	private String prefixoVocabulario = null;
	private double measure = 0.0;
	
	public Metadado(String metadado){
		this.metadado = metadado;
	}
	
	/**
	 * Retorna um objeto Vocabulary que representa esse metadado com as informações básicas
	 * @return
	 */
	public Vocabulario getVocabularioInfoBasicas(){
		VocabularioService servico = Configuracao.getServicoVocabulario();
		return servico.getVocabularioByPrefixo(prefixoVocabulario);
	}
	
	public boolean possuiVocabulario(){
		return prefixoVocabulario != null;
	}
	
	//Get's e Set's
	public String getMetadado() {
		return metadado;
	}
	public void setMetadado(String metadado) {
		this.metadado = metadado;
	}
	public String getConstrutor() {
		return construtor;
	}
	public void setConstrutor(String construtor) {
		this.construtor = construtor;
	}
	public String getPrefixoVocabulario() {
		return prefixoVocabulario;
	}
	public void setPrefixoVocabulario(String prefixoVocabulario) {
		this.prefixoVocabulario = prefixoVocabulario;
	}
	public double getMeasure() {
		return measure;
	}
	public void setMeasure(double measure) {
		this.measure = measure;
	}
	@Override
	public String toString() {
		return "Metadado [metadado=" + metadado + ", construtor=" + construtor
				+ ", prefixoVocabulario=" + prefixoVocabulario + ", measure="
				+ measure + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((construtor == null) ? 0 : construtor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Metadado other = (Metadado) obj;
		if (construtor == null) {
			if (other.construtor != null)
				return false;
		} else if (!construtor.equalsIgnoreCase(other.construtor))
			return false;
		return true;
	}

	@Override
	public int compareTo(Metadado o) {
		return this.getMetadado().compareTo(o.getMetadado());
	}

	@Override
	public boolean validar() throws SenseRDFException {
		if (construtor == null) {
			throw new SenseRDFException("O metadado '" + metadado +"' não possue um construtor associado.", TipoMensagem.FATAL); 
		}
		
		return true;
	}
}
