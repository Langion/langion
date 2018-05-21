package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.ValueEntity;
import com.github.langion.form.consts.Kind;

public class AnnotationCreator extends EntityCreator<AnnotationExpr, AnnotationEntity> {

	private Annotation annotation;

	public AnnotationCreator(Optional<AnnotationExpr> node, Annotation annotation, Explorer explorer) {
		super(node, explorer.elicitCanonical(annotation), Kind.Annotation, explorer, new AnnotationEntity());
		this.annotation = annotation;
	}

	@Override
	public void decode() {
		Optional<Class<? extends Annotation>> clazz = this.getAnnotationType(this.annotation);

		if (clazz.isPresent()) {
			Class<? extends Annotation> value = clazz.get();
			this.parseClazz(value);
		}
	}

	private void parseClazz(Class<? extends Annotation> clazz) {
		Optional<Method[]> methods = this.getDeclaredMethods(clazz);

		if (methods.isPresent()) {
			Method[] value = methods.get();
			this.parseMethods(value);
		}
	}

	private void parseMethods(Method[] methods) {
		for (Method method : methods) {
			Integer parameterCount = method.getParameterCount();

			if (parameterCount > 0) {
				continue;
			}

			try {
				String name = method.getName();
				method.setAccessible(true);

				Object result = method.invoke(this.annotation);
				ValueEntity value = this.explorer.make(result);

				this.entity.Items.put(name, value);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
