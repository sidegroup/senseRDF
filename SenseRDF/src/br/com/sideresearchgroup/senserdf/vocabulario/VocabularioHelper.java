package br.com.sideresearchgroup.senserdf.vocabulario;

import java.io.File;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Diretorios;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.utils.SenseUtil;

public class VocabularioHelper {
	
	
	public static boolean validarVocabulario(Vocabulario vocabulario, String prefixo, String namespace) throws SenseRDFException {
		VocabularioService vocabularioService = Configuracao.getServicoVocabulario();
		
		Vocabulario v = vocabularioService.getVocabularioByPrefixo(prefixo);
		if (v != null && v.getId() != vocabulario.getId()) {
			throw new SenseRDFException(Sap.GESTAO_VOCABULARIOS_PREFIXO_JAEXISTE.get(), TipoMensagem.AVISO);
		}
		
		v = vocabularioService.getVocabularioByNamespace(namespace);
		if (v != null && v.getId() != vocabulario.getId()) {
			throw new SenseRDFException(Sap.GESTAO_VOCABULARIOS_NAMESAPCE_JAEXISTE.get(), TipoMensagem.AVISO);
		}
		
		return true;
	}
	
	
	public static void adicionarConstrutorOntologia(Dominio d, String termo, String superClasse) throws SenseRDFException {
		try {
			
			Logger log = Logger.getLogger(VocabularioHelper.class);
			log.debug("Adicionando termo" + termo+ " no dominio " + d.getNome());
			
			VocabularioService vocabularioService = Configuracao.getServicoVocabulario();
			DominioService dominioService = Configuracao.getServicoDominio();
			
			Dominio dominioGeracao = d;
			
			Vocabulario vocabulario = null;
			String base = null;
			String nomeArquivo = null;
			String diretorio = null;
			
			if (dominioGeracao.getIdVocabularioDominio() != 0){
				vocabulario = vocabularioService.getVocabularioById(dominioGeracao.getIdVocabularioDominio());
				base = vocabulario.getNamespace();
				nomeArquivo = vocabulario.getPath();
			
				diretorio = Configuracao.diretorioExecucao() + "/"+ Diretorios.DIRETORIO_VOCABULARIOS+nomeArquivo;
			} else {
				vocabulario = new Vocabulario();
				String nomeFormatado = SenseUtil.tratarCaracteres(dominioGeracao.getNome());
				
				base = new String("http://www.sideresearchgroup.com.br/internal/own"+nomeFormatado+"/");
				nomeArquivo = new String("own"+nomeFormatado+".owl");
				
				diretorio = Configuracao.diretorioExecucao() + "/"+ Diretorios.DIRETORIO_TEMP+nomeArquivo;
				vocabulario.setPath(diretorio);
				
				vocabulario.setNamespace(base);
				vocabulario.setPrefixo("own"+nomeFormatado);
				vocabulario.setNome(dominioGeracao.getNome());
				
				
			}

			IRI ontologyIRI = IRI.create(base);
			
			String localGravacaoOwl = SenseUtil.tratarLocal(diretorio);
			IRI documentIRI = IRI.create(localGravacaoOwl);

			// Set up a mapping, which maps the ontology to the document IRI
			SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
			
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			man.addIRIMapper(mapper);

			OWLOntology ont = null;
			if (vocabulario.getId() > 0) {
				File file = new File(Diretorios.DIRETORIO_VOCABULARIOS+ vocabulario.getPath());

				// Now load the local copy
				ont = man.loadOntologyFromOntologyDocument(file);

			} else {
				ont = man.createOntology(ontologyIRI);
			}

			OWLDataFactory factory = man.getOWLDataFactory();

			OWLClass nose = factory.getOWLClass(IRI.create(base + "#" + superClasse));

			OWLClass head = factory.getOWLClass(IRI.create(base + "#"+ termo));

			OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(head, nose);

			AddAxiom addAx = new AddAxiom(ont, ax);
			man.applyChange(addAx);

			man.saveOntology(ont);
			
			if (vocabulario.getId() == 0) {
				vocabularioService.adicionarVocabulario(vocabulario);
				dominioGeracao.setIdVocabularioDominio(vocabulario.getId());
				dominioService.adicionarDominio(dominioGeracao);
			}
		
		} catch (OWLOntologyCreationException e) {
			throw new SenseRDFException(e);
		} catch (OWLOntologyStorageException e) {
			throw new SenseRDFException(e);
		}
	}
	
}
