import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.*;


class FrameP extends JFrame{
	
	JPanel c;
	
/*Title screen*/
	JLabel title = new JLabel("Benvenuti in UNO!");
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
		this.setSize(600,500);
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
		 
		your_name.setHorizontalAlignment(SwingConstants.CENTER);
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
		
	}
	
	private void GameTable(){
		
		c.removeAll();
		c = (JPanel)this.getContentPane();
		this.setSize(300,201);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		JLabel index = new JLabel(user_1 + " VS " + user_2);
		
		c.add(index, c);
		
		
		
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
			}
			
		}
		
	}
	

	public static void main(String[] args)

	{

		
		JFrame mainWindow = new FrameP();
		

	}
	
}

