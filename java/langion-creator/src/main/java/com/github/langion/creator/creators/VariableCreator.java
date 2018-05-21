package com.github.langion.creator.creators;

import java.lang.reflect.TypeVariable;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.consts.Kind;

public class VariableCreator extends EntityCreator<Node, VariableEntity> {

	public VariableCreator(TypeVariable<?> variable, Integer position, Explorer explorer) {
		super(explorer.elicitCanonical(variable), Kind.Variable, explorer, new VariableEntity());
		this.entity.Position = position;
	}

	@Override
	public void decode() {

	}

}
