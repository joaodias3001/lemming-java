package lemming.efeito;

import lemming.lemming.LemmingBase;
import java.awt.Graphics2D;

public interface Efeito {
	void aplicarEfeito(LemmingBase lemming);
	void desenharEfeito(Graphics2D g, LemmingBase lemming);
}
