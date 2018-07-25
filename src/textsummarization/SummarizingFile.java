package textsummarization;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import zemberek.tokenization.TurkishSentenceExtractor;



public class SummarizingFile {
	// ********
	// Burası pozitif kelimeler dizisi, yeni sayac eklemedim sendekinin ismiyle
	// değiştirdim
	private String[] PositiveWords = { "özetle", "sonuçta", "kısaca", "kısacası", "sonuç olarak", "neticede","en önemlisi" };
	
	private static FileRW file = new FileRW();
	
	//harf dizisi 
	String[] harf;
	//Buyuk Harf dizisi tanımı
	private String[] buyukHarf= {"A","B","C","D","E","F","G","H","I","İ","K","L","M","N","O","Ö","P","R","S","T","U","Ü","V","Y","Z"};
	
	
	//KisiSözlugunu diziye çevirdim hala da devam etmekteyim eklemeleri yapıcam 
	//Aynı şekilde organizasyon ve yer isimleri içinde yapıyorum
	//Sizce hepsi bir dizide mi olsun ayrı dizide mi??
	
	
	
	
	// *****
	private String[] stopWords = { "acaba", "ama", "ancak", "artık", "asla", "aslında", "az", "altmış", "altı", "arada",
			"ayrıca", "bana", "bazen", "bazı", "bazıları", "bazısı", "belki", "ben", "beni", "benden", "beri", "benim",
			"beş", "bile", "bin", "bir", "birçoğu", "birçok", "birçokları", "biri", "birisi", "birkaç", "birkaçı",
			"birkez", "birşey", "birşeyi", "biz", "bize", "bizden", "bizi", "bizim", "böyle", "böylece", "bu", "buna",
			"bunda", "bundan", "bunlar", "bunları", "bunların", "bunu", "bunun", "burada", "bütün", "çoğu", "çoğuna",
			"çoğunu", "çok", "çünkü", "da", "daha", "de", "dahi", "defa", "değil", "demek", "diğer", "diğeri",
			"diğerleri", "diye", "dolayı", "doksan", "dokuz", "dolayı", "dolayısıyla", "dört", "elbette", "en",
			"edecek", "eden", "ederek", "edilecek", "ediliyor", "edilmesi", "ediyor", "eğer", "elli", "etmesi", "etti",
			"ettiği", "ettiğini", "fakat", "falan", "felan", "filan", "gene", "gibi", "göre", "halen", "hangi",
			"hangisi", "hani", "hatta", "hem", "henüz", "hep", "hepsi", "hepsine", "hepsini", "her", "her biri",
			"herhangi", "herkes", "herkese", "herkesi", "herkesin", "hiç", "hiç kimse", "hiçbiri", "hiçbirine",
			"hiçbirini", "için", "içinde", "ile", "ise", "işte", "iki", "ilgili", "itibaren", "itibariyle", "kaç",
			"kadar", "karşın", "katrilyon", "kendi", "kendine", "kendilerine", "kendini", "kendisi", "kendisine",
			"kendisini", "kez", "ki", "kim", "kimden", "kime", "kimi", "kimse", "kırk", "kimin", "kimisi", "madem",
			"mı", "mi", "milyar", "milyon", "mu", "mü", "nasıl", "ne", "ne kadar", "ne zaman", "neden", "nedenle",
			"nedir", "nerde", "nerede", "nereden", "nereye", "nesi", "neyse", "niçin", "niye", "o", "olan", "olarak",
			"oldu", "olduğu", "olduğunu", "olduklarını", "olmadı", "olmadığı", "olmak", "olması", "olmayan", "olmaz",
			"olsa", "olsun", "olup", "olur", "olursa", "oluyor", "on", "ona", "ondan", "onlar", "onlara", "onlardan",
			"onları", "onların", "onu", "onun", "otuz", "orada", "oysa", "oysaki", "öbürü", "ön", "önce", "ötürü",
			"öyle", "pek", "rağmen", "sadece", "sanki", "sana", "sekiz", "seksen", "sen", "senden", "seni", "senin",
			"siz", "sizden", "size", "sizi", "sizin", "son", "sonra", "seobilog", "şayet", "şey", "şeyden", "şeyi",
			"şeyler", "şimdi", "şöyle", "şu", "şuna", "şunda", "şundan", "şunlar", "şunları", "şunu", "şunun",
			"tarafından", "trilyon", "tabi", "tamam", "tüm", "tümü", "üzere", "üç", "üzere", "var", "vardı", "ve",
			"veya", "veyahut", "ya", "ya da", "yani", "yapacak", "yapılan", "yapılması", "yapıyor", "yapmak", "yaptı",
			"yaptığı", "yaptığını", "yaptıkları", "yedi", "yerine", "yetmiş", "yine", "yirmi", "yoksa", "yüz", "zaten",
			"zira" };
	private String[] aylar = { "ocak", "şubat", "mart", "nisan", "mayıs", "haziran", "temmuz", "ağustos", "eylül", "ekim", "kasım", "aralık",
			"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık" };
	
