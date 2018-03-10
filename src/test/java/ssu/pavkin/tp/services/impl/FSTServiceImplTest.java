package ssu.pavkin.tp.services.impl;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.util.fst.FST;
import org.junit.Test;
import ssu.pavkin.tp.services.FSTService;

public class FSTServiceImplTest {

	@Test
	public void testFST() throws IOException {
		FSTService fstService = new FSTServiceImpl();
		FST<Long> fst = fstService.buildFSTFromFile(new File("src\\main\\resources\\dictionary\\dictionary.txt"));
		fstService.saveFSTToFile(fst, "src\\main\\resources\\dictionary\\output_dictionary.txt");
	}
}
