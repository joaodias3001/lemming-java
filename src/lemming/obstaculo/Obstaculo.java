package lemming.obstaculo;

import java.awt.Rectangle;

import lemming.lemming.Lemming;
import lemming.mundo.Mundo;
import prof.jogos2D.elemento.ElementoGrafico;

/** interface que define um obstáculo. Um obstáculo é um elemento
 * do mundo que efetua alguma ação quando um lemming entra na sua zona de ação
 * Um obstáculo também é um elemento gráfico pelo
 * que terá de implementar esta interface também
 */
public interface Obstaculo extends ElementoGrafico {
	
	/** efetua uma ação sobre o lemming que está na sua zona de ação
	 * @param l o lemming que está na zona de ação
	 */
	void processaLemming( Lemming l );
	
	/** retorna a zona de ação
	 * @return a zona de ação
	 */
	Rectangle getZonaAcao();
	
	/** define o mundo onde o obstáculo opera
	 * @param mundo o mundo onde o obstáculo opera
	 */
	public void setMundo(Mundo mundo);
	
	/** retorna o mundo onde está inserido
	 * @return o mundo onde está inserido
	 */
	public Mundo getMundo();
	
}
