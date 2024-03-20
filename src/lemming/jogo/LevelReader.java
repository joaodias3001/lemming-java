package lemming.jogo;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import lemming.mundo.MapaTerrenoImagem;
import lemming.mundo.Mundo;
import lemming.mundo.Nivel;
import lemming.obstaculo.*;
import prof.jogos2D.image.*;

/** Classe responsável pela leitura dos ficheiros de nível
 */
public class LevelReader {
	
	private static ESTProperties props;    // as propriedades a ler
	// o componente a usar quando há components visuais vazios
	private static ComponenteVisual vazio; 
	
	/**
	 * método para ler o ficheiro de nível
	 * @param level número do nivel
	 * @return o Nivel criado
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static Nivel lerNivel( int level ) throws FileNotFoundException, IOException{
		String file = "niveis\\nivel" + level + ".txt";

		// ler o ficheiro como uma sequência de propriedades
		props = new ESTProperties( new FileReader( file ) );
		
		// criar o componente vazio
		vazio = new ComponenteSimples();

		// ler imagem do terreno
		BufferedImage imgMapa = ImageIO.read( new File( "art/" + props.getConfig("mundo") ));
		MapaTerrenoImagem mapa = new MapaTerrenoImagem( imgMapa );	

		// ler informações do fundo do mapa
		ComponenteMultiplo fundo = new ComponenteMultiplo();		
		int nFundos = props.getConfigAsInt("num_fundos");
		for( int i=1; i <= nFundos; i++ ) {
			String p = "fundo_" + i + "_imagem";
			String info[] = props.getConfig( p ).split("\t");
			ComponenteVisual cv = lerComponenteVisual(info, 0);
			fundo.addComponente( cv.getPosicao(), cv );			
		}
		Mundo mundo = new Mundo( fundo, new ComponenteSimples( imgMapa ), mapa);
		
		// ler obstáculos
		int numObstaculos = props.getConfigAsInt("num_obstaculos");
		for( int i=1; i <= numObstaculos; i++) {
			String prop = "obstaculo_" + i;
			String tipo = props.getConfig(prop+"_tipo");
			Obstaculo o = null;
			switch( tipo ) {
			case "saida":
				o = criarSaida( prop );
				break;
			case "limite":
				o = criarLimite( prop );
				break;
			case "morte":
				o = criarMatador( prop );
				break;
			}
			mundo.addObstaculo(o);
		}
		
		// ler efeitos, se tiver efeitos
		if( props.getConfig("num_efeitos") != null) {
			int numEfeitos = props.getConfigAsInt("num_efeitos");
			for( int i=1; i <= numEfeitos; i++) {
				String prop = "efeito_" + i;
				String nome = props.getConfig(prop+"_nome");
				String imgInfo[] = props.getConfig(prop+"_imagem").split("\t");
				//System.out.println( imgInfo[1] );
				ComponenteVisual img = lerComponenteVisual( imgInfo, 0);
				mundo.registaEfeito( nome, img );
			}
		}

		// ver a posição da entrada
		String posStr[] = props.getConfig("entrada").split("\t");
		Point entrada = lerPosicao( posStr[0], posStr[1] );

		// ler ritmo de saida
		int ritmo = props.getConfigAsInt("ritmo");
		
		// ler quantos lemmings no total
		int total = props.getConfigAsInt("total");

		GeradorLemmings gerador = new GeradorLemmings( total, ritmo, entrada, 1 );
		
		// ler quantos lemmings a salvar
		int salvar = props.getConfigAsInt("objetivo");

		// ler o tempo que tem para acabar o nivel
		int tempo = props.getConfigAsInt("tempo");

		// ler o título do nível
		String titulo = props.getConfig("titulo");
		
		// ler habilidade do nível
		int numHabil = props.getConfigAsInt("num_habilidades" );
		
		// criar um mapa com as habilidades indexadas pelo respetivo nome 
		HashMap<String,Integer> habilidades = new HashMap<>(); 
		for( int i=1; i <= numHabil; i++) {
			String prop = "habilidade_" + i;
			String hInfo[] = props.getConfig( prop ).split("\t");
			String nome = hInfo[0];
			int quantidade = Integer.parseInt( hInfo[1] );
			habilidades.put(nome, quantidade);
		}
		
		// criar e retornar o nível
		return new Nivel(titulo, mundo, gerador, habilidades, salvar, total, tempo);
	}

	/** Cria o obstáculo de saída
	 * @param prop o prefixo das infos do obstáculo
	 * @return o obstáculo
	 * @throws IOException se houver problemas na leitura
	 */
	private static Obstaculo criarSaida( String prop ) throws IOException {
		String imgInfo[] = props.getConfig(prop+"_imagem").split("\t");
		ComponenteVisual img = lerComponenteVisual( imgInfo, 0);
		Rectangle zona = lerZona( props.getConfig(prop+"_zona") );
		return new Saida(img, zona);		
	}
	
