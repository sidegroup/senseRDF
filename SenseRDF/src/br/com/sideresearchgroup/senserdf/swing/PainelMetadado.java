/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.ValidadorCorrespondencia;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.utils.SenseUtil;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioService;


/**
 * @author ayrton
 * Cada classe dessa será uma Painel para o usuário escolher entre um dos termos existentes ou à adição
 * de um termo na nosso vocabulário
 */
public class PainelMetadado {

	private JPanel painelMetadado = new JPanel();
	private JLabel metadadoLabel = null;
	private JComboBox<String> comboVocabularios = new JComboBox<String>();
	private JComboBox<String> comboConstrutores = new JComboBox<String>();
	private JButton botaoAdicionar = null;
	private JButton botaoEscolha = null;
	private boolean modoAdicionarConstrutor = false;
	private JTextField caixaVocabularios = new JTextField();
	private JTextField caixaConstrutores = new JTextField();

	private ValidadorCorrespondencia validador;
	
	/**
	 * Construtor
	 * @param tripla
	 */
	public PainelMetadado(ValidadorCorrespondencia validador){
		this.validador = validador;
		criaInterface();
		metadadoLabel.setText(validador.getMetadado());
		metadadoLabel.setToolTipText(validador.getMetadado());

		adicionarVocabulariosCombo();
	}

	public ValidadorCorrespondencia getValidador() {
		return validador;
	}

	/**
	 * Retorna o painel para ser adicionado do JDialog, criado pela classe DialogoMatchingManager
	 * @return
	 */
	public JPanel retornarPainel() {
		return painelMetadado;
	}

	/**
	 * Constroi a interface do painel
	 */
	private void criaInterface() {
		Color azul = new Color(212, 212, 240);

		painelMetadado.setLayout(null);
		painelMetadado.setBackground(azul);

		metadadoLabel = FactoryInterface.createJLabel(10, 3, 157, 30);
		painelMetadado.add(metadadoLabel);

		botaoAdicionar = FactoryInterface.createJButton(520, 3, 30, 30);
		botaoAdicionar.setIcon(FactoryInterface.criarImageIcon(Imagem.ADICIONAR));
		botaoAdicionar.setToolTipText(Sap.ADICIONAR.get(Sap.TERMO.get()));
		painelMetadado.add(botaoAdicionar);

		botaoEscolha = FactoryInterface.createJButton(560, 3, 30, 30);
		botaoEscolha.setIcon(FactoryInterface.criarImageIcon(Imagem.VOLTAR_PRETO));
		botaoEscolha.setToolTipText(Sap.ESCOLHER.get(Sap.TERMO.get()));
		botaoEscolha.setEnabled(false);
		painelMetadado.add(botaoEscolha);

		alterarModo(false);
		atribuirAcoes();
	}

	/**
	 * Esse método irá alterar a interface do painel;
	 * Se for adicionar metadados no vocabulário da ferramenta, ou apenas selecionar um termo de um vocabulário pré-existente
	 * @param modoAdicionarMetadado
	 */
	private void alterarModo(boolean modoAdicionarMetadado) {
		if (modoAdicionarMetadado) {
			//O usuário indicará o termo a ser usado no RDF
			modoAdicionarConstrutor = true;
			botaoAdicionar.setEnabled(false);
			botaoEscolha.setEnabled(true);

			painelMetadado.remove(comboConstrutores);
			painelMetadado.remove(comboVocabularios);

			caixaVocabularios.setBounds(171, 3, 157, 30);

			VocabularioService vs = Configuracao.getServicoVocabulario();
			DominioService ds = Configuracao.getServicoDominio();
			
			Dominio dominio = ds.getDominio(validador.getIdDataset());
			int id = dominio.getIdVocabularioDominio();
			
			if (id == 0){
					
				String prefixo = null;
				for (int i = 0; ; i++){
					
					if (i == 0) {
						prefixo = "own"+SenseUtil.tratarCaracteres(dominio.getNome());
					} else {
						prefixo = "own"+SenseUtil.tratarCaracteres(dominio.getNome())+i;
					}
					
					if (vs.getVocabularioByPrefixo(prefixo) != null) {
						continue;
					} else {
						break;
					}
				}
				
				caixaVocabularios.setText(prefixo);
			} else {
				Vocabulario v = vs.getVocabularioById(id);
				caixaVocabularios.setText(v.getPrefixo()); //Vai buscar o nome do prefixo do nosso vocabulário
			}
			
			caixaVocabularios.setEditable(false);
			painelMetadado.add(caixaVocabularios);

			caixaConstrutores.setBounds(339, 3, 157, 30);
			painelMetadado.add(caixaConstrutores);

			caixaConstrutores.requestFocus();

		} else {
			//O usuário escolherá um dos termos de um dos vocabulários já existente
			modoAdicionarConstrutor = false;
			botaoAdicionar.setEnabled(true);
			botaoEscolha.setEnabled(false);

			painelMetadado.remove(caixaConstrutores);
			painelMetadado.remove(caixaVocabularios);

			comboVocabularios.setBounds(171, 3, 157, 30);
			painelMetadado.add(comboVocabularios);

			comboConstrutores.setBounds(339, 3, 157, 30);
			painelMetadado.add(comboConstrutores);
		}

		painelMetadado.repaint();
	}

