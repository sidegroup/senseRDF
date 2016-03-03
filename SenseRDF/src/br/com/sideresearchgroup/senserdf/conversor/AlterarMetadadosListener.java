/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor;

import java.util.Map;

/**
 * @author Ayrton
 *
 */
public interface AlterarMetadadosListener {
	public void resultadoAlteracaoMetadado(Dataset dataset, Map<String, String> metadados);
}
