/**
 * 
 */
package br.com.sideresearchgroup.senserdf.save.db4o;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.save.CorrespondenciaService;

/**
 * @author Ayrton
 *
 */
public class GerenciadorCorrespondencia  implements CorrespondenciaService {
	
	private final String NOME_DATABASE = "correspondencias.yap";

	@Override
	public void salvarCorrespondencias(List<Metadado> correspondencias, int idDominio) {
		for (Metadado correspondencia: correspondencias) {
			salvarCorrespondencia(correspondencia, idDominio);
		}
	}

	@Override
	public void salvarCorrespondencia(Metadado correspondencia, int idDominio) {
		ObjectContainer db = abrirDatabase();
		List<DadosCorrespondencia> dados = buscar(db, correspondencia.getMetadado(), idDominio);
		
		if (dados.isEmpty()) {
			DadosCorrespondencia salvar = ConversorHelper.converter(correspondencia, idDominio);
			db.store(salvar);
		} else {
			for (DadosCorrespondencia dado: dados) {
				dado.setTermo(correspondencia.getConstrutor());
				dado.setPrefixoVocabulario(correspondencia.getPrefixoVocabulario());
				db.store(dado);
			}
		}
		
		db.commit();
		db.close();
	}

	@Override
	public List<Metadado> buscarPorDominio(int idDominio) {
		ObjectContainer db = abrirDatabase();
		try {
			List<DadosCorrespondencia> dados = buscar(db, null, idDominio);
			List<Metadado> metadados = new ArrayList<>(dados.size());
			
			for (DadosCorrespondencia dado: dados) {
				metadados.add(ConversorHelper.converter(dado));
			}
			
			return metadados;
		} finally {
			db.close();
		}
	}

	@Override
	public void removerCorrespondencia(String metadado, int idDominio) {
		ObjectContainer db = abrirDatabase();
		try {
			List<DadosCorrespondencia> dados = buscar(db, metadado, idDominio);
			
			for (DadosCorrespondencia dado: dados) {
				db.delete(dado);
			}
	
			db.commit();
		} finally {
			db.close();
		}
	}

	@Override
	public void removerCorrespondencia(int idDominio) {
		ObjectContainer db = abrirDatabase();
		try {
			List<DadosCorrespondencia> dados = buscar(db, null, idDominio);
			
			for (DadosCorrespondencia dado: dados) {
				db.delete(dado);
			}
			db.commit();
		} finally {
			db.close();
		}
	}

	@Override
	public Metadado buscarPorMetadado(String metadado, int idDominio) {
		ObjectContainer db = abrirDatabase();
		try {
			List<DadosCorrespondencia> dados = buscar(db, metadado, idDominio);
			if (dados.isEmpty()) {
				return null;
			}
			
			return ConversorHelper.converter(dados.get(0));
		} finally {
			db.close();
		}
	}
	
	
	private ObjectContainer abrirDatabase() {
		return Db4oEmbedded.openFile(NOME_DATABASE);
	}
	
	private List<DadosCorrespondencia> buscar(ObjectContainer db, String metadado, int idDominio) {
		DadosCorrespondencia dados = new DadosCorrespondencia();
		dados.setMetadado(metadado);
		dados.setIdDominio(idDominio);
		
		List<DadosCorrespondencia> metadados = db.queryByExample(dados);
		
		return metadados;
	}
}
