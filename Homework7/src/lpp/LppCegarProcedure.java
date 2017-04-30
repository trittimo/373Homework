package lpp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LppCegarProcedure {
	public static boolean DEBUG = LppCegarRunner.DEBUG;
	
	public enum OptimizationType {
		Maximize,
		Mininmize
	}
	
	private String yicesCommand;
	private String codeFile;
	private int maxLoop;

	private boolean sat;
	private boolean optimum;

	private OptimizationType optimization;
	private String optimizationVar;

	private List<Map<String, String>> results;
	private String yicesCode;
	
	public LppCegarProcedure(String yicesCommand, String codeFile) {
		this.yicesCommand = yicesCommand;
		this.codeFile = codeFile;
		
		this.sat = false;
		this.optimum = false;
		this.results = new ArrayList<>();
	}
	
	/**
	 * In the absence of this method yices will only be invoked once to solve the 
	 * Linear Programming Problem (LPP). With this method called, Yices will be
	 * invoked several times using the intermediate results to refined the yices code
	 * to force yices to find optimal solution.  
	 * 
	 * @param variable The variable used in the yices file to be used for optimization. 
	 * 			e.g., if Yices code contains an example constraint such as <em>(assert (> y 0))</em>,
	 * 			then the parameter <em>"y"</em> could be passed for optimization.  
	 * @param type	Maximize or Minimize \link{OptimizationType}. 
	 * @param maxLoop The upper bound on loop after which the program will terminate without finding
	 * 			optimal solution. Pass <em>0</em> to force optimal solution before terminating.
	 * @throws IOException
	 */
	public void optimize(String variable, OptimizationType type, int maxLoop) throws IOException {
		this.optimizationVar = variable;
		this.optimization = type;
		this.maxLoop = maxLoop;
		
		Path codeFilePath = Paths.get(this.codeFile);
		this.yicesCode = new String(Files.readAllBytes(codeFilePath));
		
		Path dir = codeFilePath.getParent();
		String name = codeFilePath.getFileName().toString();
		name = name + ".refined";
		
		Path refinedFilePath = Paths.get(dir.toString(), name); 
		this.codeFile = refinedFilePath.toString();
		Files.write(refinedFilePath, this.yicesCode.getBytes());
	}
	
	/**
	 * Returns if the procedure terminated after finding an optimal solution.
	 */
	public boolean isOptimum() {
		return this.optimum;
	}

	/**
	 * Returns true if a solution is found and false otherwise.
	 */
	public boolean isSatisfiable() {
		return this.sat;
	}

	/**
	 * Returns the total number of iterations the procedure ran before terminating.
	 */
	public int getTotalIterations() {
		return this.results.size() + 1;
	}
	
	/**
	 * Returns all of the results found by the procudure including the optimal one.
	 */
	public List<Map<String, String>> getAllResults() {
		return this.results;
	}

	/**
	 * Returns the optimal solution
	 */
	public Map<String, String> getBestResult() {
		return this.results.get(this.results.size() - 1);
	}

	/**
	 * Returns the refined version of the Yices code.
	 */
	public String getRefinedYicesCode() {
		return this.yicesCode;
	}
	
	/**
	 * The main loop of the procedure that calls Yices on the input file,
	 * refines the yices file based on the intermediate results,
	 * and continues the loop until an optimal answer has been found or 
	 * the iterations hit the upper bound.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void execute() throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(this.yicesCommand, this.codeFile);
		
		int loopCount = this.maxLoop;
		while(true) {
			if(DEBUG)
				System.out.format("Running command:'%s %s' ...%n", this.yicesCommand, this.codeFile);
			
			Process process = pb.start();
			int errorCode = process.waitFor();
			
			if(errorCode != 0) {
				System.err.println("The program terminated with an error code " + errorCode);
				System.err.println(this.readAndInterpretOutput(process.getErrorStream(), errorCode));
				return;
			}
			String output = readAndInterpretOutput(process.getInputStream(), errorCode);
			
			if(DEBUG)
				System.out.println(output);
			
			if(this.optimizationVar == null)
				return;
			
			if(this.optimum)
				return;
			
			if(--loopCount == 0)
				return;
			
			this.refineAbstraction();
		}
	}
	
	/**
	 * Reads the output returned by Yices and populates the result set.
	 * If the result is <em>unsat</em> in the current loop but was previously
	 * <em>sat</em>, then this function sets the optimum flag to true.  
	 * 
	 * @param in
	 * @param errorCode
	 * @return
	 * @throws IOException
	 */
	private String readAndInterpretOutput(InputStream in, int errorCode) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line = br.readLine();
		if(line == null)
			return "";
		
		boolean first = false;
		boolean currentSat = false;
		if(errorCode == 0 && line.trim().equals("sat")) {
			currentSat = true;
			first = true;
		}
		
		Map<String, String> result = new HashMap<>();
		
		while(line != null) {
			sb.append(line);
			sb.append("\n");
			
			if(currentSat) {
				if(first)
					first = false;
				else {
					String assignment = line.substring(3, line.length() - 1);
					String[] terms = assignment.split(" ");
					result.put(terms[0], terms[1]);
				}
			}
			
			line = br.readLine();
		}
		br.close();
		
		if(currentSat)
			this.results.add(result);
		
		if(this.sat && !currentSat) {
			this.optimum = true;
		}
		
		this.sat = this.sat? this.sat : currentSat;
		
		return sb.toString();
	}
	
	/**
	 * Refines the Yices code by adding extra constraint to force Yices to find
	 * a better model (solution) for the given LPP. It finds the <em>;;CEGAR</em> string 
	 * in the code and replaces the string with <em>;;CEGAR + Optimization Variable gt or lt Current Solution</em>
	 * based on the supplied optimization type.
	 * 
	 * @throws IOException
	 */
	private void refineAbstraction() throws IOException {
		String refinementOp = ">";
		if(this.optimization == OptimizationType.Mininmize)
			refinementOp = "<";

		String value = this.getBestResult().get(this.optimizationVar);
		String refinement = String.format(";; CEGAR\n(assert (%s %s %s))", refinementOp, this.optimizationVar, value);
		
		String matchString = ";; CEGAR\\s+\\(assert \\(.+\\)\\)";
		if(this.results.size() == 1) {
			matchString = ";; CEGAR";
		}
		
		this.yicesCode = this.yicesCode.replaceFirst(matchString, refinement);
		
		
		Files.write(Paths.get(this.codeFile), this.yicesCode.getBytes());
	}
	
	/**
	 * Helper function to get result for the supplied field as boolean.
	 */
	public boolean getResultAsBoolean(String field) {
		return Boolean.parseBoolean(this.getBestResult().get(field));
	}
	
	/**
	 * Helper function to get result for the supplied field as integer.
	 */
	public int getResultAsInteger(String field) {
		return Integer.parseInt(this.getBestResult().get(field));
	}

	/**
	 * Helper function to get result for the supplied field as float.
	 */
	public float getResultAsFloat(String field) {
		return Float.parseFloat(this.getBestResult().get(field));
	}
	
	
	/**
	 * Helper function to get result for the supplied field as String.
	 */
	public String getResultAsString(String field) {
		return this.getBestResult().get(field);
	}
}
