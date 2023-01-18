package pf.bb.model;

/**
 * Klasse für das Artikelobjekt.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class Article extends EntityWithID {

	// GSON options: serialize only ID for less content in JSON, deserialize=true=default all fields from Server

	public String name;

	public String type;

	public String producer;

	public String description;

	public String characteristic;

	public int value1;

	public int value2;

	public float price;

	public String hexColor;

	@Override
	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, name='%s']", id, name);
	}

}
