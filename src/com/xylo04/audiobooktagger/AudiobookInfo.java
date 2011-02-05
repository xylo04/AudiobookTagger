package com.xylo04.audiobooktagger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AudiobookInfo {

	private static final Logger log = Logger
			.getLogger("com.xylo04.audiobooktagger.AudiobookInfo");

	private String author;
	private String book_title;
	private Integer chapterCount = 0;
	private List<String> chapterNames = new ArrayList<String>();
	private List<Integer> chapterTracks = new ArrayList<Integer>();
	private List<Integer> chapterTracksCumSum = new ArrayList<Integer>();
	private Integer trackCount = 0;
	private String yearPublished;

	public void audiobookQuestionaire() {
		BufferedReader conIn = new BufferedReader(new InputStreamReader(
				System.in));

		try {
			System.out.print("Author: ");
			this.setAuthor(conIn.readLine());

			System.out.print("Title: ");
			this.setBookTitle(conIn.readLine());

			System.out.print("Year Published: ");
			this.setYearPublished(conIn.readLine());

			int numTracks = 0;
			int chapter = 0;
			while (chapter <= 1 || numTracks != 0) {
				System.out.print("Number of tracks for chapter " + chapter
						+ ": ");
				numTracks = Integer.parseInt(conIn.readLine());

				if (numTracks > 0) {
					this.setChapterTracks(chapter, numTracks);
					System.out.print("Name of chapter " + chapter + ": ");
					this.setChapterName(chapter, conIn.readLine());
				} else if (chapter == 0 && numTracks == 0) {
					this.setChapterTracks(chapter, 0);
					this.setChapterName(chapter, "");
				}
				chapter++;
			}
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String getAuthor() {
		return author;
	}

	public String getBookTitle() {
		return book_title;
	}

	public int getChapterTracks(int chapter) {
		return chapterTracks.get(chapter);
	}

	// TODO: way to combine this with getTrackTitle()
	public String getFilename(int targetTrackNumber) {
		for (int chapter = 0; chapter < chapterCount; chapter++) {
			if (chapterTracksCumSum.get(chapter) >= targetTrackNumber) {
				NumberFormat nbrfmt = NumberFormat.getIntegerInstance();
				nbrfmt.setGroupingUsed(false);
				nbrfmt.setMinimumIntegerDigits(Integer
						.toString(getTrackCount()).length());
				String retval = nbrfmt.format(targetTrackNumber) + " ";
				retval += getTrackTitle(targetTrackNumber);
				retval += ".mp3";
				return retval;
			}
		}
		return null;
	}

	public int getTrackCount() {
		return trackCount;
	}

	// TODO: way to combine this with getFilename()
	public String getTrackTitle(Integer targetTrackNumber) {
		for (int chapter = 0; chapter < chapterCount; chapter++) {
			if (chapterTracksCumSum.get(chapter) >= targetTrackNumber) {
				NumberFormat nbrfmt = NumberFormat.getIntegerInstance();
				nbrfmt.setGroupingUsed(false);
				nbrfmt.setMinimumIntegerDigits(Integer.toString(chapterCount)
						.length());
				String retval = "Ch" + nbrfmt.format(chapter);
				int offsetFromLastChapter = 0;
				if (chapter > 0) {
					offsetFromLastChapter = targetTrackNumber
							- chapterTracksCumSum.get(chapter - 1);
				} else {
					offsetFromLastChapter = targetTrackNumber;
				}
				char offsetLetter = (char) ('a' + offsetFromLastChapter - 1);
				retval += offsetLetter + " ";
				retval += chapterNames.get(chapter);
				return retval;
			}
		}
		return null;
	}

	public String getYearPublished() {
		return yearPublished;
	}

	public void setAuthor(String newAuthor) {
		author = newAuthor;
	}

	public void setBookTitle(String newTitle) {
		book_title = newTitle;
	}

	public void setChapterName(int chapter, String name) {
		chapterNames.add(chapter, name);
	}

	public void setChapterTracks(int chapter, int numTracks) {
		try {
			if (chapterTracks.get(chapter) != null) {
				trackCount -= chapterTracks.get(chapter);
			}
		} catch (IndexOutOfBoundsException e) {
		}
		chapterTracks.add(chapter, new Integer(numTracks));
		trackCount += numTracks;
		chapterTracksCumSum.add(chapter, new Integer(trackCount));
		chapterCount = chapterTracks.size();
	}

	public void setYearPublished(String newYear) {
		yearPublished = newYear;
	}

	@Override
	public String toString() {
		return book_title + " by " + author + ": " + trackCount + " tracks";
	}
}
