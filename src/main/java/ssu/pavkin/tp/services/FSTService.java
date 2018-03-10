package ssu.pavkin.tp.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.lucene.util.fst.FST;

public interface FSTService {

	FST<Long> buildFSTFromFile(File file) throws IOException;

	void saveFSTToFile(FST<Long> fst, String path) throws IOException;
}
