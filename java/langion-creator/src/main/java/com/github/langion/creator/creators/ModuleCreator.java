package com.github.langion.creator.creators;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.ModuleEntity;
import com.github.langion.form.consts.Kind;

public class ModuleCreator extends EntityCreator<Node, ModuleEntity> {

	public ModuleCreator(String part, Explorer explorer) {
		super(part, Kind.Package, explorer, new ModuleEntity());
	}

	@Override
	public void decode() {

	}

}
