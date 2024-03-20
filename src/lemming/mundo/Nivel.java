package lemming.mundo;

import java.util.Collections;
import java.util.Map;

import lemming.jogo.GeradorLemmings;

/** Armazena as informaçãoes de um nível
 */
public class Nivel {
	private String titulo;
	private Mundo mundo;
	private GeradorLemmings gerador;
	Map<String,Integer> habilidades;
	private int numSalvar;
	private int numTotal;
	private int tempo;
	
	/** cria um nível
	 * @param titulo o título do nível
	 * @param mundo o mundo criado
	 * @param gerador o gerador usado
	 * @param habilidades mapa com as habilidades e respetiva quantidade
	 * @param numSalvar número de lemmings que é preciso salvar
	 * @param numTotal número total de lemmings a gerar
	 * @param tempo tempo limite do nível
	 */
	public Nivel(String titulo, Mundo mundo, GeradorLemmings gerador, Map<String,Integer> habilidades, int numSalvar, int numTotal, int tempo) {
		this.titulo = titulo;
		this.habilidades = habilidades;
		this.mundo = mundo;
		this.gerador = gerador;
		this.numSalvar = numSalvar;
		this.numTotal = numTotal;
		this.tempo = tempo;
	}
	
	/** Retorna o título
	 * @return o título
	 */
	public String getTitulo() {
		return titulo;
	}

	/** retorna o mundo
	 * @return o mundo
	 */
	public Mundo getMundo() {
		return mundo;
	}

	/** retorna o gerador de lemmings
	 * @return o gerador de lemmings
	 */
	public GeradorLemmings getGerador() {
		return gerador;
	}

	/** retorna o número de lemmings a salvar
	 * @return o número de lemmings a salvar
	 */
	public int getNumSalvar() {
		return numSalvar;
	}

	/** retorna o número de lemmings a gerar
	 * @return o número de lemmings a gerar
	 */
	public int getNumTotal() {
		return numTotal;
	}

	/** retorna o tempo limite
	 * @return o tempo limite
	 */
	public int getTempo() {
		return tempo;
	}

	/** retorna o mapa das habilidades
	 * @return o mapa das habilidades
	 */
	public Map<String, Integer> getHabilidades() {
		return Collections.unmodifiableMap( habilidades );
	}
}
