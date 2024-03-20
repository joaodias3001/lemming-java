package lemming.lemming;

import java.awt.Point;

import lemming.mundo.MapaTerrenoImagem;

public class Escavador extends Habilidade {
	@Override
	void fazerHabilidade(LemmingBase lemming) {
		Point pos = lemming.getPosicaoCentro();
		MapaTerrenoImagem mapa = lemming.getMundo().getMapa(); 
		mapa.limparCirculo( pos.x, pos.y, Lemming.MEIA_ALTURA+3 );
	}
	
	@Override
	void comecarCair(LemmingBase lemming) {
		Andarilho a = new Andarilho();
		lemming.setHabilidade(a);
		super.comecarCair(lemming);
	}
	
	@Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		// TODO Auto-generated method stub
		if( lemming.estaCair() )
			return false;
		lemming.setVisualAtual( "escavar");
		return super.setHabilidade(lemming, habilidade);
	}
	
	public Escavador clone() {
        Escavador e = new Escavador();
        return e;
    }
	
	
	
}
