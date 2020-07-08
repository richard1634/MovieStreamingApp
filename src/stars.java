
public class stars {
	private String id;
	private String name;
	private int birthYear;

	public stars(){
	}
	public stars(String id, String name, int birthYear) {
		this.id = id;
		this.name = name;
		this.birthYear  = birthYear;

	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getBirthYear() {
		return	birthYear;
	}
	public void setId(String id) {
		this.id = id;

	}
	public void setName(String name) {
		this.name = name;

	}
	public void setBirthYear(int birthYear) {
		this.birthYear  = birthYear;
	}
}
