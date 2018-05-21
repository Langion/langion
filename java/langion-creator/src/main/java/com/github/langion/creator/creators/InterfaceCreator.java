package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.InterfaceEntity;
import com.github.langion.form.MethodEntity;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.consts.Kind;

public class InterfaceCreator extends EntityCreator<ClassOrInterfaceDeclaration, InterfaceEntity> {

	private Class<?> clazz;

	public InterfaceCreator(Optional<ClassOrInterfaceDeclaration> node, Class<?> clazz, Explorer explorer) {
		super(node, explorer.elicitCanonical(clazz), Kind.Interface, explorer, new InterfaceEntity());
		this.clazz = clazz;
	}

	@Override
	public void decode() {
		this.parseAnnotations();
		this.parseMethods();
		this.parseVariables();
		this.parseModifiers();
		this.parseExtends();
	}

	private void parseAnnotations() {
		Optional<Annotation[]> optionalAnnotations = this.getDeclaredAnnotations(this.clazz);

		if (!optionalAnnotations.isPresent()) {
			return;
		}

		Annotation[] annotations = optionalAnnotations.get();

		Stream.of(annotations).map(a -> this.parseAnnotation(a)).forEach(f -> this.entity.Annotations.put(f.Name, f));
	}

	private AnnotationEntity parseAnnotation(Annotation annotation) {
		Optional<AnnotationExpr> expr = this.getAnnotationByClassFromClassOrInterface(annotation, this.node);
		AnnotationEntity result = this.explorer.make(annotation, expr);
		return result;
	}

	private void parseMethods() {
		Optional<Method[]> optionalDeclaredMethods = this.getDeclaredMethods(this.clazz);

		if (!optionalDeclaredMethods.isPresent()) {
			return;
		}

		Method[] declaredMethods = optionalDeclaredMethods.get();

		Stream.of(declaredMethods).map(m -> this.parseMethod(m)).forEach(m -> this.entity.Methods.put(m.Name, m));
	}

	private MethodEntity parseMethod(Method method) {
		Optional<List<MethodDeclaration>> optionalMethods = this.getMethodsByName(method, this.node);
		MethodEntity result = this.explorer.make(method, optionalMethods);
		return result;
	}

	private void parseVariables() {
		Optional<TypeVariable<?>[]> optionalTypeParameters = this.getTypeParameters(this.clazz);

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
		Optional<Integer> optionalModifiers = this.getModifiers(this.clazz);

		if (!optionalModifiers.isPresent()) {
			return;
		}

		Integer modifiers = optionalModifiers.get();
		this.entity.Modifiers = this.explorer.make(modifiers);
	}

	private void parseExtends() {
		Optional<Type[]> optionalParent = this.getGenericInterfaces(this.clazz);

		if (!optionalParent.isPresent()) {
			return;
		}

		Type[] parents = optionalParent.get();

		Stream.of(parents).map(i -> this.explorer.make(i)).forEach(i -> this.entity.Extends.put(i.Name, i));
	}

}
