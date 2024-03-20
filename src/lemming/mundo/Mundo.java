package lemming.mundo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lemming.lemming.Lemming;
import lemming.obstaculo.Obstaculo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/* represnta o mundo, onde se passa a ação do jogo
 */
public class Mundo {
	private ComponenteVisual fundo;   // imagem de fundo do nível
	private ComponenteVisual terreno; // imagem do terreno do nível
	private MapaTerrenoImagem mapa;   // mapa do terreno

	private ArrayList<Lemming> lemmings = new ArrayList<Lemming>();
	private ArrayList<Obstaculo> obstaculos = new ArrayList<Obstaculo>();
	private ArrayList<EfeitoEspecial> efeitos = new ArrayList<EfeitoEspecial>();

	// os efeitos registados no mundo
	private Map<String,ComponenteVisual> mapaEfeitos = new HashMap<>(); 
	
	/** construtor do mundo
	 * @param fundo a imagem de fundo
	 * @param terreno a imagm do terreno
	 * @param mapa o mapa do terreno
	 * @throws IOException se algum dos efeitos base não for carregado
	 */
	public Mundo( ComponenteVisual fundo, ComponenteSimples terreno, MapaTerrenoImagem mapa ) throws IOException{
		this.fundo = fundo;
		this.mapa = mapa;
		this.terreno = terreno;
		
		// carregar os "efeitos especiais" base
		mapaEfeitos.put("morrer", ComponenteAnimado.fromDiretorio( "art/lemming/morrer", 1 ) );
		mapaEfeitos.put("sair", ComponenteAnimado.fromDiretorio( "art/lemming/sair", 1 ) );
		mapaEfeitos.put("explosao", new ComponenteAnimado( new Point(-100,-100), "art/efeitos/explosao.gif", 17, 3 ) );
	}
	
	/** vai desenhar todo os elementos do mundo
	 * @param g onde vai desenhar
	 */
	public synchronized void desenhar( Graphics2D g ){
		fundo.desenhar( g );

		for( Obstaculo o : obstaculos )
			o.desenhar( g );

		terreno.desenhar( g );
		
		for( Lemming l : lemmings )
			l.desenhar( g );
		
		for( EfeitoEspecial fx : efeitos )
			fx.desenhar(g);
	}
	
	/**
	 * Atualiza todos os elementos do mundo e remove
	 * os elementos que já não são necessários
	 * Cada chamada a este método conta como um ciclo de processamento.
	 */
	public synchronized void atualiza(){
		for( Lemming l : lemmings ) {
			l.atualizar();
		}
		
		for( Obstaculo o : obstaculos ) {
			o.atualizar();
		}

		for( EfeitoEspecial fx : efeitos ) {
			fx.atualizar();
		}
		
		for( int i = lemmings.size()-1; i >= 0; i-- )
			if( !lemmings.get(i).estaAtivo() )
				lemmings.remove( i );
		
		for( int i = efeitos.size()-1; i >= 0; i-- )
			if( !efeitos.get(i).estaAtivo() )
				efeitos.remove( i );
	}
	
	/** adiciona um lemming ao mundo
	 * @param l lemming a adicionar
	 */
	public void addLemming( Lemming l ) {
		lemmings.add( l );
		l.setMundo( this );
	}
	
	/** remove um lemming ao mundo
	 * @param l lemming a remover
	 */
	public void removeLemming( Lemming l ) {
		lemmings.remove( l );
		l.setMundo( null );
	}

	
	/** retorna os lemmings 
	 * @return os lemmings
	 */
	public List<Lemming> getLemmings() {
		return Collections.unmodifiableList( lemmings );
	}
	
	/** retorna o lemming que está numa dada coordenada
	 * @param p a posição da verificar
	 * @return o lemming na posição ou null se não houver lemmings
	 */
	public Lemming getLemmingAt( Point p ) {
		for( Lemming l : lemmings )
			if( l.getEspacoOcupado().contains( p ) )
				return l;
		return null;
	}
	
	/** retorna o mapa do terreno
	 * @return o mapa do terreno
	 */
	public MapaTerrenoImagem getMapa() {
		return mapa;
	}

	/** adiciona um obstáculo ao mundo
	 * @param o obstáculo a adicionar
	 */
	public void addObstaculo(Obstaculo o) {
		obstaculos.add( o );
		o.setMundo( this );
	}
	
	/** remove um obstáculo do mundo
	 * @param o obstáculo a remover
	 */
	public void removeObstaculo(Obstaculo o) {
		obstaculos.remove( o );
	}
	
	/** retorna todo os obstáculos
	 * @return os obstáculos
	 */
	public List<Obstaculo> getObstaculos() {
		return Collections.unmodifiableList( obstaculos );
	}
	
	/** adiciona um efeito ao mundo
	 * @param p posição onde colocar
 	 * @param codigo identificador do efeito
	 */
	public void addEfeito( Point p, String codigo ) {
		ComponenteVisual fx = mapaEfeitos.get( codigo );
		if( fx == null )
			return;
		
		efeitos.add( new EfeitoEspecial( p, fx.clone() ) );
	}
	
	/** remove um efeito especial
	 * @param fx o efeito a remover
	 */
	public void removeEfeito(EfeitoEspecial fx ) {
		efeitos.remove( fx );
	}
	
	/** retorna os efeitos
	 * @return os efeitos
	 */
	public List<EfeitoEspecial> getEfeitos() {
		return Collections.unmodifiableList( efeitos );
	}

	/** regista um efeito no mundo para posterior uso
	 * @param nome nom plo qual vai ser idntificado
	 * @param img a imgam a usar para o feito
	 */
	public void registaEfeito(String nome, ComponenteVisual img) {
		mapaEfeitos.put( nome, img );		
	}
}
