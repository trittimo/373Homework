package sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// NOTE: You don't need to edit this file unless there are unintentional bugs that need fixing

/**
 * <p>
 * The template class that performs all of the plumbing work for automating sudoku solving
 * and delegates Yices code generation and Yices result interpretation works to its subtypes.
 * </p>
 * <p>
 * Subtypes are expected to properly implement {@link #generateYicesCode(int[][])} and
 * {@link #interpretResult(Map, int[][])} methods.
 * </p>
 */
public abstract class AbstractSudokuSolver {
	public static final boolean DEBUG = SudokuSolverApp.DEBUG;
	public static final int SIZE = 9;
	
	private String dataFile;
	private String yicesCommand;
	private String autoYicesFile;

	private int[][] board;

	private Process process;
	private Map<String, String> result;
	
	public AbstractSudokuSolver(String dataFile, String yicesCommand, String autoYicesFile) {
		this.dataFile = dataFile;
		this.yicesCommand = yicesCommand;
		this.autoYicesFile = autoYicesFile;
		this.board = new int[9][9];
	}

	/**
	 * Templeate method that does most of the heavy lifting work, such as
	 * input file reading, running yices process, and writting result file
	 * by itself. Delegates yices code generation and interpretation of result
	 * to its subtypes.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public final void solve() throws IOException, InterruptedException {
		this.result = null;
		readFromDataFile();
		String code = generateYicesCode(this.board);
		generateYicesFile(code);
		runYices();
		interpretResult(this.result, this.board);
		writeToDataFile();
	}
	
	/**
	 * The method provides the current configuration of Sudoku board as input 
	 * and expects the subtype of this class to return a full yices code for 
	 * solving the supplied partial sudoku board.
	 */
	protected abstract String generateYicesCode(int[][] board);
	
	/**
	 * This method provides a mapping of Yices variable values and its associated assignment
	 * after parsing the result generated after running Yices solver. It expects the subclass
	 * to update the provided board based on the provided Yices result.  
	 * 
	 * @param result The Yices variable to value mapping after running the Yices solver. 
	 * 			<em>null</em> if Yices returned <em>unsat</em> or in case Yices encountered
	 * 			error in the autogenerated code.
	 * @param board The sudoku board to be updated based on the supplied result.
	 */
	protected abstract void interpretResult(Map<String, String> result, int[][] board);

	
	
	
	
	private void readFromDataFile() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(this.dataFile));
		
		if(DEBUG) {
			System.out.println("\nReading input from: " + this.dataFile);
			System.out.println("----------------- Initial Sudoku Board --------------------");
		}
		
		for(int i = 0; i < SIZE; ++i) {
			String line = lines.get(i);

			if(DEBUG) {
				System.out.println(line);
			}

			String terms[] = line.split(",");
			for(int j = 0; j < SIZE; ++j) {
				String term = terms[j].trim();
				
				if(!term.equals("-"))
					this.board[i][j] = Integer.parseInt(terms[j]);
			}
		}
		
		if(DEBUG) {
			System.out.println("----------------- Initial Sudoku Board Ends -----------------");
		}
	}
	
	
	
	
	
	private void writeToDataFile() throws IOException {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < SIZE; ++i) {
			for(int j = 0; j < SIZE; ++j) {
				int value = this.board[i][j];
				if(value == 0)
					buffer.append("-");
				else
					buffer.append("" + this.board[i][j]);
				
				if(j != SIZE - 1)
					buffer.append(",");
					
			}
			buffer.append(System.lineSeparator());
		}

		Path dataFilePath = Paths.get(this.dataFile);
		Path dir = dataFilePath.getParent();
		String name = dataFilePath.getFileName().toString();
		name = "solution-" + name;
		
		Path outputFilePath = Paths.get(dir.toString(), name);
		
		String finalOutput = buffer.toString();
		if(DEBUG) {
			System.out.println("\nWritting final output to: " + outputFilePath);
			System.out.println("----------------- Final Sudoku Board --------------------");
			System.out.println(finalOutput);
			System.out.println("----------------- Final Sudoku Board Ends -----------------");
		}
		
		Files.write(outputFilePath, finalOutput.getBytes());
	}

	
	
	
	
	private void generateYicesFile(String code) throws IOException {
		if(DEBUG) {
			System.out.println("\nWritting the following Yices file to: " + this.autoYicesFile);
			System.out.println("----------------- Yices File --------------------");
			System.out.println(code);
			System.out.println("---------------- Yices File Ends ----------------");
		}

		Files.write(Paths.get(this.autoYicesFile), code.getBytes());
	}

	
	
	
	
	
	private void runYices() throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(this.yicesCommand, this.autoYicesFile);
		
		if(DEBUG)
			System.out.format("\nRunning command:'%s %s' ...%n", this.yicesCommand, this.autoYicesFile);
		this.process = processBuilder.start();
		
		int errorCode = this.process.waitFor();
		
		if(errorCode != 0) {
			System.err.println("Yices terminated with the error code: " + errorCode);
			String errorText = this.parseOutput(process.getErrorStream(), errorCode);
			System.err.println(errorText);
		}
		
		String outputText = this.parseOutput(this.process.getInputStream(), errorCode);
		if(DEBUG) {
			System.out.println("----------------- Yices Result --------------------");
			System.out.println(outputText);
			System.out.println("---------------- Yices Result Ends ----------------");
		}
	}
	
	
	
	
	
	private String parseOutput(InputStream in, int errorCode) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line = br.readLine();
		if(line == null)
			return "";
		
		boolean first = false;
		if(errorCode == 0 && line.trim().equals("sat")) {
			this.result = new HashMap<>();
			first = true;
		}
		
		while(line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			
			if(this.result != null) {
				if(first)
					first = false;
				else {
					line = line.trim();
					if(line.length() > 0) {
						String assignment = line.substring(3, line.length() - 1);
						String[] terms = assignment.split(" ");
						this.result.put(terms[0], terms[1]);
					}
				}
			}
			
			line = br.readLine();
		}
		br.close();
		
		return sb.toString();
	}
}
