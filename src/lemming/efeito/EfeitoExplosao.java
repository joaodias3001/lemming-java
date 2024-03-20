package lemming.efeito;

import java.awt.Point;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Color;

import lemming.lemming.LemmingBase;

public class EfeitoExplosao implements Efeito{
	private int ciclos, totalCiclos;
	private int countDown;
	
	
	public EfeitoExplosao(int ciclos, int countDown) {
        this.ciclos = ciclos;
        this.totalCiclos = ciclos;
        this.countDown = countDown;
    }
	
	public void aplicarEfeito(LemmingBase l) {
			ciclos--;
			if( ciclos == 0 ){
				// chegou ao fim do tempo, ativar a animação explosiva
				l.setVisualAtual("explodir");
				// e não faz mais nada como lemming
				return;
			}
			
			else if( ciclos < 0 ) {
				// se acabou a imagem de explodir, é preciso morrer
				if( l.getImage().numCiclosFeitos() >= 1 ) {
					Point p = l.getPosicaoCentro();
					l.getMundo().getMapa().limparCirculo( p.x, p.y, LemmingBase.ALTURA + 3 );
					l.morrer( "explosao" );
				}
				return;
			}
		}
	
	public void desenharEfeito(Graphics2D g, LemmingBase lemming) {
        if (ciclos < 0)
            return;

        Rectangle r = lemming.getBounds();
        g.setColor(Color.WHITE);
        g.drawString("" + (1 + (ciclos * countDown / totalCiclos)), r.x + r.width / 2, r.y - 10);
    }
		
	}


