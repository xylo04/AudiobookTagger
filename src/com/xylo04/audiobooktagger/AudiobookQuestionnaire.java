package com.xylo04.audiobooktagger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class AudiobookQuestionnaire extends AudiobookInfoFiller {

	Logger log = Logger.getLogger("com.xylo04.audiobooktagger");

	public AudiobookQuestionnaire(AudiobookInfo abi) {
		super(abi);
	}

	@Override
	public void fillAudiobook() {
		BufferedReader conIn = new BufferedReader(new InputStreamReader(
				System.in));

		try {
			System.out.print("Author: ");
			audiobook.setAuthor(conIn.readLine());

			System.out.print("Title: ");
			audiobook.setBookTitle(conIn.readLine());

			System.out.print("Year Published: ");
			audiobook.setYearPublished(conIn.readLine());

			int numTracks = 0;
			int chapter = 0;
			while (chapter <= 1 || numTracks != 0) {
				System.out.print("Number of tracks for chapter " + chapter
						+ ": ");
				numTracks = Integer.parseInt(conIn.readLine());

				if (numTracks > 0) {
					audiobook.setChapterTracks(chapter, numTracks);
					System.out.print("Name of chapter " + chapter + ": ");
					audiobook.setChapterName(chapter, conIn.readLine());
				} else if (chapter == 0 && numTracks == 0) {
					audiobook.setChapterTracks(chapter, 0);
					audiobook.setChapterName(chapter, "");
				}
				chapter++;
			}
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}
