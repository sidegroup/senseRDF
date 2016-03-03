/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.arq.erro.TipoMensagem;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.utils.ArquivoUtil;
import br.com.sideresearchgroup.senserdf.utils.KeyValue;
import br.com.sideresearchgroup.senserdf.utils.SenseUtil;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioHelper;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioService;


/**
 * IFPB - Instituto Federal de Educação, Ciência e Tecnologia da Paraíba
 * Side 
 * Interface que irá gerenciar os vocabulários da SenseRDF
 * 
 * 01/03/2013
 * @author ayrton
 */
public class GerenciarVocabularios extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel painelPrincipal = null;
	// Texto da interface
	private JLabel labelMensagem = null;
	private JLabel labelCombo = null;
	private JLabel labelCaminho = null;
	private JLabel labelPrefixo = null;
	private JLabel labelNome = null;
	private JLabel labelNamespace = null;
	private JLabel labelTermos = null;

	// Botões usados na interface
	private JButton botaoCarregar = null;
	private JButton botaoAlterar = null;
	private JButton botaoCarregarVocabulario = null;
	private JButton botaoRemover = null;

	// Caixas de textos
	private JTextField textFieldCaminho = null;
	private JTextField textFieldPrefixo = null;
	private JTextField textFieldNome = null;
	private JTextField textFieldNamespace = null;

	private JScrollPane scroll = null;

	// ComboBox que contem os nomes dos vocabulários
	private JComboBox<KeyValue> comboVocabularios = null;

	// Lista de Construtores do Vocabulario selecionado ou do que será adicionado
	private JList<String> listaTermos = null;
	
	//Variáveis
	private VocabularioService servico;
	
	private br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario vocabulario;
	
	public GerenciarVocabularios(JFrame pai){
		super(pai, Sap.GERENCIAR.get(Sap.VOCABULARIOS.get()), true);
		
		servico = Configuracao.getServicoVocabulario();
		
		painelPrincipal = new JPanel();
		painelPrincipal.setLayout(null);
		this.add(painelPrincipal);
		
		construirInterface();
		carregarAcoes();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width - 700) / 2, (screenSize.height - 550) / 2, 700, 550);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void construirInterface() {
		comboVocabularios = new JComboBox<KeyValue>();
		listaTermos = new JList<String>();
		Color azul = new Color(212, 212, 240);

		labelMensagem = FactoryInterface.createJLabel(10, 0, 670, 60);
		JPanel painelInterno = new JPanel();
		painelInterno.setBackground(azul);
		painelInterno.add(labelMensagem);
		painelInterno.setBounds(10, 10, 680, 40);
		
		labelCaminho = FactoryInterface.createJLabel(10, 60, 350, 30);
		textFieldCaminho = FactoryInterface.createTextField(10, 90, 350, 30);
		botaoCarregar = FactoryInterface.createJButton(370, 90, 150, 30, Imagem.ADICIONAR);

		labelCombo = FactoryInterface.createJLabel(10, 120, 350, 30);
		comboVocabularios.setBounds(10, 160, 350, 30);

		botaoRemover = FactoryInterface.createJButton(525, 160, 150, 30, Imagem.LIXEIRA);
		botaoCarregarVocabulario = FactoryInterface.createJButton(370, 160, 150, 30, Imagem.CARREGAR);

		labelTermos = FactoryInterface.createJLabel(460, 215, 350, 30);

		scroll = new JScrollPane(listaTermos);
		scroll.setBounds(370, 250, 325, 270);

		labelNome = FactoryInterface.createJLabel(10, 215, 350, 30);
		textFieldNome = FactoryInterface.createTextField(10, 250, 350, 30);
		labelPrefixo = FactoryInterface.createJLabel(10, 285, 350, 30);
		textFieldPrefixo = FactoryInterface.createTextField(10, 320, 350, 30);
		labelNamespace = FactoryInterface.createJLabel(10, 355, 350, 30);
		textFieldNamespace = FactoryInterface.createTextField(10, 390, 350, 30);
		botaoAlterar = FactoryInterface.createJButton(100, 430, 200, 30, Imagem.SALVAR);

		listaTermos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaTermos.setLayoutOrientation(JList.VERTICAL);
		listaTermos.setAutoscrolls(true);

		painelPrincipal.add(botaoCarregar);
		painelPrincipal.add(botaoAlterar);
		painelPrincipal.add(botaoCarregarVocabulario);
		painelPrincipal.add(botaoRemover);
		painelPrincipal.add(labelMensagem);
		painelPrincipal.add(painelInterno);
		painelPrincipal.add(labelCombo);
		painelPrincipal.add(labelCaminho);
		painelPrincipal.add(labelPrefixo);
		painelPrincipal.add(labelNome);
		painelPrincipal.add(labelNamespace);
		painelPrincipal.add(labelTermos);
		painelPrincipal.add(comboVocabularios);
		painelPrincipal.add(scroll);
		painelPrincipal.add(textFieldCaminho);
		painelPrincipal.add(textFieldNome);
		painelPrincipal.add(textFieldPrefixo);
		painelPrincipal.add(textFieldNamespace);

		carregarDadosCombo();
		aplicarIdioma();
	}
	
	/**
	 * Carrega os dados do combo. Chama-se quando eh criado ou removido algum
	 * vocabulário.
	 */
	private void carregarDadosCombo() {
		try {
			List<Vocabulario> lista = servico.getAllVocabularios();
			
			comboVocabularios.removeAllItems();
			comboVocabularios.addItem(new KeyValue(-1, Sap.COMBO_SELECIONE.get()));
			for (Vocabulario l : lista) {
				KeyValue kv = new KeyValue(l.getId(), l.getPrefixo() + " - "+ l.getNome());
				comboVocabularios.addItem(kv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Utilizado para recarregar o idioma da interface caso haja uma mudança na
	 * escolha da linguagem.
	 */
	public void aplicarIdioma() {
		labelMensagem.setText(Sap.GESTAO_VOCABULARIOS_DESCRICAO.get());
		labelCombo.setText(Sap.VOCABULARIOS.get());
		labelCaminho.setText(Sap.CAMINHO.get());
		labelPrefixo.setText(Sap.PREFIXO.get());
		labelNome.setText(Sap.NOME.get());
		labelNamespace.setText(Sap.NAMESPACE.get());
		labelTermos.setText(Sap.TERMOS.get());

		botaoCarregar.setText(Sap.ADICIONAR.get());
		botaoAlterar.setText(Sap.SALVAR.get());
		botaoRemover.setText(Sap.REMOVER.get());
		botaoCarregarVocabulario.setText(Sap.CARREGAR.get());
	}
	
	/**
	 * Especifica as ações da interface
	 */
	private void carregarAcoes() {
		botaoCarregar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFilter filtro = new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						return pathname.getName().toLowerCase().endsWith("rdf")
								|| pathname.getName().toLowerCase().endsWith("owl")
								|| pathname.getName().toLowerCase().endsWith("rdfs")
								|| pathname.isDirectory();
					}

					@Override
					public String getDescription() {
						return Sap.FILTRO_ARQUIVOS.get("RDF, RDFS, OWL ");
					}
				};

				File arquivo = ArquivoUtil.abrirEscolhaArquivo(filtro);
				if (arquivo == null)
					return;

				textFieldCaminho.setText(arquivo.getAbsolutePath());
				vocabulario = new Vocabulario();
				vocabulario.setPath(arquivo.getAbsolutePath());
				vocabulario.setConstrutores(servico.identificarConstrutores(arquivo.getAbsolutePath()));
				
				if (vocabulario.getConstrutores().isEmpty()){
					JOptionPane.showMessageDialog(null, Sap.GESTAO_VOCABULARIOS_TERMOS.get(), Sap.ERRO.get(), JOptionPane.WARNING_MESSAGE);
					vocabulario = null;
					return;
				}
				
				carregarDadosInterface();
			}
		});

		botaoCarregarVocabulario.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int index = comboVocabularios.getSelectedIndex();
					if (index == 0) {
						JOptionPane.showMessageDialog(null, Sap.MENSAGEM_COMBO_OBRIGATORIO.get(Sap.VOCABULARIOS.get()), Sap.AVISO.get(), JOptionPane.WARNING_MESSAGE);
						return;
					}
						
					//Pega o objeto KeyValue
					KeyValue kv = (KeyValue) comboVocabularios.getSelectedItem();
					
					//Carrega o vocabulário com a "chave" do KeyValue
					vocabulario = servico.getVocabularioById(kv.getKey());
					
					limparCampos();
					carregarDadosInterface();
				} catch (Exception e1) {
					// Comportamento inesperado
				}

			}
		});

		botaoAlterar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String nome = null;
				try {
					if (vocabulario == null) {
						JOptionPane.showMessageDialog(null, Sap.MENSAGEM_COMBO_OBRIGATORIO.get(Sap.VOCABULARIOS.get()), Sap.AVISO.get(), JOptionPane.WARNING_MESSAGE);
						return;
					}
					String prefixo = textFieldPrefixo.getText();
					String nameSpace = textFieldNamespace.getText();
					
					if (!nameSpace.endsWith("/") && !nameSpace.endsWith("#")) {
						throw new SenseRDFException(Sap.GESTAO_VOCABULARIOS_NAMESPACE.get(), TipoMensagem.FATAL);
					}
					SenseUtil.validarEndereco(nameSpace);
					
					nome = textFieldNome.getText();
					
					try {
						VocabularioHelper.validarVocabulario(vocabulario, prefixo, nameSpace);
					}catch (SenseRDFException e1) {
						JOptionPane.showMessageDialog(null, e1.getMensagem(), Sap.AVISO.get(), JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					vocabulario.setPrefixo(prefixo);
					vocabulario.setNamespace(nameSpace);
					vocabulario.setNome(nome); 
					
					servico.adicionarVocabulario(vocabulario);
					
					JOptionPane.showMessageDialog(null, Sap.MENSAGEM_INFORMACAO_SUCESSO.get(), Sap.INFORMACAO.get(), JOptionPane.INFORMATION_MESSAGE);
					
					
					carregarDadosCombo();
					limparCampos();
				} catch (Exception e1) {
					SwingHelper.tratarSenseException(null, e1);
				}
			}
		});

		botaoRemover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = comboVocabularios.getSelectedIndex();

				if (index == 0) {
					JOptionPane.showMessageDialog(null, Sap.MENSAGEM_COMBO_OBRIGATORIO.get(Sap.VOCABULARIOS.get()), Sap.AVISO.get(), JOptionPane.WARNING_MESSAGE);
					return;
				}

				KeyValue kv = (KeyValue) comboVocabularios.getSelectedItem();
				
				int opcao = SwingHelper.questionarUsuario(Sap.MENSAGEM_AVISO_REMOVER_ITEM.get(kv.getValue()), Sap.REMOVER.get(), Sap.SIM.get(), Sap.NAO.get());
				if (opcao == 1)
					return;

				servico.removerVocabulario(new Vocabulario(kv.getKey()));

				carregarDadosCombo();
				limparCampos();
				JOptionPane.showMessageDialog(null, Sap.MENSAGEM_INFORMACAO_SUCESSO.get(), Sap.INFORMACAO.get(), JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	/**
	 * Limpa os campos quando eu escolho um novo vocabulário para adicionar ou
	 * alterars
	 */
	private void limparCampos() {
		textFieldCaminho.setText("");
		textFieldNamespace.setText("");
		textFieldNome.setText("");
		textFieldPrefixo.setText("");
		listaTermos.setModel(new DefaultListModel<String>());
	}

	/**
	 * Carrega os dados de um vocabulário na interface
	 */
	private void carregarDadosInterface() {
		textFieldPrefixo.setText(vocabulario.getPrefixo());
		textFieldNamespace.setText(vocabulario.getNamespace());
		textFieldNome.setText(vocabulario.getNome());

		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String termo : vocabulario.getConstrutores()) {
			listModel.addElement(termo);
		}

		listaTermos.setModel(listModel);
	}

}
