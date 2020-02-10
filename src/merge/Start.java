package merge;

import java.io.IOException;

public class Start {

	public static void main(String[] args) {
// start param		-a -s data/merged.txt data/str0.txt data/str1.txt data/str2.txt data/str3.txt data/str4.txt data/str5.txt data/str6.txt data/str7.txt data/str8.txt
		long start = System.currentTimeMillis();

		/*
		 * Arguments handling
		 */
		if (args.length < 4)
			argError();

		int numOfArg = 0;
		String sortOrder = args[numOfArg].matches("-a|-d") ? args[numOfArg++] : "-a";
		boolean ascending = sortOrder == "-a" ? true : false;
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
		
		if (!result) {
			exitByEnter();
		}
		
		/*
		 * Display errors
		 */
		if (Merger.sortError > 0) {
			System.out.println("Sort order in input files is broken. " + Merger.sortError + " lines have been skipped");
		}

		if (Merger.parseToIntError > 0) {
			System.out.println("Non-number strings found. " + Merger.parseToIntError + " lines have been skipped.");
		}

		long runtime = System.currentTimeMillis() - start;
		System.out.println(runtime);
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
