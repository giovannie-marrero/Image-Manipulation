import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *  Este programa hace lo siguiente: 
	1. Esconde un mensaje dentro de una imagen.
	2. Esconde una imagen dentro de otra imagen.
	3. Revela el mensaje que existe escondido.
	4. Revela la imagen que esta escondida.
 * @author Jean L. González Milán 841-19-7031
 * @author Giovannie Marrero Barreto 841-18-5942
 * COTI 4260 LH1
 * 
 */


public class ImageManipulationApp {

	public static void main(String[] args) throws IOException
	{
		
		Scanner scan = new Scanner(System.in);
		//El usuario entra por consola el nombre de la imagen con la que quiere trabajar(debe incluir la extension .ppm)
		System.out.println("Enter the image you want to work with: ");
		String filename = scan.nextLine();
		
		/*
		 * - El usuario escogera entre encriptar un message o una imagen.
		 * - Las opciones son "a" o "b" sin importar si es mayuscula o miniscula
		 * - el programa validara si la opcion escogida es correcta o no
		 * - Si el usuario escoge 'a' debera escribir el message
		 * - Si se escoge 'b' pues escribrira el nombre de la imagen con la extension.
		 */
		System.out.println("Would you like to encrypt a message or an image?\n\t A.Message\tB.Image");
		char decision = scan.nextLine().charAt(0);
		if(decision == 'A' || decision == 'a') {
			System.out.println("Enter the message to encrypt: ");
			String message = scan.nextLine();
			System.out.println("Enter the name for the new encrypted image: (.ppm)");
			String encryptedFilename = scan.nextLine();
			List<Integer> binaryMessage = convertToBinary(message);
			encryptMessage(filename, encryptedFilename, binaryMessage);
			decryptMessage(filename, encryptedFilename, binaryMessage);
		}
		else if(decision == 'B' || decision == 'b') {
			System.out.println("Enter the image to encrypt: ");
			String image = scan.nextLine();
			System.out.println("Enter the name for the new encrypted image: (.ppm)");
			String encryptedFilename = scan.nextLine();
			encryptImage(filename, image, encryptedFilename);
			decryptImage(encryptedFilename, image);
		}
		else
			System.out.println("Invalid choice");

		scan.close();
	}

	/**
	 * Convierte el message del usuario en binario y lo devuelve en una lista de numberos
	 * @param str
	 * @return list
	 */
	public static List<Integer> convertToBinary(String str) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		byte[] bytes = str.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		for (int j = 0; j < binary.length(); j++) {
			char binaryNumber = binary.charAt(j);
			list.add(Integer.parseInt(String.valueOf(binaryNumber)));
		}

