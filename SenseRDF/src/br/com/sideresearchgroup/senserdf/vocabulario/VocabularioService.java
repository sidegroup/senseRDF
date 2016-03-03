/**
 * 
 */
package br.com.sideresearchgroup.senserdf.vocabulario;

import java.util.List;

/**
 * @author Ayrton
 *
 */
public interface VocabularioService {
	
	public List<Vocabulario> getAllVocabularios();
	
	public List<Vocabulario> getAllVocabulariosPreenchido();
	
	public Vocabulario getVocabularioById(int id);
	
	public Vocabulario getVocabularioByPrefixo(String prefixo);
	
	public Vocabulario getVocabularioByNamespace(String namespace);
	
	public void adicionarVocabulario(Vocabulario vocabulario);
	
	public void removerVocabulario(Vocabulario vocabulario);
	
	public void carregarVocabularios();
	
	public List<String> identificarConstrutores(String path);
	
	public Vocabulario getVocabularioPreenchido(int id);
}
