package lemming.lemming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import lemming.mundo.MapaTerrenoImagem;

public class Bloqueador extends Habilidade {
	
	private static Color corMascara = new Color( 125, 125, 125, 255 );
	private Rectangle bloqueio;

	void fazerHabilidade(LemmingBase lemming) {
		Graphics2D g = lemming.getMundo().getMapa().getGraphics();
		Rectangle r = lemming.getBounds();
		g.setColor( corMascara );
		g.fillRect( bloqueio.x, bloqueio.y, bloqueio.width, bloqueio.height );
	}
	
	@Override
	void baterParede(LemmingBase lemming) {
	
	}
	
	@Override
	void comecarCair(LemmingBase lemming) {
		Andarilho a = new Andarilho();
		MapaTerrenoImagem m = lemming.getMundo().getMapa();
		m.limpar( bloqueio.x, bloqueio.y, bloqueio.width, bloqueio.height );
		lemming.setHabilidade( a );
		super.comecarCair(lemming);
	}
	
	@Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		if( lemming.estaCair() )
			return false;
		lemming.setVisualAtual("bloquear");
		Graphics2D g = lemming.getMundo().getMapa().getGraphics();
		g.setColor( corMascara );
		Point p = lemming.getPosicaoCentro();
		bloqueio = new Rectangle(p.x-Lemming.MEIA_LARGURA/2, p.y-Lemming.MEIA_ALTURA/2, Lemming.LARGURA/2, Lemming.ALTURA/2 );
		g.fillRect( bloqueio.x, bloqueio.y, bloqueio.width, bloqueio.height );
		return super.setHabilidade(lemming, habilidade);
	}
	
	@Override
	boolean podeAtivarNovaHabilidade() {
		return false;
	}
	
	public Bloqueador clone() {
        Bloqueador b = new Bloqueador();
        b.bloqueio = this.bloqueio;
        b.corMascara = this.corMascara;
        return b;
    }
	
}

