/**
 * 
 */
package br.com.sideresearchgroup.senserdf.dominio;

import java.util.ArrayList;
import java.util.List;

import br.com.sideresearchgroup.senserdf.arq.Entidade;
import br.com.sideresearchgroup.senserdf.arq.Validate;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;

/**
 * @author ayrton
 *
 */
public class Dominio extends Entidade implements Validate {
	private String nome;
	private String nomeBase;
	private int idVocabularioDominio;
	private List<Integer> vocabularios = new ArrayList<Integer>();
	
	public Dominio(int id) {
		setId(id);
	}
	
	public Dominio(){}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeBase() {
		return nomeBase;
	}
	public void setNomeBase(String nomeBase) {
		this.nomeBase = nomeBase;
	}
	public List<Integer> getVocabularios() {
		return vocabularios;
	}
	public void setVocabularios(List<Integer> vocabularios) {
		this.vocabularios = vocabularios;
	}
	public void addVocabulario(Integer vocabulario){
		this.vocabularios.add(vocabulario);
	}
	public int getIdVocabularioDominio() {
		return idVocabularioDominio;
	}
	public void setIdVocabularioDominio(int idVocabularioDominio) {
		this.idVocabularioDominio = idVocabularioDominio;
	}

	@Override
	public boolean validar() throws SenseRDFException {
		if (nome == null || nome.trim().equals("")) {
			throw new SenseRDFException(Sap.MENSAGEM_CAMPO_OBRIGATORIO.get(Sap.NOME.get()), TipoMensagem.AVISO);
		}
		
		if (vocabularios.isEmpty()) {
			throw new SenseRDFException(Sap.MENSAGEM_CAMPO_OBRIGATORIO.get(Sap.VOCABULARIOS.get()), TipoMensagem.AVISO);
		}
		return false;
	}
}
