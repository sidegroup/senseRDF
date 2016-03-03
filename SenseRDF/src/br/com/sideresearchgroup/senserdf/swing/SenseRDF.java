package br.com.sideresearchgroup.senserdf.swing;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.arq.Parametro;
import br.com.sideresearchgroup.senserdf.arq.Setup;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.conversor.GerenciadorDataset;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.geracao.GeradorJenaRDF;
import br.com.sideresearchgroup.senserdf.resources.Idioma;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.utils.SenseUtil;




public class SenseRDF extends JFrame {

	private static final long serialVersionUID = -7059652024197011194L;
	//INTERFACE
	private JMenuBar menu;
	private JMenu menuSenseRDF;
	private JMenuItem menuItemPreferencias;
	private JMenuItem menuItemSair;
	private JMenu menuGerenciar;
	private JMenuItem menuItemGerenciarVocabularios;
	private JMenuItem menuItemGerenciarDominios;
	private JMenuItem menuItemGerenciarCorrespondencias;
	private JMenuItem menuItemSobre;
	private JMenu menuIdioma;
	
	private JPanel painelGeral;
	private JButton botaoAbrirRDF;
	private JButton botaoAlterar;
	private JButton botaoGerarRDF;
	private JButton botaoLocal;
	
	private JLabel labelArquivo;
	private JLabel labelRDFGerado;
	private JLabel labelURI;
	private JLabel labelMetadados;
	
	private JTextField caixaCaminhoURL;
	private JTextField caixaURI;
	
	private JScrollPane textRDFGerado;
	private JList<String> listaMetadados;
	private JScrollPane scrollListMetadados;
	private BarraStatus status;
	private JTextPane textPaneRDFGerado;
	
	private SenseRDF frame;
	
	//VARIÁVEIS GERAIS
	private GerenciadorDataset gerenciadorDataset;
	
	public SenseRDF(){
		super(Configuracao.NOME_FERRAMENTA);
		this.setIconImage(FactoryInterface.criarImageIcon(Imagem.SENSERDF_48).getImage());
		frame = this;
	}
	
