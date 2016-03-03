/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.sideresearchgroup.senserdf.arq.Diretorios;
import br.com.sideresearchgroup.senserdf.arq.Entidade;
import br.com.sideresearchgroup.senserdf.arq.Formato;
import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.Parametro;
import br.com.sideresearchgroup.senserdf.arq.TriplaRDFSense;
import br.com.sideresearchgroup.senserdf.arq.Validate;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.geracao.FormatoRDF;

/**
 * @author Ayrton Nadgel
 */
public class Dataset extends Entidade implements Validate {
	
	private static int sequencia = 1;
	
	private File arquivo;
	
	private Formato formato;
	
	private Dominio dominio;
	
	private String uri;
	
	private List<Metadado> metadados = new ArrayList<>(10);
	
	private ArrayList<TriplaRDFSense> triplas = new ArrayList<>(10);
	
	private FormatoRDF formatoRDF;
	
	private File arquivoRDF;
	
	private String nomeDiretorio;
	
	public Dataset(File arquivo) {
		setArquivo(arquivo);
		setUri(Parametro.getInstance().get(Parametro.URI_PADRAO));
		setId(sequencia++);
		criarNomeDiretorio(getId());
	}

	private void setArquivo(File arquivo) {
		this.arquivo = arquivo;
		
		String type = recuperaMymetypeArquivo(arquivo);
		
		
		//VERIFICA SE O MIMETYPE EH UM DOS INDENTIFICADOS PELA FERRAMENTA
		for (Formato formato: Formato.values()) {
			if (!formato.isHabilitado()) {
				continue;
			}
			
			for (String mime: formato.getExtensoes()) {
				if (mime.equalsIgnoreCase(type)) {
					this.formato = formato;
					break;
				}
			}
			
			if (this.formato != null) {
				break;
			}
		}
	}
	
	/**
	 * Tenta recuperar o mymetype do arquivo, se nao eh possivel reconhecer o metodo 
	 * ira retornar a extensao do arquivo
	 * @param arquivo
	 * @return
	 */
	private String recuperaMymetypeArquivo(File arquivo) {
		String type = null;
		//RECUPERA MIMETYPE
		try {
			type = Files.probeContentType(arquivo.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (type == null) {
			int index = arquivo.getName().lastIndexOf(".");
			type = arquivo.getName().substring(index, arquivo.getName().length());
		}
		
		return type;
	}
	
	@Override
	public boolean validar() throws SenseRDFException {
		if (uri == null) {
			throw new SenseRDFException("O dataset não possui uma URI válida.", TipoMensagem.AVISO);
		}
		
		if (dominio == null) {
			throw new SenseRDFException("O dataset não está associado a um domínio.", TipoMensagem.AVISO);
		}
		
		return true;
	}
	
	public boolean isEditavel() {
		return getFormato().isMetadadosEditavel();
	}
	
	public String getPathArquivo() {
		if (arquivo != null) {
			return arquivo.getAbsolutePath();
		}
		
		return "";
	}
	
	public String getNomeArquivo() {
		if (arquivo != null) {
			return arquivo.getName();
		}
		
		return "";
	}
	
	public Metadado buscarMetadado(String metadado) {
		return metadados.stream()
				.filter(m -> m.getMetadado().equalsIgnoreCase(metadado))
					.findFirst().orElse(null);
	}
	
	public Metadado buscarOuCriaMetadado(String metadado) {
		Optional<Metadado> optinal = metadados.stream()
								.filter(m -> m.getMetadado().equalsIgnoreCase(metadado))
									.findFirst();
		Metadado retorno = null;
		if (optinal.isPresent()) {
			retorno = optinal.get();
		} else {
			retorno = new Metadado(metadado);
			this.metadados.add(retorno);
		}
		
		return retorno;
	}

	public File getArquivo() {
		return arquivo;
	}

	public Formato getFormato() {
		return formato;
	}

	public void setFormato(Formato formato) {
		this.formato = formato;
	}

	public File getArquivoRDF() {
		return arquivoRDF;
	}

	public void setArquivoRDF(File arquivoRDF) {
		this.arquivoRDF = arquivoRDF;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public FormatoRDF getFormatoRDF() {
		return formatoRDF;
	}

	public void setFormatoRDF(FormatoRDF formatoRDF) {
		this.formatoRDF = formatoRDF;
	}

	public final List<Metadado> getMetadados() {
		return metadados;
	}

	public final void setMetadados(List<Metadado> metadados) {
		this.metadados = metadados;
	}

	public final ArrayList<TriplaRDFSense> getTriplas() {
		return triplas;
	}

	public final void setTriplas(ArrayList<TriplaRDFSense> triplas) {
		this.triplas = triplas;
	}
	
	private void criarNomeDiretorio(int id) {
		LocalDateTime agora = LocalDateTime.now();
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		nomeDiretorio = Diretorios.DIRETORIO_CONVERSAO+agora.format(formatador)+"_"+id+"/"; 
	}
	
	public String diretorioConversao() {
		return this.nomeDiretorio;
	}
}
