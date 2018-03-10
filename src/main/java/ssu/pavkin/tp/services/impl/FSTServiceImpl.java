package ssu.pavkin.tp.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.FST;

import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;
import ssu.pavkin.tp.services.FSTService;

public class FSTServiceImpl implements FSTService {

	@Override
	public FST<Long> buildFSTFromFile(File file) throws IOException {
		Scanner scanner = new Scanner(file);
		List<String> words = new ArrayList<>();;
		while (scanner.hasNextLine()) {
			words.add(scanner.nextLine());
		}
		words.sort(String::compareTo);

		Builder<Long> builder = new Builder<>(FST.INPUT_TYPE.BYTE1, PositiveIntOutputs.getSingleton());
		IntsRefBuilder intsRefBuilder = new IntsRefBuilder();

		for (int i = 0; i < words.size(); i++) {
			BytesRef bytesRef = new BytesRef(words.get(i));
			builder.add(Util.toIntsRef(bytesRef, intsRefBuilder), (long) i);
		}
		return builder.finish();
	}

	@Override
	public void saveFSTToFile(FST<Long> fst, String path) throws IOException {
		fst.save(new File(path).toPath());
	}
}
