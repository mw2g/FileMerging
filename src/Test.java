import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Test {
	public static void main(String[] args) {

		Random rnd = new Random(System.currentTimeMillis());
//		
//		for (int i = 0; i < 9; i++) {
//			int number = 1;
//			
//			try (FileWriter writerOutputFile = new FileWriter("data/gen" + i + ".txt")) {
//				
//				for (int j = 0; j < 1_000_000 + rnd.nextInt(300_000); j++) {
//					
//					number += rnd.nextInt(25);
//					
//					writerOutputFile.write(number + "\n");
//					
//				}
//				
//				
//			} catch (IOException e) {
//				System.out.println("Error to writing in output file.");
//			}
//		}


//		System.out.println("c".compareTo("asdb"));
		
		for (int i = 0; i < 9; i++) {
			int number = 1;
			
			try (FileWriter writerOutputFile = new FileWriter("data/str" + i + ".txt")) {
				
				for (int j = 0; j < 100 + rnd.nextInt(300); j++) {
					
					number += rnd.nextInt(25);
					
					writerOutputFile.write(number + "\n");
					
				}
				
				
			} catch (IOException e) {
				System.out.println("Error to writing in output file.");
			}
		}
		
		
		
		
		
		
//		for (int i = 0; i < 9; i++) {
//
//			String symbols = "qwertyuiopasdfghjklzxcvbnm";
//			int count = rnd.nextInt(5);
//			String prev = "a";
//			StringBuilder randString = new StringBuilder();
//
//			try (FileWriter writerOutputFile = new FileWriter("data/str" + i + ".txt")) {
//
//				for (int j = 0; j < 100 + rnd.nextInt(300); j++) {
//
//					
//					while (true) {
//
//						
//						for (int k = 0; k < count; k++) {
//							randString.append(symbols.charAt(rnd.nextInt(26)));
//						}
//						
////						System.out.println(prev.compareTo(randString.toString()));
//						if (prev.compareTo(randString.toString()) < 0) {
//							break;
//						} 
//						
//						randString.setLength(0);
//					}
//					
//					writerOutputFile.write(randString + "\n");
//					System.out.println(randString);
//					prev = randString.toString();
//					randString.setLength(0);
//
//				}
//
//			} catch (IOException e) {
//				System.out.println("Error to writing in output file.");
//			}
//		}
	}
}
