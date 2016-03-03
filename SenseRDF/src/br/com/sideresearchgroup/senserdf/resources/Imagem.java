package br.com.sideresearchgroup.senserdf.resources;

import java.net.URL;

public enum Imagem {
	CONFIGURACAO("1920_16x16.png"),
	LIXEIRA("5031_16x16.png"),
	IDIOMA("5961_16x16.png"),
	EDITAR("277_16x16.png"),
	SALVAR("8370_16x16.png"),
	CARREGAR("8371_16x16.png"),
	REFLESH("8376_16x16.png"),
	ESTRELA("8380_16x16.png"),
	SETA_ESQUERDA("8395_16x16.png"),
	PAGINA_BRANCO("8410_16x16.png"),
	SETA_BAIXO("8411_16x16.png"),
	EXPORTAR("8414_16x16.png"),
	SETA_DIREITA("8415_16x16.png"),
	IMPORTAR("841_16x16.png"),
	HD("9582_16x16.png"),
	ADICIONAR("add.png"),
	CANCELAR("cancelar.png"),
	FECHAR_VERMELHO("close_16.png"),
	CONCLUIR("concluir.png"),
	FECHAR_PRETO("fechar.png"),
	SENSERDF_48("LogoSense48.png"),
	SENSERDF_64("LogoSense64.png"),
	STATUS_VERDE("status1.png"),
	STATUS_AMARELO("status2.png"),
	STATUS_VERMELHO("status3.png"),
	VOLTAR_PRETO("voltar.png")
	;
	
	private String PATH_DEFAULT = "/br/com/sideresearchgroup/senserdf/resources/imagens/";
	
	private String fileName;
	
	private Imagem(String filename) {
		this.fileName = filename;
	}
	
	public URL getURL() {
		return getClass().getResource(PATH_DEFAULT+fileName);
	}
	
	public String nomeArquivo() {
		return this.fileName;
	}
}

