package lemming.lemming;

public class Andarilho extends Habilidade {
	void fazerHabilidade(LemmingBase lemming) {
		lemming.mover();
	}
	@Override
	void comecarCair(LemmingBase lemming) {
		// TODO Auto-generated method stub
		super.comecarCair(lemming);
	}
	@Override
	boolean setHabilidade(LemmingBase lemming, Habilidade habilidade) {
		// TODO Auto-generated method stub
		lemming.setVisualAtual( lemming.getDirecao() > 0? "mover_dir": "mover_esq");
		return super.setHabilidade(lemming,habilidade);

	}
	
	public Andarilho clone() {
        Andarilho a = new Andarilho();
        return a;
    }

}
