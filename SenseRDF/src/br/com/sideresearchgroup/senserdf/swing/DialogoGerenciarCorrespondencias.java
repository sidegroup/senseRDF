/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.Metadado;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.save.CorrespondenciaService;
import br.com.sideresearchgroup.senserdf.utils.KeyValue;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioService;

/**
 * @author ayrton
 *
 */
public final class DialogoGerenciarCorrespondencias extends JDialog {
	
	private static final long serialVersionUID = -2765353555862254853L;
	private final int widthJanela = 700;
	private final int heightJanela = 550;
	private VocabularioService vocabularioService = Configuracao.getServicoVocabulario();
	private DominioService dominioService = Configuracao.getServicoDominio();
	private CorrespondenciaService correspondenciaService = Configuracao.getCorrespondenciaService();
	
	private Metadado matchingResult = null;
	
	private JButton botaoNovo = null;
	private JButton botaoSalvar = null;
	private JButton botaoExcluir = null;
	private JButton botaoAtualizar = null;
	
	private JLabel labelNome = null;
	private JLabel labelMedida = null;
	private JLabel labelVocabulario = null;
	private JLabel labelTermoVocabulario = null;
	private JLabel labelCorrespondencia = null;
	private JLabel labelDominio = null;
	
	private JTextField textFieldNome = null;
	private JTextField textFieldArquivo = null;
	
	private JComboBox<KeyValue> comboDominios =new JComboBox<KeyValue>();
	private JComboBox<KeyValue> comboVocabularios =new JComboBox<KeyValue>();
	private JComboBox<String> comboTermosVocabulario =new JComboBox<String>();
	
	private JPanel painelCorrespondencias = null;
	private JPanel painelGeral = null;
	
	private JScrollPane rolagem = null;
	
	private int idDominio = 0;
	
	public DialogoGerenciarCorrespondencias(){		
		inicializacaoComum();
	}
	
	public DialogoGerenciarCorrespondencias(int idDominio){
		this.idDominio = idDominio;
		inicializacaoComum();
		
	}
	
	/**
	 * Configurações em comum a inicialização do dialogo
	 */
	private void inicializacaoComum(){
		try {
			inicializarInterfaceGrafica();
			atribuirAcoes();
			popularComboDominio();
			popularComboVocabulario();
			limparCampos();
			
			if (idDominio != 0)
				carregarCorrespondenciaDominio(idDominio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setTitle(Sap.GERENCIAR.get(Sap.CORRESPONDENCIA.get()));
		this.setLayout(null);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width - widthJanela) / 2, (screenSize.height - heightJanela) / 2, widthJanela, heightJanela);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setModal(true);
		this.setVisible(true);
	}
	
