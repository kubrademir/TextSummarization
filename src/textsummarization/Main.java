/*
 * Bu Program Otomatik Metin Özetleme Programıdır.
 * Bu program aracılığı ile metin özetleme işlemi gerçekleştirebilmek için
 * ya metni kopyala yapıştır yapmanız gerekiyor 
 * yada dosya oku butonundan bilgisayarınızdaki bir dosyayı okutarak yapabilirsiniz.
 * dosya dan özet çıkarma işlemi öncesinde bilmeniz gereken bazı öncelikler mevcut.
 * Dosya formatı txt olmalıdır.
 * Dosya içerisinde yazı utf-8 türünde olmalıdır.
 */

package textsummarization;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.zemberek.erisim.Zemberek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.Kelime;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField KeyTextField;
	public final JTextArea OrjinalTextArea;
	public final JTextArea SummarizationTextArea;
	JScrollPane scroll;
	


	private SentenceIdentification SI = new SentenceIdentification();
	private SummarizingFile SMF = new SummarizingFile();
	private SummarizingTxt SMT = new SummarizingTxt();

	private boolean FileReadClick = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public String textSplitting(String text){
		int sayac=0;
		String[] kelimeler = text.split(" ");
		for (int i = 0; i < kelimeler.length; i++) {
			if (kelimeler[i].length() + sayac < 75){
				sayac = sayac + kelimeler[i].length() + 1;
			}
			else {
				kelimeler[i] = kelimeler[i] + "\r\n";
				sayac = 0;
			}
		}
		text = " ";
		for (int j = 0; j < kelimeler.length; j++){
			text = text + kelimeler[j] + " ";
		}
		return text;
	}
	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		KeyTextField = new JTextField();
		KeyTextField.setFont(new Font("Serif", Font.PLAIN, 17));
		KeyTextField.setBounds(910, 5, 352, 27);
		KeyTextField.setColumns(10);

		JLabel lblAnahtarKelimeler = new JLabel("Anahtar Kelimeler");
		lblAnahtarKelimeler.setBounds(751, 4, 147, 27);
		lblAnahtarKelimeler.setFont(new Font("Serif", Font.PLAIN, 20));

		JButton btnSummarization = new JButton("Özetle");
		btnSummarization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!FileReadClick) {
					SummarizationTextArea.setText("");
					
					if (!OrjinalTextArea.getText().equals("")) {
						// Burada bazı sorunlar olabilir tekrar kontrol edilmesi gerekir.
						SMT.SentencePosition(OrjinalTextArea.getText().toString());

						try {

							// Başlık da geçen kelimelerin kontrolü yapılacak.
							SMT.titleWords(SI.Cumle(OrjinalTextArea.getText().toString()));

							// Pozitif kelime kontrolü yapılıyor.
							SMT.PositiveWord(SI.Cumle(OrjinalTextArea.getText().toString()));

							// Negatif kelime kontrolü yapılıyor.
							SMT.NegativeWord(SI.Cumle(OrjinalTextArea.getText().toString()));

							// Noktalamalara göre puanlandırma
							SMT.PunctuationPointing(SI.Cumle(OrjinalTextArea.getText().toString()));

							//
							SMT.QuotationPointing(SI.Cumle(OrjinalTextArea.getText().toString()));

							// Tarihe göre puanlandırma
							SMT.DatePointing(SI.Cumle(OrjinalTextArea.getText().toString()));

							// Özel İsim kelime kontrol
							 SMT.properNoun(SI.Cumle(OrjinalTextArea.getText().toString()));
							 SMT.ProperNoun(SI.Cumle(OrjinalTextArea.getText().toString()));
						
						} catch (Exception io) {
							System.out.println(io.getMessage());
						}

						if (!KeyTextField.getText().equals("")) { // Eğer anahtar kelime girilirse.
							try { // Anahtar kelimeleri kontrol yapılıyor.
								SMT.KeyWord(SI.Cumle(OrjinalTextArea.getText().toString()),
										KeyTextField.getText().toString());
							}

							catch (IOException e1) {
								e1.printStackTrace();
							}
						}

						// Özetleme işlemi gerçekleştirilecektir.
						
						String str2 = SMT.SortignSentence(OrjinalTextArea.getText().toString());
						String[] str = str2.split("(, ,)|(null)");
						String[] temp = str[1].split("\n\n");
						for (int i = 1; i < temp.length; i++) {
							SummarizationTextArea.append(temp[i] + "\n\n");
							SummarizationTextArea.setCaretPosition(SummarizationTextArea.getDocument().getLength());	
						}
						
						for(int i = 0; i < SMF.counter.length; i++) {
							SMF.counter[i] = 0;
						}
					}
				} else {
					FileReadClick = false;
					if (!OrjinalTextArea.getText().equals("")) {

						SummarizationTextArea.setText("");
						
						SMF.SentencePosition(OrjinalTextArea.getText().toString());// Cümlenin konumunu kontrol
																					// ediliyor.
						try {

							SMF.NegativeWord(SI.Cumle(OrjinalTextArea.getText().toString()));// Negatif kelime kontrol ediliyor.

							SMF.titleWords(SI.Cumle(OrjinalTextArea.getText().toString()));// Baslik kontrol
							SMF.PositiveWord(SI.Cumle(OrjinalTextArea.getText().toString()));// Pozitif kelime kontrol
							SMF.ProperNoun(SI.Cumle(OrjinalTextArea.getText().toString()));// Özel İsim kelime kontrol

							SMF.PunctuationPointing(SI.Cumle(OrjinalTextArea.getText().toString()));
							SMF.QuotationPointing(SI.Cumle(OrjinalTextArea.getText().toString()));
							SMF.DatePointing(SI.Cumle(OrjinalTextArea.getText().toString()));

						} catch (IOException e1) {
							e1.printStackTrace();
						}
						if (!KeyTextField.getText().equals("")) // Eğer anahtar kelime girilirse.
							try {
								SMF.KeyWord(SI.Cumle(OrjinalTextArea.getText().toString()),
										KeyTextField.getText().toString());// Anahtar kelimeleri kontrol ediliyor.
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						// Özetleme işlemi gerçekleştirilecektir.
						SummarizationTextArea.setText("");
						String[] str = SMF.SortignSentence(OrjinalTextArea.getText().toString()).split("(, ,)|(null)");
						String[] temp = str[1].split("\r\n\r\n");
						
						for (int i = 1; i < temp.length; i++) {
							SummarizationTextArea.append(temp[i] +"--");
						}

						String fixedResult= SummarizationTextArea.getText().replaceAll("--","\r\n\r\n");
						String fixedResult2 = fixedResult.replace("[", "");
						fixedResult = fixedResult2.replace("]","");
						SummarizationTextArea.setText(fixedResult);
						
						for(int i = 0; i < SMF.counter.length; i++) {
							SMF.counter[i] = 0;
						}
					}
				}
			}
		});
		btnSummarization.setBounds(545, 273, 194, 58);
		btnSummarization.setFont(new Font("Serif", Font.PLAIN, 17));

		JButton btnFileRead = new JButton("Dosyadan Oku");
		btnFileRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileReadClick = true;

				OrjinalTextArea.setText("");
				FileRW temp = new FileRW();
				File f = null;
				String path = null;

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(fileChooser);
				f = fileChooser.getSelectedFile();
				if (f != null) {
				path = f.getAbsolutePath();

				try {
					String txt = temp.FileRead(path);
					String txt2=txt.replaceAll(", , ","\r\n\r\n");
					OrjinalTextArea.append(txt2);

				} catch (IOException e1) { 
					e1.printStackTrace();
				}
				}
			}
		});
		btnFileRead.setBounds(22, 622, 163, 50);
		btnFileRead.setFont(new Font("Serif", Font.PLAIN, 17));

		JButton btnClear = new JButton("Temizle");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OrjinalTextArea.setText("");
				SummarizationTextArea.setText("");
			}
		});
		btnClear.setBounds(1138, 622, 124, 50);
		btnClear.setFont(new Font("Serif", Font.PLAIN, 17));

		contentPane.setLayout(null);
		contentPane.add(lblAnahtarKelimeler);
		contentPane.add(KeyTextField);
		contentPane.add(btnSummarization);
		contentPane.add(btnFileRead);
		contentPane.add(btnClear);

		JLabel OrjinalTextLabel = new JLabel("Orjinal Metin");
		OrjinalTextLabel.setFont(new Font("Serif", Font.PLAIN, 22));
		OrjinalTextLabel.setBounds(201, 45, 163, 27);
		contentPane.add(OrjinalTextLabel);

		JLabel TextSummarizationLabel = new JLabel("Özet Metin");
		TextSummarizationLabel.setFont(new Font("Serif", Font.PLAIN, 22));
		TextSummarizationLabel.setBounds(959, 45, 163, 27);
		contentPane.add(TextSummarizationLabel);

		JScrollPane OrjinalTextScrollPane = new JScrollPane();
		OrjinalTextScrollPane.setBounds(22, 85, 511, 536);
		contentPane.add(OrjinalTextScrollPane);

		OrjinalTextArea = new JTextArea();
		OrjinalTextArea.setFont(new Font("Serif", Font.PLAIN, 17));
		OrjinalTextScrollPane.setViewportView(OrjinalTextArea);
		OrjinalTextArea.setLineWrap(true);
		OrjinalTextArea.setWrapStyleWord(true);

		JScrollPane SummarizationTextScrollPane = new JScrollPane();
		SummarizationTextScrollPane.setBounds(751, 85, 511, 536);
		contentPane.add(SummarizationTextScrollPane);

		SummarizationTextArea = new JTextArea();
		SummarizationTextScrollPane.setViewportView(SummarizationTextArea);
		SummarizationTextArea.setFont(new Font("Serif", Font.PLAIN, 17));
		SummarizationTextArea.setLineWrap(true);
	
		//Kelime çözümlemesi yapılıyor.
		JButton btnKelimeKkBul = new JButton("Kelime Çözümle");
		btnKelimeKkBul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String[][] str=SI.Cumle(OrjinalTextArea.getText().toString());
					Kelime[] yeni=new Kelime[100];
					Zemberek zemberek = new Zemberek(new TurkiyeTurkcesi());
					
					SummarizationTextArea.setText(" ");
					for (int i = 0; i < str.length; i++) {
						String[] word = str[i][0].split("( )|(\\.)|(\\,)|(\\?)|(\n\n)");

						for (int j = 0; j < word.length; j++)
							yeni =zemberek.kelimeCozumle(word[j]);
							for(int k=0;k<yeni.length;k++) {
								SummarizationTextArea.append(yeni[k].toString()+"\n\n");
								
						}
					}
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
						
			}
		});
		btnKelimeKkBul.setFont(new Font("Serif", Font.PLAIN, 17));
		btnKelimeKkBul.setBounds(545, 344, 194, 58);
		contentPane.add(btnKelimeKkBul);

	}
}

