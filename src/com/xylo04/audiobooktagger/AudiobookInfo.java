package com.xylo04.audiobooktagger;

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
		String retval = null;
		for (int chapter = 0; chapter < chapterCount && retval == null; chapter++) {
			if (chapterTracksCumSum.get(chapter) >= targetTrackNumber) {
				NumberFormat nbrfmt = NumberFormat.getIntegerInstance();
				nbrfmt.setGroupingUsed(false);
				nbrfmt.setMinimumIntegerDigits(Integer
						.toString(getTrackCount()).length());
				retval = nbrfmt.format(targetTrackNumber) + " ";
				retval += getTrackTitle(targetTrackNumber);
				retval += ".mp3";

			}
		}
		log.finest("Generated filename for track " + targetTrackNumber + " is "
				+ retval);
		return retval;
	}

	public int getTrackCount() {
		return trackCount;
	}

	// TODO: way to combine this with getFilename()
	public String getTrackTitle(Integer targetTrackNumber) {
		String retval = null;
		for (int chapter = 0; chapter < chapterCount && retval == null; chapter++) {
			if (chapterTracksCumSum.get(chapter) >= targetTrackNumber) {
				NumberFormat nbrfmt = NumberFormat.getIntegerInstance();
				nbrfmt.setGroupingUsed(false);
				nbrfmt.setMinimumIntegerDigits(Integer.toString(chapterCount)
						.length());
				retval = "Ch" + nbrfmt.format(chapter);
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
			}
		}
		log.finest("Generated title for track " + targetTrackNumber + " is "
				+ retval);
		return retval;
	}

	public String getYearPublished() {
		return yearPublished;
	}

	public void setAuthor(String newAuthor) {
		log.finest("Setting author " + newAuthor);
		author = newAuthor;
	}

	public void setBookTitle(String newTitle) {
		log.finest("Setting book title " + newTitle);
		book_title = newTitle;
	}

	public void setChapterName(int chapter, String name) {
		log.finest("Setting chapter " + chapter + " name " + name);
		chapterNames.add(chapter, name);
	}

	public void setChapterTracks(int chapter, int numTracks) {
		log.finest("Setting chapter " + chapter + " tracks " + numTracks);
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
		log.finest("Setting year published " + newYear);
		yearPublished = newYear;
	}

	@Override
	public String toString() {
		return book_title + " by " + author + ": " + trackCount + " tracks";
	}
}
