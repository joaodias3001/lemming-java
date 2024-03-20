package lemming.jogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import lemming.lemming.Bloqueador;
import lemming.lemming.Construtor;
import lemming.lemming.Demolidor;
import lemming.lemming.Escavador;
import lemming.lemming.Flutuador;
import lemming.lemming.Habilidade;
import lemming.lemming.Lemming;
import lemming.lemming.LemmingBase;
import lemming.lemming.LemmingObserver;
import lemming.lemming.Mineiro;
import lemming.lemming.Trepador;
import lemming.mundo.Mundo;
import lemming.mundo.Nivel;

/** Representa o jogo dos lemmings
 */
@SuppressWarnings("serial")
public class JogoLemmings extends JFrame implements LemmingObserver {
	
	// elementos do jogo
	private Mundo mundo;  		  	 // o mundo atual
	private GeradorLemmings gerador; // o gerador de lemmigns
	private int nivel; 			     // nível atual do jogo

	// estatísticos do jogo
	private int numSalvar, numTotal, numSalvos, numFora;
	private long tempo;
	private int ciclos; 

	// os fornecedores da habilidades
	private ParBotaoFornecedor parAtual;
	// o mapa para saber para cada habiladide qual o fornecedor certo
	private HashMap<String, ParBotaoFornecedor> habilitadores = new HashMap<>();

	// o gestor dos tempos e respetivas configurações
	private Timer temporizador;
	private int DELAY_NORMAL = 33;
	private int DELAY_ACELARADO = 11;
	
	// as dimensões da zona de jogo
	private static final int COMPRIMENTO = 1200;
	private static final int ALTURA = 600;

	// imagem usada para melhorar as animações
	private Image ecran;
	
	// variáveis para os vários elementos visuais do jogo
	private JPanel zonaJogo = null;
	private static final Font fontStatus = new Font("ROMAN", Font.BOLD, 26 ); 
	private JLabel numSalvosLbl;
	private JLabel numForaLbl;
	private JLabel tempoLbl;

	/**
	 * construtor da aplicação
	 */
	public JogoLemmings( ) {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		initialize();  // inicializações da janela
	}	

	/** Método que começa o jogo
	 */
	public void comecarJogo(){
		// alterar o nível se quiserem testar um nível específico
		nivel = 1;     
		// jogar o nível
		jogarNivel();
	}
	
	/**
	 * Jogar um dado nível
	 */
	private void jogarNivel(){
		try {
			// ler o nível
			Nivel n = LevelReader.lerNivel(nivel);			
			mundo = n.getMundo();
			gerador = n.getGerador();

			// inciiar as estatísticas
			numSalvar = n.getNumSalvar();
			numSalvos = 0;
			numFora = 0;
			numTotal = n.getNumTotal();
			tempo = n.getTempo();

			// preparar a zona de jogo
			criarImagemAuxiliar( mundo.getMapa().getComprimento(), mundo.getMapa().getAltura() );
			Dimension d = new Dimension( mundo.getMapa().getComprimento(), mundo.getMapa().getAltura() );
			zonaJogo.setSize( d );
			zonaJogo.setPreferredSize( d );
			zonaJogo.getParent().revalidate();

			// atualizar a interface das estatíticas
			atualizarDisplayFora();
			atualizarDisplaySalvos();
			
			// desativar todos os habilitadores
			for( ParBotaoFornecedor p : habilitadores.values() ) {
				p.botao.setEnabled( false );
				p.fornecedor.setQuantidade( 0 );
			}
			
			// ativar os habilitadores que forem usados no nível
			Map<String,Integer> habil = n.getHabilidades();
			for( Entry<String,Integer> e: habil.entrySet() ) {
				ParBotaoFornecedor p = habilitadores.get( e.getKey() );
				//System.out.println( p + " para " + e.getKey() );
				p.fornecedor.setQuantidade( e.getValue() );
				p.botao.setText( ""+e.getValue() );
				p.botao.setEnabled( true );
			}

			// apresentar a mensagem de início do nível
			String titulo = n.getTitulo();
			String salvarStr = "<br>Objetivo: salvar " + numSalvar + " lemmings em " + numTotal;  
			long minutos = tempo / 60;
			long segundos = tempo % 60;
			String tempoStr = "<br>Tempo: " + minutos + ":" + (segundos>10? segundos: "0" + segundos);
			JOptionPane.showMessageDialog( this, "<html><p><center><h1>"+titulo+"</h1></center></p>" +
                    salvarStr + tempoStr + "</html>");
			
			// arrancar com o atualizador que vai atualizar o mundo 
			// a partir de agora será com o atualizarJogo
			temporizador.start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog( this, "E os ficheiros de nível?");
			e.printStackTrace();
		}
	}
	