	/**
	 * Cria os objetos da interface
	 */
	private void inicializarInterfaceGrafica(){
		int x = 10;
		int y = 10;
		int width = widthJanela - (2 * x);
		int height = 30;
		
		int widthBotao = 120;
		
		labelDominio = FactoryInterface.createJLabel(Sap.DOMINIO.get(), 200, y, 90, height);
		comboDominios.setBounds(300, y, 200, height);
		botaoAtualizar = FactoryInterface.createJButton(510, y, 30, height, Imagem.REFLESH);
		this.add(labelDominio);
		this.add(comboDominios);
		this.add(botaoAtualizar);
		y = FactoryInterface.atualizarY(y, height);
		
		painelGeral = FactoryInterface.createJPanel(x, y, width - 10, 470, Color.WHITE);
		painelGeral.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.add(painelGeral);
		
		x = 5;
		y = 5;
		
		labelCorrespondencia = FactoryInterface.createJLabel(Sap.CORRESPONDENCIA.get(), x, y, 200, height);
		painelGeral.add(labelCorrespondencia);
		y = FactoryInterface.atualizarY(y, height);
		
		
		painelCorrespondencias = FactoryInterface.createJPanel(x, y, 200, 420, Color.WHITE);
		rolagem = new JScrollPane(painelCorrespondencias);
		rolagem.setBounds(x, y, 200, 420);
		painelGeral.add(rolagem);
		
		x = 200 + 15;
		labelNome = FactoryInterface.createJLabel(Sap.NOME.get(), x, y, widthBotao, height);
		painelGeral.add(labelNome);
		y = FactoryInterface.atualizarY(y, height);
		
		textFieldNome = FactoryInterface.createJTextField(x, y, 380, height);
		painelGeral.add(textFieldNome);
		y = FactoryInterface.atualizarY(y, height);
		
		labelVocabulario = FactoryInterface.createJLabel(Sap.VOCABULARIO.get(), x, y, 200, height);
		labelTermoVocabulario = FactoryInterface.createJLabel(Sap.TERMO.get(), 200 + x + 10, y, 200, height);
		painelGeral.add(labelVocabulario);
		painelGeral.add(labelTermoVocabulario);
		y = FactoryInterface.atualizarY(y, height);
		
		comboVocabularios = new JComboBox<KeyValue>();
		comboVocabularios.setBounds(x, y, 200, height);
		comboTermosVocabulario = new JComboBox<String>();
		comboTermosVocabulario.setBounds(x + 200 + 10, y, 200, height);
		painelGeral.add(comboVocabularios);
		painelGeral.add(comboTermosVocabulario);
		y = FactoryInterface.atualizarY(y, height);
		
		labelMedida = FactoryInterface.createJLabel(Sap.CONFIANCA.get(), x, y, 200, height);
		painelGeral.add(labelMedida);
		y = FactoryInterface.atualizarY(y, height);
		
		textFieldArquivo = FactoryInterface.createJTextField(x, y, 200, height);
		painelGeral.add(textFieldArquivo);
		y = FactoryInterface.atualizarY(y, 50);
		
		botaoNovo = FactoryInterface.createJButton(Sap.NOVO.get(), x, y, widthBotao, height,  Imagem.PAGINA_BRANCO);
		painelGeral.add(botaoNovo);
		
		botaoSalvar = FactoryInterface.createJButton(Sap.SALVAR.get(), x + widthBotao + 10, y, widthBotao, height, Imagem.SALVAR);
		painelGeral.add(botaoSalvar);
		
		botaoExcluir = FactoryInterface.createJButton(Sap.REMOVER.get(), x + (2 * widthBotao) + 20, y, widthBotao, height, Imagem.LIXEIRA);
		painelGeral.add(botaoExcluir);
	}
	
	
	/**
	 * Popula o combo dos dominios
	 */
	private void popularComboDominio(){
		comboDominios.removeAll();
		comboDominios.addItem(new KeyValue(0, Sap.COMBO_SELECIONE.get()));
		
		List<Dominio> dominios = dominioService.getAllDominios();
		for (Dominio dominio: dominios){
			KeyValue kv = new KeyValue(dominio.getId(), dominio.getNome());
			comboDominios.addItem(kv);
			
			if (dominio.getId() == idDominio){
				comboDominios.setSelectedItem(kv);
			}
		}
	}
	
	/**
	 * Carrega os dados da Correspondencias
	 * @throws Exception
	 */
	private void carregaDadosMetadado(Metadado oid) {
		matchingResult = oid;
		botaoExcluir.setEnabled(true);
		botaoNovo.setEnabled(true);
		
		textFieldNome.setText(matchingResult.getMetadado());
		textFieldArquivo.setText(String.valueOf(matchingResult.getMeasure()));
		popularComboVocabulario();
	}
	
