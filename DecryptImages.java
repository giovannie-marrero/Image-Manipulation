import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecryptImages 
{

	public static void main(String[] args)
	{
		System.out.println("The choosen image was:");
		System.out.println("cotorra_boricua.ppm");
		String filename = "cotorra_boricua.ppm";
		
		System.out.println("The message to encrypt was:");
		System.out.println("Hello World!");
		String message = "Hello World!";
		
		System.out.println("The name for the new encrypted image is:");
		System.out.println("estegoimagen.ppm");
		String encryptedFilename = "estegoimagen.ppm";
		
		List<Integer> binaryMessage = convertToBinary(message);
		encryptMessage(filename, encryptedFilename, binaryMessage);
		decryptMessage(filename, encryptedFilename, binaryMessage);
		
		
		String filenamePrueba = "cotorra_boricua-prueba.ppm";
		System.out.println("png image");
		String encryptedFilenamePng = "estegoimagen-png.ppm";
		decryptMessageImage(filenamePrueba, encryptedFilenamePng, binaryMessage);
		
		System.out.println("jpg image");
		String encryptedFilenameJpg = "estegoimagen - jpg.ppm";
		decryptMessageImage(filenamePrueba, encryptedFilenameJpg, binaryMessage);
		
		System.out.println("gif image");
		String encryptedFilenameGif = "estegoimagen-gif.ppm";
		decryptMessageImage(filenamePrueba, encryptedFilenameGif, binaryMessage);
		
	}

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
	
	public static void decryptMessageImage (String originalImage, String encryptedImage, List<Integer> list){
		try {
			BufferedReader bro = new BufferedReader(new FileReader(originalImage));
			BufferedReader bre = new BufferedReader(new FileReader(encryptedImage));
			ArrayList<String> binaryDecryption = new ArrayList<String>();
			for(int i = 0; i < 3; i++) {
				bro.readLine();
				bre.readLine();
			}
			String originalImageRgb = null;
			String encryptedImageRgb = null;
			for(int i = 0; i < list.size(); i++) {
				originalImageRgb = bro.readLine();
				encryptedImageRgb = bre.readLine();
				for(int j = 0; j < originalImageRgb.length(); j++) {
					if (Integer.parseInt(originalImageRgb) == Integer.parseInt(encryptedImageRgb)){
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
}