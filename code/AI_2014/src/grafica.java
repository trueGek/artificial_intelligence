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
	JButton start = new JButton("Inizia gioco");
	JButton close = new JButton("Chiudi");

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
		titles.setSize(600,400);
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
	
	
	class StartGame implements ActionListener {
		
		public void actionPerformed(ActionEvent e){
			
			JButton button = (JButton)e.getSource();
			if(button.getText()=="Inizia gioco"){
				String name = JOptionPane.showInputDialog(null,"Come ti chiami?", null);

			}
			
		}
		
	}
	

	public static void main(String[] args)

	{

		
		JFrame mainWindow = new FrameP();
		

	}
	
}

