package lemming.lemming;

import java.awt.Graphics2D;

import java.awt.Rectangle;

import lemming.efeito.Efeito;
import lemming.mundo.Mundo;
import prof.jogos2D.elemento.ElementoGrafico;

/** Interface que representa um Lemming.
 * Reparar que o Lemming é um elemento gráfico, logo tem de
 * implementar também esta interface
 */
public interface Lemming extends ElementoGrafico, Cloneable {

	// constantes das características dos lemmings 
	int LARGURA = 25, ALTURA = 46;
	int MEIA_LARGURA = LARGURA/2, MEIA_ALTURA = ALTURA / 2;
	int MAX_ALTURA_DEGRAU = 16;
	int MAX_ALTURA_QUEDA = 250;
	int VELOCIDADE = 3;

	// identificação das habilidades
	// TODO FEITO ESTAS CONSTANTES TÊM DE DESAPARECER! TODAS!
	//      NEM UMA DEVE FICAR! ISTO É MESMO A SÉRIO!!!
	//      NEM PENSAR EM SUBSTITUIR POR OUTRAS, OK?
	//	int ANDARILHO = 0; 
	//	int ESCAVADOR = 1;
	//	int FLUTUADOR = 2;
	//	int MINEIRO = 4;
	//	int TREPADOR = 5;
	//	int BLOQUEADOR = 6;
	//	int DEMOLIDOR = 7;
	//	int CONSTRUTOR = 8;
	//	int BOMBA = 9;
	
		
	/** indica ao lemming que vai sair pela porta
	 * @param efeitoSaida nome do efeito a aplicar à saída
	 */
	void sair( String efeitoSaida );

	/** indica ao lemming que morreu
	 * @param efeitoMorte nome do efeito da morte
	 */
	void morrer( String efeitoMorte );

	/** indica se o lemming pode ativar uma nova habilidade
	 * @return true se pode ativar
	 */
	boolean podeAtivarNovaHabilidade();

	/** Ativa uma nova habilidade no lemming
	 * @param habilidade a habilidade a ativar
	 * @return true se conseguir ativar
	 */
	boolean setHabilidade(Habilidade habilidade);

	/** retorna o espaço ocupado pelo lemming. Não confundir
	 * com o getBounds que retorna o espaço ocupado pela imagem
	 * do lemming o espaço ocupado pelo lemming
	 * @return
	 */
	Rectangle getEspacoOcupado();

	/** indica se o lemming está ativo, isto é, ainda 
	 * faz ações no mundo. Um lemming inativo deve ser removido
	 * @return true se estiver atico
	 */
	boolean estaAtivo();

	/** define se o lemming está ativo
	 * @param a ativar/desativar o lemming
	 */
	void setAtivo( boolean a );

	/** define o mundo onde o lemming se movimentas
	 * @param mundo o mundo onde vai ser colocado o lemming
	 */
	void setMundo(Mundo mundo);

	/** retorna o mundo onde o lemming está colocado
	 * @return o mundo onde o lemming está colocado
	 */
	Mundo getMundo();

	/** Retorna a habilidade que está ativada neste momento
	 * @return a habilidade que está ativada neste momento
	 */
	Habilidade getHabilidade();

	/** define qual o visual a usar
	 * @param visual nome do visual a usar
	 */
	void setVisualAtual(String visual);

	/** método que detona o lemming
	 * TODO FEITO a técnica tem de ser outra, pois assim só funciona para este efeito
	 * @param ciclos quantos ciclos leva a detonar
	 * @param countDown o contador de "tempo"
	 */

	void setEfeito(Efeito efeito);

	void detonar(int ciclos, int countDown);
	//void paralisar(int duracao);


}