	public int[] counter = new int[stopWords.length];
	
	private int[] KTekrar = new int[750];

	static int FToplam = 0;

	static float ortalama = 0;

	// **************Burası yeni eklenen yerler************

	// Pozitif kelime kontrolü yapılmakta.
	public void PositiveWord(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word;

		try {

			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++)
					for (int k = 0; k < PositiveWords.length; k++)
						if (word[j].equals(PositiveWords[k]) && !word[j].equals(null))
							counter[i] += 15;

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		//for (int i = 0; i < 100; i++) {
		//	System.out.println("PositiveWord - " + i + " :" + counter[i]);
		//}
	}


	public void titleWords(String[][] Sentence) {

		String[][] str = Sentence;
		String[] word;

		String[] title=str[0][0].split(", ,"); // İlk cümle başlık cümlesi olarak diziye atılıyor.
		String[] titleWords=title[0].split(" "); //başlık cümlesi kelimelere ayrılıyor.

		try {

			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++)
					for (int k = 0; k < titleWords.length; k++)
						if (word[j].equals(titleWords[k]) && !word[j].equals(null))
							counter[i] += 20;

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		//for (int i = 0; i < 100; i++) {
		//	System.out.println("TitleWord - " + i + " :" + counter[i]);
		//}
	}


	// *****
	
	//Özel isim sözlük kontrolü fonksiyonu
	public void ProperNoun(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word;
		
		
		String line;
		List<String> liste = new ArrayList<String>();
		String[] properNoun;
		try {
			
			File file = new File("C:/YeniMetinler/Ozel_Isimler/CustomName.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			

			while ((line = br.readLine()) != null) {
				liste.add(line);
				
			}

			line = liste.toString();
			properNoun = line.split("\n");
			
			//bu döngü  ilk kelimeler sözlükte var ise puan ataması yapıyor.
			for (int i = 1; i < str.length; i++) {
				word = str[i][1].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])"); //her cümlenin ilk kelimesi alınıyor
				
				for (int j = 0; j < word.length; j++)
					for (int k = 0; k < properNoun.length; k++)
						if (word[j].equals(properNoun[k]) && !word[j].equals(null)) {
							counter[i] += 3;
						
						}
			}


			fr.close();
			br.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
//		for (int i = 0; i < 100; i++) {
//			System.out.println("ProperNoun - " + i + " :" + counter[i]);
//		}

		
	}
	
