package com.xylo04.audiobooktagger;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class ABTMain {

	/**
	 * @param args
	 * @throws InvalidAudioFrameException
	 * @throws ReadOnlyFileException
	 * @throws TagException
	 * @throws IOException
	 * @throws CannotReadException
	 */
	public static void main(String[] args) throws CannotReadException,
			IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {
		File f = new File("06 - Mad World.mp3");
		AudioFile af = AudioFileIO.read(f);
		Tag tag = af.getTag();
		System.out.println("Title: " + tag.getFirst(FieldKey.TITLE));
		System.out.println("Artist: " + tag.getFirst(FieldKey.ARTIST));
		System.out.println("Album: " + tag.getFirst(FieldKey.ALBUM));

	}

}
