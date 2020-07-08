
public class movies {
	private String id;
	private String title;
	private int year;
	private String director;
	public movies(){
	}
	public movies(String id, String title, int year,String director) {
		this.id = id;
		this.title = title;
		this.year  = year;
		this.director = director;
	}
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public int getYear() {
		return year;
	}
	public String getDirector() {
		return director;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setDirector(String director) {
		this.director = director;
	}
}
