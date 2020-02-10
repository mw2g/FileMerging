package merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Merger {

	static int sortError;
	static int parseToIntError;
	private static int bufferSize = 1000;
	private static Integer lastIntLine;
	private static String lastStringLine;
	private static String outputFileName;

	private static LinkedList<String> inFileData = new LinkedList<>();
	private static LinkedList<String> tempFileData = new LinkedList<>();

	public static boolean merger(String dataType, boolean ascending, String outputFile, String[] inputFileNames) {
		outputFileName = outputFile;

		/*
		 * Create or clear output file. Create temporary file
		 */
		try (FileWriter writerOutputFile = new FileWriter(outputFileName);
				FileWriter writerTempFile = new FileWriter(outputFileName + "_temp")) {
		} catch (IOException e) {
			System.out.println("Error creating new files.");
			return false;
		}

		/*
		 * Main loop
		 */
		for (int i = 0; i < inputFileNames.length; i++) {

			try (BufferedReader readerInputFile = new BufferedReader(new FileReader(inputFileNames[i]));
					BufferedReader readerTempFile = new BufferedReader(new FileReader(outputFileName + "_temp"))) {
				
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
						if (dataType == "-i") {
							if(!toMergeInt(ascending, tail)) {
								return false;
							}
						}
						else {
							if(!toMergeString(ascending, tail)) {
								return false;
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Input file not found.");
				return false;
			} catch (IOException e) {
				System.out.println("Error reading input file.");
				return false;
			}

			if (i + 1 < inputFileNames.length) { // rename output file to temporary if next input file exist
				File temp = new File(outputFileName + "_temp");
				temp.delete();
				new File(outputFileName).renameTo(temp);
			}

			lastIntLine = null; // resetting last line when moving to the next file
			lastStringLine = null;

		}

		/*
		 * Delete temporary file
		 */
		File tempFile = new File(outputFileName + "_temp");
		tempFile.delete();
		
		return true;
	}

	/*
	 * Merging string data. Merge inFileData tempFileData and add to output file.
	 */
	private static boolean toMergeString(boolean ascending, Boolean tail) {
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
				sortError++;
			}
			
			if (!result.isEmpty())
				lastStringLine = result.getLast();

		}

		return writeResult(result);
	}

	/*
	 * Merging integer data. Merge inFileData tempFileData and add to output file.
	 */
	private static boolean toMergeInt(Boolean ascending, Boolean tail) {
		LinkedList<Integer> result = new LinkedList<>();
		while (!inFileData.isEmpty() || !tempFileData.isEmpty()) {

			if (!inFileData.isEmpty() && !tempFileData.isEmpty()) {

				Integer inF = parseToInt(inFileData.getFirst());
				if (inF == null) {
					inFileData.removeFirst();
					continue;
				}

				Integer tempF = parseToInt(tempFileData.getFirst());
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

				Integer inF = parseToInt(inFileData.getFirst());
				if (inF == null) {
					inFileData.removeFirst();
					continue;
				}
				inFileData.removeFirst();
				result.addLast(inF);

			} else if (!tempFileData.isEmpty() && tail) {

				Integer tempF = parseToInt(tempFileData.getFirst());
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
				sortError++;
			}

			if (!result.isEmpty())
				lastIntLine = result.getLast();
		}

		return writeResult(result);
	}

	/*
	 * Add result to output file
	 */
	private static <T> boolean writeResult(LinkedList<T> result) {

		try (FileWriter writerOutputFile = new FileWriter(outputFileName, true)) {
			for (T res : result) {
				writerOutputFile.write(res + "\n");
			}
		} catch (IOException e) {
			System.out.println("Error to writing in output file.");
			return false;
		}
		return true;
	}

	/*
	 * Parsing string to integer
	 */
	private static Integer parseToInt(String str) {
		Integer in = null;
		try {
			in = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			parseToIntError++;
		}
		return in;
	}
}
