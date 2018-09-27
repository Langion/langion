package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class EnumEntity extends Entity {

	public TypeEntity Extends;
	public ModifierEntity Modifiers;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();
	public Map<String, TypeEntity> Implements = new HashMap<String, TypeEntity>();
	public Map<String, VariableEntity> Variables = new HashMap<String, VariableEntity>();
	public Map<String, String> Items = new HashMap<String, String>();

}