	/** 
	 * método chamado automaticamente para atualizar o jogo.
	 * Atenção! Este método NÃO desenha nada. Usar o método desenharJogo para isso
	 */
	private void actualizarJogo() {
		// atualizar o mundo
		mundo.atualiza();
		
		// adicionar lemmings ao mundo
		LemmingBase l = gerador.criarLemming();
		if( l != null ) {
			mundo.addLemming(l);
			l.setJogo( this );
			numFora++;
			atualizarDisplayFora();
		}
		
		// atualizar o label que tem o etmpo
		ciclos--;
		long minutos = tempo / 60;
		long segundos = tempo % 60;
		tempoLbl.setText( minutos + ":" + (segundos>10? segundos: "0" + segundos) );
		if( ciclos <= 0 ) {
			tempo--;
			ciclos = 33;
		}
		
		testaFim(); // verificar se já acabou
	}
	
	public void lemmingMorreu(Lemming l) {
		numFora--;
		atualizarDisplayFora();
	}
	
	public void lemmingSaiu(Lemming l) {
		numFora--;
		numSalvos++;
		atualizarDisplayFora();
		atualizarDisplaySalvos();
	}
	
	/**
	 * Testa se o jogo chegou ao fim.
	 * Se chegou ao fim apresenta as opções de final de jogo
	 */
	private void testaFim(){
		if( numFora > 0 || gerador.temMais() || mundo.getEfeitos().size() != 0 )
			return;
		temporizador.stop();

		if( numSalvos >= numSalvar ) {
			String opcoes[] = {"Refazer este Nível", "Próximo Nivel", "Sair do jogo" };
			int res = JOptionPane.showOptionDialog(this, "Parabéns, completou este nível", "Vitória", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[1]);
			switch( res  ) {
				case 0: jogarNivel( ); break;
				case 1: nivel++; jogarNivel( ); break;
				case 2: System.exit( 0 );
			}
		} else {
			String opcoes[] = {"Voltar a tentar", "Sair do jogo" };
			int res = JOptionPane.showOptionDialog(this, "Não salvaste lemmings suficientes", "Pobres lemmings!", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[1]);
			switch( res  ) {
				case 0: jogarNivel( ); break;
				case 1: System.exit( 0 );
			}
		}
	}

	/**
	 * Método que vai ser usado para desenhar os elementos do jogo
	 * QUALQUER DESENHO DEVE SER FEITO AQUI
	 * @param g ambiente gráfico onde se vai desenhar
	 */
	private void desenharJogo( Graphics2D g ){
		if( ecran == null )
			return;
		
		// Usar um graphics2D da imagem auxiliar
		Graphics2D ge = (Graphics2D )ecran.getGraphics();

		// desenhar o mundo
		mundo.desenhar( ge );
		
		// agora que está tudo desenhado na imagem auxiliar, desenhar no ecrán
		g.drawImage( ecran, 0, 0, null );		
	}
	
	/** atualiza o display do número de lemmings salvos */
	private void atualizarDisplaySalvos() {
		numSalvosLbl.setText( numSalvos + "/" + numSalvar );
	}
	
	/** atualiza o display do número de lemmings ainda no mundo */
	private void atualizarDisplayFora() {
		numForaLbl.setText( "" + numFora );
	}

	/** processa o rato ser premido na zona de jogo
	 * @param e evento do rato
	 */
	protected void ratoPremido(MouseEvent e) {
		// ver qual o lemming selecionado, se algum
		Lemming l = mundo.getLemmingAt( e.getPoint() );
		
		// se há lemming e um habilitador selecionado		
		if( l != null && parAtual != null) {
			// é preciso ativar a habilidade do lemming
			parAtual.fornecedor.aplicarHabilidade( l );
			// e atualizar o botão da habilidade
			parAtual.botao.setText( ""+parAtual.fornecedor.getQuantidade() );
		}
	}
	
