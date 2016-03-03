package br.com.sideresearchgroup.senserdf.arq;

import java.util.ArrayList;

public class TriplaRDFSense {
	private Metadado metadado1 = null;
	private String dado = null;

	// Lista de atributos de uma Tripla
	private ArrayList<TriplaRDFSense> atributos = new ArrayList<TriplaRDFSense>();
	// Triplas podem ser formados por outras triplas
	private ArrayList<TriplaRDFSense> colecaoTriplas = new ArrayList<TriplaRDFSense>();
	// Aqui � uma cole��o de metadados
	private ArrayList<TriplaRDFSense> colecaoMetadado = new ArrayList<TriplaRDFSense>();
	
	/**
	 * Construtor 4
	 * @param metadado
	 * @param dado
	 */
	public TriplaRDFSense(Metadado metadado, String dado){
		this.setMetadado(metadado);
		this.setDado(dado);
	}

	/**
	 * Construtor 2 - para o caso de nn ter o metadado nem o dado
	 */
	public TriplaRDFSense() {
	}
	
	/**
	 * Adiciona apenas uma Tripla, representado outros triplas, a colecao;
	 * 
	 * @param other
	 */
	public void addTriplaNaColecao(TriplaRDFSense other) {
		colecaoTriplas.add(other);
	}

	/**
	 * Adiciona apenas um atributo a Tripla
	 * 
	 * @param otherAtributo
	 */
	public void addAtributo(TriplaRDFSense otherAtributo) {
		atributos.add(otherAtributo);
	}

	/**
	 * Adiciona apenas um metadado a tripla
	 * 
	 * @param otherMetadado
	 */
	public void addNaColecaoMetadados(TriplaRDFSense otherMetadado) {
		colecaoMetadado.add(otherMetadado);
	}

	/**
	 * Adiciona uma cole�ao de Triplas, nas Triplas dos metadados
	 * 
	 * @param moreTriplas
	 */
	public void addColecaoTriplas(ArrayList<TriplaRDFSense> moreTriplas) {
		colecaoTriplas.addAll(moreTriplas);
	}

	/**
	 * Adiciona uma cole�ao de Atributos, nas Triplas dos metadados
	 * 
	 * @param moreTriplas
	 */
	public void addColecaoAtributos(ArrayList<TriplaRDFSense> moreAtributos) {
		atributos.addAll(moreAtributos);
	}

	/**
	 * Adicioan uma cole�ao de metadados ao j� existentes
	 */
	public void addColecaoMetadados(ArrayList<TriplaRDFSense> moreMetadados) {
		colecaoMetadado.addAll(moreMetadados);
	}

	/* Inicio dos Getters e Setters */
	/**
	 * Retorna os Atributos da Tripla
	 * 
	 * @return
	 */
	public ArrayList<TriplaRDFSense> getAtributos() {
		return this.atributos;
	}

	/**
	 * Retorna a cole��o de Triplas que compoem a tripla
	 * 
	 * @return
	 */
	public ArrayList<TriplaRDFSense> getColecaoTriplas() {
		return colecaoTriplas;
	}

	/**
	 * Retorna a cole��o de Metadados que compoem a tripla
	 * 
	 * @return
	 */
	public ArrayList<TriplaRDFSense> getColecaoMetadados() {
		return colecaoMetadado;
	}

	/**
	 * Adiciona uma cole��o de Triplas a tripla, observe que esse m�todo n�o
	 * acresenta, ele sobrescreve
	 * 
	 * @param otherTriplas
	 */
	public void setColecaoTriplas(ArrayList<TriplaRDFSense> otherTriplas) {
		this.colecaoTriplas = otherTriplas;
	}

	/**
	 * Adiciona uma cole��o de Atributos a tripla, observe que esse m�todo n�o
	 * acresenta, ele sobrescreve
	 * 
	 * @param otherTriplas
	 */
	public void setAtributos(ArrayList<TriplaRDFSense> othersAtributos) {
		this.atributos = othersAtributos;
	}

	/**
	 * Adiciona uma cole��o de Metadados a tripla, observe que esse m�todo n�o
	 * acresenta, ele sobrescreve
	 * 
	 * @param otherTriplas
	 */
	public void setColecaoMetadados(ArrayList<TriplaRDFSense> othersMetadados) {
		this.colecaoMetadado = othersMetadados;
	}

	public final String getDado() {
		return dado;
	}

	public final void setDado(String dado) {
		this.dado = dado;
	}
	
	public void setMetadado(Metadado metadado){
		this.metadado1 = metadado;
	}
	public Metadado getMetadado(){
		return this.metadado1;
	}
}
