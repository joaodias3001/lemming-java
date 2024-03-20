package lemming.lemming;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lemming.efeito.Efeito;
import lemming.efeito.EfeitoExplosao;
import lemming.efeito.EfeitoParalisante;
import lemming.jogo.JogoLemmings;
import lemming.mundo.MapaTerrenoImagem;
import lemming.mundo.Mundo;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteMultiVisual;

/** Cria uma implementação base de um lemming.
 * A ideia era poder ter vários tipos de lemming, mas não se revelou uma boa ideia
 * porque o lemming é sempre o mesmo, só varia a habilidade
 */
public class LemmingBase extends ElementoGraficoDefault implements Lemming {

	private Mundo mundo;   // o mundo onde está inserido
	private int direcao = 1, direcaoAntiga; // a direção do movimento
	// 1 para andar para a direita, -1 para a esquerda, 0 parado

	private boolean ativo;  // se está ativo 
	private boolean cair;   // se está a cair
	private int topoQueda;  // a posição de onde começou a cair 

	// TODO FEITO cuidado com isto, provavelmente leva a switchs

	private Andarilho andarilho = new Andarilho();
	//	private Flutuador flutuador = new Flutuador();
	//	private Trepador trepador = new Trepador();
	//	private Construtor construtor = new Construtor();
	//	private Mineiro mineiro = new Mineiro();
	//	private Bloqueador bloqueador = new Bloqueador();
	//	private Escavador escavador = new Escavador();
	//	private Demolidor demolidor = new Demolidor();

	private Habilidade habilidadeAtiva; // a habilidade ativa

	//	// TODO FEITO variáveis e constantes exclusivas para a parte do trepador (atulha a parte normal do lemming)
	//	private boolean comecouSubir = false;
	//	private boolean subir;
	//
	//	// TODO FEITO variáveis e constantes exclusivas para a parte do mineiro (atulha a parte normal do lemming)
	//	private int raio = (int)(Math.sqrt( Lemming.MEIA_ALTURA*Lemming.MEIA_ALTURA + Lemming.MEIA_LARGURA*Lemming.MEIA_LARGURA) + 3);
	//
	//	// TODO FEITO variáveis e constantes exclusivas para a parte do bloqueador (atulha a parte normal do lemming)
	//	private static Color corMascara = new Color( 125, 125, 125, 255 );
	//	private Rectangle bloqueio;
	//	
	//	// TODO FEITO variáveis e constantes exclusivas para a parte do construtor (atulha a parte normal do lemming)
	//	private int degraus = 0;	
	//	private static final int ALTURA_DEGRAU = 4;
	//	private static final int COMPRIMENTO_DEGRAU = 15;
	//
	//	// TODO FEITO constante exclusiva para a parte do demolidor (atulha a parte normal do lemming)
	//	private static int DISTANCIA_DEMOLIR = 2;
	//	
	//	// TODO FEITO variável comum ao mineiro e so construtor (atulha a parte normal do lemming)
	//	private int lastAnim = 0;

	// TODO FEITO deve ser refeita esta parte, não deve depender do jogo
	//private JogoLemmings jogo; // o jogo 
	private List<LemmingObserver> jogo ;

	public void addObserver(LemmingObserver observer) {
		jogo.add(observer);
	}

	public void removeObserver(LemmingObserver observer) {
		jogo.remove(observer);
	}

	// TODO FEITO esta parte do efeito bomba deve ser refeita, para dar a qualquer outro efeito
	//      estas variáveis tem de ssair daqui, pois são da bomba
	//	private boolean temBomba = false;
	//	private int countDown;
	//	private int ciclos, totalCiclos;
	private Efeito efeito;


	/** Cria um lemming
	 * @param imagem conjunto de animações para os vários visuais
	 * @param direcao direção inicial do movimento
	 */
	public LemmingBase(ComponenteMultiVisual imagem, int direcao) {
		super( imagem );
		setHabilidade( andarilho );
		this.direcao = direcao;
		direcaoAntiga = direcao;
		setVisualAtual( direcao > 0? "mover_dir": "mover_esq");
		ativo = true;
		this.jogo = new ArrayList<>();
	}

