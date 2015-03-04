# RecipeFinder

Steps to run:

1.Download code from GitHub
2.Run the below maven command in the root directory where pom.xml loaded
mvn clean install
3.Above command generates target folder and jar file inside it.
4 Run the program  using below command
java -jar <jar file name> <fridge csv file name> <recipe file name>

input files should be located in the folder where jar file is.

Also added JUnit test case inside src/test/java/RecipeFinderTest.java.

Or  

Import to eclipse and directly run the RecipeFinder.java file where main method exist by passing input file names as arguments.

Required libs can be located under lib folder.