package lemming.obstaculo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import lemming.lemming.Lemming;
import prof.jogos2D.image.ComponenteVisual;

/** Representa um obstáculo que mata o lemming sempre que
 * este entra na sua zona de ação. O efeito de morte pode
 * ser configurado. assim podemos colocar uma animação
 * personalizada para cada obstáculo
 */
public class ObstaculoMatador extends ObstaculoDefault {

	// qual o efeito a aplicar quando o lemming morre
	private String efeitoMorte;
	
	/** Cria o matador
	 * @param image a imagem que representa o obstáculo
	 * @param zonaAcao a zona de ação do obstáculo
	 * @param efeitoMorte nome do efeito a usar para representar a morte do lemming
	 */
	public ObstaculoMatador(ComponenteVisual image, Rectangle zonaAcao, String efeitoMorte ) {
		super(image, zonaAcao);
		this.efeitoMorte = efeitoMorte;
	}

	@Override
	public void processaLemming(Lemming l) {
		l.morrer( efeitoMorte );
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
