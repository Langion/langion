package com.github.langion.creator.creators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.json.JSONObject;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Configuration;
import com.github.langion.creator.Explorer;
import com.github.langion.form.Langion;
import com.github.langion.form.ModuleEntity;
import com.github.langion.form.consts.Kind;

public class LangionCreator extends EntityCreator<Node, Langion> {

	private List<Class<?>> clazzes;
	private Explorer explorer;

	public LangionCreator(List<Class<?>> clazzes, Configuration config) {
		super("Java", Kind.Langion, null, new Langion());
		this.clazzes = clazzes;
		this.explorer = new Explorer(this, config);
	}

	@Override
	public void decode() {
		this.clazzes.stream().forEach(c -> this.explorer.make(c));
	}

	public ModuleEntity getModuleEntity(Class<?> clazz) {
		Package pack = clazz.getPackage();

		String name = pack.getName();
		ModuleEntity entity = this.getModuleEntity(name);
		return entity;
	}

	public ModuleEntity getModuleEntity(String path) {
		String[] parts = path.split("\\.");
		String rootName = parts[0];

		ModuleEntity root = this.entity.Modules.get(rootName);

		if (root == null) {
			ModuleCreator creator = new ModuleCreator(rootName, this.explorer);
			creator.decode();
			root = creator.entity;
			this.entity.Modules.put(rootName, root);
		}

		ModuleEntity result = root;

		ArrayList<String> canonical = new ArrayList<String>();

		for (String part : parts) {
			canonical.add(part);

			if (part.equals(rootName)) {
				continue;
			}

			ModuleEntity child = null;
			Optional<ModuleEntity> first = result.Modules.values().stream().filter(n -> part.equals(n.Name))
					.findFirst();

			if (first.isPresent()) {
				child = first.get();
			} else {
				String name = String.join(".", canonical);
				ModuleCreator creator = new ModuleCreator(name, this.explorer);
				creator.decode();
				child = creator.entity;
				result.Modules.put(child.Name, child);
			}

			result = child;

		}

		return result;
	}

	public Stream<Class<?>> getClassFromPackageName(String packageName) {
		Stream<Class<?>> result = this.clazzes.stream().filter(v -> v.getPackage().getName().equals(packageName));
		return result;
	}

	@Override
	public String toString() {
		JSONObject json = this.toJson();
		String result = json.toString(2);
		return result;
	}

}
