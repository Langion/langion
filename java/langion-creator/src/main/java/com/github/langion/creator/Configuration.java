package com.github.langion.creator;

import java.util.List;
import java.util.Optional;

public class Configuration {

	public String outFileName;
	public List<String> pattern;
	public Optional<List<String>> srcBaseJavaFolders = Optional.empty();

}
