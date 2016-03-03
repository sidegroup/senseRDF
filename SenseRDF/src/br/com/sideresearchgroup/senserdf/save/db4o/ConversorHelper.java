/**
 * 
 */
package br.com.sideresearchgroup.senserdf.save.db4o;

import br.com.sideresearchgroup.senserdf.arq.Metadado;

/**
 * @author Ayrton
 *
 */
final class ConversorHelper {
	
	public static Metadado converter(DadosCorrespondencia dados) {
		
		Metadado metadado = new Metadado(dados.getMetadado());
		metadado.setConstrutor(dados.getTermo());
		metadado.setPrefixoVocabulario(dados.getPrefixoVocabulario());
		metadado.setMeasure(dados.getConfianca());
		
		return metadado;
	}
	
	public static DadosCorrespondencia converter(Metadado m, int idDominio) {
		DadosCorrespondencia dados = new DadosCorrespondencia();
		
		dados.setConfianca(m.getMeasure());
		dados.setIdDominio(idDominio);
		dados.setMetadado(m.getMetadado());
		dados.setPrefixoVocabulario(m.getPrefixoVocabulario());
		dados.setTermo(m.getConstrutor());
		
		return dados;
	}
}
