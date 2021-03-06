package com.github.langion.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceEntity extends Entity {

	public ModifierEntity Modifiers;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();
	public Map<String, TypeEntity> Extends = new HashMap<String, TypeEntity>();
	public Map<String, List<MethodEntity>> Methods = new HashMap<String, List<MethodEntity>>();
	public Map<String, VariableEntity> Variables = new HashMap<String, VariableEntity>();

}
