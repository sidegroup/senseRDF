/**
 * 
 */
package br.com.sideresearchgroup.senserdf.swing;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import br.com.sideresearchgroup.senserdf.resources.Imagem;

/**
 * @author ayrton
 * 
 */
public class FactoryInterface {

	public static JButton createJButton(String texto, int x, int y, int width, int height) {
		return createJButton(texto, x, y, width, height, null);
	}

	public static JButton createJButton(int x, int y, int width, int height) {
		return createJButton(null, x, y, width, height, null);
	}
	
	public static JButton createJButton(int x, int y, int width, int height, Imagem imagem) {
		return createJButton(null, x, y, width, height, imagem);
	}
	
	public static JButton createJButton(String texto, int x, int y, int width, int height, Imagem imagem) {
		JButton botao = new JButton();
		botao.setBounds(x, y, width, height);
		botao.setText(texto);
		if (imagem != null){
			botao.setIcon(criarImageIcon(imagem));
		}
		botao.setBackground(new Color(235, 236, 237));
		return botao;
	}

	public static JLabel createJLabel(String texto, int x, int y, int width, int height) {
		JLabel label = new JLabel();
		label.setText(texto);
		label.setBounds(x, y, width, height);
		return label;
	}

	public static JLabel createJLabel(int x, int y, int width, int height) {
		return createJLabel(null, x, y, width, height);
	}

	public static JTextField createTextField(int x, int y, int width, int height) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, width, height);
		return textField;
	}
	
	public static JTextField createJTextField(int x, int y, int width, int height){
		JTextField jtf = new JTextField();
		jtf.setBounds(x, y, width, height);
		return jtf;
	}

	public static JPanel createPanelText(int x, int y, int width, int height, String texto, Color color) {
		JPanel painel = new JPanel();
		painel.setBounds(x, y, width, height);
		painel.setBackground(color);
		painel.setLayout(null);

		JLabel label = new JLabel();
		label.setText(texto);
		label.setBounds(5, 5, width - 20, height - 20);
		painel.add(label);
		return painel;
	}
	
	public static JCheckBox createJCkeckBox(String texto, int x, int y, int width, int height){
		JCheckBox check = new JCheckBox();
		check.setBounds(x, y, width, height);
		check.setText(texto);
		return check;
	}
	
	public static JCheckBox createJCkeckBox(int x, int y, int width, int height){
		return createJCkeckBox(null, x, y, width, height);
	}
	
	public static JPanel createJPanel(int x, int y, int width, int height, Color color){
		JPanel painel = new JPanel();
		painel.setBounds(x, y, width, height);
		painel.setBackground(color);
		painel.setLayout(null);
		
		return painel;
	}
	
	public static JMenuItem createJMenuItem(Imagem imagem){
		JMenuItem jmi = new JMenuItem();
		
		if (imagem != null) {
			jmi.setIcon(criarImageIcon(imagem));
		}
		
		return jmi;
	}
	
	public static JMenu createJMenu(Imagem imagem) {
		JMenu jm = new JMenu();
		if (imagem != null){
			jm.setIcon(criarImageIcon(imagem));
		}
		return jm;
	}
	
	
	
	public static JTextPane createJTextPane(int x, int y, int width, int height){
		JTextPane jta = new JTextPane();
		jta.setBounds(x, y, width, height);
		jta.setEditable(false);
		return jta;
	}
	
	public static JScrollPane createJScrollPane(JComponent painel, int x, int y, int width, int height){
		if (painel instanceof JPanel) {
			((JTextPane) painel).setEditable(false);
		}
		JScrollPane scroll = new JScrollPane(painel);
		scroll.setBounds(x, y, width, height);
		
		return scroll;
	}
	
	public static ImageIcon criarImageIcon(Imagem imagem){
		return new ImageIcon(imagem.getURL());
	}
	
	/**
	 * Usado para adicionar um item abaixo do outro automáticamente, onde o resultado será (y + adicional + 3)
	 * @param y
	 * @param adicional
	 * @return
	 */
	public static int atualizarY(int y, int adicional) {
		return y + adicional + 3;
	}
}
