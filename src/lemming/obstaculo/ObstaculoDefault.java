package lemming.obstaculo;

import java.awt.Rectangle;

import lemming.mundo.Mundo;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteVisual;

public abstract class ObstaculoDefault extends ElementoGraficoDefault implements Obstaculo {

	private Mundo mundo;
	private Rectangle zonaAcao;
	
	/** cria o obstáculo
	 * @param image a imagem que representa o obstáculo
	 * @param zonaAcao a zona de ação do obstáculo
	 */
	public ObstaculoDefault(ComponenteVisual image, Rectangle zonaAcao ) {
		super(image);
		this.zonaAcao = zonaAcao;
	}
	
	@Override
	public void atualizar() {
		// verifica se algum lemming está na zona de ação do obstáculo e processa-o
		getMundo().getLemmings().forEach( l -> { if( l.getEspacoOcupado().intersects(zonaAcao) ) processaLemming( l );} );
	}
	
	@Override
	public Rectangle getZonaAcao() {
		return zonaAcao;
	}

	@Override
	public void setMundo(Mundo mundo) {
		this.mundo = mundo;
	}
	
	@Override
	public Mundo getMundo() {
		return mundo;
	}
	
}
