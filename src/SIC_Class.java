//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.Scanner;
//
//public class SIC_Class implements ISIC {
//
//	String ruler = ".23456789012345678901234567890123456";
//	String label = ".Label.  Opcode  The  O p e r a n d ";
//	String reqBytes = "1–8 label 9 blank 10–15 operation code 16–17 blank 18–35 operand 36–66 comment";
//
//	public static void main(String[] args) throws FileNotFoundException {
//		SIC_Class s = new SIC_Class();
//		// s.writeOpTable();
//		// s.ReadSourceCode(new FileReader("optable.txt"));
//		// String str = "theGirlIsStrong";
//		// System.out.println(str.substring(1, 8));
//
//		// String line=;
//
//	}
//
//	@Override
//	public boolean ReadSourceCode(FileReader file) {
//
//		BufferedReader br = null;
//		br = new BufferedReader(file);
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter("optable.txt", "UTF-8");
//		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		try {
//			String line = br.readLine();
//			String tempLine = null;
//			tempLine = line.toUpperCase();
//
//			String label = line.substring(0, 8);
//			String blank1 = line.substring(8, 9);
//			if (!blank1.equals(" ")) {
//				return false;
//			}
//			String opCode = line.substring(9, 15);
//
//			String blank2 = line.substring(15, 17);
//			if (!blank2.equals("  ")) {
//				return false;
//			}
//			String operand = line.substring(17, 35);
//			// NumberUtils.isNumber(myStringValue);
//			if (!isNumeric(operand)) {
//				return false;
//			}
//
//			// int startAdd =Integer.parseInt(operand);
//			String comment = line.substring(35, 66);
//			int tempAdd = Integer.parseInt(operand);
//			tempAdd = convert(tempAdd);
//			String writeLine = tempAdd + " ";
//
//			if (opCode.equals("Start   ")) {
//				writeLine.concat(line);
//
//				writer.println(writeLine);
//
//			} else {
//				writer.close();
//				return false;
//			}
//			line = br.readLine();
//
//			while (line != null && !opCode.equals("END     ")) {
//
//				System.out.println(line);
//
//				tempLine = line.toUpperCase();
//
//				 label = line.substring(0, 8);
//				 blank1 = line.substring(8, 9);
//				if (!blank1.equals(" ")) {
//					return false;
//				}
//				 opCode = line.substring(9, 15);
//
//				 blank2 = line.substring(15, 17);
//				if (!blank2.equals("  ")) {
//					return false;
//				}
//				 operand = line.substring(17, 35);
//				
//				
//				
//				
//				line = br.readLine();
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		// BufferedReader br = null;
//		// br = new BufferedReader(file);
//		// try {
//		// String line = br.readLine();
//		//
//		// while (line != null) {
//		//
//		// System.out.println(line);
//		//
//		// line = br.readLine();
//		//
//		// }
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// } finally {
//		// try {
//		// br.close();
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// }
//		// }
//		return false;
//	}
//
//	//
//	// boolean searchFile(String opString,File file){
//	// final Scanner scanner = new Scanner(file);
//	// while (scanner.hasNextLine()) {
//	// final String lineFromFile = scanner.nextLine();
//	// if(lineFromFile.contains(name)) {
//	// // a match!
//	// System.out.println("I found " +name+ " in file " +file.getName());
//	// break;
//	// }
//	// }
//	// }
//
//	public static boolean searchUsingScanner(String filePath, String searchQuery) throws FileNotFoundException {
//		searchQuery = searchQuery.trim();
//		Scanner scanner = null;
//		try {
//			scanner = new Scanner(new File(filePath));
//			while (scanner.hasNextLine()) {
//				String line = scanner.nextLine();
//				if (line.contains(searchQuery)) {
//					return true;
//				} else {
//				}
//			}
//		} finally {
//			try {
//				if (scanner != null)
//					scanner.close();
//			} catch (Exception e) {
//				System.err.println("Exception while closing scanner " + e.toString());
//			}
//		}
//
//		return false;
//	}
//
//	public static int convert(int n) {
//		return Integer.valueOf(String.valueOf(n), 16);
//	}
//
//	public static boolean isNumeric(String str) {
//		try {
//			Integer d = Integer.parseInt(str);
//		} catch (NumberFormatException nfe) {
//			return false;
//		}
//		return true;
//	}
//
//	@Override
//	public void objectCode(FileInputStream file) {
//
//	}
//
//	@Override
//	public void listingFile(FileInputStream file) {
//
//	}
//
//	private boolean checkValidFile() {
//		return false;
//
//	}
//
//	private void writeOpTable() throws FileNotFoundException {
//		String optable[] = { "add 24", "and 64", "comp 40", "div 36", "j 60", "jeq 48", "jgt 52", "jlt 56", "jsub 72",
//				"lda 0", "ldch 80", "ldl 8", "ldx 4", "mul 32", "or 68", "rd 216", "rsub 76", "sta 12", "stch 84",
//				"stl 20", "stx 16", "sub 28", "td 224", "tix 44", "wd 220" };
//		try {
//			PrintWriter writer = new PrintWriter("optable.txt", "UTF-8");
//			for (int i = 0; i < optable.length; i++) {
//				writer.println(optable[i]);
//
//			}
//
//			writer.close();
//		} catch (IOException e) {
//			// do something
//		}
//	}
//
//}
