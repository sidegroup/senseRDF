package br.com.sideresearchgroup.senserdf.swing;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

	private List<Produto> produtos = new ArrayList<Produto>();
	
	public void addProduto(Produto p){
		produtos.add(p);
	}
	
	public void imprimirProdutos(){
		for(Produto p : produtos){
			System.out.println(p);
		}
	}
	
}
