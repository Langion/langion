package com.github.langion.creator.creators;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.GenericEntity;
import com.github.langion.form.WildcardEntity;
import com.github.langion.form.consts.Kind;

public class WildcardCreator extends EntityCreator<Node, WildcardEntity> {

	private WildcardType wildcard;

	public WildcardCreator(WildcardType wildcard, Explorer explorer) {
		super(explorer.elicitCanonical(wildcard), Kind.Wildcard, explorer, new WildcardEntity());
		this.wildcard = wildcard;
	}

	@Override
	public void decode() {
		try {
			Type[] upper = this.wildcard.getUpperBounds();
			Type[] lower = this.wildcard.getLowerBounds();

			for (Integer i = 0; i < upper.length; i++) {
				GenericEntity entity = this.explorer.make(upper[i], i);
				this.entity.Upper.put(i, entity);
			}

			for (Integer i = 0; i < lower.length; i++) {
				GenericEntity entity = this.explorer.make(lower[i], i);
				this.entity.Lower.put(i, entity);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
