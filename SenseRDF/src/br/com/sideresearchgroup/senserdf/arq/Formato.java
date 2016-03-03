package br.com.sideresearchgroup.senserdf.arq;

import br.com.sideresearchgroup.senserdf.conversor.Extrator;
import br.com.sideresearchgroup.senserdf.conversor.ExtratorNulo;
import br.com.sideresearchgroup.senserdf.conversor.json.JSONExtrator;
import br.com.sideresearchgroup.senserdf.conversor.pdf.PDFExtrator;
import br.com.sideresearchgroup.senserdf.conversor.xml.XMLExtrator;

/**
 * Formatos de dados suportados para conversão de dados
 * @author Ayrton
 * @version 1.0
 */
public enum Formato {
	PDF(true, true, new String[] {"application/pdf",".pdf"}, PDFExtrator.class),
	XML(false, true, new String[] {"text/xml", ".xml"}, XMLExtrator.class),
	JSON(false, true, new String[] {".json"}, JSONExtrator.class);
	
	private boolean metadadosEditavel;
	
	private boolean habilitado;
	
	private String[] extensoes;
	
	private Class<? extends Extrator> classe;
	
	private Formato(boolean metadadoEditavel, boolean habilitado, String[] extensoes, Class<? extends Extrator> classe) {
		this.metadadosEditavel = metadadoEditavel;
		this.habilitado = habilitado;
		this.extensoes = extensoes;
		this.classe = classe;
	}
	
	/**
	 * Informa se o formato atual podem ter os metadados alterados
	 * @return
	 */
	public boolean isMetadadosEditavel() {
		return metadadosEditavel;
	}

	/**
	 * Informa se o Formato atual esta habilidado para uso na ferramenta
	 * @return
	 */
	public boolean isHabilitado() {
		return habilitado;
	}

	/**
	 * Retorna os mymetypes do formato atual
	 * @return
	 */
	public String[] getExtensoes() {
		return extensoes;
	}
	
	public Extrator getExtrator() {
		try {
			return classe.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return new ExtratorNulo();
	}
}
