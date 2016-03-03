/**
 * 
 */
package br.com.sideresearchgroup.senserdf.geracao;

import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;

/**
 * @author Ayrton
 *
 */
public interface PosGeracaoRDF {
	public void posGeracaoRDF(Dataset dataset) throws SenseRDFException;
}
