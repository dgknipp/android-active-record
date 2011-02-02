package org.kroz.activerecord.annotations;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordTestCase;

import android.test.AndroidTestCase;

public class ManyToManyRelationshipTest extends ActiveRecordTestCase {
	
	private static class Recipe extends ActiveRecordBase{
		public String name;
		@ManyToManyRelation(
				target=Ingredient.class, 
				joinTableName="RECIPE_INGREDIENTS")
		List<Ingredient> ingredients;
	}
	
	private static class Ingredient extends ActiveRecordBase{
		@ManyToManyRelation(
				target=Recipe.class,
				joinTableName="RECIPE_INGREDIENTS")
		List<Recipe> recipes;
		public List<Recipe> getRecipes(){ return recipes; }
		public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; }
		
		public String name;
		public String amount;
	}
	
	private static Class[] classes = {Ingredient.class, Recipe.class};
	
	public void setUp() throws Exception {
		super.setUp(classes);
	}
	
	
	
	

}
