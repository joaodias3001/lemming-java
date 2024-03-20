package lemming.mundo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Objects;

/** Responsável por mantr a imagem do terreno de jogo.
 * O terreno de jogo é uma imagem onde os pixeis transparentes são 
 * os pixeis por onde o lemming se pode movimentar
 */
public class MapaTerrenoImagem {
	private BufferedImage imagem;
	private int comprimento, altura;
	
	/** Criar o mapa de terreno
	 * @param img imagem do terreno
	 */
	public MapaTerrenoImagem( BufferedImage img ){
		Objects.requireNonNull(img, "img não pode ser null");
	    if( img.getAlphaRaster() == null )
	    	throw new IllegalArgumentException( "img tem de ser uma imagem com canal alfa" );	    

	    comprimento = img.getWidth();
		altura = img.getHeight();
		imagem = img;
	}
	
	/** indica se um dado bloco rectangular está livre.
	 * Um bloco está livre se todos os pixeis forem tranaparentes
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @return true se estiver livre
	 */
	public boolean estaLivre( int x, int y, int comp, int alt ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y + alt > altura )
			throw new IllegalArgumentException();

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
	    
		int endY = y + alt;
		for( int ya = y; ya < endY; ya++ ) {
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 )
				if( pixels[offset] != 0 )
					return false;
		}
		return true;
		/*
		int endY = y + alt;
		int ocupados = 0;
		for( int ya = y; ya < endY; ya++ ) {
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 )
				if( pixels[offset] != 0 )
					ocupados++;
		}
		return ocupados < comp*alt/2;
		*/
	}
	
	/** retorna quantos pixeis ocupados (não transparentes) tem
	 *  um bloco retangular
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @return o número de pixeis não transparentes no bloco
	 */
	public int ocupados( int x, int y, int comp, int alt ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y + alt > altura )
			throw new IllegalArgumentException();

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		int endY = y + alt;
		int ocupados = 0;
		for( int ya = y; ya < endY; ya++ ) {
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 )
				if( pixels[offset] != 0 )
					ocupados++;
		}
		return ocupados;
	}
	
	/** Verifica se tem parede num dado bloco retangular. Para ser considerada uma
	 * pard tm d havr um número mínimo de pixeis seguidos na msma coluna
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @param min número minimo de pixeis em altura
	 * para ser coniderada uma parede
	 * @return
	 */
	public boolean temParede( int x, int y, int comp, int alt, int min ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y + alt > altura )
			throw new IllegalArgumentException();

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		int endX = x + comp;
		int endY = y + alt;
		int ocupados = 0;
		for( int xa = x; xa < endX; xa++ ) {
			for( int ya=y; ya < endY; ya++ ) {
				int offset = ( ya * comprimento + xa)*4;
				if( pixels[offset] != 0 )
					ocupados++;
				else
					ocupados = 0;
				if( ocupados == min )
					return true;
			}
		}
		return false;
	}

	/** retorna a altura do chão mais próximo num dado bloco,
	 * o chão é a linha mais alta onde não há pixeis transparentes 
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @return  a altura do chão mais próximo
	 */
	public int chaoMaisProximo( int x, int y, int comp, int alt ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento )
			throw new IllegalArgumentException();

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
	    
	    // ver a linha de base para ver se está vazia
		int offset = ((y-1) * comprimento + x)*4;
		boolean vazia = true;
		for( int i=0; i < comp ; i++, offset+=4 ) {
			if( pixels[offset] != 0 ) {
				vazia = false;
				break;
			}
		}
		
		// se linha está vazia, ver para baixo
		if( vazia )
			return chaoMaisProximo(x, y, comp);
		
		int degrau = degrauMaisProximo(x, y, comp, alt);
		return degrau == -1? y: degrau;
	}
	
	/** retorna a altura do chão mais próximo num dado comprimento,
	 * o chão é a linha mais alta onde não há pixeis transparentes 
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @return  a altura do chão mais próximo
	 */
	public int chaoMaisProximo( int x, int y, int comp ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento )
			throw new IllegalArgumentException();

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		for( int ya = y; ya < altura; ya++ ) {
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 )
				if( pixels[offset] != 0 )
					return ya;
		}
		
		return altura;
	}
	
	/** retorna o degrau mais próximo num bloco
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @return
	 */
	public int degrauMaisProximo( int x, int y, int comp, int alt ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y-alt < 0 )
			throw new IllegalArgumentException( x + "  + " + comp + " > " + comprimento + "  " + y + " - " +alt + " < 0");

		int chao = -1, altLivre = 0;
	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		for( int ya = y; ya > 0; ya-- ) {
			boolean livre = true;
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 ) {
				if( pixels[offset] != 0 ) {
					livre = false;
					break;
				}
			}
			if( livre ) {
				if( chao == -1 )
					chao = ya+1;
				altLivre++;
				if( altLivre == alt )
					return chao;
			}
			else
				chao = -1;
		}
		
		return -1;
	}
	
	/** retorna a posição x da parede mais próxima dentro de um bloco, varrendo
	 * o bloco da esquerda para a direita
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @return a posição x da parede mais próxima
	 */
	public int paredeMaisProximaDireita( int x, int y, int comp, int alt ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y-alt < 0 )
			throw new IllegalArgumentException( x + "  + " + comp + " > " + comprimento + "  " + y + " - " +alt + " < 0");

		int parede = -1;
	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
	    
		int endY = y + alt;
		for( int ya = y; ya < endY; ya++ ) {
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 ) {
				if( pixels[offset] != 0 ) {
					if( parede == -1 || parede > i )
						parede = i;
					break;
				}
			}
		}
		return x + parede;
	}
	
	/** retorna a posição x da parede mais próxima dentro de um bloco, varrendo
	 * o bloco da direita para a esquerda
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 * @return a posição x da parede mais próxima
	 */
	public int paredeMaisProximaEsquerda( int x, int y, int comp, int alt ) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y-alt < 0 )
			throw new IllegalArgumentException( x + "  + " + comp + " > " + comprimento + "  " + y + " - " +alt + " < 0");

		int parede = -1;
	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		int endY = y + alt;
		for( int ya = y; ya < endY; ya++ ) {
			int offset = (ya * comprimento + x + comp-1)*4;
			for( int i=comp-1; i >= 0; i--, offset-=4 ) {
				if( pixels[offset] != 0 ) {
					if( parede == -1 || parede < i )
						parede = i;
					break;
				}
			}
		}
		
		return x + parede;
	}

	/** verifica se as coordnadas são válidas
	 * @param x coordenada x
	 * @param y coordenada y
	 * @throws IllegalArgumentException se forem inválidas
	 */
	private void checkCoordenadas(int x, int y) throws IllegalArgumentException {
		if( x < 0 || x > comprimento )
			throw new IllegalArgumentException( "x inválido " + x);
		if( y < 0 || y > altura )
			throw new IllegalArgumentException( "y inválido " + y);
	}

	/** coloca transparentes todos os pixeis dentro do bloco
	 * @param x coordenada x do bloco
	 * @param y coordenada y do bloco
	 * @param comp comprimento do bloco
	 * @param alt altura do bloco
	 */
	public void limpar(int x, int y, int comp, int alt) {
		checkCoordenadas(x, y);
		if( x+comp > comprimento || y + alt > altura )
			throw new IllegalArgumentException();

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		int endY = y + alt;
		for( int ya = y; ya < endY; ya++ ) {
			int offset = (ya * comprimento + x)*4;
			for( int i=0; i < comp; i++, offset+=4 ) {
				pixels[offset] = 0;
			}
		}
	}
	
	/** Coloca transparentes todos os pexeis dentro de um circulo
	 * @param x centro x do círculo
	 * @param y centro y do círculo
	 * @param raio do círculo
	 */
	public void limparCirculo(int x, int y, int raio) {
		checkCoordenadas(x, y);

	    byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		//int endY = y + raio;
		int raioSq = raio*raio; 
		for( int ya = -raio; ya < raio; ya++ ) {
			int larg = (int)Math.sqrt( raioSq - ya*ya  );
			int yreal = ya + y;
			if( yreal < 0 || yreal >= altura )
				continue;
			int offset = (yreal * comprimento + x-larg)*4;
			for( int i=x-larg; i < x+larg; i++, offset+=4 ) {
				if( i < 0 || i >= comprimento )
					continue;
				pixels[offset] = 0;
			}
		}
	}
	
	/** Retorna um ambiente gráfico da imagem, que permite
	 * desenhar no terreno
	 * @return o ambinte gráfico da imagem do terreno
	 */
	public Graphics2D getGraphics() {
		return imagem.createGraphics();
	}
	
	/** retorna o comprimento do mapa do terreno
	 * @return o comprimento do mapa do terreno
	 */
	public int getComprimento() {
		return comprimento;
	}
	
	/** retorna a altura do mapa do terreno
	 * @return a altura do mapa do terreno
	 */
	public int getAltura() {
		return altura;
	}
}
