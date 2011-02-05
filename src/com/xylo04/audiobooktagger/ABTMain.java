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
				+ " MP3 files under directory "
				+ ds.getRoot().getAbsolutePath());

		// Ask user to enter audiobook metadata, repeat if number of tracks
		// doesn't match directory scan (should probably be more forgiving)
		AudiobookInfo abi = new AudiobookInfo();
		do {
			abi.audiobookQuestionaire();
			if (abi.getTrackCount() != ds.getNumFiles()) {
				System.out
						.println("Track numbers didn't match! Please try again.\n");
			}
		} while (abi.getTrackCount() != ds.getNumFiles());
		System.out.println(abi.toString());

		// Generate new names
		List<File> newFilenames = new ArrayList<File>();
		for (int i = 0; i < ds.getNumFiles(); i++) {
			newFilenames.add(new File(ds.getRoot().getAbsolutePath()
					+ File.separator + abi.getFilename(i + 1)));
		}

		// Confirm renaming procedure
		System.out
				.println("\nWARNING: The following changes are about to take place:");
		for (int i = 0; i < ds.getNumFiles(); i++) {
			System.out.println(ds.get(i).getAbsolutePath() + " ==> "
					+ newFilenames.get(i).getAbsolutePath());
		}
		// System.out.println("Is this OK?\n");

		// Proceed with renaming
		System.out.println("Transforming files...");
		transformFiles(ds.getFiles(), newFilenames, abi);
		System.out.println("Done.");

	}

	private static String parseArgs(String[] args) {
		if (args.length == 0) {
			System.out.println("usage:  java ABTMain <directory>");
			System.exit(1);
		}
		String directory = args[0];
		return directory;
	}

	private static void transformFiles(List<File> oldFiles,
			List<File> newFiles, AudiobookInfo abi) {
		for (int i = 0; i < oldFiles.size(); i++) {
			oldFiles.get(i).renameTo(newFiles.get(i));
			try {
				AudioFile af = AudioFileIO.read(newFiles.get(i));
				Tag tag = af.getTag();
				tag.setField(FieldKey.ARTIST, abi.getAuthor());
				tag.setField(FieldKey.ALBUM_ARTIST, abi.getAuthor());
				tag.setField(FieldKey.ALBUM, abi.getBookTitle());
				tag.setField(FieldKey.YEAR, abi.getYearPublished());
				tag.setField(FieldKey.TITLE, abi.getTrackTitle(i + 1));
				tag.setField(FieldKey.TRACK, Integer.toString(i + 1));
				tag.setField(FieldKey.GENRE, "Audiobook");
				af.commit();
			} catch (Exception e) {
				log.warning(e.getLocalizedMessage());
			}
		}
	}
}
