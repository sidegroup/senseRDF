/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.dominio.DominioService;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;
import br.com.sideresearchgroup.senserdf.vocabulario.Vocabulario;
import br.com.sideresearchgroup.senserdf.vocabulario.VocabularioService;

/**
 * @author ayrton
 *
 */
public class DialogoGerenciarDominios extends JDialog {
	

	private static final long serialVersionUID = 8044074485111747486L;
	private final int widthJanela = 700;
	private final int heightJanela = 550;
	private Dominio dominio = new Dominio();
	private DominioService gerenciador = Configuracao.getServicoDominio();
	
	private JButton botaoNovo = null;
	private JButton botaoSalvar = null;
	private JButton botaoExcluir = null;
	
	private JButton botaoAdicionar = null;
	private JButton botaoRemover = null;
	
	private JLabel labelNome = null;
	private JLabel labelVocabularios = null;
	private JLabel labelVocabulariosUsar = null;
	
	private JTextField textFieldNome = null;
	
	private JPanel painelDominios = null;
	
	private JScrollPane rolagem = null;
	private JScrollPane rolagemVocabulario = null;
	private JScrollPane rolagemVocabularioDominio = null;
	
	private JList<String> listaVocabularios = new JList<String>();
	private DefaultListModel<String> defaultListaVocabularios = new DefaultListModel<String>();
	
	private JList<String> listaVocabulariosAdicionados = new JList<String>(); 
	private DefaultListModel<String> defaultListaVocabulariosAdd = new DefaultListModel<String>();
	
	public DialogoGerenciarDominios()  {
		this.setTitle(Sap.GERENCIAR.get(Sap.DOMINIO.get()));
		this.setLayout(null);
		
		try {
			inicializarInterfaceGrafica();
			popularDominios();
			atribuirAcoes();
			carregarConfiguracaoDominio();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		botaoNovo.setEnabled(false);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width - widthJanela) / 2, (screenSize.height - heightJanela) / 2, widthJanela, heightJanela);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void inicializarInterfaceGrafica(){
		int x = 10;
		int y = 10;
		int width = widthJanela - (2 * x);
		int height = 30;
		
		int widthBotao = 120;
		
		JPanel painelMensagem = FactoryInterface.createPanelText(x, y, width, height + 20, Sap.GESTAO_DOMINIOS_DESCRICAO.get(), SwingHelper.COR_MENSAGEM_DESCRICAO);
		this.add(painelMensagem);
		y = FactoryInterface.atualizarY(y, height + 20); 
		
		botaoNovo = FactoryInterface.createJButton(Sap.NOVO.get(), x, y, widthBotao, height, Imagem.ADICIONAR);
		this.add(botaoNovo);
		botaoSalvar = FactoryInterface.createJButton(Sap.SALVAR.get(), widthJanela - 20 - widthBotao, y, widthBotao, height, Imagem.SALVAR);
		this.add(botaoSalvar);
		botaoExcluir = FactoryInterface.createJButton(widthJanela - 60 - widthBotao , y, 30, height, Imagem.LIXEIRA);
		botaoExcluir.setEnabled(false);
		this.add(botaoExcluir);
		y = FactoryInterface.atualizarY(y, height);
		
		painelDominios = FactoryInterface.createJPanel(0, 0, 600, width / 2, Color.WHITE);
		rolagem = new JScrollPane(painelDominios);
		rolagem.setBounds(x, y, 250, heightJanela - y - 30);
		this.add(rolagem,  BorderLayout.CENTER);
		 x = 250 + 20;
		 width = widthJanela - 250 - 40;
		
		labelNome = FactoryInterface.createJLabel(Sap.NOME.get(), x, y, width, height);
		this.add(labelNome);
		y = FactoryInterface.atualizarY(y, height);
		
		textFieldNome = FactoryInterface.createJTextField(x, y, width, height);
		this.add(textFieldNome);
		y = FactoryInterface.atualizarY(y, height);
		
		labelVocabularios = FactoryInterface.createJLabel(Sap.VOCABULARIOS.get(), x, y, 150, height);
		this.add(labelVocabularios);
		
		labelVocabulariosUsar = FactoryInterface.createJLabel(Sap.GESTAO_DOMINIOS_VOCABULARIO.get(), x + 200 + 10, y, 200, height);
		this.add(labelVocabulariosUsar);
		y = FactoryInterface.atualizarY(y, height);
		
		
		rolagemVocabulario = new JScrollPane(listaVocabularios);
		rolagemVocabulario.setBounds(x, y, 160, 180);
		this.add(rolagemVocabulario);
		
		botaoAdicionar = FactoryInterface.createJButton(x+170, y + 50, 30, height, Imagem.SETA_DIREITA);
		this.add(botaoAdicionar);
		
		botaoRemover = FactoryInterface.createJButton(x+170, y + 80, 30, height, Imagem.SETA_ESQUERDA);
		this.add(botaoRemover);
		
		rolagemVocabularioDominio = new JScrollPane(listaVocabulariosAdicionados);
		rolagemVocabularioDominio.setBounds(x + 210, y, 160, 180);
		this.add(rolagemVocabularioDominio);
		y = FactoryInterface.atualizarY(y, 180);
	}
	
