/**
 * 
 */
package br.com.sideresearchgroup.senserdf.matching;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.utils.SenseUtil;
import br.com.sideresearchgroup.senserdf.utils.XMLUtil;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;
import fr.inrialpes.exmo.align.impl.BasicParameters;
import fr.inrialpes.exmo.align.impl.method.StringDistAlignment;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;

/**
 * @author Ayrton
 *
 */
public class AlignmentMatchingService implements MatchingService {

	@Override
	public void identificarCorrespondencias(Dataset dataset) throws SenseRDFException {
		String owlConstrutores = gerarOWLConstrutores(dataset);
		
		//NAO EXISTE MAIS CONSTRUTORES A SEREM IDENTIFICADOS
		if (owlConstrutores == null) {
			return;
		}
		
		String owlVocabularios = gerarOWLVocabularios(dataset);
		
		String fileNameAlinhamento = Configuracao.diretorioExecucao() +"/"+ dataset.diretorioConversao()+"alinhamento.rdf";
		
		try {
			URI onto1 = new URI(owlConstrutores);
			URI onto2 = new URI(owlVocabularios);
			
			Properties params = new BasicParameters();

			AlignmentProcess a2 = new StringDistAlignment();
			params.setProperty("stringFunction", "smoaDistance");
			a2.init(onto1, onto2);
			a2.align((Alignment) null, params);

			// Outputing
			PrintWriter writer2 = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							fileNameAlinhamento), "UTF-8")), true);
			AlignmentVisitor renderer2 = new RDFRendererVisitor(writer2);
			a2.render(renderer2);

			writer2.flush();
			writer2.close();
			
			relacionarCorrespondencias(fileNameAlinhamento, dataset);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SenseRDFException();
		}
	}
	
	private String gerarOWLConstrutores(Dataset dataset) {
		String fileNameOwlMetadados = "/"+dataset.diretorioConversao()+"owlmetadados";
		String localGravacaoMetadados = null;
		try {
			List<Metadado> metadados = dataset.getMetadados()
						.stream().filter(m -> m.getPrefixoVocabulario() == null)
							.collect(Collectors.toList());

			if (metadados.isEmpty())
				return null;

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			IRI ontologyIRI = IRI.create(dataset.getUri()+"v"+dataset.diretorioConversao()+"/");

			// Create a document IRI which can be resolved to point to where our ontology will be saved.
			localGravacaoMetadados = SenseUtil.tratarLocalGravacao(Configuracao.diretorioExecucao()+"/"+fileNameOwlMetadados,"owl");

			IRI documentIRI = IRI.create(localGravacaoMetadados);

			// Set up a mapping, which maps the ontology to the document IRI
			SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
			manager.addIRIMapper(mapper);

			// Now create the ontology - we use the ontology IRI (not the physical IRI)
			OWLOntology ontology;

			ontology = manager.createOntology(ontologyIRI);

			OWLDataFactory factory = manager.getOWLDataFactory();

			for (Metadado m : metadados) {
				OWLClass clsConstrutor = factory.getOWLClass(IRI.create("#"+ m.getMetadado()));

				String base = "http://SideReserachGroup.com/ontologies/";
				PrefixManager pm = new DefaultPrefixManager(base);

				OWLNamedIndividual niConstrutor = factory.getOWLNamedIndividual(":"+ m.getMetadado(), pm);

				OWLClassAssertionAxiom classAssertion1 = factory.getOWLClassAssertionAxiom(clsConstrutor, niConstrutor);

				manager.addAxiom(ontology, classAssertion1);
			}
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			System.out.println(e);
		} catch (OWLOntologyCreationException e) {
			System.out.println(e);
		}

		return localGravacaoMetadados;
	}
	
	private String gerarOWLVocabularios(Dataset dataset) {
		String filename = null;
		try {
			
			//CARREGANDO OS VOCABULARIOS DO DOMINIO
			DominioService servico = Configuracao.getServicoDominio();
			Dominio dominio = servico.getDominio(dataset.getDominio().getId());

			List<Vocabulario> vocabularios = new ArrayList<Vocabulario>(dominio.getVocabularios().size() + 1);
			for (Integer id : dominio.getVocabularios()){
				vocabularios.add(Configuracao.getServicoVocabulario().getVocabularioPreenchido(id));
			}
			if (dominio.getIdVocabularioDominio() > 0) {
				vocabularios.add(Configuracao.getServicoVocabulario().getVocabularioPreenchido(dominio.getIdVocabularioDominio()));
			}

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			//Busca a ultima versao do arquivo
			String baseNS = new String("http://www.sideresearchgroup.com.br/internalontology/v" + dataset.diretorioConversao()+ "/");
			IRI ontologyIRI = IRI.create(baseNS);
			//Cria o nome a ser gerado do OWL do vocabulario
			String nomeArquivo = dataset.diretorioConversao()+"owlvocabularios";

			// Create a document IRI which can be resolved to point to where our
			// ontology will be saved.
			filename = SenseUtil.tratarLocalGravacao(Configuracao.diretorioExecucao() + "/"+ nomeArquivo, "owl");
			IRI documentIRI = IRI.create(filename);

			// Set up a mapping, which maps the ontology to the document IRI
			SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
			manager.addIRIMapper(mapper);

			// Now create the ontology - we use the ontology IRI (not the
			// physical IRI)
			OWLOntology ontology;

			ontology = manager.createOntology(ontologyIRI);

			OWLDataFactory factory = manager.getOWLDataFactory();

			// Get hold of references to class A and class B. Note that the
			// ontology does not
			// contain class A or classB, we simply get references to objects
			// from a data factory that represent
			// class A and class B

			for (Vocabulario vocabulario : vocabularios) {
				List<String> construtores = vocabulario.getConstrutores();

				for (String construtor : construtores) {
					OWLClass clsConstrutor = factory.getOWLClass(IRI.create(baseNS + vocabulario.getPrefixo() + "#"+ construtor));

					String base = vocabulario.getNamespace();
					PrefixManager pm = new DefaultPrefixManager(base);

					OWLNamedIndividual niConstrutor = factory.getOWLNamedIndividual(construtor, pm);

					OWLClassAssertionAxiom classAssertion1 = factory.getOWLClassAssertionAxiom(clsConstrutor,niConstrutor);

					manager.addAxiom(ontology, classAssertion1);
				}
			}

			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			System.out.println(e);
		} catch (OWLOntologyCreationException e) {
			System.out.println(e);
		}
		
		return filename;
	}
	
	private void relacionarCorrespondencias(String pathCorrespondencias, Dataset dataset) {
		List<Metadado> correspondencias = new ArrayList<>();
		
		//CRIA AS CORRESPONDENCIAS
		Document resultado = XMLUtil.carregarXML(pathCorrespondencias);
		List<Element> filhos = resultado.getRootElement().getChildren();
		for (Element alinhamento : filhos) {
			if (alinhamento.getName().equalsIgnoreCase("alignment")) {
				List<Element> filhosDeAlignment = alinhamento.getChildren();
				for (Element filho : filhosDeAlignment) {
					if (filho.getName().equalsIgnoreCase("map")) {
						correspondencias.add(gerarCorrespondencia(filho));
					}
				}
			}
		}
		
		//ASSOCIA AOS METADADOS DO DATASET
		for (Metadado correspondencia: correspondencias) {
			Metadado metadado = dataset.getMetadados().stream()
									.filter(m -> m.getMetadado().equals(correspondencia.getMetadado()))
										.findFirst().orElse(new Metadado(null));
			
			metadado.setConstrutor(correspondencia.getConstrutor());
			metadado.setPrefixoVocabulario(correspondencia.getPrefixoVocabulario());
			metadado.setMeasure(correspondencia.getMeasure());
		}
	}
	
	private Metadado gerarCorrespondencia(Element map) {
		Metadado correspondencia = new Metadado(null);
		
		Element element = map.getChildren().get(0);
		List<Attribute> atributos = null;
		for (Element filhoCell : element.getChildren()) {
			if (filhoCell.getName().equalsIgnoreCase("entity1")) {
				atributos = filhoCell.getAttributes();

				for (Attribute atributo : atributos) {
					String uri = atributo.getValue();
					int inicioPrefixo = uri.lastIndexOf("#");
					correspondencia.setMetadado(uri.substring(inicioPrefixo + 1));
				}
			} else if (filhoCell.getName().equalsIgnoreCase("entity2")) {
				atributos = filhoCell.getAttributes();

				for (Attribute atributo : atributos) {
					int inicioPrefixo = atributo.getValue().lastIndexOf("/");
					int terminoPrefixo = atributo.getValue().lastIndexOf("#");
					if (inicioPrefixo != -1) {
						correspondencia.setPrefixoVocabulario(atributo.getValue().substring(inicioPrefixo + 1, terminoPrefixo));
					} else {
						correspondencia.setPrefixoVocabulario(atributo.getValue().substring(0, terminoPrefixo));
					}
					
					inicioPrefixo = atributo.getValue().lastIndexOf("#");
					correspondencia.setConstrutor(atributo.getValue().substring(inicioPrefixo + 1));
				}
			} else if (filhoCell.getName().equalsIgnoreCase("measure")) {
				String valor = filhoCell.getTextTrim();
				correspondencia.setMeasure(Double.parseDouble(valor));
			}
		}
		
		return correspondencia;
	}
}
