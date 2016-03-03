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
public interface GeradorRDF {
	public void gerarRDF(Dataset dataset) throws SenseRDFException;
}
