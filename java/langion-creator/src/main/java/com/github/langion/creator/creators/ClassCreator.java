package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.ClassEntity;
import com.github.langion.form.FieldEntity;
import com.github.langion.form.MethodEntity;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.consts.Kind;

public class ClassCreator extends EntityCreator<ClassOrInterfaceDeclaration, ClassEntity> {

	private Class<?> clazz;

	public ClassCreator(Optional<ClassOrInterfaceDeclaration> node, Class<?> clazz, Explorer explorer) {
		super(node, explorer.elicitCanonical(clazz), Kind.Class, explorer, new ClassEntity());
		this.clazz = clazz;
	}

	@Override
	public void decode() {
		this.parseModifiers();
		this.parseExtends();
		this.parseImplements();
		this.parseAnnotations();
		this.parseVariables();
		this.parseFields();
		this.parseMethods();
		this.parseConstructors();
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
		try {
			Optional<Type> optionalParent = this.getGenericSuperclass(this.clazz);

			if (!optionalParent.isPresent()) {
				return;
			}

			Type parent = optionalParent.get();

			String parentName = parent.toString();
			Boolean isObject = parentName.contains("class java.lang.Object");

			if (parent != null && !isObject) {
				this.entity.Extends = this.explorer.make(parent);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void parseImplements() {
		Optional<Type[]> optionalGenericInterfaces = this.getGenericInterfaces(this.clazz);

		if (!optionalGenericInterfaces.isPresent()) {
			return;
		}

		Type[] genericInterfaces = optionalGenericInterfaces.get();

		Stream.of(genericInterfaces).map(i -> this.explorer.make(i))
				.forEach(f -> this.entity.Implements.put(f.Name, f));
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

	private void parseFields() {
		Optional<Field[]> optionalDeclaredField = this.getDeclaredFields(this.clazz);

		if (!optionalDeclaredField.isPresent()) {
			return;
		}

		Field[] declaredField = optionalDeclaredField.get();

		Stream.of(declaredField).map(f -> this.parseField(f)).forEach(f -> this.entity.Fields.put(f.Name, f));
	}

	private FieldEntity parseField(Field field) {
		Optional<FieldDeclaration> optionalField = this.getFieldByName(field, this.node);
		FieldEntity result = this.explorer.make(field, optionalField);
		return result;
	}

	private void parseMethods() {
		Optional<Method[]> optionalDeclaredMethods = this.getDeclaredMethods(this.clazz);

		if (!optionalDeclaredMethods.isPresent()) {
			return;
		}

		Method[] declaredMethods = optionalDeclaredMethods.get();

		Stream.of(declaredMethods).map(m -> this.parseMethod(m)).forEach(m -> {
			List<MethodEntity> methods = this.entity.Methods.get(m.Name);

			if (methods == null) {
				methods = new ArrayList<MethodEntity>();
				this.entity.Methods.put(m.Name, methods);
			}

			methods.add(m);
		});
	}

	private MethodEntity parseMethod(Method method) {
		Optional<List<MethodDeclaration>> optionalMethods = this.getMethodsByName(method, this.node);
		MethodEntity result = this.explorer.make(method, optionalMethods);
		return result;
	}

	private void parseConstructors() {
		Optional<Constructor<?>[]> optionalDeclaredMethods = this.getDeclaredConstructors(this.clazz);

		if (!optionalDeclaredMethods.isPresent()) {
			return;
		}

		Constructor<?>[] declaredMethods = optionalDeclaredMethods.get();

		Stream.of(declaredMethods).map(m -> this.parseConstructor(m)).forEach(m -> this.entity.Constructors.add(m));
	}

	private MethodEntity parseConstructor(Constructor<?> constructor) {
		Optional<ConstructorDeclaration> optionalConstructor = this.getConstructorByParameterTypes(constructor, this.node);
		MethodEntity result = this.explorer.make(constructor, this.clazz, optionalConstructor);
		return result;
	}

}
