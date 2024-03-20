package lemming.lemming;

import java.awt.Point;

import lemming.mundo.MapaTerrenoImagem;

public class Demolidor extends Habilidade {

	private static int DISTANCIA_DEMOLIR = 2;
	@Override
	void fazerHabilidade(LemmingBase lemming) {
		Andarilho a = new Andarilho();

		Point pos = lemming.getPosicaoCentro();
		MapaTerrenoImagem mapa = lemming.getMundo().getMapa(); 
		pos.x += Lemming.VELOCIDADE*lemming.getDirecao()/2;
		int limparX = pos.x + (lemming.getDirecao() > 0? Lemming.MEIA_LARGURA: -Lemming.MEIA_ALTURA-DISTANCIA_DEMOLIR);
		mapa.limpar( limparX, pos.y-Lemming.MEIA_ALTURA, DISTANCIA_DEMOLIR, Lemming.ALTURA);
		mapa.limparCirculo( pos.x+lemming.getDirecao()*Lemming.MEIA_LARGURA, pos.y, Lemming.ALTURA/2);			
		lemming.setPosicaoCentro(pos);

		int limpoX = pos.x + (lemming.getDirecao() > 0? Lemming.MEIA_LARGURA: -Lemming.MEIA_ALTURA-5*DISTANCIA_DEMOLIR);
		if( mapa.estaLivre( limpoX, pos.y-Lemming.MEIA_ALTURA, 5*DISTANCIA_DEMOLIR, Lemming.ALTURA) )
		super.setHabilidade(lemming, a);
	}
	 @Override
	void baterParede(LemmingBase lemming) {
		 Point pos = lemming.getPosicaoCentro();
			pos.x += Lemming.VELOCIDADE*lemming.getDirecao()/2;
			lemming.setPosicaoCentro(pos);
	}
	 
	 @Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		 if( lemming.estaCair() )
				return false;
			lemming.setVisualAtual( lemming.getDirecao() > 0? "demolir_dir": "demolir_esq");
		return super.setHabilidade(lemming, habilidade);
	}
	 
	 public Demolidor clone() {
	        Demolidor d = new Demolidor();
	        d.DISTANCIA_DEMOLIR=this.DISTANCIA_DEMOLIR;
	        return d;
	    }
}
