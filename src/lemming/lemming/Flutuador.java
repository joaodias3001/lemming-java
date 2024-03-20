package lemming.lemming;

public class Flutuador extends Habilidade {
	
	@Override
	void fazerHabilidade(LemmingBase lemming) {
		lemming.mover();
	}
	
	@Override
	void comecarCair(LemmingBase lemming) {
		
		super.comecarCair(lemming);
		lemming.setVisualAtual("paraquedas");
	}
	
	@Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		// TODO Auto-generated method stub
		if( lemming.estaCair() )
			lemming.setVisualAtual("paraquedas");
		

		return super.setHabilidade(lemming, habilidade);
	}
	
	public Flutuador clone() {
        Flutuador f = new Flutuador();
        return f;
        }
	
}
