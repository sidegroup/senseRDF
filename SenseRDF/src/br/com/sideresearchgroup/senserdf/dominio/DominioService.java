/**
 * 
 */
package br.com.sideresearchgroup.senserdf.dominio;

import java.util.List;

/**
 * @author Ayrton
 *
 */
public interface DominioService {
	
	public Dominio getDominio(int id);
	
	public void adicionarDominio(Dominio dominio);
	
	public void removerDominio(Dominio dominio);
	
	public void removerDominio(int id);
	
	public List<Dominio> getAllDominios();
	
	public void carregarDominios();
}
