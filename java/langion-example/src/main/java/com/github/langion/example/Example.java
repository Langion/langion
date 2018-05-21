package com.github.langion.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.langion.creator.Configuration;
import com.github.langion.creator.Creator;
import com.github.langion.creator.creators.LangionCreator;

/**
 * test
 * 
 * @author Dmitrii_Pikulin
 *
 */
public class Example {

	/**
	 * MyOne
	 */
	public List<String> one;

	public static void main(String[] args) throws IOException {
		Example example = new Example();
		example.parse();
	}

	/**
	 * SuperParse
	 * 
	 * @throws IOException
	 */
	public void parse() throws IOException {
		Configuration config = new Configuration();

		String targetClassesPath = this.getClass().getClassLoader().getResource("").getPath();
		String path = targetClassesPath + "../../src\\main\\java";
		File file = new File(path);

		List<String> srcBaseJavaFolders = new ArrayList<String>();
		srcBaseJavaFolders.add(file.getCanonicalPath());

		config.srcBaseJavaFolders = Optional.of(srcBaseJavaFolders);
		config.outFileName = "example.json";
		config.pattern = new ArrayList<String>();
		config.pattern.add("com.github.langion.example.Test");

		Creator parser = new Creator(config);
		ClassLoader context = Thread.currentThread().getContextClassLoader();
		LangionCreator result = parser.create(context);
		System.out.println(result.toString());
	}

}
