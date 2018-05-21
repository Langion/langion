package com.github.langion.creator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.github.langion.creator.creators.LangionCreator;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class Creator {

	private Configuration config;

	public Creator(Configuration config) {
		this.config = config;
	}

	public LangionCreator create(ClassLoader clazzLoader) throws IOException {
		ClassPath context = ClassPath.from(clazzLoader);
		List<Class<?>> clazzes = new ArrayList<Class<?>>();

		for (ClassInfo clazzInfo : context.getAllClasses()) {
			String name = clazzInfo.getName();

			if (this.isMatched(name)) {
				try {
					Class<?> clazz = clazzInfo.load();
					clazzes.add(clazz);
				} catch (Throwable e) {
					String message = e.toString();
					System.err.println("Error: " + message + " while loading: " + name);
				}
			}
		}

		LangionCreator langion = new LangionCreator(clazzes, this.config);
		langion.decode();
		this.writeToFile(langion);

		return langion;
	}

	private void writeToFile(LangionCreator langion) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(this.config.outFileName, "UTF-8");
		String content = langion.toString();
		writer.println(content);
		writer.close();
	}

	private boolean isMatched(final String name) {
		return this.config.pattern.stream().anyMatch(p -> FilenameUtils.wildcardMatch(name, p));
	}

}
