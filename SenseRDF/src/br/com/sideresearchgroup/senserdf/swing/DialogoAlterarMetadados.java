/**value
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
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import br.com.sideresearchgroup.senserdf.arq.TriplaRDFSense;
import br.com.sideresearchgroup.senserdf.conversor.AlterarMetadadosListener;
import br.com.sideresearchgroup.senserdf.conversor.Dataset;
import br.com.sideresearchgroup.senserdf.resources.Sap;


/**
 * @author Ayrton
 *
 */
public class DialogoAlterarMetadados extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783485282910413812L;
	private JDialog caixaDialogo = new JDialog();
	private JLabel textoExplicativo = new JLabel();

	private JLabel textoMetadado = new JLabel();
	private JPanel painelMetadado = new JPanel();

	private JLabel textoVocabulario = new JLabel();
	private JPanel painelVocabulario = new JPanel();

	private JButton botaoConcluir = new JButton();
	private JButton botaoCancelar = new JButton();

	private JPanel painelMetadados = new JPanel();
	private JPanel painelPrincipal = new JPanel();
	private ArrayList<PainelInterno> paineisMetadados = new ArrayList<PainelInterno>();
	private JFrame framePai = null;
	private JScrollPane rolagem = null;

	private boolean canceladoOperacao = false;
	private Dataset dataset;
	private AlterarMetadadosListener listener;

	public DialogoAlterarMetadados(JFrame framePai, Dataset dataset, AlterarMetadadosListener listener) {
		this.dataset = dataset;
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

		caixaDialogo = new JDialog(framePai, Sap.ALTERAR.get(Sap.METADADOS.get()), true);
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
		painelMetadado.setBounds(10, 100, 200, 30);
		painelMetadado.setBackground(vermelho);
		painelPrincipal.add(painelMetadado);

		painelVocabulario.add(textoVocabulario);
		painelVocabulario.setBounds(220, 100, 410, 30);
		painelVocabulario.setBackground(vermelho);
		painelPrincipal.add(painelVocabulario);

		painelMetadados.setLayout(null);
		criarPaineisMetadados();
		rolagem = new JScrollPane();
		rolagem.setViewportView(painelMetadados);
		rolagem.setBounds(10, 130, 620, 300);
		painelPrincipal.add(rolagem, BorderLayout.CENTER);

		botaoConcluir.setBounds(170, 433, 150, 30);
		painelPrincipal.add(botaoConcluir);

		botaoCancelar.setBounds(330, 433, 150, 30);
		painelPrincipal.add(botaoCancelar);

		caixaDialogo.getContentPane().add(painelPrincipal);
		this.atributirAcoes();
		
		textoExplicativo.setText(Sap.ALTERAR_METADADOS_DESCRICAO.get());
		textoVocabulario.setText(Sap.DADO.get());
		textoMetadado.setText(Sap.METADADOS.get());
		botaoConcluir.setText(Sap.CONCLUIR.get());
		botaoCancelar.setText(Sap.CANCELAR.get());
	}

	/**
	 * Responsável pela criação dos paineis
	 */
	private void criarPaineisMetadados() {
		int controlador = 0;
		List<TriplaRDFSense> triplas = dataset.getTriplas();
		
		for (TriplaRDFSense t: triplas) {
			String chave = t.getMetadado().getMetadado();
			String valor = t.getDado();
			
			PainelInterno painel = new PainelInterno(chave, valor);
			paineisMetadados.add(painel);
			
			painel.setBounds(0, controlador, 620, 40);
			controlador += 43;
			
			painelMetadados.add(painel);
		}

		painelMetadados.setPreferredSize(new Dimension(580, controlador));

	}

	/**
	 * Atribuir as ações aos botões
	 */
	private void atributirAcoes() {
		
		//Aqui é onde será verificada o que o usuário escolheu/adicionou
		botaoConcluir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					HashMap<String, String> map = new HashMap<String, String>();
					for (PainelInterno pi : paineisMetadados){
						String[] hash = pi.getValor();
						map.put(hash[0], hash[1]);
					}
				
					listener.resultadoAlteracaoMetadado(dataset, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				caixaDialogo.dispose();
			}
		});

		botaoCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int opcao = SwingHelper.questionarUsuario(Sap.MENSAGEM_AVISO_CANCELAR.get(),Sap.ALTERAR.get(Sap.METADADOS.get()), Sap.SIM.get(), Sap.NAO.get());
				if (opcao == 0) {
					caixaDialogo.setVisible(false);
					caixaDialogo.dispose();
					canceladoOperacao = true;
				} else {
					return;
				}
			}
		});
	}

	public boolean cancelouSelecao() {
		return canceladoOperacao;
	}
	
	private class PainelInterno extends JPanel {
		
		private static final long serialVersionUID = 3529812997681365656L;
		private JLabel labelMetadado;
		private JTextField caixaDado;
		
		public PainelInterno(String metadado, String dado){
			Color azul = new Color(212, 212, 240);

			this.setLayout(null);
			this.setBackground(azul);
			
			labelMetadado = FactoryInterface.createJLabel(metadado, 10, 3, 200, 30);
			caixaDado = FactoryInterface.createJTextField(210,3, 380, 30);
			caixaDado.setText(dado);
			
			this.add(labelMetadado);
			this.add(caixaDado);
		}
		
		public String[] getValor(){
			String[] array = new String[2];
			array[0] = labelMetadado.getText();
			array[1] = caixaDado.getText();
			return array;
		}
	}
}
