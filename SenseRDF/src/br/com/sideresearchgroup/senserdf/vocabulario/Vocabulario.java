/**
 * 
 */
package br.com.sideresearchgroup.senserdf.vocabulario;

import java.util.List;

import br.com.sideresearchgroup.senserdf.arq.Entidade;

/**
 * @author Ayrton Nadgel
 * 
 */
public class Vocabulario extends Entidade {
	
	private String nome;
	
	private String namespace;
	
	private String prefixo;
	
	private String path;
	
	private List<String> construtores;
	
	
	public Vocabulario() { }
	
	public Vocabulario(int id) {
		this(id, null);
	}
	
	public Vocabulario(int id, String nome) {
		setId(id);
		this.nome = nome;
	}
	
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getPrefixo() {
		return prefixo;
	}
	
	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public List<String> getConstrutores() {
		return construtores;
	}
	
	public void setConstrutores(List<String> construtores) {
		this.construtores = construtores;
	}
}