	//Cümlede ki ilk kelimeden sonra ki kelimelerin büyük harf kontrolü
	public void properNoun(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word;
		String[] wordNew= new String[55];
		String deneme;
		
			try
			{
			//bu döngü de ise ilk kelime haricinde ki kelimeler büyük harfle başlıyor ise özel isimdir deyip puan atıyor.
				for(int i=1;i < str.length; i++) {
					word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");
					for(int m=2; m <word.length; m++) {
						wordNew[m-2] += word[m].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");
										
					}		
				}
				
				for(int i=1;i < str.length; i++) {
					for (int j = 0; j < wordNew.length; j++) {
						deneme = wordNew[j].substring(0,1);
						for(int k=0;k<buyukHarf.length;k++) {
							if(buyukHarf[k].equals(deneme) )
								counter[i] += 3;
							
						}
					}

				}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
//		for (int i = 0; i < 100; i++) {
//			System.out.println("ProperNoun - " + i + " :" + counter[i]);
//		}

		
	}
	
	
	public void FrequencyCalculation(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word = new String[100];
		List<String> liste = new ArrayList<String>();
		String line;
		
		
		try {
			// Gelen çift boyutlu diziyi bir string değişkenine atadık.
			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++) {
					liste.add(word[i]);
				}
					
			}
			
			line = liste.toString();
			word = line.split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");
			
			int i, j, k;
			List<String> list = new ArrayList<String>();
			for (i = 0; i < word.length; i++) {
				if (list.contains(word[i]))
					continue;
				list.add(word[i]);
				for (j = 0; j < word.length; j++) {
					if (word[i].equals(word[j])) {
						KTekrar[i]++;
					}

				}

				FToplam += KTekrar[i];
				ortalama = FToplam / list.size();
			}

			for (k = 0; k < KTekrar.length; k++) {
				if (KTekrar[k] > ortalama ) {
					counter[k] += KTekrar[k];
					//System.out.println("Kelime:"+word[k]+"Frekansı:"+KTekrar[k]);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		
	}
	// ****************Burada son buluyor yaptıklarım****************

	// ****
	// Negatif kelime kontrolü yapılmaktadır.
	public void NegativeWord(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word = new String[100];

		try {
			// Gelen çift boyutlu diziyi bir string değişkenine atadık.
			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++)
					for (int k = 0; k < stopWords.length; k++)
						if (word[j].equals(stopWords[k]) && !word[j].equals(null))
							counter[i] += -20;

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		// for (int i = 0; i < 100; i++) {
		// System.out.println( i + "- NegativeWord - " + str[i][0] +" :" + counter[i]);
		// }

	}

	// ****
	// Eğer anahtar kelimler girilirse ona göre puan değerleri atanıyor.
	public void KeyWord(String[][] Sentence, String key) {
		// Zemberek zemberek = new Zemberek(new TurkiyeTurkcesi());

		ArrayList<String> list = new ArrayList<>();

		list.add(key);

		for (String lst : list) {
			key = lst.toLowerCase();
		}

		String[] KeyWord = key.split(",");
		String[][] str = Sentence;
		String[] word;

		/*
		 * String[] KeyWordRoot = new String[KeyWord.length];
		 * 
		 * for (int j = 0; j < KeyWord.length; j++) { if
		 * (zemberek.kelimeDenetle(KeyWord[j])) KeyWordRoot[j] =
		 * zemberek.kelimeCozumle(KeyWord[j])[0].kok().icerik(); else KeyWordRoot[j] =
		 * KeyWord[j]; }
		 */

		try {
			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");
				/*
				 * String[] wordRoot = new String[word.length]; for (int j = 0; j < word.length;
				 * j++) { if (zemberek.kelimeDenetle(word[j])) wordRoot[j] =
				 * zemberek.kelimeCozumle(word[j])[0].kok().icerik(); else wordRoot[j] =
				 * KeyWord[j]; }
				 */

				for (int j = 0; j < word.length; j++) {
					for (int k = 0; k < KeyWord.length; k++)
						if (word[j].equals(KeyWord[k]) && !word[j].equals(null))
							counter[i] += 8;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		/*
		 * for (int i = 0; i < 100; i++) { System.out.println("KeyWord - " + i + " :" +
		 * counter[i]); }
		 */
	}

	// ****
	// Buranın kontrolü yapıldı şuanda ilk ve son paragraftaki cümlelerin puanlarını
	// atanıyor.
	public void SentencePosition(String Sentence) {
		String[] paragraph;
		String[] firstParagraph, lastParagraph;
		String[] SentenceCount;

		String firstParagraphSentence, lastParagraphSentence;

		paragraph = Sentence.split("(, ,)|(\\[)|(\\])");
		SentenceCount = Sentence.split("(\\.)|(\\[)|(\\])");

		firstParagraphSentence = paragraph[1];
		firstParagraph = firstParagraphSentence.split("\\.");

		for (int i = 0; i < firstParagraph.length; i++) {
			counter[i] += 10;
		}

		lastParagraphSentence = paragraph[paragraph.length - 1];
		lastParagraph = lastParagraphSentence.split("\\.");
		for (int i = SentenceCount.length - 2; i >= (SentenceCount.length - lastParagraph.length - 1); i--) {
			counter[i] += 10;
		}

		/*
		 * for (int i = 0; i < 100; i++) { System.out.println("SentencePosition - " + i
		 * + " :" + counter[i]); }
		 */

	}

	// burada noktalama işaretlerine göre puanlama yapılıyor(kelimenin sonunda soru
	// veya ünlem işareti varsa +2 puan veriliyor)
	public void PunctuationPointing(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word;
		String[] punctuate = {"?", "!"};

		try {
			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++){
					for (int k = 0; k < 2; k++){
						if (word[j].equals(punctuate[k])){
							counter[i] += 2;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

//		for (int i = 0; i < 2; i++) {
//			System.out.println("SpecialPunctuation - " + i + " :" + counter[i]);
//		}
	}

	// tırnağa göre puanlama
	public void QuotationPointing(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word;

		try {
			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++){
					if (!word[j].equals(null)){
						String substrtemp = word[j].substring(0,1);
						System.out.println(substrtemp);
						if (substrtemp == "'"){
							counter[i] += 1;
						}
						}
						}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
//
//		for (int i = 0; i < 2; i++) {
//			System.out.println("Quote - " + i + " :" + counter[i]);
//		}
	}

	// tarih puanlandırması
public void DatePointing(String[][] Sentence) {
		String[][] str = Sentence;
		String[] word;

		try {
			for (int i = 0; i < str.length; i++) {
				word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\\[)|(\\])");

				for (int j = 0; j < word.length; j++){
					for (int k = 0; k < aylar.length; k++){
						if (word[j].equals(aylar[k]) && !word[j].equals(null)){
						counter[i] += 1;
						}}}
				
				for (int l = 0; l < word.length; l++){
				if (((word[l].length() - word[l].replace(".", "").length() == 2) || (word[l].length() - word[l].replace("/", "").length() == 2)) && !word[l].equals(null)){
					counter[i] += 1;
				}}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

//		for (int i = 0; i < 5; i++) {
//			System.out.println("Date - " + i + " :" + counter[i]);
//		}
	}

	public List<String> list = new ArrayList<String>();
	// *****
	// Burda her cümlenin puanına bakılarak özet e eklenecek olan cümle
	// belirlencektir.
	public String SortignSentence(String Sentence) {

		String[] paragraph = new String[100];
		String str = null;
		
		
		int temp=0;
		int c=0;
		
		for(int i=0; i < counter.length;i++){
			c++;
		}
		
		for(int i=0; i<counter.length;i++) {
			temp += counter[i];
		}
		
		temp= temp / c;
		
		//TurkishSentenceExtractor ekstra = TurkishSentenceExtractor.DEFAULT;
		//list = ekstra.fromParagraph(Sentence);
		
		paragraph = Sentence.split("(\\.)");

		
		for (int i = 0; i < paragraph.length; i++) {
			if (counter[i] > temp) {
				str += paragraph[i] + ". ";
			}
		}

		return str;

	}

}