		return list;
	}

	/**
	 * convierte una lista de numeros binarios a un String
	 * @param list
	 * @return sb.toString()
	 */
	private static String convertToString(List<String> list) {
		StringBuilder str = new StringBuilder();
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < list.size(); i++) {
			str.append(list.get(i));
		}

		String converted = str.toString();

		for(int j = 0; j <= converted.length(); j++) {

			if(j % 8  == 0 && j != 0) {

				int num = Integer.parseInt(converted.substring(j-8, j),2);
				if(num == 35)
					return converted;
				char ch = (char) num;
				sb.append(ch);
			}

		}

		return sb.toString();
	}

	//Encripta el mensage del usuario dentro de una imagen:
	/**
	 * se lee el documento original ignorando los espacios y usando la lista 
	 * de numeros binarios entoces se le sumaria o restara un uno dependiendo
	 * si el valor rgb, pero si el valor de la lista es cero entoces el valor
	 * rgb se queda igual. entonces Todo eso se escribira el documento escogido a encriptar
	 * @param file - documento original
	 * @param imageToEncrypt - documento a encriptar
	 * @param list - lista de numeros binarios
	 */
	public static void encryptMessage(String file, String imageToEncrypt, List<Integer> list) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			BufferedWriter bw = new BufferedWriter(new FileWriter(imageToEncrypt));

			String P3 = br.readLine();
			String lengthWidth = br.readLine();
			String rgbMaxValue = br.readLine();
			String str;
			String[] rgb = null;
			int j = 0;
			bw.write(P3 + "\n");
			bw.write(lengthWidth + "\n");
			bw.write(rgbMaxValue + "\n");
			while((str = br.readLine()) != null) {
				int i = 0; 
				rgb = str.split(" ");
				while(i < rgb.length) {

					if(j < list.size()) {
						if(list.get(j) == 1) {
							if(Integer.parseInt(rgb[i]) != 255)
								rgb[i] = Integer.toString(Integer.parseInt(rgb[i]) + 1);
							else
								rgb[i] = Integer.toString(Integer.parseInt(rgb[i]) - 1);
						}
					}
					i++;
					j++;
				}

				for (int k = 0; k < rgb.length; k++) {
					bw.write(rgb[k] + " ");
					if(k == rgb.length - 1)
						bw.write("\n");
				}
			}
			br.close();
			bw.close();
		}

		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	
	/**
	 * Compara los valores rgb @param originalImage con los de @encryptedImage y creara una lista de numeros
	 * si los valores rgb son iguales se le añadira un uno a la lista, si son diferentes entoces se añade un 0
	 * El tamaño de la lista de numeros va ser igual que el de @param list
	 * @param originalImage
	 * @param encryptedImage
	 * @param list
	 */
	public static void decryptMessage(String originalImage, String encryptedImage, List<Integer> list){
		try {
			BufferedReader bro = new BufferedReader(new FileReader(originalImage));
			BufferedReader bre = new BufferedReader(new FileReader(encryptedImage));
			ArrayList<String> binaryDecryption = new ArrayList<String>();
			for(int i = 0; i < 3; i++) {
				bro.readLine();
				bre.readLine();
			}
			String [] originalImageRgb = null;
			String [] encryptedImageRgb = null;
			for(int i = 0; i < list.size(); i++) {
				originalImageRgb = bro.readLine().split(" ");
				encryptedImageRgb = bre.readLine().split(" ");
				for(int j = 0; j < originalImageRgb.length; j++) {
					if (Integer.parseInt(originalImageRgb[j]) == Integer.parseInt(encryptedImageRgb[j])) {
						binaryDecryption.add("0");
					}
					else {
						binaryDecryption.add("1");
					}
				}

			}
			bro.close();
			bre.close();
			System.out.println("Decrypted Message: " + convertToString(binaryDecryption));

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convierte a @param str en una ArrayList y le hace @return
	 * @param str
	 * @return bits
	 */
	public static ArrayList<String> convertImageToBinary(String [] str){

		ArrayList<String> bits = new ArrayList<String>();
		for(String rgbValue : str) {
			bits.add(String.format("%8s", Integer.toBinaryString(Integer.parseInt(rgbValue))).replaceAll(" ", "0"));
		}
		return bits;
	}

	/**
	 * Valida si @param imageToEncrypt es mas pequena a @param imageRecipient
	 * Escoge los primero 4 rgb values de cada imagen y los combina para crear una imagen
	 * @param imageRecipient
	 * @param imageToEncrypt
	 * @param encryptedImage
	 */
	public static void encryptImage(String imageRecipient, String imageToEncrypt, String encryptedImage){
		try {
			BufferedReader br = new BufferedReader(new FileReader(imageRecipient));
			BufferedReader br2 = new BufferedReader(new FileReader(imageToEncrypt));
			BufferedWriter bw = new BufferedWriter(new FileWriter(encryptedImage));

			String P3Recipient = br.readLine(); br2.readLine();
			String[] lengthWidthRecipient = br.readLine().split(" "); String[] lengthWidthImageToEncrypt = br2.readLine().split(" ");
			String lengthRecipient = lengthWidthRecipient[0]; String lengthImageToEncrypt = lengthWidthImageToEncrypt[0];
			String widthRecipient = lengthWidthRecipient[1]; String widthImageToEncrypt = lengthWidthImageToEncrypt[1];
			String rgbMaxValueRecipient = br.readLine(); br2.readLine();
			String strImageToEncrypt; String strImageRecipient; String[] rgbImageToEncrypt = null; String[] rgbImageRecipient = null;
			ArrayList<String> merged = new ArrayList<String>();

			if(Integer.parseInt(lengthImageToEncrypt)*Integer.parseInt(widthImageToEncrypt) > Integer.parseInt(lengthRecipient)*Integer.parseInt(widthRecipient)) {
				System.err.print("Image to encrypt is larger than the recipient image");
			}
			else {
				bw.write(P3Recipient + "\n");
				bw.write(lengthRecipient + " " + widthRecipient + "\n");
				bw.write(rgbMaxValueRecipient + "\n");
				while((strImageToEncrypt = br2.readLine()) != null) {
					strImageRecipient = br.readLine();
					rgbImageToEncrypt = strImageToEncrypt.split(" ");
					rgbImageRecipient = strImageRecipient.split(" ");
					merged = merge(convertImageToBinary(rgbImageRecipient),convertImageToBinary(rgbImageToEncrypt));
					ArrayList<String> rgbValues = convertBinaryToRgbValues(merged);
					for (int k = 0; k < rgbValues.size(); k++) {
						bw.write(rgbValues.get(k) + " ");
						if(k == rgbValues.size() - 1)
							bw.write("\n");
					}
				}
				while((strImageRecipient = br.readLine()) != null) {
					rgbImageRecipient = strImageRecipient.split(" ");
					for (int k = 0; k < rgbImageRecipient.length; k++) {
						bw.write(rgbImageRecipient[k] + " ");
						if(k == rgbImageRecipient.length - 1)
							bw.write("\n");
					}
				}
			}

			br.close();
			br2.close();
			bw.close();

		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Uno los primero cuatro bits de las dos imagenes
	 * @param rgbBinaryValuesRecipient
	 * @param rgbBinaryValuesToEncrypt
	 * @return merged
	 */
	private static ArrayList<String> merge(ArrayList<String> rgbBinaryValuesRecipient, ArrayList<String> rgbBinaryValuesToEncrypt){
		ArrayList<String> merged = new ArrayList<String>();
		for(int i = 0; i < rgbBinaryValuesToEncrypt.size(); i++) {
			String substring1 = rgbBinaryValuesToEncrypt.get(i).substring(0,4);
			String substring2 = rgbBinaryValuesRecipient.get(i).substring(0,4);
			merged.add(substring2 + substring1);
		}
		return merged;

	}

	
	/**
	 * Escoge los ultimo 4 rgb values y los pone al principio de una lista y pone los ultimos 4 digitos de la lista como 0
	 * @param rgbBinaryValuesEncryptedImage
	 * @return merged
	 */
	private static ArrayList<String> mergeForDecryption(ArrayList<String> rgbBinaryValuesEncryptedImage){
		ArrayList<String> merged = new ArrayList<String>();
		for(int i = 0; i < rgbBinaryValuesEncryptedImage.size(); i++) {
			String substring = rgbBinaryValuesEncryptedImage.get(i).substring(4,8);
			merged.add(substring + "0000");
		}
		return merged;

	}
	
	/**
	 * Retorna una de numeros rgb usando una lista de numeros binarios
	 * @param mergedList
	 * @return list
	 */
	private static ArrayList<String> convertBinaryToRgbValues(ArrayList<String> mergedList){
		ArrayList<String> list = new ArrayList<String>();
		for(String rgbBinaryValue: mergedList) {
			list.add(String.valueOf(Integer.parseInt(rgbBinaryValue, 2)));
		}
		return list;

	}
	
	/**
	 * Decripta una imagen escondida de una imagen encriptada 
	 * @param encryptedImage
	 * @param hiddenImage
	 */
	public static void decryptImage(String encryptedImage, String hiddenImage) {
		try{
			BufferedReader bre = new BufferedReader(new FileReader(encryptedImage));
			BufferedReader brhi = new BufferedReader(new FileReader(hiddenImage));
			BufferedWriter bw = new BufferedWriter(new FileWriter("decryptedImage.ppm"));

			brhi.readLine(); String[] lengthWidthHiddenImage = brhi.readLine().split(" ");
			String length = lengthWidthHiddenImage[0];
			String width = lengthWidthHiddenImage[1]; 
			String strEncryptedImage; 
			String[] rgbEncryptedImage = null;
			ArrayList<String> merged = new ArrayList<String>();
			for(int i = 0; i < 3; i++) {
				bre.readLine();
			}
			bw.write("P3\n");
			bw.write(length + " " + width + "\n");
			bw.write("255\n");
			int i = 0;
			while(i <= Math.ceil(Double.parseDouble(length)*Double.parseDouble(width)/4)) {
				strEncryptedImage = bre.readLine();

				rgbEncryptedImage = strEncryptedImage.split(" ");
				merged = mergeForDecryption(convertImageToBinary(rgbEncryptedImage));
				ArrayList<String> binaryDecryption = convertBinaryToRgbValues(merged);
				for (int k = 0; k < binaryDecryption.size(); k++) {
					bw.write(binaryDecryption.get(k) + " ");
					if(k == binaryDecryption.size() - 1)
						bw.write("\n");
				}
				i++;
			}
			bre.close();
			brhi.close();
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}