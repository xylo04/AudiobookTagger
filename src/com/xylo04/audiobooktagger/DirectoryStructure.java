package com.xylo04.audiobooktagger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

public class DirectoryStructure {
	Collection<File> files = new ArrayList<File>();

	public Collection<File> getFiles() {
		return files;
	}

	public int getNumFiles() {
		return files.size();
	}

	public void scanDirectory(File file) {
		scanDirectoryRecursive(file, new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
	}

	public void scanDirectory(File file, FileFilter fileFilter) {
		scanDirectoryRecursive(file, fileFilter);
	}

	private void scanDirectoryRecursive(File file, FileFilter fileFilter) {
		final File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				if (fileFilter.accept(child)) {
					files.add(child);
				}
				scanDirectoryRecursive(child, fileFilter);
			}
		}
	}

}
