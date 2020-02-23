package utils.csvReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVDataProvider {
    public Iterator<Object> parseCsvData(String fileName) throws IOException {
        BufferedReader input = null;
        File file = new File(fileName);
        input = new BufferedReader(new FileReader(file));
        String line = null;
        ArrayList<Object> data = new ArrayList<Object>();
        while ((line = input.readLine()) != null) {
            String in = line.trim();

            data.add(in);
        }
        input.close();
        return data.iterator();
    }
}
