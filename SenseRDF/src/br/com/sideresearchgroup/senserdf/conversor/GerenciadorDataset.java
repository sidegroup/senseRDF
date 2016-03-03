/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor;

import java.io.File;
import java.util.List;

import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;

/**
 * @author Ayrton Nadgel
 */
public interface GerenciadorDataset extends AlterarMetadadosListener, ValidacaoCorrespondenciaListener {
	
	/**
	 * Adiciona um {@link File} para a listagem de conversao. Ao indicar um {@link File} eh gerado um objeto {@link Dataset} interno.
	 * Caso o formato seja desconhecido um {@link SenseRDFException} eh lancado.
	 * @param f
	 * @throws SenseRDFException
	 */
	public void adicionarDataset(File f) throws SenseRDFException;
	
	/**
	 * Remove o {@link Dataset} com o ID indicado da lista para conversao
	 * @param id
	 * @throws SenseRDFException
	 */
	public void removerDataset(int id) throws SenseRDFException;
	
	/**
	 * Remove o {@link Dataset} da lista para conversao
	 * @param d
	 * @throws SenseRDFException
	 */
	public void removerDataset(Dataset d) throws SenseRDFException;
	
	/**
	 * Retorna um dataset adicionado pelo ID
	 * @param id
	 * @return Retorna o {@link Dataset} com o id indicado ou <code>null</code> caso nao localizado 
	 * 	
	 * @throws SenseRDFException
	 */
	public Dataset getDataset(int id) throws SenseRDFException;
	
	/**
	 * Inicia o processo de alteracao dos metadados do dataset, se este suportar.
	 * @param dataset
	 * @throws SenseRDFException
	 */
	public void alterarMetadados(Dataset dataset) throws SenseRDFException;
	
	/**
	 * Realiza a extracao de metadados do dataset e verifica na base se existe alguma correspondencia
	 * @param dataset
	 * @throws SenseRDFException
	 */
	public void extrairMetadados(Dataset dataset) throws SenseRDFException;
	
	/**
	 * Inicia o processo de geracao do RDF de todos os datasets adicionados
	 * @throws SenseRDFException
	 */
	public void gerarRDF() throws SenseRDFException;
	
	/**
	 * Inicia o processo de geracao do RDF do Dataset passado como parametro
	 * @param dataset
	 * @throws SenseRDFException
	 */
	public void gerarRDF(Dataset dataset) throws SenseRDFException;
	
	/**
	 * Faz a validacao das correspondencias com o usuario
	 * @param dataset
	 */
	public void iniciarValidacaoCorrespondencias(Dataset dataset);
	
	/**
	 * Lista  todos os datasets adicionados para realizar a conversao
	 * @return
	 */
	public List<Dataset> getDatasets();
}
