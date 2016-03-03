/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.Parametro;
import br.com.sideresearchgroup.senserdf.arq.ValidadorCorrespondencia;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.geracao.GeradorRDF;
import br.com.sideresearchgroup.senserdf.matching.MatchingService;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.save.CorrespondenciaService;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioHelper;

/**
 * @author Ayrton Nadgel
 *
 */
public abstract class GerenciadorDatasetGeneric implements GerenciadorDataset {

	private List<Dataset> datasets;
	
	private boolean cancelouValidacao;
	
	@Override
	public List<Dataset> getDatasets() {
		if (datasets == null) {
			datasets = new ArrayList<>(3);
		}
		
		return datasets;
	}
	
	@Override
	public void adicionarDataset(File f) throws SenseRDFException {
		Dataset dataset = new Dataset(f);
		getDatasets().add(dataset);
	}
	
	@Override
	public void removerDataset(int id) throws SenseRDFException {
		if (getDatasets().isEmpty()) {
			return;
		}
		
		for (Dataset d: datasets) {
			if (d.getId() == id) {
				datasets.remove(d);
				break;
			}
		}
	}
	
	@Override
	public void removerDataset(Dataset d) throws SenseRDFException {
		if (d == null) {
			return;
		}
		
		removerDataset(d.getId());
	}
	
	@Override
	public Dataset getDataset(int id) throws SenseRDFException {
		
		return datasets.stream().filter(m -> m.getId() == id)
					.findFirst().orElse(null);

	}
	
	@Override
	public void extrairMetadados(Dataset dataset) throws SenseRDFException {
		Extrator extrator = dataset.getFormato().getExtrator();
		extrator.extrairMetadados(dataset);
		
		//VERIFICAR SE EXISTE CORRESPONDENCIAS JA SALVAS NA BASE DE DADOS
		CorrespondenciaService sc = Configuracao.getCorrespondenciaService();
		for (Metadado metadado : dataset.getMetadados()) {
			
			Metadado metadadoSalvo = sc.buscarPorMetadado(metadado.getMetadado(), dataset.getDominio().getId());
			
			if (metadadoSalvo != null) {
				metadado.setPrefixoVocabulario(metadadoSalvo.getPrefixoVocabulario());
				metadado.setConstrutor(metadadoSalvo.getConstrutor());
			}
		}
	}

	/* (non-Javadoc)
	 * @see br.com.sideresearchgroup.senserdf.conversor.GerenciadorDataset#alterarMetadados(java.util.Map, br.com.sideresearchgroup.senserdf.conversor.Dataset)
	 */
	@Override
	public final void alterarMetadados(Dataset dataset) throws SenseRDFException {
		
		if (!dataset.isEditavel()) {
			throw new SenseRDFException(Sap.MENSAGEM_AVISO_FORMATO_NAOEDITAVEL.get(), TipoMensagem.AVISO);
		}
		
		iniciarInterfaceAlteracaoMetadados(dataset, this);
	}
	
	public abstract void iniciarInterfaceAlteracaoMetadados(Dataset dataset, AlterarMetadadosListener gerenciador);
	
	@Override
	public final void resultadoAlteracaoMetadado(Dataset dataset, Map<String, String> metadados) {
		try {
			
			//ALTERA O ARQUIVO CRIANDO UM NOVO
			Extrator extrator = dataset.getFormato().getExtrator();
			extrator.alterarMetadados(dataset, metadados);
			
		} catch (SenseRDFException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see br.com.sideresearchgroup.senserdf.conversor.GerenciadorDataset#gerarRDF()
	 */
	@Override
	public void gerarRDF() throws SenseRDFException {
		//VALIDA TODOS OS DATASETS A SEREM GERADOS
		for (Dataset d: datasets) {
			d.validar();
		}
		
		for (Dataset d: getDatasets()) {
			gerarRDF(d);
		}
	}
	
	
	@Override
	public void gerarRDF(Dataset dataset) throws SenseRDFException {
		//VALIDA O DATASET
		dataset.validar();
		
		File f = new File(dataset.diretorioConversao());
		if (!f.exists()) {
			f.mkdirs();
		}
		
		//REALIZA O ALINHAMENTO E A VALIDACAO DAS CORRESPONDENCIAS
		MatchingService matching = Configuracao.getServicoMatching();
		matching.identificarCorrespondencias(dataset);
		
		iniciarValidacaoCorrespondencias(dataset);
		
		if (cancelouValidacao){
			return;
		}
		
		for (Metadado m: dataset.getMetadados()) {
			m.validar();
		}
		
		//SALVA AS CORRESPONDENCIAS PARA USO NA PROXIMA CONVERSAO
		CorrespondenciaService sc = Configuracao.getCorrespondenciaService();
		sc.salvarCorrespondencias(dataset.getMetadados(), dataset.getDominio().getId());
		
		//GERA O RDF FINAL
		GeradorRDF gerador = Configuracao.getGeradorRDF();
		gerador.gerarRDF(dataset);
	}

	@Override
	public final void iniciarValidacaoCorrespondencias(Dataset dataset) {
		
		boolean validarTodos = Parametro.getInstance().getAsBoolean(Parametro.VALIDAR_TODOS_METADADOS);
		List<Metadado> metadados = dataset.getMetadados();
		List<ValidadorCorrespondencia> validadores = new ArrayList<>(metadados.size());
		for (Metadado metadado: metadados) {
			
			if (!validarTodos && (metadado.getPrefixoVocabulario() != null && !metadado.getPrefixoVocabulario().equals(""))) {
				continue;
			}
			
			ValidadorCorrespondencia validador = new ValidadorCorrespondencia(dataset, metadado);
			validadores.add(validador);
		}
		
		//TODOS OS METADADOS JA POSSUEM SEUS TERMOS E USUARIO NAO QUER VALIDAR =)
		if (validadores.isEmpty()) {
			return;
		}
		
		iniciarInterfaceValidacao(validadores, this);
	}
	
	public abstract void iniciarInterfaceValidacao(List<ValidadorCorrespondencia> metadados, ValidacaoCorrespondenciaListener listener);

	@Override
	public final void resultadoValidacaoCorrespondencia(List<ValidadorCorrespondencia> resultado) throws SenseRDFException {
		
		if (resultado == null) {
			cancelouValidacao = true;
			return;
		}
		
		
		for(ValidadorCorrespondencia v: resultado) {
			if (!v.isValido()) {
				throw new SenseRDFException(Sap.MENSAGEM_AVISO_METADADO_SEMTERMO.get(v.getMetadado()), TipoMensagem.AVISO);
			}
		}
		
		Dataset dataset = getDataset(resultado.get(0).getIdDataset());

		for (ValidadorCorrespondencia correspondencia: resultado) {
			Metadado metadado = dataset.buscarMetadado(correspondencia.getMetadado());
			metadado.setConstrutor(correspondencia.getTermoValidado());
			metadado.setPrefixoVocabulario(correspondencia.getVocabularioValidado());
			
			if (correspondencia.isAdicionarOntologia()) {
				VocabularioHelper.adicionarConstrutorOntologia(dataset.getDominio(), metadado.getMetadado(), correspondencia.getTermoValidado());
			}
		}
		
		registrarEstatisticas(resultado);
	}

	private void registrarEstatisticas(List<ValidadorCorrespondencia> resultado) {
		
	}
}
