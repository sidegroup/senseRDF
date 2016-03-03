package br.com.sideresearchgroup.senserdf.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class XMLUtil {
	
	public static Element criarElemento(String nome, String conteudo) {
		Element elemento = new Element(nome);
		elemento.setText(conteudo);
		return elemento;
	}
	
	public static boolean salvarXML(Document documento, String path) {
		
		try (FileOutputStream f =new FileOutputStream(path)) {
		
			XMLOutputter xout = new XMLOutputter();
			xout.output(documento, f);
		
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static Document carregarJSON(String path) {
		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		File json = new File(path);
		
		if (json.exists()) { 
			try {
				documento = builder.build(json);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
		
		return documento;
	}
	
	public static Document carregarXML(String path) {
		SAXBuilder builder = new SAXBuilder();
		Document documento = null;
		File xml = new File(path);
		
		if (xml.exists()) { 
			try {
				documento = builder.build(xml);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
		
		return documento;
	}
}
