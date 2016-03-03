/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.util.List;

import br.com.sideresearchgroup.senserdf.arq.ValidadorCorrespondencia;
import br.com.sideresearchgroup.senserdf.conversor.AlterarMetadadosListener;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.conversor.GerenciadorDatasetGeneric;
import br.com.sideresearchgroup.senserdf.conversor.ValidacaoCorrespondenciaListener;

/**
 * @author Ayrton Nadgel
 *
 */
public class GerenciadorDatasetSwing extends GerenciadorDatasetGeneric {

	@Override
	public void iniciarInterfaceValidacao(List<ValidadorCorrespondencia> validadores, ValidacaoCorrespondenciaListener listener) {
		new DialogoMatchingManager(null, validadores, listener);
	}

	@Override
	public void iniciarInterfaceAlteracaoMetadados(Dataset dataset, AlterarMetadadosListener listener) {
		new DialogoAlterarMetadados(null, dataset, listener);
	}
}
