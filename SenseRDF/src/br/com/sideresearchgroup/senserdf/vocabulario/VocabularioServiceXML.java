/**
 * 
 */
package br.com.sideresearchgroup.senserdf.vocabulario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Diretorios;
import br.com.sideresearchgroup.senserdf.utils.XMLUtil;

/**
 * @author Ayrton Nadgel
 */
public class VocabularioServiceXML extends VocabularioServiceAbstrato {
	
	private final String LOCAL_CONFIG_VOCABULARIO = Diretorios.DIRETORIO_CONFIGURACAO.nome()+"configVocabulario.xml";
	private final String VOCABULARIOS = "vocabularios";
	private final String VOCABULARIO = "vocabulario";
	private final String ID = "id";
	private final String NOME = "nome";
	private final String PREFIXO = "prefixo";
	private final String NAMESPACE = "namespace";
	private final String PATH = "arquivo";
	
	public void adicionarVocabulario(Vocabulario vocabulario) {
		
		if (vocabulario.getId() == 0) {
			File arquivoVocabulario = new File(vocabulario.getPath());
			try (FileChannel sourceChannel = new FileInputStream(arquivoVocabulario).getChannel();
					FileChannel destinationChannel = new FileOutputStream(Configuracao.diretorioExecucao() + "/" + Diretorios.DIRETORIO_VOCABULARIOS + arquivoVocabulario.getName()).getChannel()) {
			
				sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			vocabulario.setPath(arquivoVocabulario.getName());
		}
		
		
		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		File configuracao = new File(LOCAL_CONFIG_VOCABULARIO);
		
		if (configuracao.exists()) { 
			try {
				documento = builder.build(configuracao);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
				return;
			}
			
			Element elementoRaiz = documento.getRootElement();
		
			if (vocabulario.getId() == 0) {
				vocabulario.setId(gerarIDVocabulario());
			} else {
				for (Element elementVocabulario: elementoRaiz.getChildren()) {
					int idVocabulario = Integer.parseInt(elementVocabulario.getChild(ID).getValue());
					if (idVocabulario == vocabulario.getId()) {
						documento.getRootElement().removeContent(elementVocabulario);
						break;
					}
				}
			}
			
			elementoRaiz.addContent(criarElementByVocabulario(vocabulario));
		
		} else {
			documento = new Document();
			Element elementoRaiz = new Element(VOCABULARIOS);
			vocabulario.setId(gerarIDVocabulario());
			elementoRaiz.setContent(criarElementByVocabulario(vocabulario));
			documento.setRootElement(elementoRaiz);
		}
		
		XMLUtil.salvarXML(documento, LOCAL_CONFIG_VOCABULARIO);
		
		carregarVocabularios();
	}
	
	public void removerVocabulario(Vocabulario vocabulario) {
		Vocabulario vocabularioRemover = getVocabularioById(vocabulario.getId());
		
		
		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		File configuracao = new File(LOCAL_CONFIG_VOCABULARIO);
		
		if (configuracao.exists()) { 
			try {
				documento = builder.build(configuracao);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
				return;
			}
			
			Element elementoRaiz = documento.getRootElement();
			for (Element elementVocabulario: elementoRaiz.getChildren()) {
				int idVocabulario = Integer.parseInt(elementVocabulario.getChild(ID).getValue());
				if (idVocabulario == vocabulario.getId()) {
					elementoRaiz.removeContent(elementVocabulario);
					break;
				}
			}
			
			XMLUtil.salvarXML(documento, LOCAL_CONFIG_VOCABULARIO);
			
			File arquivo = new File(Diretorios.DIRETORIO_VOCABULARIOS + vocabularioRemover.getPath());
			arquivo.delete();
		}
		carregarVocabularios();
	}
	
	public void carregarVocabularios() {
		vocabularios.clear();
		
		File configuracao = new File(LOCAL_CONFIG_VOCABULARIO);
		if (!configuracao.exists()) {
			return;
		}
		
		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		try {
			documento = builder.build(configuracao);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			return;
		}
		
		Element raiz = documento.getRootElement(); //TAG VOCABULARIOS
		raiz.getChildren().stream().forEach((elementoVocabulario) -> {
			vocabularios.add(criarVocabulario(elementoVocabulario));
		});
	}
	
	/**
	 * Cria um objeto Vocabulario de acordo com um Element (vocabulario) da configuracao
	 * @param elemento
	 * @return
	 */
	private Vocabulario criarVocabulario(Element elemento) {
		String id = elemento.getChild(ID).getValue();
		String nome = elemento.getChild(NOME).getValue();
		String prefixo = elemento.getChild(PREFIXO).getValue();
		String namespace = elemento.getChild(NAMESPACE).getValue();
		String path = elemento.getChild(PATH).getValue();
		
		Vocabulario vocabulario = new Vocabulario();
		vocabulario.setId(Integer.parseInt(id));
		vocabulario.setNome(nome);
		vocabulario.setPrefixo(prefixo);
		vocabulario.setNamespace(namespace);
		vocabulario.setPath(path);
		
		return vocabulario;
	}
	
	/**
	 * Cria um Element (vocabulario) de acordo com o objeto vocabulario
	 * @param vocabulario
	 * @return
	 */
	private Element criarElementByVocabulario(Vocabulario vocabulario) {
		Element elementoVocabulario = new Element(VOCABULARIO);
		elementoVocabulario.addContent(XMLUtil.criarElemento(ID, String.valueOf(vocabulario.getId())));
		elementoVocabulario.addContent(XMLUtil.criarElemento(NOME, vocabulario.getNome()));
		elementoVocabulario.addContent(XMLUtil.criarElemento(PREFIXO, vocabulario.getPrefixo()));
		elementoVocabulario.addContent(XMLUtil.criarElemento(NAMESPACE, vocabulario.getNamespace()));
		elementoVocabulario.addContent(XMLUtil.criarElemento(PATH, vocabulario.getPath()));
		
		return elementoVocabulario;
	}
	
	private int gerarIDVocabulario() {
		int id = 1;
		File configuracao = new File(LOCAL_CONFIG_VOCABULARIO);
		
		//SE NAO EXISTE O ID EH 1
		if (!configuracao.exists()) { 
			return id;
		}

		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		try {
			documento = builder.build(configuracao);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			return -1;
		}
		List<Element> vocabularios = documento.getRootElement().getChildren();
		
		//EXISTE MAS NAO TEM VOCABULARIOS SALVOS O ID EH 1
		if (vocabularios.isEmpty()) {
			return id;
		}
		
		for (Element vocabulario: vocabularios){
			int valor = Integer.parseInt(vocabulario.getChild(ID).getValue());
			if (valor > id) {
				id = valor;
			}
		}
		
		return ++id;
	}
}
