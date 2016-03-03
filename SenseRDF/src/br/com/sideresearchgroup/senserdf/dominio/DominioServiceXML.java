/**
 * 
 */
package br.com.sideresearchgroup.senserdf.dominio;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import br.com.sideresearchgroup.senserdf.arq.Diretorios;
import br.com.sideresearchgroup.senserdf.utils.XMLUtil;

/**
 * @author Ayrton
 *
 */
public class DominioServiceXML implements DominioService {
	
	private final String CONF_DOMINIO =  Diretorios.DIRETORIO_CONFIGURACAO + "dominios.xml";
	
	private final String ROOT_ELEMENT = "config-dominios";
	private final String DOMINIO = "dominio";
	private final String ID = "id";
	private final String NOME = "nome";
	private final String ARQUIVO = "arquivo";
	private final String VOCABULARIO_DOMINIO = "vocabulario_dominio";
	private final String VOCABULARIOS = "vocabularios";
	private final String VOCABULARIO_ID = "vocabulario_id";
	
	private List<Dominio> dominios = null;

	@Override
	public Dominio getDominio(int id) {
		Document documento = XMLUtil.carregarXML(CONF_DOMINIO);
		if (documento == null) {
			return null;
		}
		
		Dominio dominio = null;
		List<Element> dominios = documento.getRootElement().getChildren();
		for (Element d: dominios) {
			int idDominio = Integer.parseInt(d.getChild(ID).getValue());
			if (idDominio == id) {
				dominio = criarDominio(d);
				break;
			}
		}
		
		return dominio;
	}

	@Override
	public void adicionarDominio(Dominio dominio) {
		
		if (dominio.getId() == 0) {
			dominio.setId(gerarIDDominio());
		}
		
		Document documento = XMLUtil.carregarXML(CONF_DOMINIO);
		if (documento == null) {
			documento = new Document();
			documento.setRootElement(new Element(ROOT_ELEMENT));
		} else {
			if (dominio.getId() != 0) {
				List<Element> dominios = documento.getRootElement().getChildren();
				for (Element d: dominios) {
					int idDominio = Integer.parseInt(d.getChild(ID).getValue());
					if (idDominio == dominio.getId()) {
						documento.getRootElement().removeContent(d);
						break;
					}
				}
			}
		}
		
		documento.getRootElement().addContent(criarElementByDominio(dominio));
		XMLUtil.salvarXML(documento, CONF_DOMINIO);
		
		carregarDominios();
	}

	private Element criarElementByDominio(Dominio dominio) {
		
		Element elementoDominio = new Element(DOMINIO);
		elementoDominio.addContent(XMLUtil.criarElemento(ID, String.valueOf(dominio.getId())));
		elementoDominio.addContent(XMLUtil.criarElemento(NOME, dominio.getNome()));
		elementoDominio.addContent(XMLUtil.criarElemento(ARQUIVO, dominio.getNomeBase()));
		elementoDominio.addContent(XMLUtil.criarElemento(VOCABULARIO_DOMINIO, String.valueOf(dominio.getIdVocabularioDominio())));
		
		Element tagVocabularios = new Element(VOCABULARIOS);
		List<Integer> vocabularios = dominio.getVocabularios(); 
		for (Integer v: vocabularios){
			tagVocabularios.addContent(XMLUtil.criarElemento(VOCABULARIO_ID, String.valueOf(v)));
		}
		elementoDominio.addContent(tagVocabularios);
		
		return elementoDominio;
	}
	
	private Dominio criarDominio(Element element) {
		Dominio dominio = new Dominio();
		dominio.setId(Integer.valueOf(element.getChild(ID).getValue()));
		dominio.setIdVocabularioDominio(Integer.valueOf(element.getChild(VOCABULARIO_DOMINIO).getValue()));
		dominio.setNome(element.getChildText(NOME));
		dominio.setNomeBase(element.getChildText(ARQUIVO));
		
		List<Element> vocabularios = element.getChild(VOCABULARIOS).getChildren();
		List<Integer> lista = new ArrayList<Integer>(vocabularios.size());
		for (Element vocabulario: vocabularios) {
			lista.add(Integer.parseInt(vocabulario.getText().trim()));
		}
		dominio.setVocabularios(lista);
		
		return dominio;
	}

	@Override
	public void removerDominio(Dominio dominio) {
		Document documento = XMLUtil.carregarXML(CONF_DOMINIO);
		
		if (documento == null) {
			return;
		}
		
		List<Element> dominios = documento.getRootElement().getChildren();
		for (Element d: dominios) {
			int idDominio = Integer.parseInt(d.getChild(ID).getValue());
			if (idDominio == dominio.getId()) {
				documento.getRootElement().removeContent(d);
				break;
			}
		}
		
		XMLUtil.salvarXML(documento, CONF_DOMINIO);
		
		carregarDominios();
	}

	@Override
	public void removerDominio(int id) {
		removerDominio(new Dominio(id));
	}

	@Override
	public List<Dominio> getAllDominios() {
		return dominios;
	}

	@Override
	public void carregarDominios() {
		Document documento = XMLUtil.carregarXML(CONF_DOMINIO);
		
		if (documento == null) {
			dominios = new ArrayList<Dominio>(0);
			return;
		}
		
		Element tagRootElement = documento.getRootElement();
		List<Dominio> dominios = new ArrayList<Dominio>(tagRootElement.getChildren().size());
		
		for (Element elemento: tagRootElement.getChildren()) {
			dominios.add(criarDominio(elemento));
		}
		
		this.dominios = dominios;
	}
	
	private int gerarIDDominio() {
		int id = 1;
		Document documento = XMLUtil.carregarXML(CONF_DOMINIO);
		if (documento == null){
			return id;
		}
		
		List<Element> dominios = documento.getRootElement().getChildren();
		
		//EXISTE MAS NAO TEM DOMINIOS SALVOS O ID EH 1
		if (dominios.isEmpty()) {
			return id;
		}
		
		for (Element dominio: dominios){
			int valor = Integer.parseInt(dominio.getChild(ID).getValue());
			if (valor > id) {
				id = valor;
			}
		}
		
		return ++id;
	}
}
