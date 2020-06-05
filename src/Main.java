/*
 * Darey Lee
 * CS 4050
 * Assignment 4
 */

public class Main {

	public static void main(String[] args) {
		JavaRandom javaRandom = new JavaRandom();
		javaRandom.createDistributionArrayCSV();
		javaRandom.createBitMap();
		javaRandom.analyze();

		ObjectHashCodeRandom objectHashCodeRandom = new ObjectHashCodeRandom();
		objectHashCodeRandom.createDistributionArrayCSV();
		objectHashCodeRandom.createBitMap();
		objectHashCodeRandom.analyze();
	}
	
}
