package lemming.obstaculo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import lemming.lemming.Lemming;
import prof.jogos2D.image.ComponenteVisual;

/** Um obstáculo limitador é um obstáculo invisível que serve
 * para marcar os limites de um nível, para evitar que o lemming
 * saia do mapa do terreno. Quando o lemming toca nele, morre 
 */
public class ObstaculoLimitador extends ObstaculoDefault {

	/** cria o obstáculo limitador
	 * @param image a imagem que representa o obstáculo
	 * @param zonaAcao a zona de ação do obstáculo
	 */
	public ObstaculoLimitador(ComponenteVisual image, Rectangle zonaAcao) {
		super(image, zonaAcao);
	}

	@Override
	public void processaLemming(Lemming l) {
		l.morrer( "morrer" );
	}
	
//	Descomentar o método se quiserem ver a zona de ação do obstáculo
//	@Override
//	public void desenhar(Graphics2D g) {
//		super.desenhar(g);
//		g.setColor( Color.ORANGE );
//		Rectangle r =  getZonaAcao();
//		g.drawRect( r.x,  r.y, r.width, r.height);
//
//	} 
}