	/**
	 * Carrega os dados dos vocabulários
	 * @throws Exception
	 */
	private void popularComboVocabulario() {
		//Remove todos os vocabulários anteriores
		comboVocabularios.removeAllItems();
		comboVocabularios.addItem(new KeyValue(0, Sap.COMBO_SELECIONE.get()));
		//Pega todos os vocabulários do arquivo de configuração
		List<Vocabulario> vocabularios = vocabularioService.getAllVocabularios();
		
		for (Vocabulario vocabulario: vocabularios){
			KeyValue kv = new KeyValue(vocabulario.getId(), vocabulario.getNome());
			comboVocabularios.addItem(kv);
			//Se tiver um matchingResult então vai selecionar automaticamente o item correto do combo
			if (matchingResult != null && vocabulario.getPrefixo().startsWith(matchingResult.getPrefixoVocabulario())){
				comboVocabularios.setSelectedItem(kv);
				preencherComboConstrutores(vocabulario.getId());
			}
		}
	}

	/**
	 * Atribui as acoes aos elementos da interface
	 */
	private void atribuirAcoes(){
		/**
		 * Inicia um novo dominio
		 */
		botaoNovo.addActionListener(actionEvent -> {
			botaoExcluir.setEnabled(false);
			limparCampos();
		});
		
		/**
		 * Salva o dominio atual 
		 */
		botaoSalvar.addActionListener(actionEvent -> { 
			if (temErros())
				return;
			
			String metadado = textFieldNome.getText();
			double measure;
			try {
				 measure = Double.parseDouble(textFieldArquivo.getText());
				 if (measure <= 0 || measure > 1){
					JOptionPane.showMessageDialog(null,Sap.MENSAGEM_AVISO_VALOR_INVALIDO.get(Sap.CONFIANCA.get()), Sap.ERRO.get(), JOptionPane.ERROR_MESSAGE);
					return;
				 }
			
				 KeyValue chave = (KeyValue)comboVocabularios.getSelectedItem();
				 
				 Vocabulario v = vocabularioService.getVocabularioById(chave.getKey());
				 
				 String termoVocabulario = (String) comboTermosVocabulario.getSelectedItem();
			
				 matchingResult.setMetadado(metadado);
				 matchingResult.setMeasure(measure);
				 matchingResult.setConstrutor(termoVocabulario);
				 matchingResult.setPrefixoVocabulario(v.getPrefixo());
			
			} catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(null, Sap.MENSAGEM_AVISO_VALOR_INVALIDO.get(Sap.CONFIANCA.get()), Sap.ERRO.get(), JOptionPane.ERROR_MESSAGE);
				return;
			} 
			
			
			correspondenciaService.salvarCorrespondencia(matchingResult, idDominio);
			
			limparCampos();
			carregarCorrespondenciaDominio(idDominio);
		});
		
		/**
		 * Irá usar o ID atual do domínio selecionado para remover
		 */
		botaoExcluir.addActionListener(actionEvent -> {
			int opcao = SwingHelper.questionarUsuario(Sap.MENSAGEM_AVISO_REMOVER_ITEM.get(matchingResult.getMetadado()) , Sap.REMOVER.get(), Sap.SIM.get(), Sap.NAO.get());
			if (opcao != 0){
				return;
			}
			
			correspondenciaService.removerCorrespondencia(matchingResult.getMetadado(), idDominio);
			
			KeyValue valorDominio = (KeyValue) comboDominios.getSelectedItem();
			carregarCorrespondenciaDominio(valorDominio.getKey());
			
			painelCorrespondencias.repaint();
			rolagem.repaint();
		});
		
		//Ao selecionar um item ele deverá atualizar o comboConstrutores
		comboVocabularios.addItemListener(itemEvent -> {
			
			KeyValue tal = (KeyValue) itemEvent.getItem();
			if (tal.getKey() == 0) {
				comboTermosVocabulario.removeAllItems();
				return;
			}
			preencherComboConstrutores(tal.getKey());
		});
		
