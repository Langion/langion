package com.github.langion.creator.creators;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.langion.creator.Explorer;
import com.github.langion.creator.SafeAccess;
import com.github.langion.form.Entity;
import com.github.langion.form.consts.Kind;
import com.google.common.base.CaseFormat;

public abstract class EntityCreator<T extends Node, E extends Entity> implements SafeAccess {

	public E entity;

	protected Optional<T> node;
	protected Explorer explorer;

	public EntityCreator(Optional<T> node, String canonical, Kind kind, Explorer explorer, E entity) {
		this.explorer = explorer;
		this.entity = entity;
		this.entity.Kind = kind;
		this.entity.Path = this.getPath(canonical);
		this.entity.Name = this.getPrettyName(canonical);
		this.entity.Canonical = canonical;

		if (node == null) {
			this.node = Optional.empty();
		} else {
			this.node = node;
		}

		this.fillComments();
	}

	public EntityCreator(String canonical, Kind kind, Explorer explorer, E entity) {
		this(Optional.<T>empty(), canonical, kind, explorer, entity);
	}

	public abstract void decode();

	public JSONObject toJson() {
		JSONObject json = this.toJson(this.entity);
		return json;
	}

	public JSONObject toJson(Entity entity) {
		JSONObject json = new JSONObject();

		Class<? extends Entity> clazz = entity.getClass();
		Field[] fields = clazz.getFields();

		for (Field field : fields) {
			try {
				String key = field.getName();
				Object value = field.get(entity);

				if (value instanceof Entity) {
					JSONObject converted = this.toJson((Entity) value);
					json.put(key, converted);
				} else if (value instanceof List<?>) {
					List<?> listValue = (List<?>) value;
					List<JSONObject> converted = this.listToJson(listValue);
					json.put(key, converted);
				} else if (value instanceof Map<?, ?>) {
					JSONObject map = this.mapToJson((Map<?, ?>) value);
					json.put(key, map);
				} else {
					json.put(key, value);
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return json;
	}

	private void fillComments() {
		if (this.node.isPresent()) {
			T node = this.node.get();
			Optional<Comment> optionalComment = node.getComment();

			Boolean isPresent = optionalComment.isPresent();

			if (isPresent) {
				Comment comment = optionalComment.get();
				this.entity.Comment = comment.toString();
			}
		}

	};

	private JSONObject mapToJson(Map<?, ?> map) {
		JSONObject jsonMap = new JSONObject();

		map.keySet().stream().forEach(k -> {
			Object mapValue = map.get(k);

			if (mapValue instanceof Entity) {
				Entity entityValue = (Entity) mapValue;
				JSONObject converted = this.toJson(entityValue);
				jsonMap.put(k.toString(), converted);
			} else if (mapValue instanceof List<?>) {
				List<?> listValue = (List<?>) mapValue;
				List<JSONObject> converted = this.listToJson(listValue);
				jsonMap.put(k.toString(), converted);
			} else {
				jsonMap.put(k.toString(), mapValue);
			}

		});

		return jsonMap;
	}

	private List<JSONObject> listToJson(List<?> listValue) {
		List<JSONObject> convertedList = new ArrayList<JSONObject>();

		listValue.forEach(v -> {
			if (v instanceof Entity) {
				Entity entityValue = (Entity) v;
				JSONObject converted = this.toJson(entityValue);
				convertedList.add(converted);
			}
		});

		return convertedList;
	}

	private String getPrettyName(Object name) {
		if (name == null) {
			return "";
		}

		String path = this.getPath(name);
		String[] pathName = path.split("\\.");
		String entityName = pathName[pathName.length - 1];

		Integer dashPosition = entityName.indexOf("-");

		if (dashPosition >= 0) {
			entityName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, entityName);
		}

		return entityName;
	}

	private String getPath(Object name) {
		if (name == null) {
			return "";
		}

		String withoutArray = name.toString().replaceFirst("\\[.*", "");
		String withoutGeneric = withoutArray.replaceFirst("<.*", "");

		return withoutGeneric;
	}

}
