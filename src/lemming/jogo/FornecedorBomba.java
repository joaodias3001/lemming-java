package lemming.jogo;

import lemming.lemming.Lemming;
import lemming.mundo.Mundo;

/** Representa um fornecedor de bombas, isto é,
 * adiciona a capacidade de explodir a um lemming  
 */
public class FornecedorBomba extends FornecedorHabilidade {
	
	public FornecedorBomba(int quantidade) {
		super(quantidade, null);
	}

	@Override
	public void aplicarHabilidade( Lemming l ) {
		if( getQuantidade() <= 0 )
			return;

		l.detonar( 150, 5 );
		setQuantidade( getQuantidade()-1 );
	}
}
