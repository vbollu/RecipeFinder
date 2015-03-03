package com.pactera.test;

import java.util.Map;
import java.util.TreeMap;

import com.pactera.test.model.Item;

/**
 * 
 */

/**
 * @author vikram
 *
 */
public class RecipeFinder {

	Map<String, Item> fridgeItemsMap = new TreeMap<String, Item>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length == 0 || args.length == 1) {
			System.out.println("Error: Missing required input file names.");
			return;
		}
		String itemsFileName = args[0];
		String recipesFileName = args[1];
		
		RecipeFinderProcessor processor= new  RecipeFinderProcessor();
		
		//Processing input request
		processor.process(itemsFileName, recipesFileName);
		
		//Displaying recipes on the screen
		processor.printPossibleRecipes();

	}

}
