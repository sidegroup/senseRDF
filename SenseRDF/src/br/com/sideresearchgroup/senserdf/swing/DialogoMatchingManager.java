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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import br.com.sideresearchgroup.senserdf.arq.ValidadorCorrespondencia;
import br.com.sideresearchgroup.senserdf.arq.erro.SenseRDFException;
import br.com.sideresearchgroup.senserdf.conversor.ValidacaoCorrespondenciaListener;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;


/**
 * @author ayrton
 * 
 */
public class DialogoMatchingManager {

	private JDialog caixaDialogo = new JDialog();
	private JLabel textoExplicativo = new JLabel();

	private JLabel textoMetadado = new JLabel();
	private JPanel painelMetadado = new JPanel();

	private JLabel textoConstrutor = new JLabel();
	private JPanel painelConstrutor = new JPanel();

	private JLabel textoVocabulario = new JLabel();
	private JPanel painelVocabulario = new JPanel();

	private JLabel textoAdicionar = new JLabel();
	private JPanel painelAdicionar = new JPanel();

	private JButton botaoConcluir = new JButton();
	private JButton botaoCancelar = new JButton();

	private JPanel painelMetadados = new JPanel();
	private JPanel painelPrincipal = new JPanel();
	private ArrayList<PainelMetadado> paineisMetadados = new ArrayList<PainelMetadado>();
	private JFrame framePai = null;
	private JScrollPane rolagem = null;

	private List<ValidadorCorrespondencia> dataset = null;
	private ValidacaoCorrespondenciaListener listener = null;

	public DialogoMatchingManager(JFrame framePai, List<ValidadorCorrespondencia> metadados, ValidacaoCorrespondenciaListener listener) {
		this.dataset = metadados;
		this.listener = listener;
		this.construirDialogoMatching(framePai);
	}

	/**
	 * Constroi a interface do Dialogo
	 * 
	 * @param frame
	 */
	private void construirDialogoMatching(JFrame frame) {
		framePai = frame;

		caixaDialogo = new JDialog(framePai, Sap.MATCHING_TITULO.get(), true);
		iniciarInterface();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		caixaDialogo.setBounds((screenSize.width - 640) / 2, (screenSize.height - 500) / 2, 640, 500);
		caixaDialogo.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		caixaDialogo.setResizable(false);
		caixaDialogo.setVisible(true);
	}

	/**
	 * Posiciona as partes da interface nos seus devidos lugares
	 */
	private void iniciarInterface() {
		Color vermelho = new Color(240, 217, 217);

		painelPrincipal.setLayout(null);

		textoExplicativo.setBounds(10, 10, 620, 70);
		painelPrincipal.add(textoExplicativo);

		painelMetadado.add(textoMetadado);
		painelMetadado.setBounds(10, 100, 165, 30);
		painelMetadado.setBackground(vermelho);
		painelPrincipal.add(painelMetadado);

		painelVocabulario.add(textoVocabulario);
		painelVocabulario.setBounds(180, 100, 165, 30);
		painelVocabulario.setBackground(vermelho);
		painelPrincipal.add(painelVocabulario);

		painelConstrutor.add(textoConstrutor);
		painelConstrutor.setBounds(350, 100, 165, 30);
		painelConstrutor.setBackground(vermelho);
		painelPrincipal.add(painelConstrutor);

		painelAdicionar.setLayout(null);
		textoAdicionar.setBounds(10, 2, 100, 28);
		painelAdicionar.add(textoAdicionar);
		painelAdicionar.setBounds(520, 100, 110, 30);
		painelAdicionar.setBackground(vermelho);
		painelPrincipal.add(painelAdicionar);

		painelMetadados.setLayout(null);
		
		criarPaineisMetadados();
		
		rolagem = new JScrollPane();
		rolagem.setViewportView(painelMetadados);
		rolagem.setBounds(10, 130, 620, 300);
		painelPrincipal.add(rolagem, BorderLayout.CENTER);

		botaoConcluir.setBounds(170, 433, 150, 30);
		botaoConcluir.setIcon(FactoryInterface.criarImageIcon(Imagem.CONCLUIR));
		painelPrincipal.add(botaoConcluir);

		botaoCancelar.setBounds(330, 433, 150, 30);
		botaoCancelar.setIcon(FactoryInterface.criarImageIcon(Imagem.CANCELAR));
		painelPrincipal.add(botaoCancelar);

		caixaDialogo.getContentPane().add(painelPrincipal);
		this.atributirAcoes();
		this.aplicarIdiomaEscolhido();
	}

	/**
	 * Responsável pela criação dos paineis
	 */
	private void criarPaineisMetadados() {
		int controlador = 0;

		for (ValidadorCorrespondencia tripla : dataset) {
				
			PainelMetadado painelMetadado = new PainelMetadado(tripla);
			paineisMetadados.add(painelMetadado);

			JPanel painelConstruido = painelMetadado.retornarPainel();
			painelConstruido.setBounds(0, controlador, 620, 40);
			controlador += 43;

			painelMetadados.add(painelConstruido);

		}

		painelMetadados.setPreferredSize(new Dimension(580, controlador));

	}

	/**
	 * Aplica o Idioma
	 */
	private void aplicarIdiomaEscolhido() {
		textoExplicativo.setText(Sap.MATCHING_DESCRICAO.get());
		textoConstrutor.setText(Sap.TERMO.get());
		textoVocabulario.setText(Sap.VOCABULARIO.get());
		textoMetadado.setText(Sap.METADADO.get());
		textoAdicionar.setText(Sap.ADICIONAR.get(Sap.TERMO.get()));
		botaoConcluir.setText(Sap.CONCLUIR.get());
		botaoCancelar.setText(Sap.CANCELAR.get());
	}

	/**
	 * Atribuir as ações aos botões
	 */
	private void atributirAcoes() {
		
		//Aqui é onde será verificada o que o usuário escolheu/adicionou
		botaoConcluir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				for (PainelMetadado painel : paineisMetadados) {
					ValidadorCorrespondencia validador = painel.getValidador();
					String[] resultado = painel.getResultado();
					validador.setResultadoValidacao(resultado[0], resultado[1]);
					validador.setAdicionarVocabulario(painel.isAdicionarOntologia());
				}
				
				try {
					listener.resultadoValidacaoCorrespondencia(dataset);
				} catch (SenseRDFException e) {
					SwingHelper.tratarSenseException(framePai, e);
					return;
				}

				caixaDialogo.setVisible(false);
				caixaDialogo.dispose();
			}
		});

		botaoCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int opcao = SwingHelper.questionarUsuario(Sap.MENSAGEM_AVISO_CANCELAR.get(Sap.MATCHING_CANCELAMENTO.get()), Sap.CANCELAR.get(), Sap.SIM.get(), Sap.NAO.get());
				if (opcao == 0) {
					caixaDialogo.setVisible(false);
					caixaDialogo.dispose();
					try {
						listener.resultadoValidacaoCorrespondencia(null);
					} catch (SenseRDFException e) {
						SwingHelper.tratarSenseException(framePai, e);
					}
				}
			}
		});
	}
}