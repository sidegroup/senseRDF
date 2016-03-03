/**
 * 
 */
package br.com.sideresearchgroup.senserdf.utils;

import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Parametro;

/**
 * @author Ayrton
 *
 */
public class ArquivoUtil {
	
	public static File abrirEscolhaArquivo(FileFilter filtro) {
		File arquivoSelecionado = null;
		File ultimoLocal = new File(Parametro.getInstance().get(Parametro.ULTIMO_PATH_USADO));
		
		JFileChooser fileChooser = new JFileChooser(ultimoLocal);
		Locale local = Configuracao.getLocale();
		fileChooser.setLocale(local);
		fileChooser.updateUI();
		fileChooser.setFileFilter(filtro);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			arquivoSelecionado = fileChooser.getSelectedFile();
			
			String pathArquivo = arquivoSelecionado.getPath();
			int index = 0;
			if (Configuracao.isUnixSO()) {
				index = pathArquivo.lastIndexOf("/");
			} else {
				index = pathArquivo.lastIndexOf("\\");
			}
			
			Parametro.getInstance().alterarConfiguracao(Parametro.ULTIMO_PATH_USADO, pathArquivo.substring(0, index));
		}
		
		return arquivoSelecionado;
	}
	
	public static String tratarPath(String path) {
		String pathTratado = null;
		
		if (!Configuracao.isUnixSO()) {
			pathTratado = path.replace("/", "\\");
		}
		
		return pathTratado;
	}
	
	public static void criarDiretorio(String nome) {
		File diretorio = new File(nome);
		diretorio.mkdir();
	}
	
	public static void excluirArquivos(String nomePasta) {
		File pasta = new File(nomePasta);
		File[] arquivos = pasta.listFiles();

		for (int i = 0; i < arquivos.length; i++) {
			
			if (arquivos[i].isDirectory())
				excluirArquivos(arquivos[i].getAbsolutePath());
			
			arquivos[i].delete();
		}
	}
}
