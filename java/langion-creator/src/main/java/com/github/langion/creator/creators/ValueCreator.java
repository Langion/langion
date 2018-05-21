package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.ValueEntity;
import com.github.langion.form.consts.Kind;

public class ValueCreator extends EntityCreator<Node, ValueEntity> {

	public ValueCreator(Object result, Explorer explorer) {
		super(explorer.elicitCanonical(result), Kind.Value, explorer, new ValueEntity());
		this.entity.Content = result;
	}

	@Override
	public void decode() {
		Type type;

		if (this.entity.Content instanceof Type) {
			type = (Type) this.entity.Content;
		} else if (this.entity.Content instanceof Annotation) {
			Annotation annotation = (Annotation) this.entity.Content;
			type = annotation.annotationType();
		} else {
			type = this.entity.Content.getClass();
		}

		this.entity.Type = this.explorer.make(type);
	}

}
