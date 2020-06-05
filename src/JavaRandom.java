import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;

public class JavaRandom {

	private int[] distributionArray = new int[1024];	
	private int[] sequenceArray = new int[10000000];
	private int[][] clusterOfTwoArray = new int[1024][1024];
	private String binaryStringOfSequenceArray;
	private Random random = new Random();
	
	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	
	public JavaRandom() {
		generateRandomNumbers();
		generateBinaryStringOfSequenceArray();
	}
	
	public void createDistributionArrayCSV() {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int counter : distributionArray) {
			stringBuilder.append(counter + ",");
		}
		
		try {
			FileWriter fileWriter = new FileWriter("javaRandom.csv");
			fileWriter.write(stringBuilder.toString());
			fileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createBitMap() {
		BufferedImage bitMap = new BufferedImage(10000, 10000, BufferedImage.TYPE_3BYTE_BGR);
		int xCoordinate = 0;
		int yCoordinate = 0;
			
		for (int i = 0; i < binaryStringOfSequenceArray.length(); i++) {
			if (binaryStringOfSequenceArray.charAt(i) == '0') {
				bitMap.setRGB(xCoordinate, yCoordinate, Color.WHITE.getRGB());
			}
			
			yCoordinate++;
			
			if (yCoordinate == 10000) {
				yCoordinate = 0;
				xCoordinate++;
			}
		}	
		
		try {
			File javaRandomBitMap = new File("javaRandomBitMap.jpg");
			ImageIO.write(bitMap, "jpg", javaRandomBitMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void analyze() {
		System.out.println("##############################");
		System.out.println("######### Java Random ########");
		System.out.println("##############################");
		
		runsTestBits();
		runsTestMedian();
		chiSquared();
		clusterOfTwo();
	}
	
	private void runsTestBits() {
		int runs = 0;
		int bitOf0 = 0;
		int bitOf1 = 0;
		boolean runOf0 = false;
		boolean runOf1 = false;
		
		for (int i = 0; i < binaryStringOfSequenceArray.length(); i++) {
			if (binaryStringOfSequenceArray.charAt(i) == '0') {
				bitOf0++;
				if (!runOf0) {
					runOf0 = true;
					runOf1 = false;
					runs++;
				}
				
			} else {
				bitOf1++;
				if (!runOf1) {
					runOf0 = false;
					runOf1 = true;
					runs++;
				}
			}
		}
		
		System.out.println("========== RUNS TEST BITS ==========");
		System.out.println("Bits with 0: " + numberFormat.format(bitOf0));
		System.out.println("Bits with 1: " + numberFormat.format(bitOf1));
		System.out.println("Observed Number of Runs: " + numberFormat.format(runs));
	}
	
	private void runsTestMedian() {
		int runs = 0;
		int belowMedian = 0;
		int aboveMedian = 0;
		boolean runBelowMedian = false;
		boolean runAboveMedian = false;
		double median = findMedian();
		
		for (int number : sequenceArray) {
			if (number < median) {
				belowMedian++;
				if (!runBelowMedian) {
					runBelowMedian = true;
					runAboveMedian = false;
					runs++;
				}
			} else if (number > median) {
				aboveMedian++;
				if (!runAboveMedian) {
					runBelowMedian = false;
					runAboveMedian = true;
					runs++;
				}
			}
		}
		
		System.out.println("========== RUNS TEST MEDIAN ==========");
		System.out.println("Median: " + median);
		System.out.println("Amount of Numbers Below Median: " + numberFormat.format(belowMedian));
		System.out.println("Amount of Numbers Above Median: " + numberFormat.format(aboveMedian));
		System.out.println("Observed Number of Runs: " + numberFormat.format(runs));
	}
	
	private void chiSquared() {
		int degreesOfFreedom = distributionArray.length - 1;
		double expectedFrequency = (double) sequenceArray.length / distributionArray.length;
		double culmulativeD = 0;
		
		for (int count : distributionArray) {
			culmulativeD += Math.pow(count - expectedFrequency, 2) / expectedFrequency;
		}
		
		System.out.println("========== CHI SQUARED FOR INDEPENDENCE ==========");
		System.out.println("Degrees of Freedom: " + degreesOfFreedom);
		System.out.println("Expected Frequency: " + expectedFrequency);
		System.out.println("Chi Squared Value D: " + culmulativeD);
	}
	
	private void clusterOfTwo() {
		int degreesOfFreedom = (int) Math.pow(clusterOfTwoArray.length, 2) - 1;
		double expectedFrequency = (double) (sequenceArray.length - 1) / Math.pow(distributionArray.length, 2);
		double culmulativeD = 0;
		
		for (int i = 0; i < sequenceArray.length - 1; i++) {
			clusterOfTwoArray[sequenceArray[i]][sequenceArray[i + 1]]++;
		}
		
		for (int[] leadingNumber : clusterOfTwoArray) {
			for (int countOfFollowingNumber : leadingNumber) {
				culmulativeD += Math.pow(countOfFollowingNumber - expectedFrequency, 2) / expectedFrequency;
			}
		}
		
		System.out.println("========== CLUSTER OF TWO ==========");
		System.out.println("Degrees of Freedom: " + numberFormat.format(degreesOfFreedom));
		System.out.println("Expected Frequency: " + expectedFrequency);
		System.out.println("Chi Squared Value D: " + numberFormat.format(culmulativeD));
	}
	
	private void generateRandomNumbers() {
		for (int i = 0; i < sequenceArray.length; i++) {
			int randomNumber = random.nextInt(distributionArray.length);
			
			sequenceArray[i] = randomNumber;
			distributionArray[randomNumber]++;
		}
	}
	
	private void generateBinaryStringOfSequenceArray() {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int number : sequenceArray) {
			stringBuilder.append(String.format("%10s", Integer.toBinaryString(number)).replace(' ', '0'));
		}
		
		binaryStringOfSequenceArray = stringBuilder.toString();
	}
	
	private double findMedian() {
		HoareQuicksort hoareQuicksort = new HoareQuicksort(sequenceArray.clone());
		int[] sortedArray = hoareQuicksort.getArray();
		
		return (double) (sortedArray[sortedArray.length / 2] + sortedArray[(sortedArray.length / 2) - 1]) / 2;
	}
}