	@Override
	public void atualizar() {
		// TODO FEITO isto tem de desaparecer, pois só serve para bombas
		if (efeito != null) {
			efeito.aplicarEfeito(this);
			return;
		}
		//		if( temBomba ) {
		//			ciclos--;
		//			if( ciclos == 0 ){
		//				// chegou ao fim do tempo, ativar a animação explosiva
		//				setVisualAtual("explodir");
		//				// e não faz mais nada como lemming
		//				return;
		//			}
		//			// se está no ciclo de explodir não faz nada como lemming
		//			else if( ciclos < 0 ) {
		//				// se acabou a imgaem de explodir, é preciso morrer
		//				if( getImage().numCiclosFeitos() >= 1 ) {
		//					Point p = getPosicaoCentro();
		//					getMundo().getMapa().limparCirculo( p.x, p.y, ALTURA + 3 );
		//					morrer( "explosao" );
		//				}
		//				return;
		//			}
		//		}


		// primeiro faz a sua habilidade
		fazerHabilidade( );

		// ver onde está  a altura a que está 
		Point pos = getPosicaoCentro();
		int chaoAntes = pos.y + MEIA_ALTURA;

		// verificar se bateu numa parede e processar
		if( bateuParede() ) {
			baterParede();
		}
		else {
			// verificar o chão, pois pode estar
			// num buraco ou a subir um degrau
			int chao = verChao();
			//System.out.print( pos.x+","+chaoAntes + " -> " + pos.x+","+ chao );
			// ver se está num degrau
			if( Math.abs(chao - chaoAntes) < MAX_ALTURA_DEGRAU )		
				definirChao(pos, chao);
			// senão é porque está a cair
			else if( chao > chaoAntes ) {
				cair();
			}
			//System.out.println( " : " + (getPosicaoCentro().x + MEIA_ALTURA) +"," + (getPosicaoCentro().y + MEIA_ALTURA) );
		}
	}

	/** faz a habilidade
	 */
	private void fazerHabilidade() {
		habilidadeAtiva.fazerHabilidade(this);
	}

	/** faz o lemming andar
	 */
	protected void mover() {
		if( getDirecao() != 0 )
			setDirecaoAntiga( getDirecao() );

		if( getDirecao() == 0 || estaCair() )
			return;

		Point pos = getPosicaoCentro();
		pos.x += getDirecao()*Lemming.VELOCIDADE;
		setPosicaoCentro( pos );
	}

	/** procedimento quando bate numa parede
	 */
	private void baterParede() {
		habilidadeAtiva.baterParede(this);
	}

	protected void bateuParedeNormal() {
		setDirecao( -getDirecao() );
		setVisualAtual( getDirecao() > 0? "mover_dir": "mover_esq");
		mover();
	}


	/** procedimento a fazer quando começa a cair
	 */
	private void comecarCair() {
		cair = true;
		direcao = 0;
		habilidadeAtiva.comecarCair(this);
	}

	protected void comecarCairNormal() {
		Point centro = getPosicaoCentro();
		setTopoQueda( centro.y );
		setVisualAtual( "cair" );
		setDirecao( 0 );
		setCair( true );
		setPosicaoCentro( centro );
	}

	/** procedimento de quando está a cair
	 */
	protected void cair() {
		if( !estaCair() ) {
			comecarCair();			
		}
		else {
			if( habilidadeAtiva instanceof Flutuador ){
				Point pos = getPosicaoCentro();
				pos.y += (int)(Lemming.VELOCIDADE/2);
				setPosicaoCentro( pos );
			}
			else
				cairNormal();
		}
	}

	protected void cairNormal() {
		Point pos = getPosicaoCentro();
		pos.y += Lemming.VELOCIDADE;
		setPosicaoCentro( pos );
	}


	/** procedimento a fazer quando aterra após uma queda
	 */
	private void aterrar() {
		cair = false;
		direcao = direcaoAntiga;
		if( habilidadeAtiva instanceof Flutuador ) {
			setVisualAtual( getDirecao() > 0? "mover_dir": "mover_esq");
		}
		else
			aterrarNormal();
	}

	protected void aterrarNormal() {
		setCair( false );
		Point pos = getPosicaoCentro();
		int queda = pos.y - getTopoQueda();
		if( queda < Lemming.MAX_ALTURA_QUEDA )
			setVisualAtual( getDirecao() > 0? "mover_dir": "mover_esq");
		else {
			morrer( "morrer" );
		}
	}

	/** garante que o lemming está sempre num chão (ou aterrou)
	 * @param pos a posição horizontal do lemming 
	 * @param chao o chão onde colocar o lemming
	 */
	private void definirChao(Point pos, int chao) {
		setPosicaoCentro( new Point(pos.x, chao-MEIA_ALTURA) );
		if( cair ) {
			cair = false;
			aterrar();
		}
	}

	/** indica se está a cair
	 * @return true se está a cair
	 */
	protected boolean estaCair() {
		return cair;
	}

	/** define que está/não está a cair
	 * @param cair true para cair, false para deixar de cair
	 */
	protected void setCair(boolean cair) {
		this.cair = cair;
	}

	/** define a direção do movimento
	 * @param direcao 1 para andar para a direita, -1 para a esquerda, 0 parado
	 */
	public void setDirecao(int direcao) {
		this.direcao = direcao;
	}

	/** retorna a direção do movimento
	 * @return a direção do movimento, 1 para a direita, -1 para a esquerda, 0 parado
	 */
	public int getDirecao() {
		return direcao;
	}

	/** retorna a última direção usada
	 * @return a última direção usada
	 */
	protected int getDirecaoAntiga() {
		return direcaoAntiga;
	}

