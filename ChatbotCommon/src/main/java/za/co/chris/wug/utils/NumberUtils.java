package za.co.chris.wug.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberUtils {

	private static Logger logger = LoggerFactory.getLogger(NumberUtils.class);
	public static String roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return twoDForm.format(d);
	}

	public static List<Integer> parseIndexes(String... indexes){
		return parseIndexes(Arrays.asList(indexes));
	}
	public static List<Integer> parseIndexes(List<String> indexes) {
		List<Integer> parsedIndexes = new ArrayList<>();
		for (String index : indexes) {
			try {
				if(index.contains("-")){
					parsedIndexes.addAll(expandIndexes(Integer.parseInt(index.split("-")[0].trim()),Integer.parseInt(index.split("-")[1].trim())));
				}else{
					parsedIndexes.add(Integer.parseInt(index));
				}
			} catch (NumberFormatException e) {
				logger.error(index + " could not be parsed");
			}
		}
		return parsedIndexes;
	}

	private static List<Integer> expandIndexes(int indexOne, int indexTwo){
		int start;
		int end;
		List<Integer> indexes = new ArrayList<>();
		if(indexOne <indexTwo){
			start= indexOne;
			end = indexTwo;
		}else{
			end= indexOne;
			start = indexTwo;
		}
		for(int i = start;i<end+1;i++){
			indexes.add(i);
		}
		return indexes;
	}
}
