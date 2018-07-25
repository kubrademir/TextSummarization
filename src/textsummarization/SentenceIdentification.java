package textsummarization;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import zemberek.tokenization.TurkishSentenceExtractor;

public class SentenceIdentification {
	

	public List<String> list = new ArrayList<String>();


	public String[][] Cumle(String txt) throws IOException {

		list.add(txt);
		for (String lst : list) {
			txt = lst.toLowerCase();
		}

		TurkishSentenceExtractor ekstra = TurkishSentenceExtractor.DEFAULT;
		list = ekstra.fromParagraph(txt);

		String[][] str = new String[list.size()][100];
		String[] word;

		// Cumlenin icerisindeki kelimeleri cumleyle baglantı kurma.
		for (int i = 0; i < list.size(); i++) {
			str[i][0] = list.get(i);
			word = str[i][0].split(" ");
			for (int j = 1; j < word.length; j++) {
				str[i][j] = word[j - 1];
				if (j == (word.length - 1))
					str[i][j + 1] = word[j];
			}

		}
		return str;
	}


	
	public String[][] BuyukHarfCumle(String txt) throws IOException {

		list.add(txt);

		TurkishSentenceExtractor ekstra = TurkishSentenceExtractor.DEFAULT;
		list = ekstra.fromParagraph(txt);

		String[][] str = new String[list.size()][100];
		String[] word;

		// Cumlenin icerisindeki kelimeleri cumleyle baglantı kurma.
		for (int i = 0; i < list.size(); i++) {
			str[i][0] = list.get(i);
			word = str[i][0].split(" ");
			for (int j = 1; j < word.length; j++) {
				str[i][j] = word[j - 1];
				if (j == (word.length - 1))
					str[i][j + 1] = word[j];
			}

		}
		return str;
	}
}
