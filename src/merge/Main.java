package merge;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Main {

	private static int sortError;
	private static int parseToIntError;
	private static int bufferSize = 1000;
	private static Integer lastIntLine;
	private static String lastStringLine;
	private static String outputFileName;

	private static LinkedList<String> inFileData = new LinkedList<>();
	private static LinkedList<String> tempFileData = new LinkedList<>();

	public static void main(String[] ar) {
		
		long start = System.currentTimeMillis();
		String[] args = new String[12];

		args[0] = "-a";
		args[1] = "-s";
		args[2] = "data/merged.txt";
//		args[3] = "data/gen0.txt";
//		args[4] = "data/gen1.txt";
//		args[5] = "data/gen2.txt";
//		args[6] = "data/gen3.txt";
//		args[7] = "data/gen4.txt";
//		args[8] = "data/gen5.txt";
//		args[9] = "data/gen6.txt";
//		args[10] = "data/gen7.txt";
//		args[11] = "data/gen8.txt";

		args[3] = "data/str0.txt";
		args[4] = "data/str1.txt";
		args[5] = "data/str2.txt";
		args[6] = "data/str3.txt";
		args[7] = "data/str4.txt";
		args[8] = "data/str5.txt";
		args[9] = "data/str6.txt";
		args[10] = "data/str7.txt";
		args[11] = "data/str8.txt";
//		args[4] = "data/two.txt";
//		args[5] = "data/three.txt";

		/*
		 * Arguments handling
		 */
		if (args.length < 4)
			argError();

		int numOfArg = 0;
		String sortOrder = args[numOfArg].matches("-a|-d") ? args[numOfArg++] : "-a";
		boolean ascending = sortOrder == "-a" ? true : false;
		String dataType = args[numOfArg].matches("-s|-i") ? args[numOfArg++] : argError();
		outputFileName = args[numOfArg++];

		String[] inputFileNames = new String[args.length - numOfArg];
		for (int i = 0; i < inputFileNames.length; i++) {
			inputFileNames[i] = args[numOfArg + i];
		}

		/*
		 * Create or clear output file Create temporary file
		 */
		try (FileWriter writerOutputFile = new FileWriter(outputFileName);
				FileWriter writerTempFile = new FileWriter(outputFileName + "_temp")) {
		} catch (IOException e) {
			System.out.println("Error creating new files.");
		}

		/*
		 * Main processing
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
						if (dataType == "-i")
							toMergeInt(ascending, tail);
						else
							toMergeString(ascending, tail);
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Input file not found.");
				exitByEnter();
			} catch (IOException e) {
				System.out.println("Error reading input file.");
				exitByEnter();
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
		 * Display errors
		 */
		if (sortError > 0) {
			System.out.println("Sort order in input files is broken. " + sortError + " lines have been skipped");
		}

		if (parseToIntError > 0) {
			System.out.println("Non-number strings found. " + parseToIntError + " lines have been skipped.");
		}

		/*
		 * Delete temporary file
		 */
		File tempFile = new File(outputFileName + "_temp");
		tempFile.delete();
		
		long runtime = System.currentTimeMillis() - start;
		System.out.println(runtime);
	}

	/*
	 * Merging string data. Merge inFileData tempFileData and add to outputFileName
	 */
	private static void toMergeString(boolean ascending, Boolean tail) {
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

			if (!result.isEmpty() && lastStringLine != null
					&& (ascending ? result.getLast().compareTo(lastStringLine) < 0
							: result.getLast().compareTo(lastStringLine) > 0)) {
				result.removeLast();
				sortError++;
			}
			if (!result.isEmpty())
				lastStringLine = result.getLast();

		}

		writeResult(result);
	}

	/*
	 * Merging integer data. Merge inFileData tempFileData and add to outputFileName
	 */
	private static void toMergeInt(Boolean ascending, Boolean tail) {
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

		writeResult(result);
	}

	/*
	 * Add result to output file
	 */
	private static <T> void writeResult(LinkedList<T> result) {

		try (FileWriter writerOutputFile = new FileWriter(outputFileName, true)) {
			for (T res : result) {
				writerOutputFile.write(res + "\n");
			}
		} catch (IOException e) {
			System.out.println("Error to writing in output file.");
		}
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

	/*
	 * Handling arguments error
	 */
	private static String argError() {
		System.out.println("Arguments are incorrect.");
		exitByEnter();
		return null;
	}

	/*
	 * Exit program by pressing Enter
	 */
	public static void exitByEnter() {
		System.out.println("Press Enter to exit.");
		try {
			System.in.read();
			System.exit(1);
		} catch (IOException ignored) {

		}
	}

}
