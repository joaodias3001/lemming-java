package lemming.jogo;

import lemming.lemming.Habilidade;
import lemming.lemming.Lemming;

/** Um fornecedor de habilidade tem a responsabilaidde
 * de ativar uma habilidade no lemming, se
 * ainda houver quantidade suficiente de habilidade
 */
public class FornecedorHabilidade {

	/** a quantidade de habildiades que pode mandar realziar */
	private int quantidade;
	/** a habilidade que vai ser ativada */
	private Habilidade habilidade;
	 
	/** cria um fornecedor de habiladide com uma dada quantidade
	 * e respetiva habilidade
	 * @param quantidade quantidade de vezes que esta habilidade pode ser ativada
	 * @param habilidade a habilidade a ativar
	 */
	public FornecedorHabilidade(int quantidade, Habilidade habilidade) {
		this.quantidade = quantidade;
		this.habilidade = habilidade;
	}

	/** Aplica a habilidade a um lemming
	 * @param l lemming onde aplicar a habilidade
	 */
	public void aplicarHabilidade( Lemming l ) {

        Habilidade habilidadeClone = habilidade.clone();  // Usar prototype para criar um clone

        if( quantidade <= 0 )
            return;

        if( !l.podeAtivarNovaHabilidade() )
            return;


        if(!l.setHabilidade(habilidadeClone))
            return;

        quantidade--;
	}
	
	/** retorna a quantidade desta habilidade qua ainda pode ativar
	 * @return a quantidade desta habilidade qua ainda pode ativar
	 */
	public int getQuantidade() {
		return quantidade;
	}

	/** modifica o número de vazes que a habilidade pode ser ativada
	 * @param quantidade o número de vazes que a habildiade pode ser ativada 
	 */
	public void setQuantidade(int quantidade) {
		if( quantidade < 0 )
			throw new IllegalArgumentException( quantidade + " < 0");
		this.quantidade = quantidade;
	}
}