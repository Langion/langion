package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.ArgumentEntity;
import com.github.langion.form.MethodEntity;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.consts.Kind;

public class MethodCreator extends EntityCreator<MethodDeclaration, MethodEntity> {

	private Method method;

	public MethodCreator(Optional<MethodDeclaration> node, Method method, Explorer explorer) {
		super(node, explorer.elicitCanonical(method), Kind.Method, explorer, new MethodEntity());
		this.method = method;
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
		Optional<Annotation[]> optionalAnnotations = this.getDeclaredAnnotations(this.method);

		if (!optionalAnnotations.isPresent()) {
			return;
		}

		Annotation[] annotations = optionalAnnotations.get();

		Stream.of(annotations).map(a -> this.parseAnnotation(a)).forEach(f -> this.entity.Annotations.put(f.Name, f));
	}

	private AnnotationEntity parseAnnotation(Annotation annotation) {
		Optional<AnnotationExpr> expr = this.getAnnotationByClassFromMethod(annotation, this.node);
		AnnotationEntity result = this.explorer.make(annotation, expr);
		return result;
	}

	private void parseVariables() {
		Optional<TypeVariable<?>[]> optionalTypeParameters = this.getTypeParameters(this.method);

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
		Optional<Integer> optionalModifiers = this.getModifiers(this.method);

		if (!optionalModifiers.isPresent()) {
			return;
		}

		Integer modifiers = optionalModifiers.get();
		this.entity.Modifier = this.explorer.make(modifiers);
	}

	private void parseReturns() {
		Type generic = null;
		Class<?> type = null;

		Optional<Type> optionalGeneric = this.getGenericReturnType(this.method);

		if (optionalGeneric.isPresent()) {
			generic = optionalGeneric.get();
		}

		Optional<Class<?>> optionaltype = this.getReturnType(this.method);

		if (optionaltype.isPresent()) {
			type = optionaltype.get();
		}

		this.entity.Returns = this.explorer.make(generic, type);
	}

	private void parseArguments() {
		Optional<Parameter[]> optionalParameters = this.getParameters(this.method);

		if (!optionalParameters.isPresent()) {
			return;
		}

		Parameter[] params = optionalParameters.get();

		for (Integer pos = 0; pos < params.length; pos++) {
			Optional<com.github.javaparser.ast.body.Parameter> dec = this.getParameter(this.method, this.node, pos);

			ArgumentEntity param = this.explorer.make(params[pos], pos, dec);
			this.entity.Arguments.put(param.Name, param);
		}
	}

}
