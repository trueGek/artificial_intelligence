import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.*;


class FrameP extends JFrame{
	
	JPanel c;
	
/*Title screen*/
	JLabel title = new JLabel("Benvenuti in UNO!");
	JLabel match = new JLabel("Uno - Partita");
	JLabel intro = new JLabel("Seleziona il tipo di gioco");
	JRadioButton game_1 = new JRadioButton("CPU VS umano");
	JRadioButton game_2 = new JRadioButton("CPU VS CPU");
	ButtonGroup group = new ButtonGroup();
	JButton cont = new JButton("Continua");
	JButton start = new JButton("Inizia gioco");
	JButton close = new JButton("Chiudi");

/*Inserimento nome*/
	String user_1 = "CPU 1";
	String user_2 = "CPU 2";
	JTextField your_name;
	
/*Structure*/
	JPanel titles = new JPanel();
	JPanel ts_buttons1 = new JPanel();
	JPanel ts_buttons2 = new JPanel();
	
	public FrameP(){
	
	FrameStart();
	
	}
	
	
	private void FrameStart(){
		
		
		
		c = (JPanel)this.getContentPane();
		this.setSize(300,200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BorderLayout());
		
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.PLAIN, 25));
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		
		/*Ascoltatore per eseguire le azioni*/
		start.addActionListener(new StartGame());
		
		group.add(game_1);
		group.add(game_2);
		
		titles.setLayout(new BorderLayout());
		titles.setSize(600,200);
		titles.add(title, BorderLayout.NORTH);
		
		ts_buttons1.setLayout(new BorderLayout());
		
		ts_buttons1.add(intro, BorderLayout.NORTH);
		ts_buttons1.add(game_1, BorderLayout.WEST);
		ts_buttons1.add(game_2, BorderLayout.EAST);
		
		start.setEnabled(true);
		close.setEnabled(true);
		
		ts_buttons2.setLayout(new FlowLayout());
		ts_buttons2.add(start);
		ts_buttons2.add(close);
		
		c.add(titles, BorderLayout.NORTH);
		c.add(ts_buttons1, BorderLayout.CENTER);
		c.add(ts_buttons2, BorderLayout.SOUTH);
		
		this.setVisible(true);
		
	}
	
	
	private void InsertName() {
				
		c.removeAll();
		
		c = (JPanel)this.getContentPane();
		this.setSize(300,200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		user_2 = "Giocatore umano";
		your_name = new JTextField(user_2,15);
		
		JLabel desc_name = new JLabel("Inserisci il tuo nome:");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.PLAIN, 25));
		desc_name.setHorizontalAlignment(SwingConstants.CENTER);
		desc_name.setFont(new Font("Serif", Font.PLAIN, 18));
		your_name.setFont(new Font("Serif", Font.PLAIN, 18));

		
		cont.addActionListener(new PlayGame());
		
		JPanel field = new JPanel();
		field.setLayout(new BorderLayout());
		
		field.add(your_name, BorderLayout.NORTH);		
		c.add(title, c);
		c.add(Box.createRigidArea(new Dimension(0,10)));
		c.add(desc_name, c);
		c.add(Box.createRigidArea(new Dimension(0,10)));
		c.add(field, c);
		c.add(Box.createRigidArea(new Dimension(0,10)));
		c.add(cont, c);
		
		this.setVisible(true);
	}
	
	private void GameTable(){
		
		c.removeAll();
		c = (JPanel)this.getContentPane();
		this.setSize(600,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		
// Intestazione
		
		JLabel index = new JLabel(user_1 + " VS " + user_2);
		index.setFont(new Font("Serif", Font.PLAIN, 18));
		match.setFont(new Font("Serif", Font.PLAIN, 25));
		
		JPanel z = new JPanel();
		JPanel s = new JPanel();
		
		z.setMaximumSize(new Dimension(300,10));
		s.setMaximumSize(new Dimension(300,10));

// Carte Giocatore 1
		
		JPanel g1i = new JPanel();
		g1i.setMaximumSize(new Dimension(700,10));
		g1i.setBackground(Color.BLUE);

		JLabel usr1 = new JLabel(user_1);
		usr1.setFont(new Font("Arial", Font.PLAIN, 18));
		usr1.setForeground(Color.WHITE);
		
		g1i.add(usr1, g1i);
		
		
		JPanel g1 = new JPanel();
		
		g1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		g1.setBackground(Color.BLUE);
		
		String deck[] = new String [100];
		
		deck[0]="3";
		deck[1]="2";
		deck[2]="+4";
		deck[3]="7";
		
		
		for(int i = 0; i < deck.length & deck[i]!=null; i++){
			
			JButton card_i= new JButton(deck[i]);
			
			card_i.setPreferredSize(new Dimension(100,100));
			card_i.setFont(new Font("Arial", Font.PLAIN, 25));
			card_i.setForeground(Color.RED);
			
			card_i.addActionListener(new PlayCard(deck[i], "red"));
			
			g1.add(card_i, g1);
			
			
			if(card_i.getText()=="+4"){
				
				System.out.print("Caccamo");
				
			}
			
			
		}
		
		
//Tavolo
		
		
		JPanel desc = new JPanel();
		
		desc.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		JLabel label_1 = new JLabel("Pila");
		label_1.setFont(new Font("Arials", Font.PLAIN, 25));
		
		JLabel label_2 = new JLabel("Mazzo");
		label_2.setFont(new Font("Arial", Font.PLAIN, 25));
		
		desc.add(label_1, desc);
		desc.add(Box.createRigidArea(new Dimension(100,10)));
		desc.add(Box.createRigidArea(new Dimension(100,10)));
		desc.add(Box.createRigidArea(new Dimension(120,10)));
		desc.add(label_2, desc);
		
		
		JPanel t = new JPanel();
		
		t.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		JButton stack = new JButton("5");
		stack.setPreferredSize(new Dimension(100,100));
		JButton pass = new JButton("Passa");
		JButton uno = new JButton("UNO!!!");
		JButton restart = new JButton("Ricomincia");
		restart.addActionListener(new ResetGame());
		JButton take = new JButton("Pesca");
		take.setPreferredSize(new Dimension(100,100));

		
		t.add(stack, t);
		t.add(pass, t);
		t.add(uno, t);
		t.add(restart, t);
		t.add(take, t);
		
// Carte Giocatore 2
		
		
		JPanel g2i = new JPanel();
		g2i.setMaximumSize(new Dimension(700,10));
		g2i.setBackground(Color.RED);

		JLabel usr2 = new JLabel(user_2);
		usr2.setFont(new Font("Arial", Font.PLAIN, 18));
		usr2.setForeground(Color.WHITE);
		
		g2i.add(usr2, g2i);
		
		JPanel g2 = new JPanel();
				
		g2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		g2.setBackground(Color.RED);
				
		String deck2[] = new String [100];
				
		deck2[0]="3";
		deck2[1]="2";
		deck2[2]="+4";
		deck2[3]="7";
				
				
		for(int i = 0; i < deck.length & deck[i]!=null; i++){
					
			JButton card_i= new JButton(deck[i]);
					
			card_i.setPreferredSize(new Dimension(100,100));
			card_i.setFont(new Font("Comic-sans", Font.PLAIN, 25));
			card_i.setForeground(Color.RED);
					
			card_i.addActionListener(new PlayCard(deck[i], "red"));
					
			g2.add(card_i, g2);
					
					
			if(card_i.getText()=="+4"){
						
				System.out.print("Caccamo");
						
			}
					
					
		}

		
// Definizione tavolo
		
		// Intestazione
		z.add(match, z);
		s.add(index, s);
		c.add(z, c);
		c.add(s, c);
		
		// Carte Giocatore 1
		c.add(Box.createRigidArea(new Dimension(0,10)));
		c.add(g1i, c);
		c.add(g1, c);
		c.add(desc, c);
		c.add(t, c);
		c.add(g2, c);
		c.add(g2i, c);
			
		this.setVisible(true);
		
	}
	
	private void ShowCard(String x, String y) {
		
		c.removeAll();
		
		c = (JPanel)this.getContentPane();
		this.setSize(300,200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BorderLayout());
		
		JLabel played_card = new JLabel("Hai giocato il " + x + " " + y);
		
		
		c.add(match, BorderLayout.NORTH);
		c.add(played_card, BorderLayout.CENTER);
		
		JButton Next = new JButton("Prossima mossa");
		c.add(Next, BorderLayout.SOUTH);

		Next.addActionListener(new PlayGame());
		
	}
	
	
	
	class ResetGame implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			JButton button = (JButton)e.getSource();
			if(button.getText()=="Ricomincia"){
				
				c.removeAll();
				
				user_2 = "CPU 2";
				
				FrameStart();
				
			}
		
		}
		
	}
	
	class StartGame implements ActionListener {
		
		public void actionPerformed(ActionEvent e){
			
			JButton button = (JButton)e.getSource();
			if(button.getText()=="Inizia gioco"){
				if(game_1.isSelected()){
					InsertName();
					start.removeActionListener(this);
				}else{
					
					if(game_2.isSelected()){
						GameTable();
						start.removeActionListener(this);
						
					}
					
				}
			}
			
		}
		
	}
	
	class PlayGame implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			JButton button = (JButton)e.getSource();
			if(button.getText()=="Continua"){
				
				user_2 = your_name.getText();
				GameTable();
				cont.removeActionListener(this);
			}else{
				
				if(button.getText()=="Prossima mossa"){
					
					GameTable();
					
				}
				
			}
			
		}
		
	}
	
	
	
	class PlayCard implements ActionListener{
		
		private final String num;
		private final String color;
		
		PlayCard(final String n, final String c){
			
			this.num = n;
			this.color = c;
			
		}
		
		public void actionPerformed(ActionEvent e){
			
			if(num=="+4"&&color=="red"){
				
				ShowCard(num, color);
				
			}
			
		}
		
	}

	public static void main(String[] args)

	{

		
		JFrame mainWindow = new FrameP();
		

	}
	
}