	/**
	 * Atribue aos combos e aos botões as ações
	 */
	private void atribuirAcoes() {

		//Ao selecionar um item ele deverá atualizar o comboConstrutores
		comboVocabularios.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// adicionarVocabulariosCombo();
				String tal = (String) arg0.getItem();
				if (tal.equalsIgnoreCase(Sap.COMBO_SELECIONE.get())) {
					comboConstrutores.removeAllItems();
					return;
				}
				preencherComboConstrutores(tal);
			}
		});

		//Ao clicar ele irá alterar o Modo para que o usuário possa informar o termo
		botaoAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				alterarModo(true);
			}
		});

		//Irá alterar o modo para que o usuário possa escolher um termo
		botaoEscolha.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				alterarModo(false);
			}
		});
		
		caixaConstrutores.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON2) {
					caixaConstrutores.setText(metadadoLabel.getText());
				}
				
			}
		});
	}

	/**
	 * Irá atualizar a lista de vocabulários
	 */
	private void adicionarVocabulariosCombo() {
		VocabularioService servico = Configuracao.getServicoVocabulario();
		List<Vocabulario> vocabularios = null;
		try {
			vocabularios = servico.getAllVocabularios();
		
			comboVocabularios.removeAllItems();
			comboVocabularios.addItem(Sap.COMBO_SELECIONE.get());
			for (Vocabulario vocabulario : vocabularios) {
				comboVocabularios.addItem(vocabulario.getPrefixo());
			}
	
			
			String vocabulario = validador.getPrefixoVocabulario();
			if (vocabulario != null) {
				comboVocabularios.setSelectedItem(vocabulario);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Preenche o combo de Vocabulário de acordo com um prefixo do vocabulário
	 * passado. Ele irá chamar os vocabulários carregados na inicialização do
	 * sistema
	 * 
	 * @param prefixo
	 */
	private void preencherComboConstrutores(String prefixo) {
		VocabularioService servico = Configuracao.getServicoVocabulario();
		try {
			Vocabulario vocabularioSelecionado = servico.getVocabularioByPrefixo(prefixo);
			vocabularioSelecionado = servico.getVocabularioPreenchido(vocabularioSelecionado.getId());
			
			comboConstrutores.removeAllItems();
			
			List<String> construtores = vocabularioSelecionado.getConstrutores(); 
			Collections.sort(construtores);
			
			for (String construtor : construtores) {
				comboConstrutores.addItem(construtor);
			}
			
			if (validador.getTermo() != null && !validador.getTermo().equals("") && validador.getPrefixoVocabulario().equals(prefixo)) {
				comboConstrutores.setSelectedItem(validador.getTermo());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the comboVocabularios
	 */
	private final String getComboVocabularios() {
		return (String) comboVocabularios.getSelectedItem();
	}

	/**
	 * @return the comboConstrutores
	 */
	private final String getComboConstrutores() {
		return (String) comboConstrutores.getSelectedItem();
	}

	/**
	 * @return the caixaVocabularios
	 */
	private final String getCaixaVocabularios() {
		return caixaVocabularios.getText();
	}

	/**
	 * @return the caixaConstrutores
	 */
	private final String getCaixaConstrutores() {
		return SenseUtil.tratarCaracteres(caixaConstrutores.getText());
	}

	
	/**
	 * Indica se foi selecionado algum valor no combo dos vocabulários
	 * @return
	 */
	public final boolean selecionouAlgumVocabulario(){
		return (comboVocabularios.getSelectedIndex() != 0);
	}
	
	public String[] getResultado() {
		if (modoAdicionarConstrutor) {
			return new String[]{  getCaixaVocabularios(), getCaixaConstrutores() }; 
		} else {
			return new String[]{ getComboVocabularios(), getComboConstrutores() };
		}
	}
	
	public boolean isAdicionarOntologia() {
		return modoAdicionarConstrutor;
	}
}
