
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SIC implements ISIC {
	private Map<String, String> optable = new HashMap<String, String>();
	private Map<String, String> symbols = new HashMap<String, String>();
	private Map<String, ArrayList<String>> literalsTable = new HashMap<String, ArrayList<String>>(3);
	private Map<String, ArrayList<String>> literals = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> directives = new ArrayList<>();
	int asterixNum = 0;
	ArrayList<String> litOrder = new ArrayList<>();
	// byte instructions only one byte
	// word only one word
	// no start with spaces

	private ArrayList<String> send = new ArrayList<String>();
	private ArrayList<String> listing = new ArrayList<String>();
	private ArrayList<String> recordSend = new ArrayList<String>();
	private ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
	private ArrayList<String> singleRecord = new ArrayList<String>();
	private int tenInstractions = 0;
	private String createRecord = "";
	private boolean newRecord = true;
	private String finalAddress = "";
	private String header = "";
	private String endRecoird = "";
	private ArrayList<String> linesworking = new ArrayList<String>();
	private ArrayList<String> realCode = new ArrayList<String>();
	private ArrayList<String> writen = new ArrayList<String>();

	private void recording(String opcode, int j) {
		if (tenInstractions < 9 && !opcode.equals("")) {

			tenInstractions++;
			finalAddress = linesworking.get(j).substring(0, 4).trim();
			String zero = "";
			for (int i = 0; i < 6 - finalAddress.length(); i++) {
				zero = zero + "0";
			}
			int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16)
					+ Integer.parseInt("3");
			if (length == 30) {
				createRecord = createRecord + "^" + opcode;
				finalAddress = zero + finalAddress;
				singleRecord.add(Integer.toString(length, 16));
				singleRecord.add(createRecord);
				records.add(singleRecord);
				singleRecord = new ArrayList<String>();
				createRecord = "";
				tenInstractions = 0;
				finalAddress = "";
				newRecord = true;
			} else if (length < 30) {
				createRecord = createRecord + "^" + opcode;
				finalAddress = "";
			} else {
				singleRecord.add(Integer.toString(length - 3, 16));
				singleRecord.add(createRecord);
				records.add(singleRecord);
				singleRecord = new ArrayList<String>();
				createRecord = opcode;
				singleRecord.add(linesworking.get(j).substring(0, 4).trim());
				tenInstractions = 1;
				finalAddress = "";
				newRecord = false;
			}

		} else {
			System.out.println("kj;dWEKJFkw;k");
			finalAddress = linesworking.get(j).substring(0, 4).trim();
			String zero = "";
			for (int i = 0; i < 6 - finalAddress.length(); i++) {
				zero = zero + "0";
			}
			String instraction = linesworking.get(j).substring(17, 22).trim();
			if (instraction.equals("WORD")) {
				instraction = "3";
			} else {
				instraction = "3";
			}
			finalAddress = zero + finalAddress;
			System.out.println(j);
			int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16)
					+ Integer.parseInt(instraction);
			if (length > 30) {
				System.out.println(length);
				System.out.println("66666666666           " + Integer.toString(length - 3, 16));
				singleRecord.add(Integer.toString(length - 3, 16));
				singleRecord.add(createRecord);
				records.add(singleRecord);
				singleRecord = new ArrayList<String>();
				createRecord = opcode;
				singleRecord.add(linesworking.get(j).substring(0, 4).trim());
				tenInstractions = 1;
				finalAddress = "";
				newRecord = false;
			} else {
				createRecord = createRecord + "^" + opcode;
				singleRecord.add(Integer.toString(length, 16));
				singleRecord.add(createRecord);
				records.add(singleRecord);
				singleRecord = new ArrayList<String>();
				createRecord = "";
				tenInstractions = 0;
				finalAddress = "";
				newRecord = true;
			}
		}
	}

	private void recordingByteX(String opcode, int j, int numberOfHexa) {

		finalAddress = linesworking.get(j).substring(0, 4).trim();
		String zero = "";
		for (int i = 0; i < 6 - finalAddress.length(); i++) {
			zero = zero + "0";
		}
		finalAddress = zero + finalAddress;
		int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16) + numberOfHexa / 2;
		if (length > 30) {
			singleRecord.add(Integer.toString(length - numberOfHexa / 2, 16));
			singleRecord.add(createRecord);
			records.add(singleRecord);
			singleRecord = new ArrayList<String>();
			createRecord = opcode;
			tenInstractions = 1;
			singleRecord.add(linesworking.get(j).substring(0, 4).trim());
			finalAddress = "";
			newRecord = false;
		} else {
			createRecord = createRecord + "^" + opcode;
			singleRecord.add(Integer.toString(length, 16));
			singleRecord.add(createRecord);
			records.add(singleRecord);
			singleRecord = new ArrayList<String>();
			createRecord = "";
			tenInstractions = 0;
			finalAddress = "";
			newRecord = true;
		}

	}

	private void recordingByteC(String opcode, int j, int numberOfCharacter) {

		finalAddress = linesworking.get(j).substring(0, 4).trim();
		String zero = "";
		for (int i = 0; i < 6 - finalAddress.length(); i++) {
			zero = zero + "0";
		}
		finalAddress = zero + finalAddress;
		int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16) + numberOfCharacter;
		if (length > 30) {
			singleRecord.add(Integer.toString(length - numberOfCharacter, 16));
			singleRecord.add(createRecord);
			records.add(singleRecord);
			singleRecord = new ArrayList<String>();
			createRecord = opcode;
			singleRecord.add(linesworking.get(j).substring(0, 4).trim());
			tenInstractions = 1;
			finalAddress = "";
			newRecord = false;

		} else {
			createRecord = createRecord + "^" + opcode;
			singleRecord.add(Integer.toString(length, 16));
			singleRecord.add(createRecord);
			records.add(singleRecord);
			singleRecord = new ArrayList<String>();
			createRecord = "";
			tenInstractions = 0;
			finalAddress = "";
			newRecord = true;
		}

	}

	private void recordingStop(int j) {
		String instractionCode = linesworking.get(j - 1).substring(17, 22).trim();
		String ending = linesworking.get(j).substring(17, 22).trim();
		if ((!instractionCode.equals("BYTE") && !instractionCode.equals("RESW") && !instractionCode.equals("RESB"))
				&& !ending.equals("END")) {
			finalAddress = linesworking.get(j - 1).substring(0, 4).trim();
			String zero = "";
			for (int i = 0; i < 6 - finalAddress.length(); i++) {
				zero = zero + "0";
			}
			String instraction = linesworking.get(j - 1).substring(17, 22).trim();
			if (instraction.equals("WORD")) {
				instraction = "3";
			} else {
				instraction = "3";
			}
			finalAddress = zero + finalAddress;
			int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16)
					+ Integer.parseInt(instraction);
			singleRecord.add(Integer.toString(length, 16));
			singleRecord.add(createRecord);
			records.add(singleRecord);
			singleRecord = new ArrayList<String>();
			createRecord = "";
			tenInstractions = 0;
			finalAddress = "";
			newRecord = true;

		} else if (ending.equals("END")) {
			System.out.println("lllllllllllllllllllllllllllllllll");
			String[] splitString = createRecord.split("^");
			int finalRealAddress = 0;
			String opMachine = "";
			System.out.println("bnfbbjh         " + splitString[splitString.length - 1]);
			for (int i = writen.size() - 1; i > 0; i--) {
				// System.out.println(writen.get(i));
				// System.out.println(writen.get(i).substring(48,
				// writen.get(i).length()).trim());
				/*
				 * if (linesworking.get(i).length() > 43) { opMachine =
				 * linesworking.get(i).substring(48,
				 * linesworking.get(finalRealAddress).length()-1).trim();
				 * if(writen.get(i).length()>48){
				 * System.out.println(splitString[splitString.length-1]);
				 * System.out.println(writen.get(i).substring(48, 53).trim());
				 * if (writen.get(i).substring(48,
				 * 53).trim().equals(splitString[splitString.length-1])) {
				 * finalRealAddress= i; System.out.println("done"); break; } } }
				 * else { //opMachine = linesworking.get(i).substring(48,
				 * 42).trim(); // trhere // is // acomment }
				 */
				if (writen.get(i).length() > 48) {
					if (!writen.get(i).substring(48, writen.get(i).length() - 1).trim().equals("")) {
						finalRealAddress = i;
						break;
					}
				}

			}
			finalRealAddress = finalRealAddress + 4;
			System.out.println(linesworking.get(finalRealAddress));
			finalAddress = linesworking.get(finalRealAddress).substring(0, 4).trim();
			String zero = "";
			for (int i = 0; i < 6 - finalAddress.length(); i++) {
				zero = zero + "0";
			}
			String instraction = linesworking.get(finalRealAddress).substring(17, 22).trim();

			finalAddress = zero + finalAddress;
			String operand = "";
			if (linesworking.get(finalRealAddress).length() < 42) {
				operand = linesworking.get(finalRealAddress).substring(25, linesworking.get(finalRealAddress).length())
						.trim();
			} else {
				operand = linesworking.get(finalRealAddress).substring(25, 42).trim(); // trhere
				// is
				// acomment
			}
			if (linesworking.get(finalRealAddress).substring(17, 22).trim().equals("BYTE")) {
				if (operand.startsWith("C")) {
					char[] chars = new char[operand.length() - 3];
					for (int i = 2; i < operand.length() - 1; i++) {
						chars[i - 2] = operand.charAt(i);
					}
					recordingByteC("", finalRealAddress, chars.length);
					return;
				} else {
					String checkHexa = operand.substring(3, operand.length() - 1);
					recordingByteX("", finalRealAddress, checkHexa.length());
					return;
				}
			}
			int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16);
			System.out.println("                                      " + length);
			System.out.println(singleRecord.get(0));

			System.out.println(finalAddress);
			singleRecord.add(Integer.toString(length + 3, 16));
			singleRecord.add(createRecord);
			records.add(singleRecord);
			singleRecord = new ArrayList<String>();
			createRecord = "";
			tenInstractions = 0;
			finalAddress = "";
			newRecord = true;

		} else {
			String operand = linesworking.get(j - 1).substring(25, linesworking.get(j - 1).length()).trim();
			if (operand.startsWith("C")) {
				finalAddress = linesworking.get(j - 1).substring(0, 4).trim();
				String zero = "";
				for (int i = 0; i < 6 - finalAddress.length(); i++) {
					zero = zero + "0";
				}
				char[] chars = new char[operand.length() - 3];
				finalAddress = zero + finalAddress;
				int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16)
						+ chars.length;
				singleRecord.add(Integer.toString(length, 16));
				singleRecord.add(createRecord);
				records.add(singleRecord);
				singleRecord = new ArrayList<String>();
				createRecord = "";
				tenInstractions = 0;
				finalAddress = "";
				newRecord = true;

			} else if (operand.startsWith("X")) {
				finalAddress = linesworking.get(j - 1).substring(0, 4).trim();
				String zero = "";
				for (int i = 0; i < 6 - finalAddress.length(); i++) {
					zero = zero + "0";
				}
				String checkHexa = operand.substring(3, operand.length() - 1);
				finalAddress = zero + finalAddress;
				int length = Integer.parseInt(finalAddress, 16) - Integer.parseInt(singleRecord.get(0), 16)
						+ checkHexa.length();
				singleRecord.add(Integer.toString(length, 16));
				singleRecord.add(createRecord);
				records.add(singleRecord);
				singleRecord = new ArrayList<String>();
				createRecord = "";
				tenInstractions = 0;
				finalAddress = "";
				newRecord = true;

			}
		}

	}

	public void objectCode(String fileName) {
		// TODO Auto-generated method stub
		File file = new File(fileName);
		literals = literalsTable;
		try {
			BufferedReader reading = new BufferedReader(new FileReader(file));

			// reading = new BufferedReader(new FileReader("optable.txt"));
			System.out.println("salma");
			String word = reading.readLine();
			while (word != null) {
				realCode.add(word);
				linesworking.add(word.toUpperCase());
				// System.out.println(word.toUpperCase());
				// String l =word.substring(8, 16);
				word = reading.readLine();
			}

			/*
			 * for (int i = 0; i < lines.size(); i++) {
			 * System.out.println(lines.get(i)); }
			 */
			String instruct = linesworking.get(3).substring(17, 22).trim();
			if (instruct.equals("START")) {
				listing.add(realCode.get(0));
				// writen.add(lines.get(0));
			}

			for (int j = 4; j < linesworking.size()
					&& !linesworking.get(j).substring(17, 22).trim().equals("END"); j++) {
				System.out.println(linesworking.get(j).substring(17, 22).trim());
				if (!linesworking.get(j).startsWith(".")) { // check line isn't
															// a comment
					System.out.println(linesworking.get(j).length());
					String label = linesworking.get(j).substring(8, 16).trim();
					System.out.println("label " + label);
					if (label.startsWith("*")) {
						instruct = linesworking.get(j).substring(17, linesworking.get(j).length()).trim();
					} else if (linesworking.get(j).length() < 22) {
						instruct = linesworking.get(j).substring(17, linesworking.get(j).length()).trim();
					} else {
						instruct = linesworking.get(j).substring(17, 22).trim();
					}
					System.out.println("instruct   +" + instruct);
					String operand = "";
					int decimal;
					boolean foundOptable = optable.containsKey(instruct);
					boolean foundDirectuve = directives.contains(instruct);
					String opcode;

					if (label.startsWith("*") && instruct.startsWith("=")) {
						operand = "";

					} else if (linesworking.get(j).length() < 23) {
						operand = "";

					} else if (linesworking.get(j).length() < 42) {
						operand = linesworking.get(j).substring(25, linesworking.get(j).length()).trim();
						System.out.println("     mjkjj +" + operand);
					} else {
						operand = linesworking.get(j).substring(25, 42).trim(); // trhere
						System.out.println("     mjkjj5 +" + operand); // is
						// acomment
					}
					System.out.println("OPERAND  " + operand);
					// System.out.println(instruct+ " "+ operand.isEmpty());
					if (newRecord && !instruct.equals("RESW") && !instruct.equals("RESB")) {
						System.out.println(instruct);
						String start = linesworking.get(j).substring(0, 4).trim();
						String zeros = "";
						for (int i = 0; i < 6 - start.length(); i++) {
							zeros = zeros + "0";
						}
						singleRecord.add(zeros + start);
						newRecord = false;
					}
					if (foundOptable) {
						String[] index = operand.split(",");
						String checkHexa = "";
						if (operand.startsWith("0X")) {
							checkHexa = operand.substring(3, operand.length() - 1);
						} else {
							checkHexa = "g";
						}

						if (isInteger(operand)) {
							int length = realCode.get(j).length();
							String wordSend = realCode.get(j);
							if (length < 44) { // there is no comment
								for (int i = length; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								decimal = Integer.parseInt(operand);
								String hexStr = Integer.toString(decimal, 16);
								int size = hexStr.length();
								String zeros = "";
								for (int i = 0; i < 4 - size; i++) {
									zeros = zeros + "0";
								}
								if (optable.get(instruct).length() == 2) {

								} else {
									zeros = zeros + "0";
								}
								opcode = optable.get(instruct) + zeros + hexStr;
								wordSend = wordSend + opcode;
								writen.add(wordSend);
								recording(opcode, j);
							} else { // there is comment
								String comment = realCode.get(j).substring(43, length);
								wordSend = realCode.get(j).substring(0, 42);
								for (int i = 21; i < 48; i++) {
									wordSend = wordSend + " ";
								}

								decimal = Integer.parseInt(operand);
								String hexStr = Integer.toString(decimal, 16);
								int size = hexStr.length();
								String zeros = "";
								for (int i = 0; i < 4 - size; i++) {
									zeros = zeros + "0";
								}
								if (optable.get(instruct).length() == 2) {

								} else {
									zeros = zeros + "0";
								}
								opcode = optable.get(instruct) + zeros + hexStr;
								wordSend = wordSend + opcode + comment;
								writen.add(wordSend);
								recording(opcode, j);
							}

						} else if (!operand.isEmpty()) {
							boolean symbolFound = symbols.containsKey(operand);
							if (symbolFound) {
								int length = linesworking.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									if (optable.get(instruct).length() == 2) {
										opcode = optable.get(instruct) + symbols.get(operand);
									} else {
										opcode = optable.get(instruct) + "0" + symbols.get(operand);
									}

									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recording(opcode, j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									if (optable.get(instruct).length() == 2) {
										opcode = optable.get(instruct) + symbols.get(operand);
									} else {
										opcode = optable.get(instruct) + "0" + symbols.get(operand);
									}
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recording(opcode, j);
								}
							} else if (operand.contains(",X") && symbols.containsKey(index[0])) { //// index
								String address = optable.get(instruct) + symbols.get(index[0]);
								decimal = Integer.parseInt(address, 16);
								decimal = decimal + 32768; // add 8000 hexa
								String hexa = Integer.toString(decimal, 16);

								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = hexa;
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recording(opcode, j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = hexa;
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recording(opcode, j);
								}
							} else if (operand.equals("*")) {
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									if (optable.get(instruct).length() == 2) {
										opcode = optable.get(instruct) + realCode.get(j).substring(0, 4);
									} else {
										opcode = optable.get(instruct) + "0" + realCode.get(j).substring(0, 4);
									}
									opcode = optable.get(instruct) + realCode.get(j).substring(0, 4);
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recording(opcode, j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									if (optable.get(instruct).length() == 2) {
										opcode = optable.get(instruct) + realCode.get(j).substring(0, 4);
									} else {
										opcode = optable.get(instruct) + "0" + realCode.get(j).substring(0, 4);
									}
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recording(opcode, j);
								}
							} else if (operand.startsWith("0X") && isHexa(checkHexa)) {
								int length = linesworking.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									int hexaAddress = operand.substring(3, operand.length() - 1).length();
									String zeros = ""; // commplet sortage of
														// ddress zeros
									if (hexaAddress < 4) {
										for (int i = 0; i < length; i++) {
											zeros = zeros + "0";
										}
									}
									if (optable.get(instruct).length() == 2) {
										opcode = optable.get(instruct) + zeros
												+ operand.substring(3, operand.length() - 1);
									} else {
										opcode = optable.get(instruct) + "0" + zeros
												+ operand.substring(3, operand.length() - 1);
									}
									opcode = optable.get(instruct) + zeros + operand.substring(3, operand.length() - 1);
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recording(opcode, j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									int hexaAddress = operand.substring(3, operand.length() - 1).length(); // commplet
																											// sortage
																											// of
																											// ddress
																											// zeros
									String zeros = "";
									if (hexaAddress < 4) {
										for (int i = 0; i < length; i++) {
											zeros = zeros + "0";
										}
									}
									if (optable.get(instruct).length() == 2) {
										opcode = optable.get(instruct) + zeros
												+ operand.substring(3, operand.length() - 1);
									} else {
										opcode = optable.get(instruct) + "0" + zeros
												+ operand.substring(3, operand.length() - 1);
									}
									// opcode = optable.get(instruct) + zeros +
									// operand.substring(3, operand.length() -
									// 1);
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recording(opcode, j);
								}

							} else { // error stop record???
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "error:invalid operand ";
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recordingStop(j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "error:invalid operand ";
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recordingStop(j);
								}
							}

						} else if (operand.isEmpty() && instruct.equals("RSUB")) { // RSUB
																					// with
																					// and
																					// without
																					// commment
							System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
							int length = realCode.get(j).length();
							String wordSend = realCode.get(j);
							if (length < 44) { // there is a comment
								for (int i = length; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								opcode = optable.get(instruct) + "0000";
								wordSend = wordSend + opcode;
								writen.add(wordSend);
								recording(opcode, j);
							} else { // there is no comment
								String comment = realCode.get(j).substring(43, length);
								wordSend = realCode.get(j).substring(0, 42);
								for (int i = 21; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								opcode = optable.get(instruct) + "0000";
								wordSend = wordSend + opcode + comment;
								writen.add(wordSend);
								recording(opcode, j);
							}
						} else if (operand.startsWith("=")) { // literal

							if (operand.length() == 2 && operand.equals("=*")) {
								System.out.println("jjjjjjjjjjjjjj222222222222222222222");
							} else {
								System.out.println("jjjjjjjjjjjjjj");
								opcode = "";
								if (optable.get(instruct).length() < 2) {
									opcode = "0" + optable.get(instruct);
								} else {
									opcode = optable.get(instruct);
								}
								opcode = opcode + literals.get(operand.substring(3, operand.length() - 1)).get(2);
								System.out.println("yyyyyyyyyyyyyyy +" + opcode);
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recording(opcode, j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recording(opcode, j);
								}

							}

						}

					} else if (foundDirectuve) {
						if (instruct.equals("WORD")) {
							if (isInteger(operand)) {
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is a comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}

									decimal = Integer.parseInt(operand);
									String hexStr = Integer.toString(decimal, 16);
									int size = hexStr.length();
									String zeros = "";
									for (int i = 0; i < 6 - size; i++) {
										zeros = zeros + "0";
									}
									opcode = zeros + hexStr;
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recording(opcode, j);
								} else { // there is no comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}

									decimal = Integer.parseInt(operand);
									String hexStr = Integer.toString(decimal, 16);
									int size = hexStr.length();
									String zeros = "";
									for (int i = 0; i < 6 - size; i++) {
										zeros = zeros + "0";
									}
									opcode = zeros + hexStr;
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recording(opcode, j);
								}
							} else { // invalid operand for word error stop
										// record???
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is a comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "errror:invalid operand";
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recordingStop(j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "errror:invalid operand";
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recordingStop(j);
								}

							}

						} else if (instruct.equals("BYTE")) {
							System.out.println(linesworking.get(j));
							System.out.println(operand);
							System.out.println(operand.length());
							String checkHexa = operand.substring(2, operand.length() - 1);
							System.out.println(isHexa(checkHexa));
							if (operand.startsWith("X") && isHexa(checkHexa)) {
								int length = linesworking.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is no comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = operand.substring(2, operand.length() - 1);
									wordSend = wordSend + opcode;
									writen.add(wordSend);

									if (tenInstractions < 9) {
										recording(opcode, j);
									} else {
										recordingByteX(opcode, j, checkHexa.length());
									}
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = operand.substring(2, operand.length() - 1);
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);

									if (tenInstractions < 9) {
										recording(opcode, j);
									} else {
										recordingByteX(opcode, j, checkHexa.length());
									}
								}

							} else if (operand.startsWith("C")) {
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (linesworking.get(j).length() < 42) {
									operand = realCode.get(j).substring(25, linesworking.get(j).length()).trim();
								} else {
									operand = realCode.get(j).substring(25, 42).trim(); // trhere
																						// is
																						// acomment
								}
								if (length < 44) { // there is a comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}

									char[] chars = new char[operand.length() - 3];
									opcode = "";
									System.out.println(operand);
									for (int i = 2; i < operand.length() - 1; i++) {
										chars[i - 2] = operand.charAt(i);
										opcode = opcode + Integer.toString((int) chars[i - 2], 16);
										System.out.println(chars[i - 2]);
									}
									wordSend = wordSend + opcode;
									System.out.println("         " + wordSend);
									writen.add(wordSend);
									if (tenInstractions < 9) {
										System.out.println("1");
										recording(opcode, j);
									} else {
										System.out.println("2");
										recordingByteC(opcode, j, chars.length);
									}
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}

									char[] chars = new char[operand.length() - 3];
									opcode = "";
									for (int i = 2; i < operand.length() - 1; i++) {
										chars[i - 2] = operand.charAt(i);
										opcode = opcode + Integer.toString((int) chars[i - 2], 16);
										System.out.println(chars[i - 2]);
									}
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									if (tenInstractions < 9) {
										recording(opcode, j);
									} else {
										recordingByteC(opcode, j, chars.length);
									}

								}

							} else {
								// invalid operand for word
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is a comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "errror:invalid operand";
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									recordingStop(j);
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "errror:invalid operand";
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									recordingStop(j);
								}

							}

						} else if (instruct.equals("RESW") || instruct.equals("RESB")) {
							if (isInteger(operand)) {
								if (tenInstractions > 0 && tenInstractions < 9) {
									recordingStop(j);
								}

								writen.add(realCode.get(j));
							} else {
								int length = realCode.get(j).length();
								String wordSend = realCode.get(j);
								if (length < 44) { // there is a comment
									for (int i = length; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "errror:invalid operand";
									wordSend = wordSend + opcode;
									writen.add(wordSend);
									if (tenInstractions > 0 && tenInstractions < 9) {
										recordingStop(j);
									}
								} else { // there is a comment
									String comment = realCode.get(j).substring(43, length);
									wordSend = realCode.get(j).substring(0, 42);
									for (int i = 21; i < 48; i++) {
										wordSend = wordSend + " ";
									}
									opcode = "errror:invalid operand";
									wordSend = wordSend + opcode + comment;
									writen.add(wordSend);
									if (tenInstractions > 0 && tenInstractions < 9) {
										recordingStop(j);
									}
								}

							}

						} else if (instruct.equals("EQU") || instruct.equals("ORG") || instruct.equals("LTORG")) {

							int length = realCode.get(j).length();
							String wordSend = realCode.get(j);
							if (length < 44) { // there is a comment
								for (int i = length; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								writen.add(wordSend);
								if (tenInstractions > 0 && tenInstractions < 9) {
									recordingStop(j);
								}
							} else { // there is a comment
								String comment = realCode.get(j).substring(43, length);
								wordSend = realCode.get(j).substring(0, 42);
								for (int i = 21; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								wordSend = wordSend + comment;
								writen.add(wordSend);
								if (tenInstractions > 0 && tenInstractions < 9) {
									recordingStop(j);
								}
							}

						}

					} else if (operand.isEmpty() && instruct.startsWith("=")) { // literal
																				// definition
																				// .

						if (operand.length() == 2 && operand.equals("=*")) {

						} else {
							String name = instruct;// .substring(3,
													// instruct.length()-1);
							System.out.println("name+ " + name);
							opcode = literals.get(name).get(0);
							int length = realCode.get(j).length();
							String wordSend = realCode.get(j);
							if (length < 44) { // there is no comment
								for (int i = length; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								wordSend = wordSend + opcode;
								writen.add(wordSend);
								recording(opcode, j);
							} else { // there is a comment
								String comment = realCode.get(j).substring(43, length);
								wordSend = realCode.get(j).substring(0, 42);
								for (int i = 21; i < 48; i++) {
									wordSend = wordSend + " ";
								}
								wordSend = wordSend + opcode + comment;
								writen.add(wordSend);
								recording(opcode, j);
							}

						}
					}

				} else {
					System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
					writen.add(realCode.get(j));
				}
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhh      +" + linesworking.get(j).substring(17, 22).trim());
			}

			for (int i = 0; i < linesworking.size(); i++) {
				if (i > 3 && i < writen.size() + 4) {
					send.add(writen.get(i - 4));
				} else {
					send.add(realCode.get(i));
				}
			}

			String name = realCode.get(3).substring(8, 16).trim();
			int startProgm = Integer.parseInt(realCode.get(3).substring(0, 4).trim(), 16);
			int counter = 0;
			for (int j = 4; j < linesworking.size()
					&& !linesworking.get(j).substring(17, 22).trim().equals("END"); j++) {
				counter = j;
			}
			counter = counter + 1;
			int end = counter;
			int endProgm = Integer.parseInt(realCode.get(counter).substring(0, 4).trim(), 16);
			counter = endProgm - startProgm;
			String length = Integer.toString(counter, 16);
			String zeros = "";
			for (int i = 0; i < 6 - length.length(); i++) {
				zeros = zeros + "0";
			}
			for (int i = realCode.get(3).substring(8, 16).trim().length(); i <= 6; i++) {
				name = name + " ";
			}
			String zerosAddress = "";
			for (int i = 0; i < 6 - realCode.get(3).substring(0, 4).trim().length(); i++) {
				zerosAddress = zerosAddress + "0";
			}
			header = "H" + "^" + name + "^" + zerosAddress + realCode.get(3).substring(0, 4).trim() + "^" + zeros
					+ length; ///// length object code

			if (realCode.get(end).substring(25, realCode.get(end).length()).trim().length() == 0) {
				endRecoird = "E" + "^" + zerosAddress + realCode.get(3).substring(0, 4).trim();
			} else {
				zerosAddress = "";
				String symbol = linesworking.get(end).substring(25, realCode.get(end).length()).trim();
				if (symbols.containsKey(symbol)) {

					for (int i = 0; i < 6 - symbols.get(symbol).length(); i++) {
						zerosAddress = zerosAddress + "0";
					}

					endRecoird = "E" + "^" + zerosAddress + symbols.get(symbol);
				} else if (isInteger(realCode.get(end).substring(25, realCode.get(end).length()).trim())) {
					for (int i = 0; i < 6
							- realCode.get(end).substring(25, realCode.get(end).length()).trim().length(); i++) {
						zerosAddress = zerosAddress + "0";
					}
					endRecoird = "E" + "^" + zerosAddress
							+ realCode.get(end).substring(25, realCode.get(end).length()).trim();
				}

			}
			if (!createRecord.equals("")) {
				recordingStop(end);
				System.out.println("end          nhfn");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			File fileSave = new File("listing file.txt");

			// if file doesn't exists, then create it
			if (!fileSave.exists()) {
				fileSave.createNewFile();
			}

			FileWriter fw = new FileWriter(fileSave.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			// write in file
			for (int i = 0; i < send.size(); i++) {
				bw.write(send.get(i));
				bw.newLine();
			}
			// close connection
			bw.close();
			fileSave = new File("object code.txt");
			fw = new FileWriter(fileSave.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			// write in file
			bw.write(header);
			bw.newLine();
			for (int i = 0; i < records.size(); i++) {
				String text = "T";
				for (int j = 0; j < 3; j++) {
					if (j != 2) {
						text = text + "^" + records.get(i).get(j);
					} else {
						text = text + records.get(i).get(j);
					}

				}
				bw.write(text);
				bw.newLine();
			}
			bw.write(endRecoird);
			/*
			 * for (int i=0; i<writen.size() ;i++ ){ bw.write(writen.get(i));
			 * bw.newLine(); }
			 */
			// close connection
			bw.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private boolean isHexa(String s) {
		boolean interger = false;
		try {
			Integer.parseInt(s, 16);

			// s is a valid integer

			interger = true;
		} catch (NumberFormatException ex) {
			// s is not an integer
		}

		return interger;
	}

	private boolean isInteger(String s) {
		boolean interger = false;
		try {
			Integer.parseInt(s);

			// s is a valid integer

			interger = true;
		} catch (NumberFormatException ex) {
			// s is not an integer
		}

		return interger;
	}

	private boolean isCharacter(String s) {
		boolean character = false;
		try {
			// char
			Integer.parseInt(s);

			// s is a valid integer

			character = true;
		} catch (NumberFormatException ex) {
			// s is not an integer
		}

		return character;
	}

	void debug(String s) {
		System.out.println(s);
	}

	@Override
	public boolean ReadSourceCode(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		FileReader file = new FileReader(fileName);
		writeOpTable();
		ArrayList<String> errors = new ArrayList<>();
		String[] dir = { "START", "END", "RESW", "RESB", "WORD", "BYTE", "ORG", "EQU", "LTORG" };
		for (int i = 0; i < dir.length; i++) {
			directives.add(dir[i]);
		}

		boolean correctFile = true;
		BufferedReader br = null;
		br = new BufferedReader(file);
		BufferedReader reading = null;

		PrintWriter writerLC = new PrintWriter("intermediate.txt", "UTF-8");
		PrintWriter writerSTBL = new PrintWriter("symbol table.txt", "UTF-8");
		boolean start = true;
		try {
			writerLC.println(">>source file");

			writerLC.println("\n");

			// assume if there is no start address is 0000.
			String line = br.readLine();
			String address = "0000";
			String[] spliter = null;
			// assume not necessary end directive.
			boolean end = false;
			String endOperand = "";
			String programLength = "0000";
			String startAddress = "0000";
			String endAddress = "0000";
			String endOp = "";

			int loop = 0;
			while (line != null) {
				debug(line);
				String tempLine = null;
				tempLine = line.toUpperCase();
				spliter = tempLine.split("");
				String opCode = "";
				String label = "";

				String blank1 = "";
				String operand = "";
				String blank2 = "";
				String comment = "";
				String tempLabel = "", tempblank1, actualblank1, actualblank2, tempblank2;
				String actuallabel, tempOpcode = null, actualOpcode, actualoperand = "", tempoperand = null;
				if (!(tempLine.replaceAll("\\s+", "").equals("") || tempLine.replaceAll("\\s+", "").charAt(0) == '.')) {

					for (int i = 0; spliter.length > 0 && i < 8 && i < spliter.length; i++) {
						label += spliter[i];
					}

					for (int i = 8; spliter.length > 8 && i < 9 && i < spliter.length; i++) {
						blank1 += spliter[i];
					}

					for (int i = 9; spliter.length > 9 && i < 15 && i < spliter.length; i++) {
						opCode += spliter[i];
					}

					for (int i = 15; spliter.length > 15 && i < 17 && i < spliter.length; i++) {
						blank2 += spliter[i];
					}

					for (int i = 17; spliter.length > 17 && i < 35 && i < spliter.length; i++) {
						operand += spliter[i];
					}
					for (int i = 35; spliter.length > 35 && i < 66 && i < spliter.length; i++) {
						comment += spliter[i];
					}
					boolean foundsymb = false;
					if (spliter.length >= 67) {
						errors.add("source tempLine exceeds 66 places!" + "\n" + tempLine);
					}

					tempLabel = label.replaceAll("\\s+", "");
					actuallabel = "";
					for (int i = 0; i < label.length(); i++) {
						if (label.charAt(i) == ' ' || (label.charAt(i) + "").equals("\t")) {
							break;
						} else {
							actuallabel += spliter[i];
						}
					}
					if (!actuallabel.equals(tempLabel)) {
						errors.add("invalid label\n" + line);
					} else if (!actuallabel.equals("")) {
						foundsymb = symbols.containsKey(actuallabel);
						if (foundsymb) {
							errors.add("duplicate symbol\n" + line);
						} else {
							String z = "";

							if (address.length() < 4) {
								for (int i = 0; i < 4 - address.length(); i++) {
									z += "0";
								}
							}
							address = z + address;
							symbols.put(tempLabel, address);
						}
					}
					tempOpcode = opCode.replaceAll("\\s+", "");
					actualOpcode = "";
					for (int i = 0; i < opCode.length(); i++) {
						if (opCode.charAt(i) == ' ' || (opCode.charAt(i) + "").equals("\t")) {
							break;
						} else {
							actualOpcode += opCode.charAt(i);
						}
					}
					if (!actualOpcode.equals(tempOpcode)) {
						errors.add("invalid Opcode\n" + line + "" + loop);
					} else {
						boolean foundOp = optable.containsKey(actualOpcode);

						if (!(directives.contains(tempOpcode) || foundOp)) {
							errors.add("invalid Opcode\n" + line + "" + loop);

						}

					}
					tempoperand = operand.replaceAll("\\s+", "");

					if (!((blank1.equals(" ")) || blank1.replaceAll("\\s+", "").equals(""))) {
						errors.add("expected to be blank1\n" + line);

					}
					if (!((blank2.equals("  ")) || blank2.replaceAll("\\s+", "").equals(""))) {
						errors.add("expected to be blank2\n" + line);

					}
					if (loop == 0) {
						if (!tempOpcode.equals("START")) {
							errors.add("expected to be start line\n" + line);
						}

					}
					loop++;

				} //
				if (isInteger(tempoperand)) {
					if (Double.parseDouble(tempoperand) >= Math.pow(2, 15)) {
						errors.add("out of memory\n" + line);
					}
				}
				if (tempLine.replaceAll("\\s+", "").equals("") || tempLine.replaceAll("\\s+", "").charAt(0) == '.') {
					line = br.readLine();
					continue;
					//
				} else if (tempOpcode.equals("START") && start) {
					if (!address.equals("0000")) {
						errors.add("start only expected in the beginning of the source file\n" + line);
					} else {
						address = tempoperand;
						startAddress = tempoperand;
					}

					address = addZeros(address);

					writerLC.println(address + "    " + line);

					start = false;
				} else if (tempOpcode.equals("START") && !start) {

					errors.add("invalid start instruction/n" + line);
					writerLC.println(address + "    " + line);

				} else if (tempOpcode.equals("RESW")) {
					address = addZeros(address);

					writerLC.println(address + "    " + line);

					String factor = tempoperand;
					if (isInteger(tempoperand)) {

						writerSTBL.println(label + "    " + address);
						address = addHexBy_Word(address, factor);
					} else {
						errors.add("fatal error:invalid symbol address\n" + line);
						break;
					}

				} else if (tempOpcode.equals("RESB")) {
					address = addZeros(address);

					writerLC.println(address + "    " + line);
					String factor = tempoperand;
					if (isInteger(tempoperand)) {
						address = addZeros(address);

						writerSTBL.println(label + "    " + address);

						address = addHexBy_Byte(address, factor);
					} else {
						errors.add("fatal error:invalid symbol address\n" + line);
						break;
					}

				} else if (tempOpcode.equals("END")) {

					address = addZeros(address);

					writerLC.println(address + "    " + line);
					end = true;

					String three = "3";
					int sta = Integer.parseInt(startAddress, 16);
					int locc = Integer.parseInt(address, 16);
					int result = locc - sta + 1;
					endAddress = address;
					programLength = convert(result);
					endOp = tempoperand;

					break;
				} else if (tempOpcode.equals("WORD")) {

					address = addZeros(address);
					writerSTBL.println(label + "    " + address);
					writerLC.println(address + "    " + line);

					address = addHexBy_3(address);

				} else if (tempOpcode.equals("BYTE")) {
					if (tempoperand.charAt(1) == '\'' && tempoperand.charAt(tempoperand.length() - 1) == '\''
							&& tempoperand.charAt(0) == 'C' && tempoperand.length() == 3) {
						address = addZeros(address);
						writerLC.println(address + "    " + line);
						int t = operand.length() - 3;
						address = addHexBy_Byte(address, t + "");
					} else if (tempoperand.charAt(1) == '\'' && tempoperand.charAt(tempoperand.length() - 1) == '\''
							&& tempoperand.charAt(0) == 'X' && tempoperand.length() == 3) {
						address = addZeros(address);
						writerLC.println(address + "    " + line);
						int t = operand.length() - 3;
						if (t % 2 == 0) {
							address = addHexBy_Byte(address, t / 2 + "");
						} else {
							errors.add("fatal error:odd byte not valid \n" + tempoperand);
							break;
						}

					} else if (tempoperand.charAt(1) == '\'' && tempoperand.charAt(tempoperand.length() - 1) == '\''
							&& tempoperand.charAt(0) == 'C') {
						address = addZeros(address);
						writerSTBL.println(label + "    " + address);
						writerLC.println(address + "    " + line);
						int t = tempoperand.length() - 3;
						address = addHexBy_Byte(address, t + "");
					} else if (tempoperand.charAt(1) == '\'' && tempoperand.charAt(tempoperand.length() - 1) == '\''
							&& tempoperand.charAt(0) == 'X') {
						address = addZeros(address);
						writerSTBL.println(label + "    " + address);
						writerLC.println(address + "    " + line);
						int t = tempoperand.length() - 3;
						if (t % 2 == 0) {
							address = addHexBy_Byte(address, t / 2 + "");
						} else {
							errors.add("fatal error:odd byte not valid \n" + tempoperand);
							break;
						}

					} else {
						errors.add("ivalid byte format\n" + tempoperand);
					}

				} else if (tempOpcode.equals("EQU")) {

					if (tempoperand.equals("*")) {

						if (tempLabel.equals("")) {
							errors.add("not expected to be blank label here\n" + line);
						} else {
							address = addZeros(address);
							symbols.put(tempLabel, address);
						}

						writerLC.println(address + "    " + line);
					} else {

						ArrayList<String> name = new ArrayList<>();
						ArrayList<String> mark = new ArrayList<>();

						String n = "";
						for (int i = 0; i < tempoperand.length(); i++) {
							if (tempoperand.charAt(i) != '+' && tempoperand.charAt(i) != '-') {
								n += tempoperand.charAt(i);
							} else {
								name.add(n);
								n = "";
								String m = tempoperand.charAt(i) + "";
								mark.add(m);
							}
						}
						name.add(n);

						int checkvalid = 0;
						int valOfExp = 0;

						for (int i = 0; i < name.size(); i++) {
							if (symbols.containsKey(name.get(i))) {

								String val = symbols.get(name.get(i));
								int dec = convertFromHexa(val);
								if (i == 0) {
									checkvalid += 1;
									valOfExp += dec;

								} else {
									String mar = mark.get(i - 1);
									if (mar.equals("+")) {
										checkvalid += 1;
										valOfExp += dec;
									} else if (mar.equals("-")) {
										checkvalid -= 1;
										valOfExp -= dec;
									}
								}

							} else if (isInteger(name.get(i))) {
								int dec = convertFromHexa(name.get(i));
								String mar = "";

								if (i - 1 < 0) {
									valOfExp += dec;

								} else {
									mar = mark.get(i - 1);

								}
								if (mar.equals("+")) {
									valOfExp += dec;
								} else if (mar.equals("-")) {
									valOfExp -= dec;
								}

							} else {
								errors.add("invalid expression\n" + line);

							}
						}

						symbols.put(tempLabel, convert(valOfExp));
						if (checkvalid == 0 || checkvalid == 1) {

							address = convert(valOfExp);
							address = addZeros(address);

							writerLC.println(address + "    " + line);

							// address = addHexBy_3(address);

						} else {
							errors.add("invalid expression\n" + line);
						}
					}

				} else if (tempOpcode.equals("ORG")) {

					if (!tempLabel.equals("")) {
						errors.add("label is expected to be blank\n" + line);
					}

					if (tempoperand.equals("*")) {
						address = addZeros(address);

						writerLC.println(address + "    " + line);
					} else {

						ArrayList<String> name = new ArrayList<>();
						ArrayList<String> mark = new ArrayList<>();

						String n = "";
						for (int i = 0; i < tempoperand.length(); i++) {
							if (tempoperand.charAt(i) != '+' && tempoperand.charAt(i) != '-') {
								n += tempoperand.charAt(i);
							} else {
								name.add(n);
								n = "";
								String m = tempoperand.charAt(i) + "";
								mark.add(m);
							}
						}
						name.add(n);
						int checkvalid = 0;
						int valOfExp = 0;

						for (int i = 0; i < name.size(); i++) {
							if (symbols.containsKey(name.get(i))) {

								String val = symbols.get(name.get(i));
								int dec = convertFromHexa(val);
								if (i == 0) {
									checkvalid += 1;
									valOfExp += dec;

								} else {
									String mar = mark.get(i - 1);
									if (mar.equals("+")) {
										checkvalid += 1;
										valOfExp += dec;
									} else {
										checkvalid -= 1;
										valOfExp -= dec;
									}
								}

							} else if (isInteger(name.get(i))) {
								int dec = convertFromHexa(name.get(i));

								String mar = "";

								if (i - 1 < 0) {
									valOfExp += dec;

								} else {
									mar = mark.get(i - 1);

								}
								if (mar.equals("+")) {
									valOfExp += dec;
								} else if (mar.equals("-")) {
									valOfExp -= dec;
								}

							} else {
								errors.add("invalid expression\n" + line);

							}
						}

						if (checkvalid == 0 || checkvalid == 1) {

							address = convert(valOfExp);
							address = addZeros(address);

							writerLC.println(address + "    " + line);

							// address = addHexBy_3(address);

						} else {
							errors.add("invalid expression\n" + line);
						}
					}
				} else if (tempOpcode.equals("LTORG")) {
					address = addZeros(address);

					writerLC.println(address + "    " + line);

					for (int j = 0; j < litOrder.size(); j++) {

						String key = litOrder.get(j);
						ArrayList<String> value = literalsTable.get(key);

						if (value.get(2).equals(".")) {

							address = addZeros(address);

							if (key.charAt(0) == 'X') {

								String literalVal = key.substring(2, key.length() - 1);
								String value2 = "";
								if (literalVal.length() % 2 != 0) {
									errors.add("odd\n" + line);
									break;
								}

								value.add(2, address);
								writerLC.print(address + "    " + "*        =" + key);
								int k = key.length();
								for (int i = 0; i < 6 - k; i++) {
									writerLC.print(" ");

								}
								writerLC.println();
								String mm = Integer.toString(((key.length() - 3) / 2));
								address = addTwoHex(address, mm);

							} else if (key.charAt(0) == 'C') {

								String literalVal = key.substring(2, key.length() - 1);
								String value2 = "";

								value.add(2, address);
								writerLC.print(address + "    " + "*        =" + key);
								int k = key.length();
								for (int i = 0; i < 6 - k; i++) {
									writerLC.print(" ");

								}
								writerLC.println();

								String mm = Integer.toString(((key.length() - 3)));
								address = addTwoHex(address, mm);
							}

						}
						addHexBy_3(address);

					}

				} else if (tempoperand.length() >= 2 && tempoperand.charAt(0) == '=') {
					String literalVal = tempoperand.substring(3, tempoperand.length() - 1);

					if (tempoperand.charAt(2) == '\'' && tempoperand.charAt(tempoperand.length() - 1) == '\''
							&& tempoperand.charAt(1) == 'C') {

						String value = "";

						for (int i = 0; i < literalVal.length(); i++) {
							char charLit = literalVal.charAt(i);
							int intLit = charLit;
							debug(intLit + "");
							String hex = convert((int) intLit);
							value += hex;
							debug(value);

						}
						if (!literalsTable.containsKey(tempoperand.split("=")[1])) {
							litOrder.add(tempoperand.split("=")[1]);
							ArrayList<String> list = new ArrayList<String>(3);
							list.add(0, value);
							String size = Integer.toString(value.length() / 2);

							list.add(1, size);
							list.add(2, ".");
							String s[] = tempoperand.split("=");
							literalsTable.put(s[1], list);

						}

						address = addZeros(address);
						writerLC.println(address + "    " + line);

						address = addHexBy_3(address);

					} else if (tempoperand.charAt(2) == '\'' && tempoperand.charAt(tempoperand.length() - 1) == '\''
							&& tempoperand.charAt(1) == 'X') {
						if (!literalsTable.containsKey(tempoperand.split("=")[1])) {
							litOrder.add(tempoperand.split("=")[1]);

							ArrayList<String> list = new ArrayList<String>(3);
							list.add(0, literalVal);
							if (literalVal.length() % 2 != 0) {
								errors.add("odd\n" + line);
								break;
							}
							String size = literalVal.length() / 2 + "";

							list.add(1, size);
							list.add(2, ".");

							String s[] = tempoperand.split("=");
							literalsTable.put(s[1], list);
						}
						address = addZeros(address);
						writerLC.println(address + "    " + line);

						address = addHexBy_3(address);

					} else if (tempoperand.charAt(1) == '*') {

						ArrayList<String> list = new ArrayList<String>(3);
						list.add(0, address);

						// assume asterix size =0
						list.add(1, "0");
						String s = "=" + literalVal + asterixNum;
						list.add(2, ".");

						literalsTable.put(s, list);
						address = addZeros(address);
						writerLC.println(address + "    " + line);

						address = addHexBy_3(address);

						asterixNum++;
					} else {
						errors.add("invalid literal\n" + line);

					}
				} else {

					if (!label.replaceAll("\\s+", "").equals("")) {

						writerSTBL.println(label + "    " + address);
					}
					address = addZeros(address);

					writerLC.println(address + "    " + line);
					address = addHexBy_3(address);

				}

				line = br.readLine();

			} // end while
				///
				// Set setOfKeysOp = literalsTable.keySet();

			// Iterator iteratorop = setOfKeysOp.iterator();
			for (int i = 0; i < litOrder.size(); i++) {

				String key = litOrder.get(i);
				ArrayList<String> value = literalsTable.get(key);

				if (value.get(2).equals(".")) {

					address = addZeros(address);
					value.add(2, address);

					writerLC.print(address + "    " + "*        =" + key);
					int k = key.length();
					for (int ii = 0; ii < 6 - k; ii++) {
						writerLC.print(" ");

					}
					writerLC.println();

					if (key.charAt(0) == 'X') {

						String mm = Integer.toString(((key.length() - 3) / 2));
						address = addTwoHex(address, mm);

					} else if (key.charAt(0) == 'C') {

						String mm = Integer.toString(((key.length() - 3)));

						address = addTwoHex(address, mm);

					}

				}

			}
			///

			int startInt = convertFromHexa(startAddress);
			int addressInt = convertFromHexa(address);
			int bound = (int) Math.pow(2, 15);
			if (symbols.containsKey(endOp)) {

			} else if (startInt >= bound || addressInt >= bound || convertFromHexa(endAddress) >= addressInt
					|| convertFromHexa(endAddress) < startInt || startInt < 0 || addressInt < 0
					|| convertFromHexa(endAddress) > bound || (!symbols.containsKey(endOp))) {
				errors.add("out of memory bound 2^15");
			}

			writerLC.println("\n\n");
			writerLC.println(">>symbol table");
			writerLC.println("\n");

			writerLC.println("Symbol  " + "  " + "Address");

			Set setOfKeys = symbols.keySet();

			Iterator iterator = setOfKeys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				int keyLen = key.length();
				String teStr = key;
				for (int i = 0; i < 10 - keyLen; i++) {
					teStr += " ";
				}

				String value = symbols.get(key);

				writerLC.println(teStr + "" + value);
			}

			/////////////

			writerLC.println("\n\n");
			writerLC.println(">>Literal table");
			writerLC.println("\n");

			writerLC.println("Literal  " + "  " + "Val" + "  " + "Length" + "  " + "Address");

			Set setOfKeyss = literalsTable.keySet();

			Iterator iterators = setOfKeyss.iterator();
			while (iterators.hasNext()) {
				String key = (String) iterators.next();
				int keyLen = key.length();
				String teStr = key;
				for (int i = 0; i < 10 - keyLen; i++) {
					teStr += " ";
				}

				ArrayList<String> value = literalsTable.get(key);

				writerLC.print(teStr + "   " + value.get(0) + "   " + value.get(1) + "   " + value.get(2));

				writerLC.println();
			}
			/////////////
			writerLC.println("\n\n");
			writerLC.println(">>Optable");
			writerLC.println("\n");

			writerLC.println("Opcode" + "  " + "Code");
			writerLC.println("\n");

			Set setOfKeysOp2 = optable.keySet();

			Iterator iteratorop2 = setOfKeysOp2.iterator();
			while (iteratorop2.hasNext()) {
				String key = (String) iteratorop2.next();
				String value = optable.get(key);

				int keyLen = key.length();
				String teStr = key;
				for (int i = 0; i < 8 - keyLen; i++) {
					teStr += " ";
				}

				writerLC.println(teStr + "" + value);

			}

			writerLC.println("\n\n");

			writerLC.println(">>program Length");

			writerLC.println(programLength);

			if (!end) {
				errors.add("no end statment.");
			}

			if (errors.size() == 0) {
				// System.out.println("jgjgj " + errors.size());
				writerLC.close();

				//objectCode("intermediate.txt");
				// System.out.println("done");

			} else {
				writerLC.println("\n\n");

				writerLC.println(">>error messages");
				writerLC.println("\n");

				for (int i = 0; i < errors.size(); i++) {
					writerLC.println(errors.get(i));

				}
				writerSTBL.close();
				writerLC.close();

				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// writerLC.close();
				writerSTBL.close();

				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void listingFile(FileInputStream file) {
		// TODO Auto-generated method stub

	}

	private int convertFromHexa(String address) {
		return Integer.parseInt(address, 16);
	}

	String addHexBy_3(String hex) {

		String three = "3";
		int num = Integer.parseInt(hex, 16);
		int thr = Integer.parseInt(three, 16);
		int result = num + thr;
		return convert(result);
	}

	String addTwoHex(String hex, String factor) {

		int num = Integer.parseInt(hex, 16);
		int result = num + Integer.parseInt(factor);
		return convert(result);
	}

	String addHexBy_Word(String hex, String factor) {

		String three = "3";
		int num = Integer.parseInt(hex, 16);
		int thr = Integer.parseInt(three, 16);
		int result = num + Integer.parseInt(factor) * thr;
		return convert(result);
	}

	String addHexBy_Byte(String hex, String factor) {

		String one = "1";
		int num = Integer.parseInt(hex, 16);
		int on = Integer.parseInt(one, 16);
		int result = num + Integer.parseInt(factor) * on;
		return convert(result);
	}

	String convert(int r) {
		return Integer.toHexString(r);

	}

	String addZeros(String label) {
		String z = "";
		for (int i = 0; i < 4 - label.length(); i++) {
			z += "0";
		}
		z += label;
		return z;
	}

	private void writeOpTable() throws FileNotFoundException {
		String optable[] = { "add  24", "and  64", "comp  40", "div  36", "j  60", "jeq  48", "jgt  52", "jlt  56",
				"jsub  72", "lda  00", "ldch  80", "ldl  08", "ldx  04", "mul  32", "or  68", "rd  216", "rsub  76",
				"sta  12", "stch  84", "stl  20", "stx  16", "sub  28", "td  224", "tix  44", "wd  220" };

		for (int i = 0; i < optable.length; i++) {
			String[] splitter = optable[i].split("  ");
			String temp = convert(Integer.parseInt(splitter[1]));

			this.optable.put(splitter[0].toUpperCase(), temp);

		}
	}

	public static void main(String[] args) {
		SIC test = new SIC();

		try {
			test.ReadSourceCode("source file.txt");
			// test.objectCode("intermediate.txt");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// test.objectCode("intermediate.txt");
	}
}