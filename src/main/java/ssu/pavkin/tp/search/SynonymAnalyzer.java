package ssu.pavkin.tp.search;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.SynonymMap.Builder;
import org.apache.lucene.util.CharsRef;

public class SynonymAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String s) {
		SynonymMap.Builder builder = new Builder(true);
		addSynonym(builder, new String[]{"яндекс", "yandex", "поисковик"});

		SynonymMap synonymMap = null;
		try {
			synonymMap = builder.build();
		} catch (IOException e) {
			System.err.println("Error creating SynonymMap");
		}
		Tokenizer tokenizer = new ClassicTokenizer();
		return new TokenStreamComponents(tokenizer,
			new SynonymGraphFilter(new LowerCaseFilter(new StandardFilter(tokenizer)), synonymMap, true));
	}

	private static void addSynonym(SynonymMap.Builder builder, String[] synonyms) {
		for (String s1 : synonyms) {
			for (String s2 : synonyms) {
				builder.add(new CharsRef(s1), new CharsRef(s2), true);
			}
		}
	}
}