	private void popularDominios() throws SenseRDFException{
		painelDominios.removeAll();
		
		List<Dominio> dominios = gerenciador.getAllDominios();
				
		if (dominios.isEmpty()){
			//TODO 
		}
		int y = 0;
		
		for (final Dominio d: dominios){
			final JButton botao = FactoryInterface.createJButton(d.getNome(), 0, y, 250, 30);
			botao.setBackground(Color.WHITE);
			botao.setBorder(null);
			botao.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					carregarDadosDominioByID(d.getId());
				}
			});
			
			//Adicionar o efeito quando o mouse passa por cima dos botões
			botao.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					
				}
				
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
				public void mouseClicked(MouseEvent arg0) {
					
				}
			});
			
			painelDominios.add(botao);
			y += 30;
		}
		
		painelDominios.setPreferredSize(new Dimension(240, y));
		rolagem.setViewportView(painelDominios);
	}
	
	/**
	 * Irá buscar o dominio pelo ID e em seguida carregar esse dominio na interface
	 * @param id
	 */
	private void carregarDadosDominioByID(int id){
		limparCampos();
		botaoNovo.setEnabled(true);
		DominioService gerenciador = Configuracao.getServicoDominio();
		dominio = gerenciador.getDominio(id);
		if (dominio == null){
			
		}
		
		VocabularioService gerenciadorVocabulario = Configuracao.getServicoVocabulario();
		
		if (dominio != null && !dominio.getVocabularios().isEmpty()) {
			List<Integer> idsVocabularioDominio = dominio.getVocabularios();
			if (dominio.getIdVocabularioDominio() != 0) {
				idsVocabularioDominio.add(dominio.getIdVocabularioDominio());
			}
			
			for (Integer vocabulario: idsVocabularioDominio) {
				Vocabulario v = null;
				try {
					v = gerenciadorVocabulario.getVocabularioById(vocabulario);
				
					defaultListaVocabulariosAdd.addElement(v.getPrefixo() + " - " + v.getNome());
					defaultListaVocabularios.removeElement(v.getPrefixo() + " - " + v.getNome());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		adicionarModels();
		
		botaoExcluir.setEnabled(true);
		carregarDadosDominio();
	}
	
	/**
	 * Usado para carregar os dados de um dominio na interface
	 */
	private void carregarDadosDominio(){
		textFieldNome.setText(dominio.getNome());
	}
	
	private void atribuirAcoes(){
		/**
		 * Inicia um novo dominio
		 */
		botaoNovo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				limparCampos();
				botaoExcluir.setEnabled(false);
				botaoNovo.setEnabled(false);
			}
		});
		
		/**
		 * Salva o dominio atual 
		 */
		botaoSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String nome = textFieldNome.getText();
				dominio.setNome(nome);
				
				VocabularioService gerenciadorVocabulario = Configuracao.getServicoVocabulario();
				
				dominio.setVocabularios(new ArrayList<Integer>());
				Enumeration<String> lista = defaultListaVocabulariosAdd.elements();
				boolean temVocabularioDominio = false;
				while(lista.hasMoreElements()){
					String l = lista.nextElement();
					String prefixo = l.split(" - ")[0];
					int id = gerenciadorVocabulario.getVocabularioByPrefixo(prefixo).getId();
					
					if (id == dominio.getIdVocabularioDominio()){
						temVocabularioDominio = true;
						continue;
					}
					
					dominio.addVocabulario(id);
				}
				
				if (!temVocabularioDominio){
					dominio.setIdVocabularioDominio(0);
				}
				
				try {
					
					dominio.validar();
					
					gerenciador.adicionarDominio(dominio);
					
					JOptionPane.showMessageDialog(null, Sap.MENSAGEM_INFORMACAO_SUCESSO.get(), Sap.CONCLUIDO.get(), JOptionPane.INFORMATION_MESSAGE);
				
					
					limparCampos();
					popularDominios();
					
				} catch (Exception e1) {
					SwingHelper.tratarSenseException(null, e1);
				}
				painelDominios.repaint();
				rolagem.repaint();
			}
		});
		
		/**
		 * Irá usar o ID atual do domínio selecionado para remover
		 */
		botaoExcluir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int opcao = SwingHelper.questionarUsuario(Sap.MENSAGEM_AVISO_REMOVER_ITEM.get(dominio.getNome()), Sap.REMOVER.get(), Sap.SIM.get(), Sap.NAO.get());
				if (opcao != 0)
					return;
				
				gerenciador.removerDominio(dominio.getId());
				
				try {
					popularDominios();
				} catch (SenseRDFException e1) {
					e1.printStackTrace();
				}
				painelDominios.repaint();
				rolagem.repaint();
				limparCampos();
				botaoNovo.setEnabled(false);
				botaoExcluir.setEnabled(false);
			}
		});
		
		/**
		 * Usado para adicionar um vocabulário a lista de geração
		 */
		botaoAdicionar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> selecionado = listaVocabularios.getSelectedValuesList();
				adicionar(defaultListaVocabulariosAdd, selecionado);
				remover(defaultListaVocabularios, selecionado);
				adicionarModels();
			}
		});
		
		/**
		 * Usado para remover um vocabulário da lista de geração
		 */
		botaoRemover.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> selecionados = listaVocabulariosAdicionados.getSelectedValuesList();
				adicionar(defaultListaVocabularios, selecionados);
				remover(defaultListaVocabulariosAdd, selecionados);
				adicionarModels();
			}
		});
	}
	
	/**
	 * Zera os campos para iniciar uma nova adição
	 */
	private void limparCampos(){
		dominio = new Dominio();
		textFieldNome.setText("");
		defaultListaVocabularios.removeAllElements();
		defaultListaVocabulariosAdd.removeAllElements();
		try {
			carregarConfiguracaoDominio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void carregarConfiguracaoDominio() throws Exception
	{
		VocabularioService gerenciadorVocabulario = Configuracao.getServicoVocabulario();
		
		//Carrega todos os vocabulários
		List<Vocabulario> vocabularios = gerenciadorVocabulario.getAllVocabularios();
		if (!vocabularios.isEmpty())
			for (Vocabulario l : vocabularios) 
				defaultListaVocabularios.addElement(l.getPrefixo() + " - "+ l.getNome());

		
		//Busca a lista de vocabulários já adicionados pelo usuário
		if (dominio != null && !dominio.getVocabularios().isEmpty()) {
			List<Integer> listaVocAdd = dominio.getVocabularios();
			for (Integer vocabulario: listaVocAdd) 
			{
				Vocabulario v = gerenciadorVocabulario.getVocabularioById(vocabulario);
				defaultListaVocabulariosAdd.addElement(v.getPrefixo() + " - " + v.getNome());
				defaultListaVocabularios.removeElement(v.getPrefixo() + " - " + v.getNome());
			}
		}
		
		adicionarModels();
	}
	
	private void adicionarModels(){
		listaVocabularios.setModel(defaultListaVocabularios);
		listaVocabulariosAdicionados.setModel(defaultListaVocabulariosAdd);
	}
	
	private void adicionar(DefaultListModel<String> lista, List<String> add){
		for (String l: add){
			lista.addElement(l);
		}
	}
	
	private void remover(DefaultListModel<String> lista, List<String> add){
		for (String l: add){
			lista.removeElement(l);
		}
	}
}
