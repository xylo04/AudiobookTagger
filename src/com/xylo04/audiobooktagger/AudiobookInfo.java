package com.xylo04.audiobooktagger;

import java.util.ArrayList;
import java.util.List;

public class AudiobookInfo {

	private String author;
	private List<String> chapterNames = new ArrayList<String>();
	private List<Integer> chapterTracks = new ArrayList<Integer>();
	private String title;
	private int trackCount = 0;
	private String yearPublished;

	public String getAuthor() {
		return author;
	}

	public int getChapterTracks(int chapter) {
		return chapterTracks.get(chapter);
	}

	public String getTitle() {
		return title;
	}

	public String getYearPublished() {
		return yearPublished;
	}

	public void setAuthor(String newAuthor) {
		author = newAuthor;
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
	}

	public void setTitle(String newTitle) {
		title = newTitle;
	}

	public void setYearPublished(String newYear) {
		yearPublished = newYear;
	}

	@Override
	public String toString() {
		return title + " by " + author + ": " + trackCount + " tracks";
	}
}
