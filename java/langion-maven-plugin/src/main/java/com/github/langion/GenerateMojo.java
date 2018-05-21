package com.github.langion;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.langion.creator.Configuration;
import com.github.langion.creator.Creator;


@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateMojo extends AbstractMojo {

	@Parameter(required = false)
	private List<String> srcBaseJavaFolders;

	@Parameter(required = true)
	private List<String> pattern;

	@Parameter(required = true)
	private String outFileName;

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	public void execute() throws MojoExecutionException {

		try {
			Configuration config = this.createConfiguration();
			Creator parser = new Creator(config);

			ClassLoader classLoader = this.createClassLoader();
			parser.create(classLoader);
		} catch (IOException | DependencyResolutionRequiredException e) {
			e.printStackTrace();
		}

	}

	private Configuration createConfiguration() {
		Configuration config = new Configuration();

		if (this.srcBaseJavaFolders != null && this.srcBaseJavaFolders.size() > 0) {
			config.srcBaseJavaFolders = Optional.of(this.srcBaseJavaFolders);
		}

		config.outFileName = this.outFileName;
		config.pattern = this.pattern;

		return config;
	}

	private ClassLoader createClassLoader() throws MalformedURLException, DependencyResolutionRequiredException {
		List<URL> list = new ArrayList<URL>();

		for (Object element : this.project.getCompileClasspathElements()) {
			String path = (String) element;
			File file = new File(path);
			list.add(file.toURI().toURL());
		}

		URL[] urls = new URL[list.size()];
		urls = list.toArray(urls);

		URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());

		return classLoader;
	}
}
