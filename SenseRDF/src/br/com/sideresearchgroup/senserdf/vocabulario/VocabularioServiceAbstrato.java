package br.com.sideresearchgroup.senserdf.vocabulario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Diretorios;
import br.com.sideresearchgroup.senserdf.utils.ArquivoUtil;

abstract class VocabularioServiceAbstrato implements VocabularioService {
	
	protected List<Vocabulario> vocabularios = new ArrayList<Vocabulario>(0);
	
	/**
	 * Retorna todos os vocabularios da ferramenta
	 */
	public List<Vocabulario> getAllVocabularios() {
		return this.vocabularios;
	}
	
	/**
	 * Retorna todos os vocabularios com seus construtores
	 */
	@Override
	public List<Vocabulario> getAllVocabulariosPreenchido() {
		String pathVocabularios = System.getProperty("user.dir").concat(Diretorios.DIRETORIO_VOCABULARIOS.nome());
		
		vocabularios.stream().forEach((vocabulario) -> {
			List<String> construtores = identificarConstrutores(pathVocabularios + vocabulario.getPath());
			vocabulario.setConstrutores(construtores);
		});
		return vocabularios;
	}
	
	/**
	 * Retorna o vocabulario que possue o ID indicado, se nao existir retorna null
	 */
	public Vocabulario getVocabularioById(int id) {
		Optional<Vocabulario> vocabulario = vocabularios.stream()
				.filter(v -> v.getId() == id).findFirst();
		if (vocabulario.isPresent()) {
			return vocabulario.get();
		}
		
		return null;
	}
	
	/**
	 * Retorna o vocabulario que possue o prefixo indicado, se nao existir retorna null
	 */
	public Vocabulario getVocabularioByPrefixo(String prefixo) {
		Optional<Vocabulario> vocabulario = vocabularios.stream()
						.filter(v -> v.getPrefixo().equalsIgnoreCase(prefixo))
							.findFirst();
														
		if (vocabulario.isPresent()) {
			return vocabulario.get();
		}
		
		return null;
	}
	
	/**
	 * Retorna o vocabulario que possue a namespace indicada, se nao existir retorna null
	 */
	public Vocabulario getVocabularioByNamespace(String namespace) {
		Optional<Vocabulario> vocabulario = vocabularios.stream().filter(v -> v.getNamespace().equalsIgnoreCase(namespace))
				.findFirst();
		if (vocabulario.isPresent()) {
			return vocabulario.get();
		}

		return null;
	}

	
	/**
	 * Remove todos os construtores do path do vocabulario
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public List<String> identificarConstrutores(String path) {
		File vocabulario = new File(path);
		if (!vocabulario.exists()) {
			throw new NullPointerException();
		}
		
		List<String> construtores = new ArrayList<String>();
		String namespace = null;
		
		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		try {
			documento = builder.build(vocabulario);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		
		Element raiz = documento.getRootElement();

		List<Element> filhos = raiz.getChildren();
		for (Element filho : filhos) {
			//RDF || RDF's || OWL deve ser ignoradas essa propriedade
			if (filho.getName().equals("AnnotationProperty"))
				continue;

			List<Attribute> valor = filho.getAttributes();
			for (Attribute val : valor) {
				//a primeira tag dos vocabulários possui a URI, que é um atributo
				if (namespace == null) {
					namespace = val.getValue();
					continue;
				}

				if (val.getName().equalsIgnoreCase("about")) {
					String valorA = val.getValue();
					if (valorA.startsWith(namespace) || valorA.startsWith("#")) {
						valorA = valorA.replace(namespace, "");
						valorA = valorA.replace("#", "");
						construtores.add(valorA);
					}
				} else if (val.getName().equalsIgnoreCase("id")) {
					String valorA = val.getValue();
					construtores.add(valorA);
				}
			}
		}
		
		return construtores;
	}
	
	@Override
	public Vocabulario getVocabularioPreenchido(int id) {
		Vocabulario vocabulario = getVocabularioById(id);
		String path = ArquivoUtil.tratarPath(Configuracao.diretorioExecucao()+"\\"+Diretorios.DIRETORIO_VOCABULARIOS.nome() + vocabulario.getPath());
		List<String> construtores = identificarConstrutores(path);
		vocabulario.setConstrutores(construtores);
		return vocabulario;
	}
}