	/** Cria um obstáculo limite
	 * @param prop o prefixo das infos do obstáculo
	 * @return o obstáculo
	 * @throws IOException se houver problemas na leitura
	 */
	private static Obstaculo criarLimite( String prop ) throws IOException {		
		Rectangle zona = lerZona( props.getConfig(prop+"_zona") );
		return new ObstaculoLimitador( vazio, zona);		
	}
	
	/** Cria um obstáculo que mata lemmings
	 * @param prop o prefixo das infos do obstáculo
	 * @return o obstáculo
	 * @throws IOException se houver problemas na leitura
	 */
	private static Obstaculo criarMatador( String prop ) throws IOException {		
		String imgInfo[] = props.getConfig(prop+"_imagem").split("\t");
		ComponenteVisual img = lerComponenteVisual( imgInfo, 0);
		Rectangle zona = lerZona( props.getConfig(prop+"_zona") );
		return new ObstaculoMatador( img, zona, props.getConfig(prop+"_morte"));		
	}

	/** lê a informação da zona de ativação do obstáculo
	 * @param rectInfo informações sobre a zona
	 * @return um rectangle representando a zona de ativação do obstáculo
	 */
	private static Rectangle lerZona(String rectInfo) {
		String info[] = rectInfo.split("\t");
		return new Rectangle( Integer.parseInt( info[0] ), Integer.parseInt( info[1] ),
				              Integer.parseInt( info[2] ), Integer.parseInt( info[3] ));
	}

	/** leitura de um componente visual
	 * @param info informação da linha
	 * @param idx índice a partir do qual está presente a info do componente
	 * @return o componente visual criado
	 * @throws IOException
	 */
	private static ComponenteVisual lerComponenteVisual(String[] info, int idx ) throws IOException {
		switch( info[idx] ) {
		case "CS":  return criarComponenteSimples( info, idx+1 );
		case "CA":  return criarComponenteAnimado( info, idx+1 );
		case "CMA": return criarComponenteMultiAnimado( info, idx+1 );
		}
		return null;
	}

	/** le a info e cria um componente simples
	 * Na linha a info é <br>
	 * CS pos x + pos y + nome da imagem
	 */
	private static ComponenteSimples criarComponenteSimples(String[] info, int idx) throws IOException {
		Point p = lerPosicao( info[idx], info[idx+1] );
		return new ComponenteSimples(p, "art/"+info[idx+2] );
	}
	
	/** le a info e cria um componente animado
	 * Na linha a info é <br>
	 * CA pos x + pos y + nome da imagem + número de frames + delay na animação
	 */
	private static ComponenteAnimado criarComponenteAnimado(String[] info, int idx ) throws IOException {
		Point p = lerPosicao( info[idx], info[idx+1] );
		String nomeImg =  "art/" + info[idx+2];
		int nFrames = Integer.parseInt( info[idx+3] );
		int delay = Integer.parseInt( info[idx+4] );
		ComponenteAnimado anim = new ComponenteAnimado( p, nomeImg, nFrames, delay );
		if( info.length-idx > 5 && info[idx+5].equals("unico") )
			anim.setCiclico( false );
		return anim;		
	}

	/** le a info e cria um componente multianimado
	 * Na linha a info é <br>
	 * CMA	pos x + pos y + nome da imagem + número de animações + número de frames + delay
	 */
	private static ComponenteMultiAnimado criarComponenteMultiAnimado(String[] info, int idx ) throws IOException {
		Point p = lerPosicao( info[idx], info[idx+1] );
		String nomeImg = "art/" + info[idx+2];
		int nAnims =  Integer.parseInt( info[idx+3] );
		int nFrames = Integer.parseInt( info[idx+4] );
		int delay = Integer.parseInt( info[idx+5] );
		return new ComponenteMultiAnimado( p, nomeImg, nAnims, nFrames, delay );
	}

	/**lê uma posição em point2D.Double
	 * @param strx coordenada x
	 * @param stry coordenada y
	 * @return a posição criada
	 */
	private static Point2D.Double lerPosicaoPrecisa(String strx, String stry) {
		double x = Double.parseDouble(strx);
		double y = Double.parseDouble(stry);
		return new Point2D.Double(x,y);
	}
	
	/**lê uma posição em point
	 * @param strx coordenada x
	 * @param stry coordenada y
	 * @return a posição criada
	 */
	private static Point lerPosicao(String strx, String stry) {
		int x = Integer.parseInt(strx);
		int y = Integer.parseInt(stry);
		return new Point(x,y);
	}
}
