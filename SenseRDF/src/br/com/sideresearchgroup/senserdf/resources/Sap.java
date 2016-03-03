package br.com.sideresearchgroup.senserdf.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;

/**
 * 
 * @author Ayrton
 * 
 */
public enum Sap {
	
	FECHAR("menu.fechar"),
	OPCOES("menu.opcoes"),
	GERENCIAR("menu.gerenciar"),
	SOBRE("menu.sobre"),
	IDIOMA("menu.idioma"),
	
	GERAR_RDF("principal.gerar.rdf"),
	CARREGAR("principal.carregar.dataset"),
	RDF_GERADO("principal.rdf.gerado"),
	URI("principal.uri"),
	
	ALTERAR("comum.alterar"),
	METADADOS("comum.metadados"),
	CAMINHO("comum.caminho"),
	ABRIR("comum.abrir"),
	CAMPO_OBRIGATORIO("comum.campo.obrigatorio"),
	DATASET("comum.dataset"),
	AVISO("comum.aviso"),
	ERRO("comum.erro"),
	INFORMACAO("comum.informacao"),
	COMPORTAMENTO_INESPERADO("comum.erro.interno"),
	RDF("comum.rdf"),
	CONCLUIR("comum.concluir"),
	CANCELAR("comum.cancelar"),
	SIM("comum.sim"),
	NAO("comum.nao"),
	DOMINIO("comum.dominio"),
	COMBO_SELECIONE("comum.combo.selecione"),
	NOVO("comum.novo"),
	SALVAR("comum.salvar"),
	NOME("comum.nome"),
	VOCABULARIO("comum.vocabulario"),
	VOCABULARIOS("comum.vocabularios"),
	CORRESPONDENCIA("comum.correspondencia"),
	CONCLUIDO("comum.concluido"),
	REMOVER("comum.remover"),
	TERMO("comum.termo"),
	TERMOS("comum.termos"),
	METADADO("comum.metadado"),
	ADICIONAR("comum.adicionar"),
	PREFIXO("comum.prefixo"),
	NAMESPACE("comum.namespace"),
	ESCOLHER("comum.escolher"),
	MENSAGEM("comum.mensagem"),
	CONFIANCA("comum.confianca"),
	
	COMPORTAMENTO_INESPERADO_MENSAGEM("comportamento.inesperado.mensagem"),
	
	MENSAGEM_CAMPO_OBRIGATORIO("mensagem.campo.obrigatorio"),
	MENSAGEM_AVISO_DATASET("mensagem.aviso.dataset"),
	MENSAGEM_AVISO_CANCELAR("mensagem.aviso.cancelar"),
	MENSAGEM_INFORMACAO_SUCESSO("mensagem.informacao.sucesso"),
	MENSAGEM_AVISO_REMOVER_ITEM("mensagem.aviso.remover.item"),
	MENSAGEM_COMBO_OBRIGATORIO("mensagem.aviso.selecionar.combo"),
	MENSAGEM_AVISO_FORMATO_RDF("mensagem.aviso.formato.rdf"),
	MENSAGEM_AVISO_FORMATO_NAOEDITAVEL("mensagem.aviso.formato.naoeditavel"),
	MENSAGEM_AVISO_METADADO_SEMTERMO("mensagem.aviso.metadado.semtermo"),
	MENSAGEM_AVISO_SEMDOMINIO_CONVERSAO("mensagem.aviso.sem.dominio"),
	MENSAGEM_AVISO_VALOR_INVALIDO("mensagem.aviso.valor.invalido"),
	
	ALTERAR_METADADOS_DESCRICAO("alterar.metadados.descricao"),
	DADO("alterar.metadados.dado"),
	
	GESTAO_DOMINIOS_DESCRICAO("gestao.dominios.descricao"),
	GESTAO_DOMINIOS_VOCABULARIO("gestao.dominios.vocabulario"),
	
	MATCHING_TITULO("matching.titulo"),
	MATCHING_DESCRICAO("matching.descricao"),
	MATCHING_METADADOS_TERMOS("matching.aviso.metadados.invalido"),
	MATCHING_CANCELAMENTO("matching.aviso.cancelamento"),
	MATCHING_TERMO_INVALIDO("matching.aviso.termo.invalido"),
	
	GESTAO_VOCABULARIOS_DESCRICAO("gestao.vocabularios.descricao"),
	GESTAO_VOCABULARIOS_TERMOS("gestao.aviso.termos"),
	GESTAO_VOCABULARIOS_NAMESPACE("gestao.aviso.namespace"),
	GESTAO_VOCABULARIOS_NAMESAPCE_JAEXISTE("gestao.aviso.namespace.existente"),
	GESTAO_VOCABULARIOS_PREFIXO_JAEXISTE("gestao.aviso.prefixo"),
	
	OPCOES_EXIBIR_RDF("opcoes.carregar.rdf"),
	OPCOES_EXIBIR_METADADOS_DESCONHECIDOS("opcoes.exibir.metadados.desconhecidos"),
	OPCOES_GERAR_RDF_FORMATO("opcoes.gerar.rdf"),
	OPCOES_GERAL("opcoes.geral"),
	OPCOES_GERACAO("opcoes.geracao"),
	
	SOBRE_DESCRICAO("sobre.descricao"),
	CREDITOS("sobre.creditos"),
	HOME("sobre.home"),
	
	STATUS_DOMINIO("status.dominio"),
	FILTRO_ARQUIVOS("filtro.arquivos"),
	FORMATO_RDF("formato.rdf"),
	URL_INVALIDA("mensagem.url.invalida"),
	XML_INVALIDO("mensagem.aviso.xml.invalido"),
	;
	
	
	private final String PATH_MENSAGENS = "br.com.sideresearchgroup.senserdf.resources.mensagens.mensagens";
	
	private String nome;
	
	private Sap(String nome) {
		this.nome = nome;
	}
	
	public String get(String ... parametros) {
		return get(nome, parametros);
	}
	
	public String get() {
		return get(nome, new String[]{});
	}
	
	private String get(String nome, String ... parametros) {
		String mensagem = null;
		try {
			mensagem = getResourceBundle().getString(nome);
		} catch(MissingResourceException mre) {
			mensagem = getResourceBundle(new Locale("pt")).getString(nome);
		}
		
		for(String parametro: parametros) {
			mensagem = mensagem.replaceFirst("%s", parametro);
		}
		
		mensagem = mensagem.replaceAll("%s", "");
		
		return mensagem.trim();
	}
	
	private ResourceBundle getResourceBundle() {
		return getResourceBundle(Configuracao.getLocale());
	}
	
	private ResourceBundle getResourceBundle(Locale locale) {
		return  ResourceBundle.getBundle(PATH_MENSAGENS, locale);
	}
}
