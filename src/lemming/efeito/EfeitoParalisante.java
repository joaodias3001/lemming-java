package lemming.efeito;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;

import lemming.lemming.Lemming;
import lemming.lemming.LemmingBase;

public class EfeitoParalisante implements Efeito {
	private static Color corMascara = new Color( 125, 125, 125, 255 );
	private Rectangle bloqueio;
	
	private int duracao;

	    public EfeitoParalisante(int duracao) {
	        this.duracao = duracao;
	    }

	@Override
	public void aplicarEfeito(LemmingBase lemming) {
		duracao--;
		if(duracao > 0) {
		lemming.setVisualAtual("bloquear");
		Graphics2D g = lemming.getMundo().getMapa().getGraphics();
		g.setColor( corMascara );
		Point p = lemming.getPosicaoCentro();
		bloqueio = new Rectangle(p.x-Lemming.MEIA_LARGURA/2, p.y-Lemming.MEIA_ALTURA/2, Lemming.LARGURA/2, Lemming.ALTURA/2 );
		g.fillRect( bloqueio.x, bloqueio.y, bloqueio.width, bloqueio.height );
		} 
		else {
			
		}
		
	}

	@Override
	public void desenharEfeito(Graphics2D g, LemmingBase lemming) {
	
	}

}
