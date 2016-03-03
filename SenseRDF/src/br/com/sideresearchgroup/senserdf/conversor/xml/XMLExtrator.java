/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor.xml;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.TriplaRDFSense;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.conversor.Extrator;
import br.com.sideresearchgroup.senserdf.utils.XMLUtil;

/**
 * @author Ayrton
 *
 */
public class XMLExtrator implements Extrator {

	@Override
	public void extrairMetadados(Dataset dataset) throws SenseRDFException {
		Document xml = XMLUtil.carregarXML(dataset.getPathArquivo());
		
		TriplaRDFSense tripla = buscarMetadadosRecursivo(dataset, xml.getRootElement());
		dataset.getTriplas().add(tripla);
	}

	private TriplaRDFSense buscarMetadadosRecursivo(Dataset dataset, Element proximo) {
		
		TriplaRDFSense tripla = new TriplaRDFSense();
		String metadado = proximo.getName();

		List<Element> filhos2 = proximo.getChildren(); // Verifica se existe mais um nivel de  Metadados
		List<Attribute> atributos = proximo.getAttributes(); // Procura uma lista de atributos

		if (!atributos.isEmpty()) {
			ArrayList<TriplaRDFSense> triplasAtributos = new ArrayList<TriplaRDFSense>();

			for (Attribute atributo : atributos) {// Gera as triplas dos atributos
				String metadadoAtributo = atributo.getName();
				String dadoAtributo = atributo.getValue();
				Metadado metadadoClasse = dataset.buscarOuCriaMetadado(metadadoAtributo);
				
				TriplaRDFSense triplaAtributo = new TriplaRDFSense( metadadoClasse, dadoAtributo);
				triplasAtributos.add(triplaAtributo);
			}
			
			tripla.setAtributos(triplasAtributos);
		}

		if (!filhos2.isEmpty()) {
			for (Element filho : filhos2) {
				TriplaRDFSense triplaElemento = buscarMetadadosRecursivo(dataset, filho);
				// Verifica se o resultado é um Metadado, ou uma outra Tripla
				if (triplaElemento.getDado() == null || "".equals(triplaElemento.getDado()))
					tripla.addTriplaNaColecao(triplaElemento);
				else {
					//Os atributos de um metadado que contenha dado será atributo do PAI
					if (!triplaElemento.getAtributos().isEmpty())
						tripla.addColecaoAtributos(triplaElemento.getAtributos());
					tripla.addNaColecaoMetadados(triplaElemento);
				}
			}
			Metadado metadadoClasse = dataset.buscarOuCriaMetadado(metadado);
			tripla.setMetadado(metadadoClasse);
		} else {
			Metadado metadadoClasse = dataset.buscarOuCriaMetadado(metadado);
			tripla.setMetadado(metadadoClasse);
			tripla.setDado(proximo.getTextTrim());
		}
		
		return tripla;
	}
}
