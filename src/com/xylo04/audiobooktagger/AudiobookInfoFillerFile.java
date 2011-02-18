package com.xylo04.audiobooktagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AudiobookInfoFillerFile extends AudiobookInfoFiller {

	private File infoFile;

	public AudiobookInfoFillerFile(AudiobookInfo abi, File infoFile) {
		super(abi);
		this.infoFile = infoFile;
	}

	@Override
	public void fillAudiobook() {
		System.out.println("Reading info from file "
				+ infoFile.getAbsolutePath());
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(infoFile));
			audiobook.setAuthor(fileReader.readLine());
			audiobook.setBookTitle(fileReader.readLine());
			audiobook.setYearPublished(fileReader.readLine());

			int chapterNum = 0;
			while (fileReader.ready()) {
				String[] lineParts = fileReader.readLine().split("\t", 2);

				Integer numTracks = Integer.parseInt(lineParts[0]);
				if (numTracks == 0) {
					continue;
				}
				String chapterName = lineParts[1];

				audiobook.setChapterTracks(chapterNum, numTracks);
				audiobook.setChapterName(chapterNum, chapterName);
				chapterNum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
