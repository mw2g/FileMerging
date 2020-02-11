package merge;

import java.io.IOException;

public class Start {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();

		/*
		 * Arguments handling
		 */
		if (args.length < 3)
			argError();

		int numOfArg = 0;
		String sortOrder = args[numOfArg].matches("-a|-d") ? args[numOfArg++] : "-a";
		boolean ascending = sortOrder.equals("-a") ? true : false;
		String dataType = args[numOfArg].matches("-s|-i") ? args[numOfArg++] : argError();
		String outputFileName = args[numOfArg++];

		String[] inputFileNames = new String[args.length - numOfArg];
		for (int i = 0; i < inputFileNames.length; i++) {
			inputFileNames[i] = args[numOfArg + i];
		}
		
		/*
		 * Run merger
		 */
		boolean result = Merger.merger(dataType, ascending, outputFileName, inputFileNames);
		
		if (result) {
			System.out.println("Success!");
			System.out.println("Number of merged strings in output file: " + Merger.numberOfMergedStrings);
		} else {
			exitByEnter();
		}
		
		displayErrors(Merger.sortError, Merger.parseToIntError);
		
		/*
		 * Time of completion
		 */
		System.out.println("Done in " + (System.currentTimeMillis() - start) + " milliseconds.");
	}
	
	
	/* Method
	 * Display errors
	 */
	private static void displayErrors(int sortError, int parseToIntError) {
		if (sortError > 0) {
			System.out.println("Sort order in input files is broken. " + sortError + " lines have been skipped");
		}

		if (parseToIntError > 0) {
			System.out.println("Non-number strings found. " + parseToIntError + " lines have been skipped.");
		}
	}



	/* Method
	 * Handling arguments error
	 */
	private static String argError() {
		System.out.println("Arguments are incorrect.");
		System.out.println("Parameter Order:");
		System.out.println("1. Sort mode (-a or -d) Optional. Default ascending.");
		System.out.println("2. Data type (-i or -s) Required.");
		System.out.println("3. Output file name. Required.");
		System.out.println("4 and next. Input file names. Required at least one.");
		exitByEnter();
		return null;
	}

	/* Method
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
