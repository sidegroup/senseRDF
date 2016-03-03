package br.com.sideresearchgroup.senserdf.conversor;

import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;

public interface Extrator {
	
	public void extrairMetadados(Dataset dataset) throws SenseRDFException;
	
	public default void alterarMetadados(Dataset dataset, Object metadados) throws SenseRDFException {
		
	}
}
