package com.xylo04.audiobooktagger;

public abstract class AudiobookInfoFiller {

	protected AudiobookInfo audiobook;

	public AudiobookInfoFiller(AudiobookInfo abi) {
		audiobook = abi;
	}

	public abstract void fillAudiobook();
}
