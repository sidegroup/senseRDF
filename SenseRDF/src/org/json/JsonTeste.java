package org.json;
import org.json.*;
public class JsonTeste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject my_obj = new JSONObject();
		my_obj.put("titulo", "json x xml: a batalha final");
		my_obj.put("ano", "2012");
		my_obj.put("genero", "ação");
		
		String json_string = my_obj.toString();
		System.out.println("objeto original ->"+json_string);
		System.out.println();
		
		String titulo = my_obj.getString("titulo");
		Integer ano = my_obj.getInt("ano");
		String genero = my_obj.getString("genero");
		
		System.out.println("Titulo: "+titulo);
		System.out.println("ano: "+ano);
		System.out.println("genero: "+genero);
	}

}
