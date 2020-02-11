package merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Merger {

	static int sortError;
	static int parseToIntError;
	static int numberOfMergedStrings;
	static Set<String> sortErrorFiles = new HashSet<>();
	static Set<String> intErrorFiles = new HashSet<>();
	private static int bufferSize = 1000;
	private static Integer lastIntLine;
	private static String lastStringLine;
	private static String outputFileName;
	private static String tempFileName;

	private static LinkedList<String> inFileData = new LinkedList<>();
	private static LinkedList<String> tempFileData = new LinkedList<>();
	
	/* Method
	 * Merging
	 * @param dataType type of data, string or integer
	 * @param ascending of sort
	 * @param outputFile name of output file
	 * @param inputFileNames array of input file names
	 * @return boolean success rate.
	 */
	public static boolean merger(String dataType, boolean ascending, String outputFile, String[] inputFileNames) {
		sortError = 0;
		parseToIntError = 0;
		parseToIntError = 0;
		sortErrorFiles.clear();
		intErrorFiles.clear();
		outputFileName = outputFile;
		tempFileName = outputFileName + "_temp";
		File tempFile = new File(tempFileName);
		
		/*
		 * Create or clear output and temporary files
		 */
		try (FileWriter writerOutputFile = new FileWriter(outputFileName);
				FileWriter writerTempFile = new FileWriter(tempFileName)) {
		} catch (IOException e) {
			System.out.println("Error creating new files (" + outputFileName + "," + tempFileName + ")");
			return false;
		}

		/*
		 * Main loop
		 */
		for (int i = 0; i < inputFileNames.length; i++) {
			numberOfMergedStrings = 0;

			try (BufferedReader readerInputFile = new BufferedReader(new FileReader(inputFileNames[i]));
					BufferedReader readerTempFile = new BufferedReader(new FileReader(tempFileName))) {
				
				while (true) {
					if (readerInputFile.ready() && inFileData.size() < bufferSize)
						inFileData.addLast(readerInputFile.readLine());

					if (readerTempFile.ready() && tempFileData.size() < bufferSize)
						tempFileData.addLast(readerTempFile.readLine());

					if (inFileData.isEmpty() && tempFileData.isEmpty()) {
						break;
					}
					
					if ((inFileData.size() == bufferSize  && tempFileData.size() == bufferSize) || (!readerInputFile.ready() || !readerTempFile.ready())) {
						Boolean tail = inFileData.isEmpty() || tempFileData.isEmpty() ? true : false;
						if (dataType.equals("-i")) {
							if(!toMergeInt(ascending, tail, inputFileNames[i])) {
								tempFile.delete();
								return false;
							}
						}
						else {
							if(!toMergeString(ascending, tail, inputFileNames[i])) {
								tempFile.delete();
								return false;
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Input file not found (" + inputFileNames[i] + ")");
				tempFile.delete();
				return false;
			} catch (IOException e) {
				System.out.println("Error reading input file (" + inputFileNames[i] + ")");
				tempFile.delete();
				return false;
			}

			if (i + 1 < inputFileNames.length) { 			// rename output file to temporary if next input file exist
				tempFile.delete();
				if(!new File(outputFileName).renameTo(tempFile)) {
					System.out.println("Error renaming output file to temp file (" + outputFileName+ "to" + tempFileName + ")");
					return false;
				}
			}

			lastIntLine = null; 							// resetting last line when moving to the next file
			lastStringLine = null;
		}
		
		tempFile.delete();
		
		return true;
	}

	/* Method
	 * Merging string data. Merge inFileData tempFileData and add to output file.
	 * @param ascending of sort
	 * @param tail true if tail of files
	 * @param inputFileName input file name
	 * @return boolean success rate.
	 */
	private static boolean toMergeString(boolean ascending, Boolean tail, String inputFileName) {
		LinkedList<String> result = new LinkedList<>();

		while (!inFileData.isEmpty() || !tempFileData.isEmpty()) {

			if (!inFileData.isEmpty() && !tempFileData.isEmpty()) {

				String inF = inFileData.getFirst();

				String tempF = tempFileData.getFirst();

				if (ascending) {
					result.addLast(inF.compareTo(tempF) < 0 ? inFileData.removeFirst() : tempFileData.removeFirst());
				} else {
					result.addLast(inF.compareTo(tempF) > 0 ? inFileData.removeFirst() : tempFileData.removeFirst());
				}

			} else if (!inFileData.isEmpty() && tail) {

				result.addLast(inFileData.removeFirst());

			} else if (!tempFileData.isEmpty() && tail) {

				result.addLast(tempFileData.removeFirst());

			} else

				break;

			if (!result.isEmpty() && lastStringLine != null							//input file sorting check
					&& (ascending ? result.getLast().compareTo(lastStringLine) < 0
							: result.getLast().compareTo(lastStringLine) > 0)) {
				result.removeLast();
				sortErrorFiles.add(inputFileName);
				sortError++;
			}
			
			if (!result.isEmpty())
				lastStringLine = result.getLast();

		}

		return writeResult(result);
	}

	/* Method
	 * Merging integer data. Merge inFileData tempFileData and add to output file.
	 * @param ascending of sort
	 * @param tail true if tail of files
	 * @param inputFileName input file name
	 * @return boolean success rate.
	 */
	private static boolean toMergeInt(Boolean ascending, Boolean tail, String inputFileName) {
		LinkedList<Integer> result = new LinkedList<>();
		while (!inFileData.isEmpty() || !tempFileData.isEmpty()) {

			if (!inFileData.isEmpty() && !tempFileData.isEmpty()) {

				Integer inF = parseToInt(inFileData.getFirst(), inputFileName);
				if (inF == null) {
					inFileData.removeFirst();
					continue;
				}

				Integer tempF = parseToInt(tempFileData.getFirst(), inputFileName);
				if (tempF == null) {
					tempFileData.removeFirst();
					continue;
				}
				if (ascending) {
					result.addLast(
							Integer.parseInt(inF < tempF ? inFileData.removeFirst() : tempFileData.removeFirst()));
				} else {
					result.addLast(
							Integer.parseInt(inF > tempF ? inFileData.removeFirst() : tempFileData.removeFirst()));
				}

			} else if (!inFileData.isEmpty() && tail) {

				Integer inF = parseToInt(inFileData.getFirst(), inputFileName);
				if (inF == null) {
					inFileData.removeFirst();
					continue;
				}
				inFileData.removeFirst();
				result.addLast(inF);

			} else if (!tempFileData.isEmpty() && tail) {

				Integer tempF = parseToInt(tempFileData.getFirst(), inputFileName);
				if (tempF == null) {
					tempFileData.removeFirst();
					continue;
				}
				tempFileData.removeFirst();
				result.addLast(tempF);

			} else

				break;

			if (!result.isEmpty() && lastIntLine != null // Checking errors of sort
					&& (ascending ? result.getLast() < lastIntLine : result.getLast() > lastIntLine)) {
				result.removeLast();
				sortErrorFiles.add(inputFileName);
				sortError++;
			}

			if (!result.isEmpty())
				lastIntLine = result.getLast();
		}

		return writeResult(result);
	}

	/* Method
	 * Add result to output file
	 * @param result list to output file
	 * @return boolean success rate.
	 */
	private static <T> boolean writeResult(LinkedList<T> result) {

		try (FileWriter writerOutputFile = new FileWriter(outputFileName, true)) {
			for (T res : result) {
				writerOutputFile.write(res + "\n");
				numberOfMergedStrings++;
			}
		} catch (IOException e) {
			System.out.println("Error to writing in output file (" + outputFileName + ")");
			return false;
		}
		return true;
	}

	/* Method
	 * Parsing string to integer
	 * @param str string
	 * @param inputFileName input file name
	 * @return Integer return result integer or null if error
	 */
	private static Integer parseToInt(String str, String inputFileName) {
		Integer in = null;
		try {
			in = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			intErrorFiles.add(inputFileName);
			parseToIntError++;
		}
		return in;
	}
}
