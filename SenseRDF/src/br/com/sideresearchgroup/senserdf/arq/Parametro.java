/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author anadgel
 *
 */
public class Parametro {
	public static final String CARREGAR_RDF_GERADO = "senserdf.carregar.rdf";
	public static final String IDIOMA = "senserdf.idioma";
	public static final String URI_PADRAO = "senserdf.uri.padrao";
	public static final String PERGUNTAR_SINTAXE_RDF = "senserdf.sintaxe.rdf.fixo";
	public static final String SINTAXE_RDF = "senserdf.sintaxe.rdf.valor";
	public static final String DOMINIO_ATUAL = "senserdf.dominio.selecionado";
	public static final String ULTIMO_PATH_USADO = "senserdf.last.path";
	public static final String MEDIDA_CONFIANCA_ALINHAMENTO = "senserdf.matching.valor";
	public static final String VALIDAR_TODOS_METADADOS = "senserdf.matching.validacao.todos";
	
	private static Parametro parametro = null;
	private static final String PATH_PARAMETROS = "setting.properties";
	private final HashMap<String, String> valoresPadroes = new HashMap<>(8);
	
	public static Parametro getInstance(){
		if (parametro == null){
			parametro = new Parametro();
		}
		
		return parametro;
	}
	
	private Parametro(){
		valoresPadroes.put(CARREGAR_RDF_GERADO,"true");
		valoresPadroes.put(IDIOMA, "pt");
		valoresPadroes.put(URI_PADRAO, "http://wwww.sidereserarchgroup.com.br/");
		valoresPadroes.put(PERGUNTAR_SINTAXE_RDF, "true");
		valoresPadroes.put(SINTAXE_RDF, "");
		valoresPadroes.put(DOMINIO_ATUAL, "");
		valoresPadroes.put(ULTIMO_PATH_USADO, "");
		valoresPadroes.put(MEDIDA_CONFIANCA_ALINHAMENTO, "0.8");
		valoresPadroes.put(VALIDAR_TODOS_METADADOS, "false");
	}
	
	public boolean configuracaoExiste() {
		File setting = new File(PATH_PARAMETROS);
		
		//CRIAR O ARQUIVO CASO NAO EXISTA
		return setting.exists();
	}
	
	public boolean getAsBoolean(String parametro) {
		String valor = get(parametro);
		return Boolean.parseBoolean(valor);
	}
	
	public double getAsDouble(String parametro) {
		String valor = get(parametro);
		return Double.parseDouble(valor);
	}
	
	public int getAsInt(String parametro) {
		String valor = get(parametro);
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public String get(String  parametro) {
		String valor = carregarProperties().getProperty(parametro);
		return valor == null ? "" : valor; 
	}
	
	public void alterarConfiguracao(String parametro, String valor) {
		Properties properties = carregarProperties();
		properties.setProperty(parametro, valor);
		criarConfiguracoes(properties);
	}
	
	private Properties carregarProperties() {
		File file = new File(PATH_PARAMETROS);      
		Properties props = new Properties(); 
		
		try (FileInputStream fis = new FileInputStream(file)){  
		    //lê os dados que estão no arquivo  
		    props.load(fis);    
		    fis.close();  
		}  catch (IOException ex) {  
		    System.out.println(ex.getMessage());  
		    ex.printStackTrace();  
		}
		
		return props;
	}
	
	public void criarConfiguracoes(Properties properties) {
		File file = new File(PATH_PARAMETROS);
		
		if (properties == null) {
			Properties props = new Properties();
			valoresPadroes.keySet().stream().forEach(configuracao -> {
				props.setProperty(configuracao, valoresPadroes.get(configuracao).toString());
			});
			
			properties = props;
		}
		
		
		FileOutputStream fos = null;  
		try {  
		    fos = new FileOutputStream(file);  
		    //grava os dados  no arquivo  
		    properties.store(fos, "Opções da SenseRDF");    
		    fos.close();  
		}  catch (IOException ex) {  
		    System.out.println(ex.getMessage());  
		    ex.printStackTrace();  
		}  
	}
}
