package lemming.jogo;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import lemming.lemming.LemmingBase;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiVisual;
import prof.jogos2D.image.ComponenteVisual;

/** Esta classe é responsável por gerar os lemmings num dado nível
 * Os lemmings são gerados num local específico segundo uma cadência
 * predeterminada (mas que pode ser alterada).
 * Também é responsável por carregar as animações base dos lemmings e
 * atribui essas ao lemming criado
 */
public class GeradorLemmings {

	private int maximoLemmmings;
	private int numGerados = 0;
	private int intervaloMinimo, intervalo, proxCriar = 0;
	private int direcao;
	private boolean gerar = true;
	private Point entrada;
	private ComponenteMultiVisual anims; 
	
	/** Cria um gerador de lemmings
	 * @param total número total de lemmings a ser criado
	 * @param intervalo número de ciclo entre criações de lemmings
	 * @param pos a posição do mundo onde colcoar o lemming
	 * @param direcao direção inical do lemming
	 */
	public GeradorLemmings( int total, int intervalo, Point pos, int direcao ) {
		this.maximoLemmmings = total;
		this.intervalo = intervalo;
		intervaloMinimo = intervalo;
		proxCriar = intervalo;
		this.direcao = direcao;
		anims = lerAnimacoesLemming();
		entrada = (Point)pos.clone();
	}

	/** carrega as animações base dos lemmings
	 * @return um componente com todas as animações
	 */
	private ComponenteMultiVisual lerAnimacoesLemming() {
		try {
			ComponenteMultiVisual cmv = new ComponenteMultiVisual();
			cmv.addComponenteVisual("cair", readImages("art/lemming/cair", 1) );
			cmv.addComponenteVisual("sair", readImages("art/lemming/sair", 1) );
			cmv.addComponenteVisual("paraquedas", readImages("art/lemming/paraquedas", 1) );
			cmv.addComponenteVisual("escavar", readImages("art/lemming/escavar", 1) );
			cmv.addComponenteVisual("morrer", readImages("art/lemming/morrer", 1) );
			cmv.addComponenteVisual("mover_dir", readImages("art/lemming/mover_dir", 1) );
			cmv.addComponenteVisual("mover_esq", readImages("art/lemming/mover_esq", 1) );
			cmv.addComponenteVisual("trepar_dir", readImages("art/lemming/trepar_dir", 1) );
			cmv.addComponenteVisual("trepar_esq", readImages("art/lemming/trepar_esq", 1) );
			cmv.addComponenteVisual("demolir_dir", readImages("art/lemming/demolir_dir", 1) );
			cmv.addComponenteVisual("demolir_esq", readImages("art/lemming/demolir_esq", 1) );
			cmv.addComponenteVisual("bloquear", readImages("art/lemming/bloquear", 1) );
			cmv.addComponenteVisual("minerar_esq", readImages("art/lemming/minerar_esq", 1) );
			cmv.addComponenteVisual("minerar_dir", readImages("art/lemming/minerar_dir", 1) );
			cmv.addComponenteVisual("construir_dir", readImages("art/lemming/construir_dir", 1) );
			cmv.addComponenteVisual("construir_esq", readImages("art/lemming/construir_esq", 1) );
			cmv.addComponenteVisual("explodir", readImages("art/lemming/explodir", 1) );
			return cmv;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** faz a leitura das várias imagens que estão num dado diretório
	 * @param dir diretório onde ler as imagens
	 * @param delay delay a usar nas animações
	 * @return a animação lida
	 * @throws IOException caso algo corra mal na leitura
	 */
	private static ComponenteVisual readImages( String dir, int delay ) throws IOException {
		File animDir = new File( dir );
		File files[] = animDir.listFiles( f -> f.isFile() );
		if( files.length == 0 )
			throw new IOException("Não há ficheiros para ler");
		Image []imagens = new Image[ files.length ];
		for( int i=0; i < files.length; i++ ) {
			imagens[i] = ImageIO.read( files[i] );
		}
		return new ComponenteAnimado( new Point(), imagens, delay);
	}

	/** Cria um lemming, se for altura para isso
	 * @return o lemming criado
	 */
	public LemmingBase criarLemming() {
		// se parou a geração ou já gerou todos
		if( !gerar || numGerados >= maximoLemmmings )
			return null;
		
		// atualizar o ciclo de criação e ver se está na altura de criar
		proxCriar--;
		if( proxCriar > 0 )
			return null;
		
		// está na altura de criar um lemming
		proxCriar = intervalo;
		LemmingBase l;
		l = new LemmingBase( anims.clone(), direcao );
		l.setPosicaoCentro( (Point)entrada.clone() );
		numGerados++;
		return l;		
	}
	
	/** Ativa/desativa a geração
	 * @param gerar se deve ativar ou desativar a geração
	 */
	public void setGeracao( boolean gerar) {
		this.gerar = gerar;
	}

	/** Indica se ainda há lemmings por criar
	 * @return true se houver lemmings por criar
	 */
	public boolean temMais() {
		return gerar && numGerados < maximoLemmmings;
	}

	/** Aumenta a cadência de geração
	 */
	public void aumentarCadencia() {
		intervalo -= 2;
		if(intervalo < 2 )
			intervalo = 2;
	}

	/** diminui a cadência de geração
	 */
	public void diminuirCadencia() {
		intervalo += 2;
		if( intervalo > intervaloMinimo )
			intervalo = intervaloMinimo;
	}
}