	/** memoriza a direção antiga
	 * @param dir a direção a memorizar
	 */
	protected void setDirecaoAntiga(int dir) {
		direcaoAntiga = dir;		
	}

	/** define a altura de onde começou a cair
	 * @param topoQueda a altura de onde começou a cair
	 */
	protected void setTopoQueda(int topoQueda) {
		this.topoQueda = topoQueda;
	}

	/** retorna a altura de onde começou a cair
	 * @return a altura de onde começou a cair
	 */
	protected int getTopoQueda() {
		return topoQueda;
	}


	/** testa se bateu numa parede (uma parede pode ser
	 *  um degrau mais alto do que o lemming consegu subir) 
	 * @return true se bateu numa parede
	 */
	private boolean bateuParede() {
		Point pos = getPosicaoCentro();
		int x = pos.x + direcao*(MEIA_LARGURA-VELOCIDADE);
		boolean temParede = getMundo().getMapa().temParede( x, pos.y-MEIA_ALTURA, VELOCIDADE, ALTURA, MAX_ALTURA_DEGRAU ); 
		return temParede;
	}

	/** vai ver qual o chão mais próximo 
	 * @return a altura do chão mais próximo
	 */
	private int verChao() {
		Point pos = getPosicaoCentro();
		return getMundo().getMapa().chaoMaisProximo(pos.x-MEIA_LARGURA+VELOCIDADE, pos.y+MEIA_ALTURA, LARGURA-2*VELOCIDADE, ALTURA);
	}


	@Override
	public void desenhar(Graphics2D g) {
		super.desenhar(g);
		// descomentar as linhas para ver a área ativa do lemming
		// g.setColor( Color.red );
		// g.drawRect( getPosicaoCentro().x-MEIA_LARGURA,  getPosicaoCentro().y-MEIA_ALTURA, LARGURA, ALTURA);

		if (efeito != null) {
			efeito.desenharEfeito(g, this);
		}

		//		if( temBomba ) {
		//			if( ciclos < 0 )
		//				 return;
		//
		//			Rectangle r = getBounds();
		//			g.setColor( Color.WHITE );
		//			g.drawString("" + (1+(ciclos * countDown / totalCiclos)), r.x+r.width/2, r.y-10);
		//		}
	}

	public void sair(String efeitoSaida) {
		setAtivo(false);
		getMundo().addEfeito(getPosicaoCentro(), efeitoSaida);

		for (LemmingObserver observer : jogo) {
			observer.lemmingSaiu(this);
		}
	}

	public void morrer(String efeitoMorte) {
		setAtivo(false);
		getMundo().addEfeito(getPosicaoCentro(), efeitoMorte);

		for (LemmingObserver observer : jogo) {
			observer.lemmingMorreu(this);
		}
	}

	@Override
	public boolean estaAtivo() {
		return ativo;
	}

	@Override
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public boolean setHabilidade(Habilidade habilidade) {
		this.habilidadeAtiva = habilidade;
		return habilidade.setHabilidade(this, habilidade);
	}
	@Override
	public Habilidade getHabilidade() {
		return habilidadeAtiva;
	}

	@Override
	public boolean podeAtivarNovaHabilidade() {
		return habilidadeAtiva.podeAtivarNovaHabilidade();
	}

	@Override
	public Rectangle getEspacoOcupado() {
		Point p = getPosicaoCentro();
		return new Rectangle( p.x - Lemming.MEIA_LARGURA, p.y - Lemming.MEIA_ALTURA, Lemming.LARGURA, Lemming.ALTURA );
	}

	@Override
	public void setMundo(Mundo mundo) {
		this.mundo = mundo;
	}

	@Override
	public Mundo getMundo() {
		return mundo;
	}

	@Override
	public void setVisualAtual(String visual ) {
		Point centro = getPosicaoCentro();
		((ComponenteMultiVisual)getImage()).setVisualAtual(visual);
		setPosicaoCentro(centro);
	}

	/** define o jogo 
	 * @param jogo o jogo dos lemmings
	 */
	//TODO FEITO esta solução é fraca pois obriga o lemming a conhecer a classe JogoLemmmings
	//e fica demasiado ligada a ela, não podendo ser usada noutros jogos

	//	public void setJogo(JogoLemmings jogo) {
	//		this.jogo = jogo;
	//	}
	public void setJogo(LemmingObserver observer) {
		addObserver(observer);
	}

	public void setEfeito(Efeito efeito) {
		this.efeito = efeito;
	}

	@Override
	public void detonar( int ciclos, int countDown ) {
		//		this.ciclos = ciclos;
		//		totalCiclos = ciclos;
		//		this.countDown = countDown;	
		//		temBomba = true;

		EfeitoExplosao explosao = new EfeitoExplosao(ciclos, countDown);
		setEfeito(explosao);
	}

	//	public void paralisar(int duracao) {
	//		EfeitoParalisante paralisar = new EfeitoParalisante(duracao);
	//        setEfeito(paralisar);
	//	}

}
