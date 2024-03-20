package lemming.obstaculo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import lemming.lemming.Lemming;
import prof.jogos2D.image.ComponenteVisual;

/** Representa a saída do nível. Quando um lemming entra 
 * na sua zona de ação é dado como salvo e removido do jogo
 */
public class Saida extends ObstaculoDefault {

	/**Cria a saída
	 * @param image a imagem que representa a saída
	 * @param zonaAcao a porta da saída
	 */
	public Saida(ComponenteVisual image, Rectangle zonaAcao) {
		super(image, zonaAcao);
	}
	
//	Descomentar o método se quiserem ver a zona de ação do obstáculo
//	@Override
//	public void desenhar(Graphics2D g) {		
//		super.desenhar(g);
//		g.setColor( Color.cyan );
//		Rectangle r =  getZonaAcao();
//		g.drawRect( r.x,  r.y, r.width, r.height);
//	}

	@Override
	public void processaLemming(Lemming l) {
		Rectangle r =  getZonaAcao();
		l.setPosicaoCentro( new Point( r.x+r.width/2, r.y + r.height/2) );
		l.sair( "sair" );
	}
}
