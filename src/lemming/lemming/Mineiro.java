package lemming.lemming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import lemming.mundo.MapaTerrenoImagem;

public class Mineiro extends Habilidade {
	
	private int raio = (int)(Math.sqrt( Lemming.MEIA_ALTURA*Lemming.MEIA_ALTURA + Lemming.MEIA_LARGURA*Lemming.MEIA_LARGURA) + 3);
	private int lastAnim = 0;
	@Override
	void fazerHabilidade(LemmingBase lemming) {
		if( lemming.getImage().numCiclosFeitos() <= lastAnim )
			return;

		lastAnim = lemming.getImage().numCiclosFeitos();
		Point pos = lemming.getPosicaoCentro();
		MapaTerrenoImagem mapa = lemming.getMundo().getMapa();

		Graphics2D g = lemming.getMundo().getMapa().getGraphics();
		pos.x += Lemming.VELOCIDADE*lemming.getDirecao();
		pos.y += 3;
		g.setColor( Color.white );
		for( int i=0; i < 3; i++ ) {
			lemming.setPosicaoCentro(pos);
			pos.x += Lemming.VELOCIDADE*lemming.getDirecao();
			mapa.limparCirculo( pos.x, pos.y, raio );
		}
	}

	@Override
	void comecarCair(LemmingBase lemming) {
		// TODO Auto-generated method stub
		Andarilho a = new Andarilho();
		lemming.setHabilidade( a );
		super.comecarCair(lemming);
	}


	@Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		// TODO Auto-generated method stub
		if( lemming.estaCair() )
			return false;
		lastAnim = 0;
		lemming.setVisualAtual( lemming.getDirecao() > 0? "minerar_dir": "minerar_esq");
		lemming.getImage().reset();
	
		return super.setHabilidade(lemming, habilidade);
	}
	
	public Mineiro clone() {
        Mineiro m = new Mineiro();
        m.lastAnim = this.lastAnim;
        m.raio = this.raio;
        return m;
    }
	
}
