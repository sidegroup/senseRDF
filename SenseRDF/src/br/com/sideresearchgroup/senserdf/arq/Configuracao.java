/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq;

import java.util.Locale;

import br.com.sideresearchgroup.senserdf.conversor.GerenciadorDataset;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.dominio.DominioServiceXML;
import br.com.sideresearchgroup.senserdf.geracao.GeradorJenaRDF;
import br.com.sideresearchgroup.senserdf.geracao.GeradorRDF;
import br.com.sideresearchgroup.senserdf.matching.AlignmentMatchingService;
import br.com.sideresearchgroup.senserdf.matching.MatchingService;
import br.com.sideresearchgroup.senserdf.save.CorrespondenciaService;
import br.com.sideresearchgroup.senserdf.save.db4o.GerenciadorCorrespondencia;
import br.com.sideresearchgroup.senserdf.swing.GerenciadorDatasetSwing;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioService;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioServiceXML;

/**
 * @author Ayrton Nadgel
 */
public final class Configuracao {
	
	public static final String NOME_FERRAMENTA = "SenseRDF";
	
	public static final String URL_PROJETO = "http://www.sideresearchgroup.com.br/";
	
	public static final String VERSAO = "2.0.0.150716";
	
	private static Locale locale = null;
	
	private static Boolean unix_so = null;
	
	private static VocabularioService servicoVocabulario = null;
	
	private static DominioService servicoDominio = null;
	
	private static MatchingService servicoMatchcing = null;
	
	private static GerenciadorDataset gerenciadorDataset = null;
	
	private static GeradorRDF geradorRDF = null;
	
	private static CorrespondenciaService servicoCorrespondencia = null;

	public static final VocabularioService getServicoVocabulario() {
		if (servicoVocabulario == null) {
			servicoVocabulario = new VocabularioServiceXML();
		}
		
		return servicoVocabulario;
	}

	public static final void setServicoVocabulario(VocabularioService servicoVocabulario) {
		Configuracao.servicoVocabulario = servicoVocabulario;
	}
	
	public static final DominioService getServicoDominio() {
		if (servicoDominio == null) {
			servicoDominio = new DominioServiceXML();
		}
		return servicoDominio;
	}
	
	public static final void setServicoDominio(DominioService servicoDominio) {
		Configuracao.servicoDominio = servicoDominio;
	}
	
	public static final GerenciadorDataset getGerenciadorDataset() {
		if (gerenciadorDataset == null) {
			gerenciadorDataset = new GerenciadorDatasetSwing();
		}
		
		return gerenciadorDataset;
	}
	
	public static final MatchingService getServicoMatching() {
		if (servicoMatchcing == null) {
			servicoMatchcing = new AlignmentMatchingService();
		}
		
		return servicoMatchcing;
	}
	
	public static final void setServicoMatching(MatchingService service) {
		servicoMatchcing = service;
	}
	
	public static final GeradorRDF getGeradorRDF() {
		if (geradorRDF == null) {
			geradorRDF = new GeradorJenaRDF();
		}
		
		return geradorRDF;
	}
	
	public static final void setGeradorRDF(GeradorRDF gerador) {
		geradorRDF = gerador;
	}
	
	public static final CorrespondenciaService getCorrespondenciaService() {
		if (servicoCorrespondencia == null) {
			servicoCorrespondencia = new GerenciadorCorrespondencia();
		}
		
		return servicoCorrespondencia;
	}
	
	public static final void setServicoCorrespondencia(CorrespondenciaService service) {
		servicoCorrespondencia = service;
	}
	
	public static final void inicializarServicos() {
		getServicoVocabulario().carregarVocabularios();
		getServicoDominio().carregarDominios();
	}
	
	public static void setLocale(Locale locale) {
		Parametro.getInstance().alterarConfiguracao(Parametro.IDIOMA, locale.getLanguage());
		Configuracao.locale = locale;
	}
	
	public static Locale getLocale() {
		return Configuracao.locale;
	}
	
	public static String diretorioExecucao() {
		return System.getProperty("user.dir");
	}
	
	public static boolean isUnixSO() {
		if (unix_so == null) {
			String nomeSo = System.getProperty("os.name");
			if (nomeSo.endsWith("inux") || nomeSo.startsWith("Mac")) {
				unix_so = true;
			} else {
				unix_so = false;
			}
		}
		
		return unix_so;
	}
}
