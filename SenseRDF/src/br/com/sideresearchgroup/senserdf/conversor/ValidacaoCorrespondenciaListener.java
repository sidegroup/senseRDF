/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor;

import java.util.List;

import br.com.sideresearchgroup.senserdf.arq.ValidadorCorrespondencia;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;

/**
 * @author Ayrton
 *
 */
public interface ValidacaoCorrespondenciaListener {
	
	/** Metodo callback para ser chamados apos a conclusao da validacao das correspondencias. <br/>
	 * Se a lista passada como parametro for nulo, sera considerado que nao houve validacao e a ferramenta nao continuara com a geracao.
	 */
	public void resultadoValidacaoCorrespondencia(List<ValidadorCorrespondencia> dataset) throws SenseRDFException;
}
