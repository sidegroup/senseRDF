/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import br.com.sideresearchgroup.senserdf.arq.Configuracao;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;


/**
 * @author ayrton
 *
 */
public class Sobre extends JDialog {
	
	private static final long serialVersionUID = -1604806363470200836L;
	private JPanel painelGeral;
	private JLabel imagemSense;
	private JLabel labelConfig;
	private JLabel labelVersao;
	private JLabel labelDescricao;
	private JLabel labelAno;
	private JButton botaoHomepage;
	private JButton botaoCredito;
	private JScrollPane rolagem;
	private int widthJanela = 300;
	private int heightJanela = 270;
	private JList<String> creditos;
	
	private boolean exibir = true;
	
	
	public Sobre(JFrame framePai){
		super(framePai, Sap.SOBRE.get(), true);
		
		imagemSense = new JLabel(FactoryInterface.criarImageIcon(Imagem.SENSERDF_64));
		imagemSense.setBounds(100, 00, 100, 100);
		
		painelGeral = FactoryInterface.createJPanel(0, 0, 350, 250, Color.WHITE);
		labelConfig = FactoryInterface.createJLabel(Configuracao.NOME_FERRAMENTA, 0, 80, widthJanela, 30);
		labelConfig.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelVersao = FactoryInterface.createJLabel(Configuracao.VERSAO, 0, 100, widthJanela, 30);
		labelVersao.setHorizontalAlignment(SwingConstants.CENTER);
		painelGeral.add(labelVersao);
		
		
		creditos = new JList<String>(creditos());
		creditos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		creditos.setLayoutOrientation(JList.VERTICAL);
		creditos.setAutoscrolls(true);
		rolagem = new JScrollPane(creditos);
		rolagem.setBounds(5, 130, 285, 60);
		painelGeral.add(rolagem);
		rolagem.setVisible(false);
		
		labelDescricao = FactoryInterface.createJLabel(Sap.SOBRE_DESCRICAO.get(), 10, 120, widthJanela - 20, 60);
		labelDescricao.setHorizontalAlignment(SwingConstants.CENTER);
		painelGeral.add(labelDescricao);
		
		labelAno = FactoryInterface.createJLabel(String.valueOf("Side Research Group [2011 - 2015]"), 0, 165, widthJanela, 30);
		labelAno.setHorizontalAlignment(SwingConstants.CENTER);
		painelGeral.add(labelAno);
		
		botaoCredito = FactoryInterface.createJButton(Sap.CREDITOS.get(), 5, (heightJanela - 70), 120, 30);
		painelGeral.add(botaoCredito);
		botaoHomepage = FactoryInterface.createJButton(Sap.HOME.get(), 170, (heightJanela - 70), 120, 30);

		
		
		botaoHomepage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop d = Desktop.getDesktop();  
				try {  
					d.browse( new URI( Configuracao.URL_PROJETO ) );  
				}  
				catch ( IOException e1 ) {  
					System.out.println(e1);  
				}  
				catch ( URISyntaxException e1 ) {  
					System.out.println(e1);  
				}
			}
		});
		
		botaoCredito.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (exibir) {
					rolagem.setVisible(true);
					labelDescricao.setVisible(false);
					labelAno.setVisible(false);
					painelGeral.repaint();
					
					exibir = false;
				} else {
					rolagem.setVisible(false);
					labelDescricao.setVisible(true);
					labelAno.setVisible(true);
					painelGeral.repaint();
					
					exibir = true;
				}
			}
		});
		
		painelGeral.add(imagemSense);
		painelGeral.add(labelConfig);
		painelGeral.add(botaoHomepage);
		this.add(painelGeral);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width - widthJanela) / 2, (screenSize.height - heightJanela) / 2, widthJanela, heightJanela);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	
	public String[] creditos(){
		String[] lista = {
				"Ayrton Nádgel",
				"Bruno Paulino",
				"Damires Souza",
				"Luiz Carlos",
				"Naftali França",
				"Paulo Linhares",
				"Walter Travassos"
		};
		
		Arrays.sort(lista);
		
		return lista;
	}
}
