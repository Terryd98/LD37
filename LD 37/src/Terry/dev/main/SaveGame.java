package Terry.dev.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import Terry.dev.main.entity.mob.Zombie;

public class SaveGame {

	public SaveGame() {

	}

	public static void save(double playerX, double playerY) {
		try {
			PrintWriter writer = new PrintWriter("save.txt", "UTF-8");
			writer.println(playerX);
			writer.println(playerY);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save(List <Zombie> zombies) {
		try {
			PrintWriter writer = new PrintWriter("saveZombies.txt", "UTF-8");
			for(int i =0; i<zombies.size(); i++) {
				writer.println((int)zombies.get(i).x);
				writer.println((int)zombies.get(i).y);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int read(int lineNum) {
		String line = "ERROR";
		try {
			line = Files.readAllLines(Paths.get("save.txt")).get(lineNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (int) Double.parseDouble(line);
	}

}
