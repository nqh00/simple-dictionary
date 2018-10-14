/*
 * Tu Dien Giao Dien Do Hoa
 * @author Nguyen Quang Huy
 * @version 1.0
 * @since 14-10-2018
 */
package dictionary;

public class Word {
	protected String word_target, word_explain;
	
	/*
	 * Getter&Setter cho word_target va word_explain
	 */
	
	public String getWord_target() {
		return word_target;
	}

	public void setWord_target(String word_target) {
		this.word_target = word_target;
	}

	public String getWord_explain() {
		return word_explain;
	}

	public void setWord_explain(String word_explain) {
		this.word_explain = word_explain;
	}
	
	public Word(String wtarget, String wexplain) {
		this.word_target = wtarget;
		this.word_explain = wexplain;
	}
	
	public Word() {}
}
