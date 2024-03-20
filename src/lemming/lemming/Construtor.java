package lemming.lemming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Construtor extends Habilidade {
	
	private int lastAnim = 0;
	private int degraus = 0;	
	private static final int ALTURA_DEGRAU = 4;
	private static final int COMPRIMENTO_DEGRAU = 15;
	@Override
	
	void fazerHabilidade(LemmingBase lemming) {
		Andarilho a = new Andarilho();
		
		if( lemming.getImage().numCiclosFeitos() <= lastAnim )
			return;

		lastAnim = lemming.getImage().numCiclosFeitos();
		Point pos = lemming.getPosicaoCentro();

		int direcao = lemming.getDirecao();

		if( degraus >= 12 )
			lemming.setHabilidade( a  );
		else {
			Graphics2D g = lemming.getMundo().getMapa().getGraphics();
			g.setColor( Color.white );
			if( direcao > 0 )
				g.fillRect( pos.x+Lemming.VELOCIDADE*direcao, pos.y+Lemming.MEIA_ALTURA-ALTURA_DEGRAU, COMPRIMENTO_DEGRAU, ALTURA_DEGRAU );
			else
				g.fillRect( pos.x+Lemming.VELOCIDADE*direcao-COMPRIMENTO_DEGRAU, pos.y+Lemming.MEIA_ALTURA-ALTURA_DEGRAU, COMPRIMENTO_DEGRAU, ALTURA_DEGRAU );
			pos.x += Lemming.VELOCIDADE*4*direcao;
			pos.y -= ALTURA_DEGRAU;
			lemming.setPosicaoCentro(pos);
			degraus++;
		}
	}
	
	@Override
	void baterParede(LemmingBase lemming) {
		// TODO Auto-generated method stub
		Andarilho a = new Andarilho();
		super.setHabilidade(lemming, a);
		super.baterParede(lemming);
	}
	
	@Override
	void comecarCair(LemmingBase lemming) {
		// TODO Auto-generated method stub
		Andarilho a = new Andarilho();
		super.setHabilidade(lemming, a);
		super.comecarCair(lemming);
	}
	@Override
	boolean podeAtivarNovaHabilidade() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		lastAnim  = 0;
		degraus = 0;
		lemming.setVisualAtual( lemming.getDirecao() > 0? "construir_dir": "construir_esq");
		lemming.getImage().reset();
		return super.setHabilidade(lemming,habilidade);
	}
	
	public Construtor clone() {
        Construtor c = new Construtor();
        c.degraus = this.degraus;
        c.lastAnim = this.lastAnim;

        return c;
    }
	
}
