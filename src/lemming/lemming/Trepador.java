package lemming.lemming;

import java.awt.Point;

import lemming.mundo.MapaTerrenoImagem;

public class Trepador extends Habilidade {

	private boolean comecouSubir = false;
	private boolean subir;

	@Override
	void fazerHabilidade(LemmingBase lemming) {
		if( !subir)  {
			if( comecouSubir ) {
				comecouSubir = false;
				lemming.setVisualAtual( lemming.getDirecao() > 0? "mover_dir": "mover_esq");
			}
			lemming.mover();
		}
		else {
			Point pos = lemming.getPosicaoCentro();
			pos.y -= Lemming.VELOCIDADE/2;
			MapaTerrenoImagem mapa = lemming.getMundo().getMapa(); 
			boolean temEspacoEmCima = mapa.estaLivre( pos.x-Lemming.MEIA_LARGURA+2*Lemming.VELOCIDADE, pos.y-Lemming.MEIA_ALTURA, Lemming.LARGURA-Lemming.VELOCIDADE*4, Lemming.ALTURA);
			if( temEspacoEmCima ){
				lemming.setPosicaoCentro( pos );
			} else {
				comecouSubir = false;
				lemming.bateuParedeNormal();
				super.comecarCair(lemming);
			}
			subir = false;	
		}
	}

	void baterParede(LemmingBase lemming) {
		if( lemming.estaCair() ) {
			lemming.cair();
			return;
		}
		subir = true;
		if( !comecouSubir ) {
			comecouSubir = true;
			lemming.setVisualAtual( lemming.getDirecao() > 0? "trepar_dir": "trepar_esq");
		}	
	}
	
	public Trepador clone() {
        Trepador t = new Trepador();
        t.comecouSubir = this.comecouSubir;
        t.subir = this.subir;
        return t;
    }
}
