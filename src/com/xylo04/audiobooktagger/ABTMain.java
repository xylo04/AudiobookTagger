package com.xylo04.audiobooktagger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class ABTMain {

	private static Logger log;

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

		// Recursively scan the directory for MP3's
		String directory = parseArgs(args);
		DirectoryStructure ds = getMp3Files(directory);
		System.out.println("Found " + ds.getNumFiles()
				+ " MP3 files under directory " + directory);

		// Ask user to enter audiobook metadata, repeat if number of tracks
		// doesn't match directory scan
		AudiobookInfo abi = new AudiobookInfo();
		do {
			abi.audiobookQuestionaire();
			if (abi.getTrackCount() != ds.getNumFiles()) {
				System.out
						.println("Track numbers didn't match! Please try again.");
			}
		} while (abi.getTrackCount() != ds.getNumFiles());
		System.out.println(abi.toString());

		// Confirm renaming procedure
		List<String> newFilenames = new ArrayList<String>();
		System.out
				.println("WARNING: The following changes are about to take place:");
		int track = 1;
		for (File f : ds.getFiles()) {
			newFilenames.add(abi.getFilename(track));
			System.out.println(f.getAbsoluteFile() + " ==> "
					+ newFilenames.get(track-1));
			track++;
		}
		System.out.println("Is this OK?");
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
		System.out.print(f.getAbsoluteFile() + "\t");
		System.out.print(tag.getFirst(FieldKey.TITLE) + "\t");
		System.out.print(tag.getFirst(FieldKey.ARTIST) + "\t");
		System.out.print(tag.getFirst(FieldKey.ALBUM) + "\n");
	}
}
