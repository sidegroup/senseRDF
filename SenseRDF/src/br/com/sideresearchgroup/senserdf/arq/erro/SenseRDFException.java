/**
 * 
 */
package br.com.sideresearchgroup.senserdf.arq.erro;

import br.com.sideresearchgroup.senserdf.resources.Sap;

/**
 * @author ayrton
 *
 */
public class SenseRDFException extends Exception{
	
	private static final long serialVersionUID = 6338784829439484687L;
	
	private String mensagem;
	
	private TipoMensagem tipo;

	public SenseRDFException(){
		super(Sap.COMPORTAMENTO_INESPERADO.get());
		mensagem = Sap.COMPORTAMENTO_INESPERADO.get();
		tipo = TipoMensagem.FATAL;
	}
	
	public SenseRDFException(Exception e) {
		this.initCause(e);
		tipo = TipoMensagem.FATAL;
	}
	
	public SenseRDFException(String erro, TipoMensagem tipo){
		super(erro);
		mensagem = erro;
		this.tipo = tipo;
	}

	public final String getMensagem() {
		return mensagem;
	}

	public final TipoMensagem getTipo() {
		return tipo;
	}
}
