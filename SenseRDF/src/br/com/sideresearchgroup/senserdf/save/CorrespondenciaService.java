/**
 * 
 */
package br.com.sideresearchgroup.senserdf.save;

import java.util.List;

import br.com.sideresearchgroup.senserdf.arq.Metadado;

/**
 * @author Ayrton
 *
 */
public interface CorrespondenciaService {
	
	public void salvarCorrespondencias(List<Metadado> correspondencias, int idDominio);
	
	public void salvarCorrespondencia(Metadado correspondencia, int idDominio);
	
	public List<Metadado> buscarPorDominio(int idDominio);
	
	public Metadado buscarPorMetadado(String metadado, int idDominio);
	
	public void removerCorrespondencia(String metadado, int idDominio);
	
	public void removerCorrespondencia(int idDominio);
}
