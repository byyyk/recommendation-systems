package pl.edu.agh.recommendationsystems.setup;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class RatingFileCropper {
	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = MovieDataGenerator.createRatingFileReader();
		String line = null;
		FileWriter fileWriter = new FileWriter("cropped_ratings.list");
		while ((line = bufferedReader.readLine()) != null) {
			if (!line.matches(".*\\{.*\\}.*")) {
				fileWriter.write(line + "\n");
			}
		}
		fileWriter.close();
		bufferedReader.close();
	}
}
