/**
 * Class that helps to obtain random elements
 * @author Joan-Antoni Vilaseca
 */

package uoc;

import java.util.List;
import java.util.Set;

public class Random {
	
	private static final java.util.Random random = new java.util.Random();

	private static java.util.Random getRandom() {
		return random;
	}
	
	/**
	 * Method to get a random element of a list.
	 * @param <T>
	 * @param list
	 * @return random T
	 */
	public static <T> T random(List<T> list) {
		if (list.size() == 0)
			throw new UnsupportedOperationException("Given list can not be empty");

		return list.get(getRandom().nextInt(list.size()));
	}
	
	/**
	 * Method to get a random double.
	 * @return random double
	 */
	public static double random() {
		return getRandom().nextDouble();
	}
	
	/**
	 * Method to get a random float.
	 * @return random float
	 */
	public static float randomFloat() {
		return getRandom().nextFloat();
	}
	
	/**
	 * Method to get a random boolean.
	 * @return random boolean
	 */
	public static boolean randomBoolean() {
		return getRandom().nextBoolean();
	}
	
	/**
	 * Method to get a random Integer.
	 * @param a
	 * @return random Integer
	 */
	public static int randomInt(int a) {
		return getRandom().nextInt(a);
	}

}
