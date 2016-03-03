/**
 * 
 */
package br.com.sideresearchgroup.senserdf.matching;

import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;

/**
 * @author Ayrton
 *
 */
@FunctionalInterface
public interface MatchingService {
	public void identificarCorrespondencias(Dataset dataset) throws SenseRDFException;
}
