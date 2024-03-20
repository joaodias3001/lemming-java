package lemming.obstaculo;

import java.awt.Rectangle;

import lemming.efeito.EfeitoParalisante;
import lemming.lemming.Lemming;
import prof.jogos2D.image.ComponenteVisual;

public class ObstaculoParalisante extends ObstaculoDefault implements Obstaculo {
	
	private int duracaoParalisia;

	public ObstaculoParalisante(ComponenteVisual image, Rectangle zonaAcao, int duracaoParalisia) {
		super(image, zonaAcao);
		this.duracaoParalisia = duracaoParalisia;
	}

	@Override
	public void processaLemming(Lemming l) {
		EfeitoParalisante efeitoParalisia = new EfeitoParalisante(duracaoParalisia);
        l.setEfeito(efeitoParalisia);
	}

}
