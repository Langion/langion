package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.ArgumentEntity;
import com.github.langion.form.consts.Kind;

public class ArgumentCreator extends EntityCreator<Parameter, ArgumentEntity> {

	private java.lang.reflect.Parameter parameter;

	public ArgumentCreator(Optional<Parameter> node, java.lang.reflect.Parameter parameter, Integer position,
			Explorer explorer) {
		super(node, explorer.elicitCanonical(parameter), Kind.Argument, explorer, new ArgumentEntity());
		this.parameter = parameter;
		this.entity.Position = position;
	}

	@Override
	public void decode() {
		this.parseType();
		this.parseAnnotations();
	}

	private void parseType() {
		Optional<Type> optionalType = this.getParameterizedType(this.parameter);

		if (!optionalType.isPresent()) {
			return;
		}

		Type type = optionalType.get();
		this.entity.Type = this.explorer.make(type);
	}

	private void parseAnnotations() {
		Optional<Annotation[]> optionalAnnotations = this.getDeclaredAnnotations(this.parameter);

		if (!optionalAnnotations.isPresent()) {
			return;
		}

		Annotation[] annotations = optionalAnnotations.get();

		Stream.of(annotations).map(a -> this.parseAnnotation(a)).forEach(f -> this.entity.Annotations.put(f.Name, f));
	}

	private AnnotationEntity parseAnnotation(Annotation annotation) {
		Optional<AnnotationExpr> expr = this.getAnnotationByClass(annotation, this.node);
		AnnotationEntity result = this.explorer.make(annotation, expr);
		return result;
	}

}
