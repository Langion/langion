package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class MethodEntity extends Entity {

	public ModifierEntity Modifier;
	public TypeEntity Returns;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();
	public Map<String, ArgumentEntity> Arguments = new HashMap<String, ArgumentEntity>();
	public Map<String, VariableEntity> Variables = new HashMap<String, VariableEntity>();

}
