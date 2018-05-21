package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.FieldEntity;
import com.github.langion.form.consts.Kind;

public class FieldCreator extends EntityCreator<FieldDeclaration, FieldEntity> {

	private Field field;

	public FieldCreator(Optional<FieldDeclaration> node, Field field, Explorer explorer) {
		super(node, explorer.elicitCanonical(field), Kind.Field, explorer, new FieldEntity());
		this.field = field;
	}

	@Override
	public void decode() {
		this.parseType();
		this.parseModifiers();
		this.parseAnnotations();
	}

	private void parseType() {
		Type generic = null;
		Class<?> type = null;

		Optional<Type> optionalGeneric = this.getGenericType(this.field);

		if (optionalGeneric.isPresent()) {
			generic = optionalGeneric.get();
		}

		Optional<Class<?>> optionaltype = this.getType(this.field);

		if (optionaltype.isPresent()) {
			type = optionaltype.get();
		}

		this.entity.Type = this.explorer.make(generic, type);
	}

	private void parseModifiers() {
		Optional<Integer> optionalModifiers = this.getModifiers(this.field);

		if (!optionalModifiers.isPresent()) {
			return;
		}

		Integer modifiers = optionalModifiers.get();
		this.entity.Modifiers = this.explorer.make(modifiers);
	}

	private void parseAnnotations() {
		Optional<Annotation[]> optionalAnnotations = this.getDeclaredAnnotations(this.field);

		if (!optionalAnnotations.isPresent()) {
			return;
		}

		Annotation[] annotations = optionalAnnotations.get();

		Stream.of(annotations).map(a -> this.parseAnnotation(a)).forEach(f -> this.entity.Annotations.put(f.Name, f));
	}

	private AnnotationEntity parseAnnotation(Annotation annotation) {
		Optional<AnnotationExpr> expr = this.getAnnotationByClassFromFields(annotation, this.node);
		AnnotationEntity result = this.explorer.make(annotation, expr);
		return result;
	}

}
