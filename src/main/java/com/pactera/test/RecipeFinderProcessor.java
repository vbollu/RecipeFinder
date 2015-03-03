/**
 * 
 */
package com.pactera.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.pactera.test.model.Ingredient;
import com.pactera.test.model.Item;

/**
 * @author vikram
 *
 */
public class RecipeFinderProcessor {

	Map<String, Item> fridgeItemsMap = new TreeMap<String, Item>();
	Map<String, List<Ingredient>> recipeList = new HashMap<String, List<Ingredient>>();
	Map<String, List<Ingredient>> validRecipeList = new HashMap<String, List<Ingredient>>();
	Map<String, List<Ingredient>> finalRecipesMap = null;

	/**
	 * 
	 * @param itemListFileName
	 * @param recipeListFileName
	 */
	public void process(String itemsFileName, String recipesFileName) {
		// Loading all items from file and putting them into map
		setFridgeItemsMap(itemsFileName);

		// Loading all recipes data from file and putting them into map
		setRecipeList(recipesFileName);

		findAllPossibleRecipesList();

		// Finding closest use-by item recipe.
		finalRecipesMap = validRecipeList;
		for (Entry<String, Item> entry : fridgeItemsMap.entrySet()) {
			finalRecipesMap = findRecipesByItem(entry.getValue(),
					finalRecipesMap);
			if (finalRecipesMap.size() == 1) {
				break;
			}
		}

	}

