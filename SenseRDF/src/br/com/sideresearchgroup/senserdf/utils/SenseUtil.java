package br.com.sideresearchgroup.senserdf.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;

import org.apache.commons.validator.routines.UrlValidator;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;


/**
 * Classe que contém algumas operações em Comum na aplicação
 * 
 * @author Ayrton
 */
public class SenseUtil {

	/**
	 * Método que retira acentuação gráfica e caracteres especiais como ç.
	 * 
	 * @param String
	 *            arg0
	 * @return String
	 */
	public static String tratarCaracteres(String arg0) {
		if (arg0 != "") {
			arg0 = arg0.replaceAll(" ", "_");
			//arg0 = arg0.toLowerCase();
			arg0 = Normalizer.normalize(arg0, Normalizer.Form.NFD);
			return arg0.replaceAll("[^\\p{ASCII}]", "");
		} else {
			return arg0;
		}
	}

	/**
	 * Método que trata a gravação de arquivos na OwlAPI e AlignAPI
	 * 
	 * @param source
	 *            - Caminho do arquivo a ser gerado
	 * @param fileName
	 *            - Nome do arquivo
	 * @param format
	 *            - Formato do arquivo
	 * @return String - Local do arquivo formatado
	 */
	public static String tratarLocalGravacao(String source, String format) {
		String retornar = source.toString() +"." + format.toString();
		return tratarLocal(retornar);
	}

	/**
	 * Método que trata a gravação de arquivos na OwlAPI e AlignAPI
	 * 
	 * @param source
	 *            - Caminho do arquivo a ser gerado
	 * @param fileName
	 *            - Nome do arquivo
	 * @param format
	 *            - Formato do arquivo
	 * @return String - Local do arquivo formatado
	 */
	public static String tratarLocal(String source) {
		String f;
		if (Configuracao.isUnixSO())
			f = "file:";
		else
			f = "file:/";

		f = f.toString() + source.toString();
		source = f.replace("\\", "/");
		source = source.replaceAll(" ", "%20");

		return source;
	}

	/**
	 * Válida uma URL informada pelo usuário (Obrigado a Apache Software Foundation)
	 * @throws SenseRDFException 
	 */
	public static String validarEndereco(String url) throws SenseRDFException {
		UrlValidator validador = new UrlValidator();
		if (!validador.isValid(url))
			throw new SenseRDFException(Sap.URL_INVALIDA.get(), TipoMensagem.AVISO);
		
		return url;
	}

	/**
	 * Retorna o nome do arquivo de um path
	 * 
	 * @param local
	 * @return
	 */
	public static String nomeArquivoFormato(String local) {
		int barra;
		if (Configuracao.isUnixSO())
			barra = local.lastIndexOf("/");
		else
			barra = local.lastIndexOf("\\");

		return SenseUtil.tratarCaracteres(local.substring(barra + 1));
	}

	/**
	 * Irá ler um arquivo texto porém trazendo apenas as 1000 primeiros caracteres, ou as 30 primeiras linhas o que vier primeiro
	 * @param localArquivo
	 * @return
	 * @throws IOException
	 */
	public static String lerTextoPuroLimitado(String localArquivo) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(localArquivo));
		StringBuilder str = new StringBuilder();
		int i = 0;
		while (in.ready()) {
			StringBuilder str2 = new StringBuilder();
			str2.append(System.getProperty("line.separator"));
			str2.append(in.readLine());
			str.append(str2);
			i++;
			if (i == 30 || str.toString().length() >= 1000)
				break;
		}

		in.close();
		if (str.toString().length() > 1000)
			return str.toString().substring(0, 1000) + " \n ...";
		else
			return str.toString() + " \n ...";
	}
}
