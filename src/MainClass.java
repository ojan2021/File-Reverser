import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Duration;
import java.time.Instant;

public class MainClass {

	private static final int BLOCK_SIZE = 4096;
	/**
	 * Default block size for Windows. Program only tested in Windows. It is not
	 * guaranteed that it will work properly on other OS.
	 **/
	static final String FILEPATH = "sampleInput"; // File should be in the same path with the program. Only the name of
													// the file is enough.

	public static void main(String[] args) throws IOException {
		File file = new File(FILEPATH);
		long fileSize = file.length();

		if (fileSize <= BLOCK_SIZE) {
			System.out.println("It is small file");
			
			Instant starts = Instant.now();
			byte[] temp = new byte[1];
			
			for (long i = 0; i < (fileSize / 2); i++) {
				temp = readFromFile(FILEPATH, i, 1);
				writeToFile(FILEPATH, readFromFile(FILEPATH, fileSize - i - 1, 1), i);
				writeToFile(FILEPATH, temp, fileSize - i - 1);
			}
			
			Instant ends = Instant.now();
			System.out.println("It took " + Duration.between(starts, ends).toSeconds() + " seconds to reverse");

			
		} else {
			System.out.println("It is big file");

			byte[] buffer1 = new byte[BLOCK_SIZE];
			byte[] buffer2 = new byte[BLOCK_SIZE];
			byte[] tempBuf = new byte[BLOCK_SIZE];

			Instant starts = Instant.now();
			long k = BLOCK_SIZE;
			
			for (long i = 0; i < (fileSize / 2); i = i + BLOCK_SIZE) {
				buffer1 = readFromFile(FILEPATH, i, BLOCK_SIZE);
				buffer2 = readFromFile(FILEPATH, fileSize - k, BLOCK_SIZE);

				for (int j = 0, l = BLOCK_SIZE - 1; j < l; j++, l--) {
					tempBuf[j] = buffer1[j];
					buffer1[j] = buffer1[l];
					buffer1[l] = tempBuf[j];
				}

				for (int j = 0, l = BLOCK_SIZE - 1; j < l; j++, l--) {
					tempBuf[j] = buffer2[j];
					buffer2[j] = buffer2[l];
					buffer2[l] = tempBuf[j];
				}

				writeToFile(FILEPATH, buffer2, i);
				writeToFile(FILEPATH, buffer1, fileSize - k);
				k = k + BLOCK_SIZE;
			}
			
			Instant ends = Instant.now();
			System.out.println("It took " + Duration.between(starts, ends).toSeconds() + " seconds to reverse");
		}

	}

	private static byte[] readFromFile(String filePath, long position, long size) throws IOException {
		RandomAccessFile file = new RandomAccessFile(filePath, "r");
		file.seek(position);
		byte[] bytes = new byte[(int) size];
		file.read(bytes);
		file.close();
		return bytes;
	}

	private static void writeToFile(String filePath, byte[] data, long position) throws IOException {
		RandomAccessFile file = new RandomAccessFile(filePath, "rw");
		file.seek(position);
		file.write(data);
		file.close();
	}

}