	/**
	 * 
	 */
	public void iniciarConstrucaoInterface(){
		
		int tamanhoPadraoBotao = 150;
		int alturaPadrao = 30;
		
		// Define a ação de sair
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Informa que não será usado nenhum layout
		frame.getContentPane().setLayout(null);
		
		
		//Criação dos objetos
		menu = new JMenuBar();
		menuSenseRDF = FactoryInterface.createJMenu(null);
		menuItemPreferencias = FactoryInterface.createJMenuItem(Imagem.CONFIGURACAO);
		menuItemSair = FactoryInterface.createJMenuItem(Imagem.FECHAR_VERMELHO);
		menuGerenciar = FactoryInterface.createJMenu(null);
		menuItemGerenciarVocabularios = FactoryInterface.createJMenuItem(null);
		menuItemGerenciarDominios = FactoryInterface.createJMenuItem(null);
		menuItemGerenciarCorrespondencias = FactoryInterface.createJMenuItem(null);
		menuItemSobre = FactoryInterface.createJMenuItem(Imagem.ESTRELA);
		menuIdioma = FactoryInterface.createJMenu(Imagem.IDIOMA);
		
		popularMenuIdioma();
		
		painelGeral = FactoryInterface.createJPanel(0, 0, SwingHelper.JANELA_PRINCIPAL_LARGURA, SwingHelper.JANELA_PRINCIPAL_ALTURA, Color.WHITE);
		int y = 00;
		labelArquivo = FactoryInterface.createJLabel(10, y, 600, alturaPadrao);
		y = FactoryInterface.atualizarY(y, alturaPadrao);
		
		caixaCaminhoURL = FactoryInterface.createJTextField(10, y, 480, alturaPadrao);
		botaoLocal = FactoryInterface.createJButton(500, y, tamanhoPadraoBotao, alturaPadrao, Imagem.HD);
		y = FactoryInterface.atualizarY(y, alturaPadrao);
		
		labelMetadados = FactoryInterface.createJLabel(10, y - 3, 480, alturaPadrao);
		listaMetadados = new JList<String>();
		listaMetadados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaMetadados.setLayoutOrientation(JList.VERTICAL);
		listaMetadados.setAutoscrolls(true);
		scrollListMetadados = FactoryInterface.createJScrollPane(listaMetadados, 10, y + 20, 480, 460);
		
		labelURI = FactoryInterface.createJLabel(10, 562, 50, alturaPadrao);
		caixaURI = FactoryInterface.createJTextField(60, 562, 430, alturaPadrao);
		
		labelRDFGerado = FactoryInterface.createJLabel(500, y - 3, 400, alturaPadrao);
		y = FactoryInterface.atualizarY(y, alturaPadrao);
		
		textPaneRDFGerado = new JTextPane();
		textRDFGerado = FactoryInterface.createJScrollPane(textPaneRDFGerado, 500, y - 13, 490, 500);
		y = FactoryInterface.atualizarY(y, 500);
		
		botaoAbrirRDF = FactoryInterface.createJButton(840, y, tamanhoPadraoBotao, alturaPadrao, Imagem.PAGINA_BRANCO);
		botaoAlterar = FactoryInterface.createJButton(180, y, tamanhoPadraoBotao, alturaPadrao, Imagem.EDITAR);
		botaoGerarRDF = FactoryInterface.createJButton(340, y, tamanhoPadraoBotao, alturaPadrao, Imagem.EXPORTAR);
		
		status = BarraStatus.getInstance();
		DominioService dominioService = Configuracao.getServicoDominio();
		status.setDominio(dominioService.getDominio(Parametro.getInstance().getAsInt(Parametro.DOMINIO_ATUAL)));
		
		
		//Adição dos objetos na interface
		
		menuSenseRDF.add(menuGerenciar);
		menuGerenciar.add(menuItemGerenciarVocabularios);
		menuGerenciar.add(menuItemGerenciarDominios);
		menuGerenciar.add(menuItemGerenciarCorrespondencias);
		menuSenseRDF.add(menuItemPreferencias);
		menuSenseRDF.addSeparator();
		menuSenseRDF.add(menuIdioma);
		menuSenseRDF.addSeparator();
		menuSenseRDF.add(menuItemSobre);
		menuSenseRDF.add(menuItemSair);
		menu.add(menuSenseRDF);
		
		painelGeral.add(botaoAbrirRDF);
		painelGeral.add(labelArquivo);
		painelGeral.add(botaoAlterar);
		painelGeral.add(botaoGerarRDF);
		painelGeral.add(caixaCaminhoURL);
		painelGeral.add(botaoLocal);
		painelGeral.add(labelRDFGerado);
		painelGeral.add(textRDFGerado);
		painelGeral.add(labelURI);
		painelGeral.add(caixaURI);
		painelGeral.add(status);
		painelGeral.add(scrollListMetadados);
		painelGeral.add(labelMetadados);
		painelGeral.setSize(SwingHelper.JANELA_PRINCIPAL_LARGURA, SwingHelper.JANELA_PRINCIPAL_ALTURA);
		
		this.setJMenuBar(menu);
		this.getContentPane().add(painelGeral);
		
		//Atribuição das Ações + aplicação do idioma
		atribuirAcoes();
		aplicarIdioma();
		
		//Inicia o processo para exibir a ferramenta
		this.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(SwingHelper.JANELA_PRINCIPAL_LARGURA, SwingHelper.JANELA_PRINCIPAL_ALTURA));
		this.setLocation((screenSize.width - SwingHelper.JANELA_PRINCIPAL_LARGURA) / 2, (screenSize.height - SwingHelper.JANELA_PRINCIPAL_ALTURA) / 2);
		this.setResizable(false);
		this.setVisible(true);
		
