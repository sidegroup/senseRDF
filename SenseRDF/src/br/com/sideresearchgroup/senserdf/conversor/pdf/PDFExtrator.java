/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.TriplaRDFSense;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.conversor.Extrator;

/**
 * @author Ayrton
 *
 */
public class PDFExtrator implements Extrator {

	/* (non-Javadoc)
	 * @see br.com.sideresearchgroup.senserdf.conversor.Extrator#extrairMetadados(br.com.sideresearchgroup.senserdf.conversor.Dataset)
	 */
	@Override
	public void extrairMetadados(Dataset dataset) throws SenseRDFException {
		try {
			
			PdfReader extrator = new PdfReader(dataset.getPathArquivo());
			HashMap<String, String> metadados = extrator.getInfo();
		
			//CRIA ALGUNS METADADOS DEFAULT
			//normalizarMetadados(metadados);
			
			//CRIA OS METADADOS PARA REALIZAR A CONVERSAO
			for (Entry<String, String> metadado : metadados.entrySet()) {
				String nome = metadado.getKey();
				String valor = metadado.getValue();
				
				Metadado m = dataset.buscarOuCriaMetadado(nome);
				TriplaRDFSense tripla = new TriplaRDFSense(m, valor);
				dataset.getTriplas().add(tripla);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new SenseRDFException();
		}
	}
	/*
	private void normalizarMetadados(HashMap<String, String> metadados) {
		
		if(!metadados.containsKey("Subject")) {
			metadados.put("Subject", " ");
		}
        
		if(!metadados.containsKey("Author")) {
        	metadados.put("Author", " ");
        }
        
        if(!metadados.containsKey("Producer")) {
        	metadados.put("Producer", " ");
        }
        
        if(!metadados.containsKey("Keywords")) {
        	metadados.put("Keywords", " ");
        }
        
        if(!metadados.containsKey("Title")) {
        	metadados.put("Title", " ");
        }
	}

	@Override
	public void alterarMetadados(Dataset dataset, Object obj) throws SenseRDFException {
		@SuppressWarnings("unchecked")
		Map<String, String> metadados = (Map<String, String>) obj;
		String path = dataset.getPathArquivo();
		
		try {
			//Cria o nome do novo arquivo PDF
			StringBuffer pdfDestinationBuffer = new StringBuffer(path);
			int num = path.indexOf(".pdf");
			pdfDestinationBuffer.insert(num, "_CHANGED");
	
			PdfReader reader = new PdfReader(path);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfDestinationBuffer.toString()));
			
			//Adiciona os novos metadados no PDF
			stamper.setMoreInfo(metadados);
			stamper.close();
		
		} catch (Exception e){
			e.printStackTrace();
			throw new SenseRDFException();
		}
		
		for (Entry<String, String> m: metadados.entrySet()) {
			String chave = m.getKey();
			String valor = m.getValue();
			
			for (TriplaRDFSense t: dataset.getTriplas()) {
				if (t.getMetadado().equals(chave)) {
					t.setDado(valor);
					break;
				}
			}
		}
	}
*/
}
