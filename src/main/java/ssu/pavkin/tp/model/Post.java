package ssu.pavkin.tp.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {

	private String author;
	private String date;
	private String title;
	private String url;
	private List<String> tags;
	private Integer rating;
}
