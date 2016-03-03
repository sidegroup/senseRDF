/**
 * 
 */
package br.com.sideresearchgroup.senserdf.conversor.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.TriplaRDFSense;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.conversor.Extrator;

/**
 * @author Ayrton
 *
 */
public class JSONExtrator implements Extrator {
	
	@Override
	public void extrairMetadados(Dataset dataset) throws SenseRDFException {
		
			JSONObject jsonObject;
			//Cria o parse de tratamento
			JSONParser parser = new JSONParser();
			//Variaveis que irao armazenar os dados do arquivo JSON
			String nome;
			String sobrenome;
			String estado;
			String pais;

			try {
				//Salva no objeto JSONObject o que o parse tratou do arquivo
				jsonObject = (JSONObject) parser.parse(new FileReader(dataset.getPathArquivo()));
				Set recebejson = jsonObject.entrySet();
				
				Set<String> candidatos = ((HashMap) jsonObject).keySet();
				for(String metadados : candidatos){
					System.out.println(metadados);
					Metadado m = dataset.buscarOuCriaMetadado(metadados);
					TriplaRDFSense tripla = new TriplaRDFSense(m, null);
					dataset.getTriplas().add(tripla);
				}	
				
				nome = (String) jsonObject.get("nome");
				sobrenome = (String) jsonObject.get("sobrenome");
				estado = (String) jsonObject.get("estado");
				pais = (String) jsonObject.get("pais");
				
				System.out.printf(
						"Nome: %s\nSobrenome: %s\nEstado: %s\nPais: %s\n",
						nome, sobrenome, estado, pais);
				
				/*
				for (Entry<String, String> metadado : metadados.entrySet()) {
					String nome = metadado.getKey();
					String valor = metadado.getValue();
					
					Metadado m = dataset.buscarOuCriaMetadado(nome);
					TriplaRDFSense tripla = new TriplaRDFSense(m, valor);
					dataset.getTriplas().add(tripla);
				}
				
				
				Iterator valores = recebejson.iterator();
				
				while(valores.hasNext()){
					System.out.println(valores);
				}
				
				for (Map.Entry candidatoEntry : recebejson.entrySet()) {
					System.out.println("Candidato: %s Votos: %d%n", 
					candidatoEntry.getKey(), candidatoEntry.getValue());
				}
				
				//HashMap.Entry<String, String> dadosjson = (Entry<String, String>) jsonObject.entrySet();
				//String value = dadosjson.getValue();
				
				Map<String, Object> map = new TreeMap<String, Object>();
				
				for (Map.Entry<String, String> candidatoEntry :dadosjson.getKey()) {
					   System.out.println("Candidato: %s Votos: n", candidatoEntry.getKey(), candidatoEntry.getValue());
					}
				
				
				for (Entry<String, String> metadado : metadados.entrySet()) {
					String nome = metadado.getKey();
					String valor = metadado.getValue();
					
					Metadado m = dataset.buscarOuCriaMetadado(nome);
					TriplaRDFSense tripla = new TriplaRDFSense(m, valor);
					dataset.getTriplas().add(tripla);
				}
				Metadado m = dataset.buscarOuCriaMetadado(nome);
				TriplaRDFSense tripla = new TriplaRDFSense(m, nome);
				dataset.getTriplas().add(tripla);
				*/
				
			} 
			//Trata as exceptions que podem ser lançadas no decorrer do processo
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		}
}
