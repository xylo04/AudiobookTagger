package com.xylo04.audiobooktagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class ABTMain {

	private static Logger log;

	private static AudiobookInfo audiobookQuestionaire() {
		AudiobookInfo abi = new AudiobookInfo();
		BufferedReader conIn = new BufferedReader(new InputStreamReader(
				System.in));

		try {
			System.out.print("Author: ");
			abi.setAuthor(conIn.readLine());

			System.out.print("Title: ");
			abi.setTitle(conIn.readLine());

			System.out.print("Year Published: ");
			abi.setYearPublished(conIn.readLine());

			int numTracks = 0;
			int chapter = 0;
			while (chapter < 1 || numTracks != 0) {
				System.out.print("Number of tracks for chapter " + chapter
						+ ": ");
				numTracks = Integer.parseInt(conIn.readLine());

				if (numTracks > 0) {
					abi.setChapterTracks(chapter, numTracks);
					System.out.print("Name of chapter " + chapter + ": ");
					abi.setChapterName(chapter, conIn.readLine());
				}
				chapter++;
			}
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			System.exit(1);
		}
		return abi;
	}

	private static DirectoryStructure getMp3Files(String directory) {
		DirectoryStructure ds = new DirectoryStructure();
		FileFilter mp3Filter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".mp3");
			}
		};
		ds.scanDirectory(new File(directory), mp3Filter);
		return ds;
	}

	public static void main(String[] args) {
		Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
		log = Logger.getLogger("com.xylo04.audiobooktagger");
		log.setLevel(Level.FINEST);

		String directory = parseArgs(args);
		DirectoryStructure ds = getMp3Files(directory);
		System.out.println("Found " + ds.getNumFiles()
				+ " MP3 files under directory " + directory);

		AudiobookInfo abi = audiobookQuestionaire();
		System.out.println(abi.toString());
	}

	private static String parseArgs(String[] args) {
		if (args.length == 0) {
			System.out.println("usage:  java ABTMain <directory>");
			System.exit(1);
		}
		String directory = args[0];
		return directory;
	}

	private static void printTags(File f) {
		AudioFile af = null;
		try {
			af = AudioFileIO.read(f);
		} catch (Exception e) {
			log.warning(e.getMessage());
			System.exit(1);
		}
		Tag tag = af.getTag();
		System.out.print(f.getName() + "\t");
		System.out.print(tag.getFirst(FieldKey.TITLE) + "\t");
		System.out.print(tag.getFirst(FieldKey.ARTIST) + "\t");
		System.out.print(tag.getFirst(FieldKey.ALBUM) + "\n");
	}
}
