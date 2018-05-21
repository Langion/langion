package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class WildcardEntity extends Entity {

	public Map<Integer, GenericEntity> Lower = new HashMap<Integer, GenericEntity>();
	public Map<Integer, GenericEntity> Upper = new HashMap<Integer, GenericEntity>();

}