		gerenciadorDataset = Configuracao.getGerenciadorDataset();
	}
	
	/**
	 * 
	 */
	private void atribuirAcoes() {
		
		menuItemGerenciarVocabularios.addActionListener(actionEvent -> {
			GerenciarVocabularios gv = new GerenciarVocabularios(frame);
			gv.dispose();
		});
		
		menuItemGerenciarDominios.addActionListener(actionEvent -> {
			new DialogoGerenciarDominios();
		});
		
		menuItemGerenciarCorrespondencias.addActionListener( actionEvent -> {
			new DialogoGerenciarCorrespondencias();
		});
		
		menuItemPreferencias.addActionListener(actionEvent -> {
			DialogoConfiguracao dc = new DialogoConfiguracao(frame);
			dc.dispose();
			DominioService dominioService = Configuracao.getServicoDominio();
			status.setDominio(dominioService.getDominio(Parametro.getInstance().getAsInt(Parametro.DOMINIO_ATUAL)));
		});
		
		menuItemSobre.addActionListener(actionEvent -> {
			Sobre sobre = new Sobre(frame);
			sobre.dispose();
		});
		
		menuItemSair.addActionListener(actionEvent -> {
			frame.dispose();
			System.exit(0);
		});
		
		botaoLocal.addActionListener(actionEvent -> {
			try {
				gerenciadorDataset.removerDataset(getDataset());
				
				if (Parametro.getInstance().getAsInt(Parametro.DOMINIO_ATUAL) == 0) {
					throw new SenseRDFException(Sap.MENSAGEM_AVISO_SEMDOMINIO_CONVERSAO.get(), TipoMensagem.AVISO);
				}
				
				File localArquivo = SwingHelper.buscarArquivoFiltro();
				if (localArquivo == null) {
					return;
				}
				
				gerenciadorDataset.adicionarDataset(localArquivo);
				
				Dataset dataset = getDataset();
				
				//ASSOCIA AO DOMINIO
				DominioService dominioService = Configuracao.getServicoDominio();
				dataset.setDominio(dominioService.getDominio(Parametro.getInstance().getAsInt(Parametro.DOMINIO_ATUAL)));
				
				gerenciadorDataset.extrairMetadados(dataset);
				
				List<Metadado> metadados = dataset.getMetadados();
				String[] lista = new String[metadados.size()];
				for (int i = 0; i < metadados.size(); i++) {
					lista[i] = metadados.get(i).getMetadado();
				}
				
				listaMetadados.setListData(new String[0]);
				listaMetadados.setListData(lista);
				caixaCaminhoURL.setText(localArquivo.getAbsolutePath());
				caixaURI.setText(dataset.getUri());
				status.setFormato(dataset.getFormato());
			} catch (Exception e1) {
				SwingHelper.tratarSenseException(null, e1);
			}
		});
		
		botaoAbrirRDF.addActionListener(actionEvent -> {
			try {
				Dataset dataset = getDataset();
				
				if (dataset == null) {
					return;
				}
					
			
				File arquivo = dataset.getArquivoRDF();
				if (arquivo == null){
					JOptionPane.showMessageDialog(frame, Sap.MENSAGEM_AVISO_DATASET.get(), Sap.AVISO.get(), JOptionPane.WARNING_MESSAGE);
					return;
				}
			
				Desktop.getDesktop().open(arquivo);
			} catch (Exception e1) {
				SwingHelper.tratarSenseException(frame, e1);
			}
		});
		
		botaoAlterar.addActionListener(actionEvent -> {
			try {
				Dataset dataset = getDataset();
				if (dataset == null) {
					return;
				}
				
				gerenciadorDataset.alterarMetadados(dataset);
			} catch (SenseRDFException e1) {
				SwingHelper.tratarSenseException(frame, e1);
			}
		});
		
		botaoGerarRDF.addActionListener(actionEvent -> {
			try {
				if (getDataset() == null) {
					return;
				}
				
				if (caixaURI.getText().equals("")){
					JOptionPane.showMessageDialog(frame,Sap.MENSAGEM_CAMPO_OBRIGATORIO.get(Sap.URI.get()), Sap.CAMPO_OBRIGATORIO.get(), JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				SenseUtil.validarEndereco(caixaURI.getText());
				status.vermelho();
				
				//Verifica se é a preferência de gerar o RDF sem pergutar o formato esta marcada
				int formato = 0;
				boolean gerarAutomaticamente = Parametro.getInstance().getAsBoolean(Parametro.PERGUNTAR_SINTAXE_RDF);
				if (gerarAutomaticamente) {
					formato = Parametro.getInstance().getAsInt(Parametro.SINTAXE_RDF);
					getDataset().setFormatoRDF(SwingHelper.formatoGerado(formato));
				} else {
					getDataset().setFormatoRDF(SwingHelper.escolheOpcoesRDF());
				}
				
				//ATUALIZA A URI
				getDataset().setUri(caixaURI.getText());
				
				Configuracao.setGeradorRDF(new GeradorJenaRDF());
				//Configuracao.setGeradorRDF(new GeradorJenaMetadadosRDF());
				
				//GERA O ARQUIVO
				gerenciadorDataset.gerarRDF();
			
				//EXIBE O RESULTADO PARA O USUARIO
				File f = getDataset().getArquivoRDF();
				if (f != null) {
					textPaneRDFGerado.setText(SenseUtil.lerTextoPuroLimitado(f.getAbsolutePath()));
				}
				
				status.verde();
			} catch (Exception e1) {
				SwingHelper.tratarSenseException(frame, e1);
			}
		});
	}

	/**
	 * Aplica o idioma na interface da ferramenta
	 */
	private void aplicarIdioma(){
		menuSenseRDF.setText(Configuracao.NOME_FERRAMENTA);
		menuItemPreferencias.setText(Sap.OPCOES.get());
		menuItemSair.setText(Sap.FECHAR.get());
		menuGerenciar.setText(Sap.GERENCIAR.get());
		menuItemGerenciarVocabularios.setText(Sap.GERENCIAR.get(Sap.VOCABULARIOS.get()));
		menuItemGerenciarDominios.setText(Sap.GERENCIAR.get(Sap.DOMINIO.get()));
		menuItemGerenciarCorrespondencias.setText(Sap.GERENCIAR.get(Sap.CORRESPONDENCIA.get()));
		menuItemSobre.setText(Sap.SOBRE.get());
		menuIdioma.setText(Sap.IDIOMA.get());
		
		botaoAbrirRDF.setText(Sap.ABRIR.get(Sap.RDF.get()));
		botaoAlterar.setText(Sap.ALTERAR.get(Sap.METADADOS.get()));
		botaoGerarRDF.setText(Sap.GERAR_RDF.get());
		
		botaoLocal.setText(Sap.CARREGAR.get());
		
		labelArquivo.setText(Sap.CAMINHO.get());
		labelRDFGerado.setText(Sap.RDF_GERADO.get());
		
		labelURI.setText(Sap.URI.get());
		labelMetadados.setText(Sap.METADADOS.get());
		
		BarraStatus.getInstance().aplicarIdioma();
	}
	
	/**
	 * Irá popular o menuIdioma com os idiomas disponíveis na pasta Idioma
	 */
	private void popularMenuIdioma(){
		ButtonGroup group = new ButtonGroup();
		
		for(Idioma idioma : Idioma.values()) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(idioma.getNome());
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Configuracao.setLocale(new Locale(idioma.getSigla()));
					aplicarIdioma();
					frame.repaint();
				}
			});
			if (idioma.getSigla().equalsIgnoreCase(Configuracao.getLocale().getLanguage())) {
				item.setSelected(true);
			}
			group.add(item);
			menuIdioma.add(item);
		}
	}
	
	public Dataset getDataset() {
		List<Dataset> datasets = gerenciadorDataset.getDatasets();
		
		if (datasets == null || datasets.isEmpty()) {
			return null;
		}
		
		return datasets.get(0);
	}
	
	/**
	 * @param args
	 */
	
	
	
	public static void main(String[] args) {
			
		Setup.iniciarFerramenta();
		SenseRDF sense = new SenseRDF();	
		sense.iniciarConstrucaoInterface();
	}

}

