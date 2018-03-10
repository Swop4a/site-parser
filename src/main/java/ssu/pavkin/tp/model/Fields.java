package ssu.pavkin.tp.model;

public enum Fields {
	AUTHOR("author"),
	DATE("date"),
	TITLE("title"),
	URL("url"),
	TAGS("tags"),
	RATING("rating");

	private String name;

	Fields(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
