package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.ArgumentEntity;
import com.github.langion.form.MethodEntity;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.consts.Kind;

public class ConstructorCreator extends EntityCreator<ConstructorDeclaration, MethodEntity> {

	private Constructor<?> constructor;
	private Class<?> clazz;

	public ConstructorCreator(Optional<ConstructorDeclaration> node, Constructor<?> constructor, Class<?> clazz,
			Explorer explorer) {
		super(node, explorer.elicitCanonical(constructor), Kind.Constructor, explorer, new MethodEntity());
		this.constructor = constructor;
		this.clazz = clazz;
	}

	@Override
	public void decode() {
		this.parseAnnotations();
		this.parseVariables();
		this.parseModifiers();
		this.parseReturns();
		this.parseArguments();
	}

	private void parseAnnotations() {
		Optional<Annotation[]> optionalAnnotations = this.getDeclaredAnnotations(this.constructor);

		if (!optionalAnnotations.isPresent()) {
			return;
		}

		Annotation[] annotations = optionalAnnotations.get();

		Stream.of(annotations).map(a -> this.parseAnnotation(a)).forEach(f -> this.entity.Annotations.put(f.Name, f));
	}

	private AnnotationEntity parseAnnotation(Annotation annotation) {
		Optional<AnnotationExpr> expr = this.getAnnotationByClassFromConstructor(annotation, this.node);
		AnnotationEntity result = this.explorer.make(annotation, expr);
		return result;
	}

	private void parseVariables() {
		Optional<TypeVariable<?>[]> optionalTypeParameters = this.getTypeParameters(this.constructor);

		if (!optionalTypeParameters.isPresent()) {
			return;
		}

		TypeVariable<?>[] parameters = optionalTypeParameters.get();

		for (int i = 0; i < parameters.length; i++) {
			TypeVariable<?> variable = parameters[i];
			VariableEntity entity = this.explorer.make(variable, i);
			this.entity.Variables.put(entity.Name, entity);
		}
	}

	private void parseModifiers() {
		Optional<Integer> optionalModifiers = this.getModifiers(this.constructor);

		if (!optionalModifiers.isPresent()) {
			return;
		}

		Integer modifiers = optionalModifiers.get();
		this.entity.Modifier = this.explorer.make(modifiers);
	}

	private void parseReturns() {

		this.entity.Returns = this.explorer.make(this.clazz);

	}

	private void parseArguments() {
		Optional<Parameter[]> optionalParameters = this.getParameters(this.constructor);

		if (!optionalParameters.isPresent()) {
			return;
		}

		Parameter[] params = optionalParameters.get();

		for (Integer pos = 0; pos < params.length; pos++) {
			Optional<com.github.javaparser.ast.body.Parameter> dec = this.getParameter(this.constructor, this.node,
					pos);

			ArgumentEntity param = this.explorer.make(params[pos], pos, dec);
			this.entity.Arguments.put(param.Name, param);
		}
	}

}
