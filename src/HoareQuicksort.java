/*
 * Hoare Quicksort implementation obtained from geeksforgeeks.org
 */

public class HoareQuicksort {
	private int[] array;
	
	public HoareQuicksort(int[] array) {
		this.array = array;
		quicksort(0, array.length - 1);
	}
	
	public int[] getArray() {
		return array;
	}
	
	private void quicksort(int low, int high) {
		if (low < high) {
		
			int pivot = array[low];
			int i = low - 1;
			int j = high + 1;
			
			while (true) {
				do {
					i++;
				} while (array[i] < pivot);
				
				do {
					j--;
				} while (array[j] > pivot);
				
				if (i < j) {
					exchange(i, j);
				} else {
					break;
				}
			}
			
			quicksort(low, j);
			quicksort(j + 1, high);
		}
	}
	
	private void exchange(int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
