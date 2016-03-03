/**
 * 
 */
package br.com.sideresearchgroup.senserdf.geracao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.TriplaRDFSense;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;

/**
 * @author Ayrton
 *
 */
public class GeradorJenaRDF implements GeradorRDF {
	

	private ArrayList<Vocabulario> vocabularios = null;
	private Vocabulario vocabularioAtual = null;
	private Model modeloRDF = null;

	/* (non-Javadoc)
	 * @see br.com.sideresearchgroup.senserdf.geracao.GeradorRDF#gerarRDF(br.com.sideresearchgroup.senserdf.conversor.Dataset)
	 */
	@Override
	public void gerarRDF(Dataset dataset) throws SenseRDFException {

		vocabularios = new ArrayList<Vocabulario>();

		modeloRDF = ModelFactory.createDefaultModel(); // Modelo onde vai ser gerado o RDF
		String nameSpace = dataset.getUri(); // URI default a ser usado no primeiro RDF:Description
		Resource nodeRaiz = modeloRDF.createResource(nameSpace);

		for (TriplaRDFSense tripla : dataset.getTriplas()) {
			// Para poder definir o prefixo do vocabulario a frente
			retornarVocabularioMetadado(tripla.getMetadado());

			if (tripla.getDado() != null && !tripla.getDado().equals("") && vocabularioAtual != null) {
				Property propriedade = modeloRDF.createProperty(vocabularioAtual.getNamespace() + tripla.getMetadado().getConstrutor());
				modeloRDF.add(nodeRaiz, propriedade, tripla.getDado());
			} else if (vocabularioAtual != null) {
				Resource propri = criarRecurso(tripla, nameSpace, 0);
				Property propriedade = modeloRDF.createProperty(vocabularioAtual.getNamespace(), tripla.getMetadado().getConstrutor());
				nodeRaiz.addProperty(propriedade, propri);
			}
		}

		for (Vocabulario vocabulario : vocabularios) {
			modeloRDF.setNsPrefix(vocabulario.getPrefixo(), vocabulario.getNamespace());
		}
		
		
		try {
			FormatoRDF formato = dataset.getFormatoRDF();
			String arquivo = dataset.diretorioConversao()+dataset.getArquivo().getName().concat(getExtensaoArquivo(formato));
			OutputStream out = new FileOutputStream(arquivo);
			modeloRDF.write(out, getDescricaoFormato(formato));
			out.flush();
			out.close();
			
			File f = new File(arquivo);
			if (f.exists()) {
				dataset.setArquivoRDF(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private String getDescricaoFormato(FormatoRDF formato) {
		
		if (formato  == FormatoRDF.RDF_XML) {
			return "RDF/XML";
		} else if (formato == FormatoRDF.TURTLE){
			return "TURTLE";
		}
		
		return "RDF/XML";
	}
	
	private String getExtensaoArquivo(FormatoRDF formato){
		if (formato  == FormatoRDF.RDF_XML) {
			return ".rdf";
		} else if (formato == FormatoRDF.TURTLE){
			return ".ttl";
		}
		
		return ".rdf";
	}

	/**
	 * Método que é chamado recursivamente para a criação dos recursos e das
	 * propriedades
	 * 
	 * @param tripla
	 *            - Tripla que sera usada para a geração do Recurso
	 * @return - Resource
	 * @throws SenseRDFException 
	 */
	private Resource criarRecurso(TriplaRDFSense tripla, String url, int seq) throws SenseRDFException {
		String urlIdentificador = null;
		Resource resource = null;
		Property propriedade = null;
		
		//Criar a URI para caso o formato seja XML
		if (url != null) {
			String usarSeparador = null;
			if (url.endsWith("/"))
				usarSeparador = "";
			else
				usarSeparador = "/";
			String urlUsar = url.concat(usarSeparador+tripla.getMetadado().getConstrutor());
		
			if (!tripla.getColecaoMetadados().isEmpty() || !tripla.getAtributos().isEmpty()){
				urlUsar = urlUsar.concat(String.valueOf(seq++));
				urlIdentificador = urlUsar;
			} else {
				urlIdentificador = urlUsar;
			}
		
			resource = modeloRDF.createResource(urlUsar);
		} else {
			resource = modeloRDF.createResource();
		}
		

		if (!tripla.getColecaoMetadados().isEmpty()) {
			 for(TriplaRDFSense metadado: tripla.getColecaoMetadados()){
				 retornarVocabularioMetadado(metadado.getMetadado());
				 //SubPropriedade
				 propriedade = modeloRDF.createProperty(vocabularioAtual.getNamespace(), metadado.getMetadado().getConstrutor());
				 resource.addProperty(propriedade, metadado.getDado());
			 }
		}

		if (!tripla.getAtributos().isEmpty()) {
			 for(TriplaRDFSense atributo: tripla.getAtributos()){
				 retornarVocabularioMetadado(atributo.getMetadado());
				 //Propriedade
				 propriedade = modeloRDF.createProperty(vocabularioAtual.getNamespace() + atributo.getMetadado().getConstrutor());
				 resource.addProperty(propriedade, atributo.getDado());
			 }
		}

		if (!tripla.getColecaoTriplas().isEmpty()) {
			int seqInterna = 0;
			 for(TriplaRDFSense subTripla: tripla.getColecaoTriplas()){
				 seqInterna++;
				 retornarVocabularioMetadado(subTripla.getMetadado());
				 Resource subPropriedadeRecurso = criarRecurso(subTripla, urlIdentificador, seqInterna);
				 propriedade = modeloRDF.createProperty(vocabularioAtual.getNamespace(), subTripla.getMetadado().getConstrutor());
				 resource.addProperty(propriedade, subPropriedadeRecurso);
			 }
		}

		return resource;
	}
	
	/**
	 * Método que irá adicionar os Vocabulários usados na geração do RDF, e
	 * retornar para o método 'criarRecurso' o vocabulário da Tripla atual
	 * 
	 * @param tripla
	 * @param vocabularios
	 * @return Vocabulary
	 * @throws SenseRDFException 
	 */
	private void retornarVocabularioMetadado(Metadado metadadoTripla) throws SenseRDFException {

		vocabularioAtual = null;

		String prefixoString = metadadoTripla.getPrefixoVocabulario();
		if (prefixoString == null)
			return;

		if (vocabularios.isEmpty()) {
			vocabularioAtual = metadadoTripla.getVocabularioInfoBasicas();
			vocabularios.add(vocabularioAtual);
		} else {
			for (Vocabulario v : vocabularios) {
				if (v.getPrefixo().equals(prefixoString)) {
					vocabularioAtual = v;
					break;
				}
			}

			if (vocabularioAtual == null) {
				vocabularioAtual = metadadoTripla.getVocabularioInfoBasicas();
				vocabularios.add(vocabularioAtual);
			}
		}
	}
}
