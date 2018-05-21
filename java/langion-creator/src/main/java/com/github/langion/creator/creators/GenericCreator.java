package com.github.langion.creator.creators;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.GenericEntity;
import com.github.langion.form.consts.Kind;

public class GenericCreator extends EntityCreator<Node, GenericEntity> {

	private Type generic;

	public GenericCreator(Type generic, Integer position, Explorer explorer) {
		super(explorer.elicitCanonical(generic), Kind.Generic, explorer, new GenericEntity());
		this.generic = generic;
		this.entity.Position = position;
	}

	@Override
	public void decode() {

		if (this.generic instanceof TypeVariable<?>) {
			TypeVariable<?> variable = (TypeVariable<?>) this.generic;
			this.entity.Variable = this.explorer.make(variable, this.entity.Position);
			this.entity.IsParameter = true;
		} else if (this.generic instanceof WildcardType) {
			WildcardType wildcard = (WildcardType) this.generic;
			this.entity.Wildcard = this.explorer.make(wildcard);
		} else {
			this.entity.Type = this.explorer.make(this.generic);
		}
	}

}