	/**
	 * Setting fridgeItemsMap
	 * 
	 * @param fileName
	 */
	public void setFridgeItemsMap(String itemsFileName) {
		try {

			BufferedReader br = new BufferedReader(
					new FileReader(itemsFileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				try {
					String[] b = line.split(",");

					// Ignoring item, if any of the item details not provided
					if (b.length < 4 || b[0].isEmpty() || b[1].isEmpty()
							|| b[2].isEmpty() || b[3].isEmpty()) {
						continue;
					}
					Item item = new Item();
					item.setItemName(b[0]);
					item.setAmount(Integer.valueOf(b[1]));
					String unit = b[2];
					if (unit.equalsIgnoreCase("of")) {
						item.setUnit(Unit.OF);
					} else if (unit.equalsIgnoreCase("grams")) {
						item.setUnit(Unit.GRAMS);
					} else if (unit.equalsIgnoreCase("ml")) {
						item.setUnit(Unit.ML);
					} else if (unit.equalsIgnoreCase("slices")) {
						item.setUnit(Unit.SLICES);
					}
					item.setUseBy(stringToDate((String) b[3]));

					// Ignoring item, if it is already expired
					if (item.getUseBy().before(new Date())) {
						continue;
					}
					fridgeItemsMap.put(item.getItemName(), item);
				} catch (Exception e) {
					// Ignoring the particular item if there is any exception
					// while parsing
					continue;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error is:" + e);
		}
		// Sorting map based on item use by date value
		fridgeItemsMap = sortByComparator(fridgeItemsMap);

	}

	public void setRecipeList(String recipesFileName) {
		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(
					recipesFileName));
			for (Iterator recipe = jsonArray.iterator(); recipe.hasNext();) {
				try {
					JSONObject jsonObject = (JSONObject) recipe.next();
					String recipeName = (String) jsonObject.get("name");
					JSONArray ingredients = (JSONArray) jsonObject
							.get("ingredients");
					List<Ingredient> ingredientList = new ArrayList<Ingredient>();
					for (Iterator<JSONObject> iterator = ingredients.iterator(); iterator
							.hasNext();) {
						JSONObject ingredient = (JSONObject) iterator.next();
						Ingredient ingredientDtls = new Ingredient();
						ingredientDtls.setItemName((String) ingredient
								.get("item"));
						ingredientDtls.setAmount(Integer
								.valueOf((String) ingredient.get("amount")));
						String unit = (String) ingredient.get("unit");
						if (unit.equalsIgnoreCase("of")) {
							ingredientDtls.setUnit(Unit.OF);
						} else if (unit.equalsIgnoreCase("grams")) {
							ingredientDtls.setUnit(Unit.GRAMS);
						} else if (unit.equalsIgnoreCase("ml")) {
							ingredientDtls.setUnit(Unit.ML);
						} else if (unit.equalsIgnoreCase("slices")) {
							ingredientDtls.setUnit(Unit.SLICES);
						}else{
							throw new Exception();
						}
						ingredientList.add(ingredientDtls);
					}
					recipeList.put(recipeName, ingredientList);
				} catch (Exception e) {
					// Ignoring recipe if there is any excpetion while parsng
					// JSON data
					continue;
				}
			}

		} catch (Exception e) {
			System.out.println("Error while parsing JSON data:" + e);
		}

	}

	/**
	 * Finding all possible recipes list
	 */
	public void findAllPossibleRecipesList() {
		for (Map.Entry<String, List<Ingredient>> entry : recipeList.entrySet()) {

			boolean matchFound = true;
			List<Ingredient> list = entry.getValue();

			for (Ingredient item : list) {
				if (fridgeItemsMap.containsKey(item.getItemName())) {
					Item fridgeItem = fridgeItemsMap.get(item.getItemName());
					if (item.getAmount() <= fridgeItem.getAmount()
							&& item.getUnit().getName()
									.equals(fridgeItem.getUnit().getName())) {
						continue;

					} else {
						matchFound = false;
						break;
					}

				} else {
					matchFound = false;
					break;
				}
			}
			if (matchFound) {
				validRecipeList.put((String) entry.getKey(), list);
				//System.out.println("Recipe found:" + entry.getKey());
			}

		}
	}

	/**
	 * Map Sort by - Item use by date
	 * 
	 * @param unsortMap
	 * @return
	 */
	private static Map<String, Item> sortByComparator(
			Map<String, Item> unsortMap) {

		List<Entry<String, Item>> list = new LinkedList<Entry<String, Item>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Item>>() {
			public int compare(Entry<String, Item> o1, Entry<String, Item> o2) {
				return o1.getValue().getUseBy()
						.compareTo(o2.getValue().getUseBy());
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Item> sortedMap = new LinkedHashMap<String, Item>();
		for (Entry<String, Item> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static Map<String, List<Ingredient>> findRecipesByItem(Item item,
			Map<String, List<Ingredient>> tempMap) {

		Map<String, List<Ingredient>> map = new HashMap<String, List<Ingredient>>();
		for (Entry<String, List<Ingredient>> entry : tempMap.entrySet()) {
			List<Ingredient> list = entry.getValue();
			for (Ingredient ingredient : list) {
				if (item.getItemName().equalsIgnoreCase(
						ingredient.getItemName())) {
					map.put(entry.getKey(), entry.getValue());
					break;
				}
			}
		}

		if (map.size() > 0) {
			return map;
		} else {
			return tempMap;
		}

	}

	/**
	 * Converting string to date.
	 * 
	 * @param dateText
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDate(String dateText) throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date date = format.parse(dateText);
		return date;
	}

	/**
	 * Printing possible recipes
	 */
	public void printPossibleRecipes() {
		if (finalRecipesMap == null || finalRecipesMap.size() == 0) {
			System.out.println("Order Takeout");
		} else {
			System.out.println("Recipe Name(s):");

			for (Entry<String, List<Ingredient>> entry : finalRecipesMap
					.entrySet()) {
				System.out.println(entry.getKey());
			}
		}

	}

	/**
	 * @return the finalRecipesMap
	 */
	public Map<String, List<Ingredient>> getFinalRecipesMap() {
		return finalRecipesMap;
	}

	/**
	 * @param finalRecipesMap
	 *            the finalRecipesMap to set
	 */
	public void setFinalRecipesMap(Map<String, List<Ingredient>> finalRecipesMap) {
		this.finalRecipesMap = finalRecipesMap;
	}

}
