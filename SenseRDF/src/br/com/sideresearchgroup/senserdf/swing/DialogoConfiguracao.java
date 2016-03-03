/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Parametro;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.utils.KeyValue;
import br.com.sideresearchgroup.senserdf.utils.SenseUtil;


/**
 * @author Ayrton
 *
 */
public class DialogoConfiguracao extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -779704111360073199L;
	private JPanel painel;
	private JTabbedPane abas;
	private JPanel painelGeral = null;
	private JButton botaoOk = null;
	private JButton botaoCancelar = null;
	
	private JCheckBox check0_001 = null;
	
	private JCheckBox check0_003 = null;
	
	private JLabel label0_004 = null;
	private JTextField caixa0_004 = null;
	
	private JCheckBox check0_005;
	
	private JLabel labelDominio = null;
	private JComboBox<KeyValue> dominios = new JComboBox<KeyValue>();
	
	private HashMap<String, String>  configuracoes = null;
	private boolean aplicarConfiguracoes = false;
	
	private JPanel painelGeracao;
	
	private ButtonGroup group = new ButtonGroup();
	private JRadioButtonMenuItem item2;
	private JRadioButtonMenuItem item1;
	
	public DialogoConfiguracao(JFrame frame){
		super(frame, Sap.OPCOES.get(), true);
		configuracoes = new HashMap<String, String>();
		
		construirInterface();
		carregarAcoes();
		carregarConfiguracaoAtual();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width - 400) / 2, (screenSize.height - 310) / 2, 400, 310);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	public void construirInterface(){
		painel = FactoryInterface.createJPanel(0, 0, 400, 300, null);
		
		this.setLayout(null);
		painelGeral = new JPanel();
		painelGeral.setLayout(null);
		painelGeral.setBounds(0, 0, 400, 260);
		
		int x = 10;
		int y = 10;
		
		botaoOk = FactoryInterface.createJButton(Sap.CONCLUIR.get(), 45, 235, 150, 30, Imagem.CONCLUIR);
		
		botaoCancelar = FactoryInterface.createJButton(Sap.CANCELAR.get(), 205 , 235, 150, 30, Imagem.CANCELAR);
		
		check0_001 = new JCheckBox(Sap.OPCOES_EXIBIR_RDF.get());
		check0_001.setBounds(x, y, 380, 20);
		y += 25;
		
		check0_003 = new JCheckBox(Sap.OPCOES_EXIBIR_METADADOS_DESCONHECIDOS.get());
		check0_003.setBounds(x, y, 380, 30);
		y += 25;
		
		labelDominio = FactoryInterface.createJLabel(Sap.DOMINIO.get(), x, y, 380, 20);
		y += 25;
		
		dominios.setBounds(x, y, 380, 30);
		y +=  25;
		
		painelGeral.add(check0_001);
		painelGeral.add(check0_003);
		painelGeral.add(labelDominio);
		painelGeral.add(dominios);
		
		painelGeracao = new JPanel();
		painelGeracao.setLayout(null);
		painelGeracao.setBounds(0, 0, 400, 260);
		y = 10;
		label0_004 = FactoryInterface.createJLabel(Sap.URI.get(), x, y, 380, 20);
		y += 23;
		caixa0_004 = FactoryInterface.createTextField(x, y, 380, 30);
		
		y += 33;
		check0_005 = FactoryInterface.createJCkeckBox(x, y, 380, 20);
		check0_005.setText(Sap.OPCOES_GERAR_RDF_FORMATO.get());
		
		painelGeracao.add(label0_004);
		painelGeracao.add(caixa0_004);
		painelGeracao.add(check0_005);
		
		abas = new JTabbedPane(JTabbedPane.TOP);
		abas.addTab(Sap.OPCOES_GERAL.get(), painelGeral);
		abas.addTab(Sap.OPCOES_GERACAO.get(), painelGeracao);
		abas.setBounds(0, 0, 400, 220);
		painel.add(abas);
		painel.add(botaoOk);
		painel.add(botaoCancelar);
		
		this.add(painel);
	}
	
	public void carregarAcoes(){
		
		check0_001.addActionListener(actionEvent -> {
			String codigo = Parametro.CARREGAR_RDF_GERADO;
			String valor = String.valueOf(check0_001.isSelected());
			configuracoes.put(codigo, valor);
		});
		
		check0_003.addActionListener(actionEvent -> {
			String codigo = Parametro.VALIDAR_TODOS_METADADOS;
			String valor = String.valueOf(check0_003.isSelected());
			configuracoes.put(codigo, valor);
			
		});
		
		check0_005.addActionListener(actionEvent -> {
			String codigo = Parametro.PERGUNTAR_SINTAXE_RDF;
			item1.setEnabled(check0_005.isSelected());
			item2.setEnabled(check0_005.isSelected());
			
			String valor = String.valueOf(check0_005.isSelected());
			configuracoes.put(codigo, valor);
		});
		
		final JDialog dialogo = this;
		
		//Irá aplicar as configurações e sair
		botaoOk.addActionListener(actionEvent -> {
			try {
				SenseUtil.validarEndereco(caixa0_004.getText());
			} catch (SenseRDFException se){
				JOptionPane.showMessageDialog(null, se.getMessage(),Sap.ERRO.get(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			aplicarConfiguracoes = true;
			aplicarConfiguracoes();
			dialogo.dispose();
		});
		
		//Ao clicar em cancelar não se deve fazer nada
		botaoCancelar.addActionListener(actionEvent -> {
			aplicarConfiguracoes = false;
			dialogo.dispose();
		});
	}
	
	/**
	 * Método que irá aplicar as configurações escolhidas pelo usuário quando ele clicar em 'OK'
	 */
	public void aplicarConfiguracoes(){
		Parametro.getInstance().alterarConfiguracao(Parametro.URI_PADRAO, caixa0_004.getText());
		
		Set<String> chaves = configuracoes.keySet();
		Iterator<String> chaveIterator =  chaves.iterator();
		while (chaveIterator.hasNext()){
			String chave = chaveIterator.next(); 
			String valor = configuracoes.get(chave);
			Parametro.getInstance().alterarConfiguracao(chave, valor);
		}
		
		int idDominio = ((KeyValue) dominios.getSelectedItem()).getKey();
		if (idDominio != 0) {
			Parametro.getInstance().alterarConfiguracao(Parametro.DOMINIO_ATUAL, String.valueOf(idDominio));
		}
	}
	
	/**
	 * Carrega todas as configurações para ser exibida para o usuário
	 */
	private void carregarConfiguracaoAtual(){
		
		Parametro param = Parametro.getInstance();
		Boolean marcar = false;
		
		marcar = param.getAsBoolean(Parametro.CARREGAR_RDF_GERADO);
		check0_001.setSelected(marcar);
		
		marcar = param.getAsBoolean(Parametro.VALIDAR_TODOS_METADADOS);
		check0_003.setSelected(marcar);
		
		String uriPadrao = param.get(Parametro.URI_PADRAO);
		caixa0_004.setText(uriPadrao);
		
		marcar = param.getAsBoolean(Parametro.PERGUNTAR_SINTAXE_RDF);
		check0_005.setSelected(marcar);
		
		item1 = new JRadioButtonMenuItem("RDF/XML");
		item1.addActionListener(actionEvent -> {
			configuracoes.put(Parametro.SINTAXE_RDF, String.valueOf(1));
		});
		
		item2 = new JRadioButtonMenuItem("N3");
		item2.addActionListener(actionEvent -> {
			configuracoes.put(Parametro.SINTAXE_RDF, String.valueOf(2));	
		});
		
		Integer formato = param.getAsInt(Parametro.SINTAXE_RDF);
		if (formato == 1) {
			item1.setSelected(true);
		} else if (formato == 2){
			item2.setSelected(true);
		}
		
		Integer dominioUsar = param.getAsInt(Parametro.DOMINIO_ATUAL);
		
		DominioService gerenciadorDominio = Configuracao.getServicoDominio();
		List<Dominio> todosDominios = gerenciadorDominio.getAllDominios();
		
		dominios.addItem(new KeyValue(0, Sap.COMBO_SELECIONE.get()));
		
		for (Dominio dominio: todosDominios){
			KeyValue kv = new KeyValue(dominio.getId(), dominio.getNome());
			dominios.addItem(kv);
			
			if (dominioUsar == dominio.getId()){
				dominios.setSelectedItem(kv);
			}
		}
		
		group.add(item1);
		group.add(item2);
		
		item1.setBounds(20, 96, 200, 20);
		item2.setBounds(20, 119, 200, 20);
		item1.setEnabled(check0_005.isSelected());
		item2.setEnabled(check0_005.isSelected());
		
		painelGeracao.add(item1);
		painelGeracao.add(item2);
	}
	
	public HashMap<String, String> getConfiguracoes(){
		return configuracoes;
	}
	
	public boolean isAplicarConfiguracoes(){
		return aplicarConfiguracoes;
	}
}
