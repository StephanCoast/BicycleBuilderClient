package pf.bb.model;

public class ConfigurationStatus extends EntityWithID {

	public String name;

	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, name='%s']", id, name);
	}
}
