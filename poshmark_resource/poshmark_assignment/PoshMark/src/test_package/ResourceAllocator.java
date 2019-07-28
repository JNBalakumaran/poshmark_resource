package test_package;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;



class ResourceAllocation{
	
	int hours, cpus;
	float price;
	boolean minimum = false;
	void setMinimum(boolean minimum) {
		this.minimum = minimum;
	}
	
	void getCosts(JsonObject instanes, int hours, int cpus, float price) {
		System.out.println(instanes+" "+hours+" "+cpus+ " "+ price);
		this.hours = hours;
		this.cpus = cpus;
		this.price = price;
		
		
	}
} 


public class ResourceAllocator { 
	
	Gson gson;
	static Scanner scanner;
	static ResourceAllocator resourceAllocator;
	static String[] regString = {"\\d* CPUs", "\\d* hours", "\\$\\d*", "minimum", "as many"};
	
	String getFilePath(String path) {
		
		String filePath = System.getProperty("user.dir") +path;
		return filePath;
		
	}
	
	int[] getStartAndEndInRegex(String regex, String string) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		int[] startEnd =  new int[2];
		if(matcher.find()) {
			 startEnd[0] = matcher.start();
			 startEnd[1] = matcher.end();
		}
		return startEnd;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		resourceAllocator = new ResourceAllocator();
		ResourceAllocation resourceAllocation = new ResourceAllocation();
		
		
		scanner = new Scanner(System.in);
		String filePath = resourceAllocator.getFilePath("/JsonFiles/inputFile.json");
		
		File file = new File(filePath);
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(file));
		JsonObject json = gson.fromJson(reader, JsonObject.class);
		
		filePath = resourceAllocator.getFilePath("/InputFile/input.txt");
		file = new File(filePath);
		String string = new String(Files.readAllBytes(file.toPath()));
		List<String> linputLines = Arrays.asList(string.split("\n"));
		System.out.println(string);
		
		for(String str : linputLines) {
			int hours = 0, cpus = 0;
			float price = 0.0f;
			boolean minimum = false;
			for( String regStr : regString) {
				int[] startEnd = resourceAllocator.getStartAndEndInRegex(regStr, str);
				if(startEnd[0] != 0 && startEnd[1] != 0) {
					String getValues = str.substring(startEnd[0], startEnd[1]).replaceAll("[A-Za-z$ ]", "");
					
					if(regStr.contains("CPUs"))
							cpus = Integer.parseInt(getValues);
					else if(regStr.contains("hours"))
							hours = Integer.parseInt(getValues);
					else if(regStr.contains("$"))
							price = Float.parseFloat(getValues);
					else if(regStr.equalsIgnoreCase("minimum"))
							minimum = true;
							
				}
				
			}
			resourceAllocation.setMinimum(minimum);
			resourceAllocation.getCosts(json, hours, cpus, price);
		}
		
		
		
		
	}

}