	/** Método chamado quando se pressiona o botão da "opção nuclear"
	 * e que deve atribuir uma bomba a cada lemming e parar a geração de lemmings
	 */
	private void rebentarNivel() {
		mundo.getLemmings().forEach( l -> l.detonar(150, 5) );
		gerador.setGeracao( false );
	}
	
	
	/** Cria os botões de ativar as habilidades e controlar o jogo
	 * @return o painel com os botões
	 */
	private Component getControlos() {
		//JPanel panelBotoes = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0) );
		JPanel panelBotoes = new JPanel( new GridLayout( 1, 0) );
		panelBotoes.setPreferredSize( new Dimension(COMPRIMENTO,80) );
		ButtonGroup grp = new ButtonGroup();
		// Criar os botões das habilidades
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/trepar.png" ), "trepador", (new Trepador()) ) );
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/escavar.png" ), "escavador", (new Escavador()) ));
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/flutuar.png" ), "flutuador", (new Flutuador()) ));
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/demolir.png" ), "demolidor", (new Demolidor()) ));
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/construir.png" ), "construtor", (new Construtor()) ));		
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/mineiro.png" ), "mineiro", (new Mineiro()) ));
		panelBotoes.add( criarBotaoHabilidade( grp, new ImageIcon( "art/icons/bloquear.png" ), "bloqueador", (new Bloqueador())));
		// Criar a bomba
		panelBotoes.add( criarBotaoFornecedor( grp, new ImageIcon( "art/icons/bomba.png" ), "bomba", new FornecedorBomba( 0 ) ) );
	
		// crfiar os botões de controlo
		panelBotoes.add( criarBotaoAcao( null, new ImageIcon( "art/icons/pausar.png" ), "Pausar", e -> pausar( (JToggleButton)e.getSource( ) ) ) );
		panelBotoes.add( criarBotaoAcao( null, new ImageIcon( "art/icons/apressar.png" ), "Acelerar", l -> temporizador.setDelay( ((JToggleButton)l.getSource()).isSelected()? DELAY_ACELARADO: DELAY_NORMAL ) ) );
		panelBotoes.add( criarBotaoSimples( new ImageIcon( "art/icons/mais.png" ), " ", l -> gerador.aumentarCadencia() ) );
		panelBotoes.add( criarBotaoSimples( new ImageIcon( "art/icons/menos.png" ), " ", l -> gerador.diminuirCadencia() ) );
		panelBotoes.add( criarBotaoSimples( new ImageIcon( "art/icons/nuclear.png" ), " ", l -> rebentarNivel() ) );

		// criar os labels para apresentar as estatísticas
		numSalvosLbl = criarLabel(new ImageIcon( "art/icons/salvos.png" ), "0/0" );
		panelBotoes.add( numSalvosLbl );

		numForaLbl = criarLabel(new ImageIcon( "art/icons/fora.png" ), "0" );
		panelBotoes.add( numForaLbl );
		
		tempoLbl = criarLabel( new ImageIcon( "art/icons/tempo.png" ), "0:0" );
		panelBotoes.add( tempoLbl );
		return panelBotoes;
	}

	/** Pausa o jogo 
	 * @param bt o botão de pausar
	 */
	private void pausar( JToggleButton bt ) {
		if( bt.isSelected() )
			temporizador.stop();
		else
			temporizador.start();
	}

	/** Cria um botão associado a uma habilidade. É um método auxiliar
	 * que cria automaticamente um fornecedor de habilidades 
	 * @param grp grupo de botões a que pertence
	 * @param i o icone do botão
	 * @param txt o texto do botão
	 * @param h a habilidade a ativar associada ao botão
	 * @return o botão criado
	 */
	private JToggleButton criarBotaoHabilidade( ButtonGroup grp, Icon i, String txt, Habilidade h ) {
		return criarBotaoFornecedor(grp, i, txt, new FornecedorHabilidade( 0, h ) );
	}
	
	/** Cria um botão associado a um fornecedor de habilidade
	 * @param grp grupo de botões a que pertence 
	 * @param i icone do botão
	 * @param txt o texto do botão
	 * @param forn o fonecedor de avalidade e ativar quando se ativa o botão
	 * @return o botão criado
	 */
	private JToggleButton criarBotaoFornecedor( ButtonGroup grp, Icon i, String txt, FornecedorHabilidade forn ) {
		JToggleButton bt = new JToggleButton( "0", i );		
		bt.setSize( 20, 30);
		ParBotaoFornecedor par = new ParBotaoFornecedor(forn, bt) ;
		habilitadores.put( txt, par );
		bt.addActionListener( e -> parAtual = par );
		bt.setHorizontalTextPosition( JButton.CENTER );
		bt.setVerticalTextPosition( JButton.TOP );
		if( grp != null )
			grp.add( bt );
		return bt;
	}

