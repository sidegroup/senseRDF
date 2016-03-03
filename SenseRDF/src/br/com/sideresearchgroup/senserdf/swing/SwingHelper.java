package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import br.com.sideresearchgroup.senserdf.arq.Formato;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.geracao.FormatoRDF;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.utils.ArquivoUtil;

public final class SwingHelper {
	
	public static final int JANELA_PRINCIPAL_ALTURA = 700;
	
	public static final int JANELA_PRINCIPAL_LARGURA = 1000;
	
	public static final Color COR_MENSAGEM_DESCRICAO = new Color(212, 212, 240);
	
	public static void tratarSenseException(JFrame frame , Throwable e) {

		//REALIZAR O TRATAMENTO PADRAO
		if ( e instanceof SenseRDFException) {
			SenseRDFException sre = (SenseRDFException) e;
			//TipoMensagem m = sre.getTipo();
			String mensagem = sre.getMensagem();
			
			JOptionPane.showMessageDialog(frame, mensagem, Sap.MENSAGEM.get(), converterTipoMensagem(sre.getTipo()));
		} else {
			//EXIBE COMPORTAMENTO NO CONSOLE
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, Sap.COMPORTAMENTO_INESPERADO_MENSAGEM.get(), Sap.COMPORTAMENTO_INESPERADO.get(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Converte os tipos de erros lancadas pela SenseRDFException nos tipos para exibicao das mensagens no JOptionaPane
	 * @param tipo
	 * @return
	 */
	private static int converterTipoMensagem(TipoMensagem tipo) {
		if (TipoMensagem.AVISO == tipo) {
			return JOptionPane.WARNING_MESSAGE;
		} else if (TipoMensagem.INFORMACAO == tipo) {
			return JOptionPane.INFORMATION_MESSAGE;
		} else {
			return JOptionPane.ERROR_MESSAGE;
		}
	}
	
	/**
	 * Dialogo que pergunta ao usuário em qual formato de RDF se deseja criar
	 * 
	 * @return
	 */
	public static FormatoRDF escolheOpcoesRDF() {
		//String[] formatos = { "RDF/XML", "N3" };
		FormatoRDF[] formatos = FormatoRDF.values();
		
		int n = JOptionPane.showOptionDialog(null, Sap.MENSAGEM_AVISO_FORMATO_RDF.get(),
				Sap.FORMATO_RDF.get(), JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, formatos, formatos[0]);
		
		return formatoGerado(n + 1);
	}
	
	public static FormatoRDF formatoGerado(int opcao){
		FormatoRDF formato = FormatoRDF.RDF_XML;
		if (opcao == 1){
			formato = FormatoRDF.RDF_XML;
		} else if (opcao == 2){
			formato = FormatoRDF.TURTLE;
		}
		
		return formato;
	}
	
	public static File buscarArquivoFiltro() {
		FileFilter filtro = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				
				if (pathname.isDirectory()) {
					return true;
				}
				
				for (Formato formato: Formato.values()) {
					if (!formato.isHabilitado()) {
						continue;
					}
					
					for (String extensao: formato.getExtensoes()) {
						if (pathname.getName().toLowerCase().endsWith(extensao)) {
							return true;
						}
					}
				}
				
				return false;
			}

			@Override
			public String getDescription() {
				StringBuilder formatosAceitos = new StringBuilder();
				for (Formato formato: Formato.values()) {
					if (!formato.isHabilitado()) {
						continue;
					}
					
					formatosAceitos.append(formato.name());
					formatosAceitos.append(", ");
				}
				
				return Sap.FILTRO_ARQUIVOS.get(formatosAceitos.toString());
			}
		};
		
		return ArquivoUtil.abrirEscolhaArquivo(filtro);
	}
	
	/**
	 * 
	 * @param frase
	 * @param titulo
	 * @param opcoes
	 * @return numero que representa a opção que foi passada como argumento,
	 *         começando de Zero;
	 */
	public static int questionarUsuario(String frase, String titulo, String... opcoes) {
		int n = JOptionPane.showOptionDialog(null, frase, titulo,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, opcoes, opcoes[0]);
		return n;
	}
}
