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
		
//		char ch = 56;
//		System.out.println(ch);
//		ch += 1;
//		System.out.println(ch);
//		
		for (int i = 0; i < 9; i++) {
			
			int min = 48;
			char[] ch = new char[6];
			
			for (int j = 0; j < ch.length; j++) {
				ch[j] = 97;
			}
			
			try (FileWriter writerOutputFile = new FileWriter("data/str" + i + ".txt")) {
				int j = 0;
				MAIN: while (j < 10_000) { // + rnd.nextInt(300_000)) {
					
					ch[0] += rnd.nextInt(5);
					
					while (true) {
						ch[1] += 1 + rnd.nextInt(5);
						
						while (true) {
							ch[2] += 1 + rnd.nextInt(5);
							
							while (true) {
								ch[3] += 1 + rnd.nextInt(5);
								
								while (true) {
									ch[4] += 1 + rnd.nextInt(5);
									
									while (true) {
										ch[5] += 1 + rnd.nextInt(5);
										String myStr = new String(ch);
										writerOutputFile.write(myStr + "\n");
										System.out.println(j);
										j++;
										if(j > 100_000)
											break MAIN;
										
										if(ch[5] >= 115) {
											ch[5] = 97;
											break;
										}
									}
									
									if(ch[4] >= 115) {
										ch[4] = 97;
										break;
									}
								}
								
								if(ch[3] >= 115) {
									ch[3] = 97;
									break;
								}
							}
							
							if(ch[2] >= 115) {
								ch[2] = 97;
								break;
							}
						}
						
						if(ch[1] >= 115) {
//							ch[1] = 97;
							break;
						}
					}
					
					if(ch[0] >= 115) {
//						ch[0] = 97;
						break;
					}
					
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
