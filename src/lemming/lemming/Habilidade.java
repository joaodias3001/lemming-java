package lemming.lemming;

public abstract class Habilidade {
	void fazerHabilidade(LemmingBase lemming) {

	}
	void baterParede(LemmingBase lemming) {
		lemming.bateuParedeNormal();
	}
	void comecarCair(LemmingBase lemming) {
		lemming.comecarCairNormal();
	}

	boolean setHabilidade(LemmingBase lemming,Habilidade habilidade) {
		return true;
	}

	boolean podeAtivarNovaHabilidade() {
		return true;

	}
	
	public abstract Habilidade clone();

	
}
