package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class TypeEntity extends Entity {

	public Boolean IsArray = false;
	public Boolean IsParameter = false;
	public Map<Integer, GenericEntity> Generics = new HashMap<Integer, GenericEntity>();

}
