package tictactoelab02;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

class ButtonFrame extends JFrame {
	
	JButton button2, button4;
	
	// constructor for buttonFrame
	ButtonFrame(String title) {
		super(title); //invoke the JFrame constructor
		setLayout(new FlowLayout()); //set the layout manager
		
		button2 = new JButton(" 2 "); // construct a JButton
		add(button2); // add the button to the JFrame
		
		button4 = new JButton(" 4 "); // construct a JButton
		add(button4); // add the button to the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		button4 = new JButton(" 6 "); // construct a JButton
		add(button4); // add the button to the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		button4 = new JButton(" 8 "); // construct a JButton
		add(button4); // add the button to the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

public class UserChoiceButtons {
	public static void main(String[] args) {
		ButtonFrame frm = new ButtonFrame("Button Demo");
		
		frm.setSize(300, 150);
		frm.setVisible(true);
	}
}