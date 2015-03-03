/**
 * 
 */
package src.test.java;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pactera.test.RecipeFinderProcessor;

/**
 * @author vikram
 *
 */
public class RecipeFinderTest {

	RecipeFinderProcessor processor= new RecipeFinderProcessor();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRecipeFinder() {
		processor.process("TestData\\itemlist.txt", "TestData\\recipes.txt");
		Map result = processor.getFinalRecipesMap();
		Assert.assertNotNull("Result should not be null",result);
		Assert.assertEquals("Result should not be empty",1,result.size());
		Assert.assertEquals("Should return 'Salad Sandwich'","salad sandwich", (String)result.keySet().iterator().next());
		processor.printPossibleRecipes();
	}

}
