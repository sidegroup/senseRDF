/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq;

import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;

/**
 * @author Ayrton
 */
public interface Validate {
	
	public boolean validar() throws SenseRDFException;
	
}
