package lemming.mundo;

import java.awt.Point;

import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteVisual;

/** Representa um efeito especial. Um efeito especial
 * é uma animação que aparece num local do mundo e é
 * eliminada assim que termina
 * Bom para explosões, mortes dramáticas, etc 
 */
public class EfeitoEspecial extends ElementoGraficoDefault {

	private boolean ativo = true;
	
	/** cria um efeito especial
	 * @param p ponto central ond colocar o efeito 
	 * @param image a imagem do efeito
	 */
	public EfeitoEspecial(Point p, ComponenteVisual image) {
		super(image);
		image.setPosicaoCentro( p );
	}

	@Override
	public void atualizar() {
		if( getImage().numCiclosFeitos() > 0 )
			setAtivo( false );
	}
	
	/** indica se está ativo
	 * @return true se stá ativo
	 */
	public boolean estaAtivo() {
		return ativo;
	}
	
	/** define se está ativo
	 * @param ativo true para ativar, false para desativar
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
