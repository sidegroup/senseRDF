package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.com.sideresearchgroup.senserdf.arq.Formato;
import br.com.sideresearchgroup.senserdf.dominio.Dominio;
import br.com.sideresearchgroup.senserdf.resources.Imagem;
import br.com.sideresearchgroup.senserdf.resources.Sap;

public class BarraStatus extends JPanel{

	private static final long serialVersionUID = 791630344854588024L;
	public final static int STATUS_VERDE = 1;
	public final static int STATUS_AMARELO = 2;
	public final static int STATUS_VERMELHO = 3;
	
	private JLabel dominioAtual;
	private JLabel formatoAtual;
	private JLabel imagemStatus;
	
	private ImageIcon imagemIco = FactoryInterface.criarImageIcon(Imagem.STATUS_VERDE);
	private ImageIcon imagemIco2 = FactoryInterface.criarImageIcon(Imagem.STATUS_AMARELO);
	private ImageIcon imagemIco3 = FactoryInterface.criarImageIcon(Imagem.STATUS_VERMELHO);
	
	private static BarraStatus barraStatus = null;
	
	private BarraStatus(int x, int y, int w, int h){
		super();
		this.setLayout(null);
		this.setBounds(x, y, w, h);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setBackground(Color.WHITE);
		
		dominioAtual = FactoryInterface.createJLabel("-", 0, 0, 190, 30);
		dominioAtual.setHorizontalAlignment(SwingConstants.CENTER);
		dominioAtual.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		formatoAtual = FactoryInterface.createJLabel("-", 195, 0, 100, 30);
		formatoAtual.setHorizontalAlignment(SwingConstants.CENTER);
		imagemStatus = FactoryInterface.createJLabel(300, 0, 25, 30);
		imagemStatus.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.add(dominioAtual);
		this.add(formatoAtual);
		this.add(imagemStatus);
		
		alterarCorStatus(STATUS_VERDE);
		
		aplicarIdioma();
		
		this.repaint();
	}
	
	public void aplicarIdioma() {
		dominioAtual.setToolTipText(Sap.STATUS_DOMINIO.get());
	}
	
	public static BarraStatus getInstance(){
		if (barraStatus == null) {
			barraStatus = new BarraStatus(660, 33, 325, 30);
		}
		
		return barraStatus;
	}
	
	public void setFormato(Formato formato){
		formatoAtual.setText(formato.name());
		this.repaint();
	}
	
	public void setDominio(Dominio dominio) {
		if (dominio == null) {
			return;
		}
		
		dominioAtual.setText(dominio.getNome());
		this.repaint();
	}
	
	private void alterarCorStatus(int status){
		switch (status) {
		case STATUS_VERDE:
			imagemStatus.setIcon(imagemIco);
			break;
		case STATUS_AMARELO:
			imagemStatus.setIcon(imagemIco2);
			break;
		case STATUS_VERMELHO:
			imagemStatus.setIcon(imagemIco3);
			break;
		default:
			imagemStatus.setIcon(imagemIco);
			break;
		}
		
		this.repaint();
	}
	
	public void verde(){
		alterarCorStatus(STATUS_VERDE);
		this.repaint();
	}
	
	public void amarelo(){
		alterarCorStatus(STATUS_AMARELO);
	}
	
	public void vermelho(){
		alterarCorStatus(STATUS_VERMELHO);
	}
}
