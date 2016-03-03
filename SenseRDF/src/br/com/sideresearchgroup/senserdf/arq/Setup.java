/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq;

import java.util.Locale;

import br.com.sideresearchgroup.senserdf.utils.ArquivoUtil;

/**
 * @author Ayrton
 *
 */
public final class Setup {
	
	
	public static void iniciarFerramenta() {
		
		//CRIA OS PARAMETROS 
		Parametro parametro = Parametro.getInstance();
		String linguagem = null;
		if (!parametro.configuracaoExiste()) {
			parametro.criarConfiguracoes(null);
			linguagem = System.getProperty("user.language");
		} else {
			linguagem = parametro.get(Parametro.IDIOMA);
		}
		
		//INICIALIZA O IDIOMA
		Configuracao.setLocale(new Locale(linguagem));
		
		//CRIA OS DIRETORIOS NECESSARIOS
		criarDiretorios();
		
		//INICIALIZAR OS SERVICOS
		Configuracao.getServicoVocabulario().carregarVocabularios();
		Configuracao.getServicoDominio().carregarDominios();
	}

	private static void criarDiretorios() {
		for (Diretorios d: Diretorios.values()) {
			ArquivoUtil.criarDiretorio(d.nome());
		}
	}
}