		/**
		 * Usado para carregar os dados de um dominio selecionado no ComboBox
		 */
		botaoAtualizar.addActionListener(actionEvent -> {
			
			KeyValue valorDominio = (KeyValue) comboDominios.getSelectedItem();
			if (valorDominio.getKey() == 0){
				JOptionPane.showMessageDialog(null, Sap.MENSAGEM_COMBO_OBRIGATORIO.get(),  Sap.DOMINIO.get(), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			carregarCorrespondenciaDominio(valorDominio.getKey());
		});
	}
	
	/**
	 * Preenche o combo de Vocabulário de acordo com um prefixo do vocabulário
	 * passado. Ele irá chamar os vocabulários carregados na inicialização do
	 * sistema
	 * 
	 * @param prefixo
	 */
	private void preencherComboConstrutores(int idVocabulario) {
		Vocabulario vocabulario = null;
		vocabulario = vocabularioService.getVocabularioPreenchido(idVocabulario);
	
		comboTermosVocabulario.removeAllItems();

		for (String construtor : vocabulario.getConstrutores()) {
			comboTermosVocabulario.addItem(construtor);
		}
		
		if (matchingResult.getConstrutor() != null) {
			comboTermosVocabulario.setSelectedItem(matchingResult.getConstrutor());
		} else {
			comboTermosVocabulario.setSelectedIndex(0);
		}
	}
	
	/**
	 * Carrega todas as Correspondencias do Banco
	 * @param idDominio
	 */
	private void carregarCorrespondenciaDominio(int idDominio){
		this.idDominio = idDominio;
		painelCorrespondencias.removeAll();
		painelCorrespondencias.repaint();
		botaoExcluir.setEnabled(false);
		
		List<Metadado> alinhamentos = correspondenciaService.buscarPorDominio(idDominio);
		
		if (alinhamentos.isEmpty())
			return;
		
		int y = 0;
		
		for (Metadado alinhamento: alinhamentos){
			final JButton botao = FactoryInterface.createJButton(alinhamento.getMetadado(), 0, y, 200, 30);
			botao.setBackground(Color.WHITE);
			botao.setBorder(null);
			
			botao.addActionListener(actionEvent -> {
				carregaDadosMetadado(alinhamento);
			});
			
			//Adicionar o efeito quando o mouse passa por cima dos botões
			botao.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) { }
				
				@Override
				public void mousePressed(MouseEvent arg0) { }
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					botao.setBackground(Color.WHITE);
					botao.setForeground(Color.BLACK);
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					botao.setBackground(new Color(36, 36, 36));
					botao.setForeground(Color.WHITE);
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) { }
			});
			
			painelCorrespondencias.add(botao);
			y += 30;
		}
		
		painelCorrespondencias.setPreferredSize(new Dimension(200, y));
		rolagem.setViewportView(painelCorrespondencias);
		painelCorrespondencias.repaint();
		rolagem.repaint();	
	}
	
	/**
	 * 
	 */
	public void limparCampos(){
		textFieldArquivo.setText("0.0");
		textFieldNome.setText("");
		comboVocabularios.setSelectedIndex(0);
		matchingResult = new Metadado(null);
		
		//Desabilitando botoes
		botaoExcluir.setEnabled(false);
		botaoNovo.setEnabled(false);;
	}
	
	private boolean temErros(){
		if (idDominio == 0) {
			JOptionPane.showMessageDialog(this, Sap.MENSAGEM_COMBO_OBRIGATORIO.get(Sap.DOMINIO.get()), Sap.CAMPO_OBRIGATORIO.get(), JOptionPane.ERROR_MESSAGE);
			return true;
		} else if (textFieldNome.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this, Sap.MENSAGEM_CAMPO_OBRIGATORIO.get(Sap.NOME.get()), Sap.CAMPO_OBRIGATORIO.get(), JOptionPane.ERROR_MESSAGE);
			return true;
		} else if (comboVocabularios.getSelectedIndex() == 0){
			JOptionPane.showMessageDialog(this, Sap.MENSAGEM_COMBO_OBRIGATORIO.get(Sap.VOCABULARIO.get()), Sap.CAMPO_OBRIGATORIO.get(), JOptionPane.ERROR_MESSAGE);
			return true;
		}
		
		return false;
	}
}