	/** Classe auxiliar que associa um fornecedor de habilidade
	 * a um botão da interface para quando se ativar um botão saber qual 
	 * o fornecedor, e quando se ativa uma habilidade
	 * saber qual o botão que se tem de atualizar
	 */
	private class ParBotaoFornecedor {
		FornecedorHabilidade fornecedor;
		JToggleButton botao;
		
		public ParBotaoFornecedor(FornecedorHabilidade fornecedor, JToggleButton botao) {
			super();
			this.fornecedor = fornecedor;
			this.botao = botao;
		}
	}
	
	/**
	 * Este método inicializa a zonaJogo, AQUI NÃO DEVEM ALTERAR NADA 	
	 */
	private JPanel getZonaJogo() {
		if (zonaJogo == null) {
			zonaJogo = new JPanel(){
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					desenharJogo( (Graphics2D)g );
				}
			};
			Dimension d = new Dimension(COMPRIMENTO, ALTURA);			
			zonaJogo.setPreferredSize( d );
			zonaJogo.setSize( d );
			zonaJogo.setMinimumSize( d );
			zonaJogo.setMaximumSize( d );
			zonaJogo.setBackground(Color.pink);
			zonaJogo.addMouseListener( new MouseAdapter(){
				public void mousePressed(MouseEvent e) {
					ratoPremido( e );
				}
			});
		}
		return zonaJogo;
	}


	/**
	 *  Inicializa a interface da aplicação, AQUI NÃO DEVEM ALTERAR NADA
	 */
	private void initialize() {
		// características da janela
		this.setLocationRelativeTo( null );
		this.setTitle("Lemmings");
		JScrollPane sp = new JScrollPane( getZonaJogo( ));
		sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		sp.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add( sp,BorderLayout.CENTER);
		getContentPane().add(getControlos(),BorderLayout.SOUTH);
	    this.pack(); 	  
	    Dimension d = new Dimension(COMPRIMENTO, ALTURA);			
		zonaJogo.setPreferredSize( d );
		zonaJogo.setSize( d );
		zonaJogo.setMinimumSize( d );
		zonaJogo.setMaximumSize( d );
	   // this.setResizable( false );
	    this.setLocationRelativeTo( null );
	    
	    // criar o temporizador
	    temporizador = new Timer( DELAY_NORMAL, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarJogo();
				zonaJogo.repaint();
			}
		} );
	}

	private void criarImagemAuxiliar( int comprimento, int altura ) {
		// criar a imagem para melhorar as animações e configurá-la para isso mesmo
		ecran = new BufferedImage( comprimento, altura, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ge = (Graphics2D )ecran.getGraphics();		
		ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}
	
	private JToggleButton criarBotaoAcao( ButtonGroup grp, Icon i, String txt, ActionListener l ) {
		JToggleButton bt = new JToggleButton( txt, i );		
		bt.setSize( 20, 30);
		bt.addActionListener( l );
		bt.setHorizontalTextPosition( JButton.CENTER );
		bt.setVerticalTextPosition( JButton.TOP );
		if( grp != null )
			grp.add( bt );
		return bt;
	}


	private JButton criarBotaoSimples( Icon i, String txt, ActionListener l ) {
		JButton bt = new JButton( txt, i );
		bt.addActionListener(l);
		bt.setHorizontalTextPosition( JButton.CENTER );
		bt.setVerticalTextPosition( JButton.TOP );
		return bt;
	}
	
	private JLabel criarLabel( Icon i, String txt ) {
		JLabel lbl = new JLabel( txt, i, JLabel.CENTER );
		lbl.setHorizontalTextPosition( JButton.CENTER );
		lbl.setVerticalTextPosition( JButton.TOP );
		lbl.setFont(fontStatus);
		return lbl;
	}
	
	public static void main( String args[] ){
		JogoLemmings ce = new JogoLemmings();
		ce.setVisible( true );
		ce.comecarJogo();
	}

}

