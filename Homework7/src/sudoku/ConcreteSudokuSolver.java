package sudoku;

import java.util.Map;

//TODO: You will need to complete this file
public class ConcreteSudokuSolver extends AbstractSudokuSolver {
	private static final String NL = System.lineSeparator();

	public ConcreteSudokuSolver(String dataFile, String yicesCommand, String autoYicesFile) {
		super(dataFile, yicesCommand, autoYicesFile);
	}

	@Override
	protected String generateYicesCode(int[][] board) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(";; Auto-Generated Yices Code for Solving Sudoku Problems" + NL);

		// TODO: Generate rest of the Yices code here based on the supplied board
		// If a position i,j has zero value, it means its empty
		// A position can have values from 1-9
		
		// TODO: Step 1 - Define necessary variables

		// TODO: Step 2 - Define that all positions can have value in the range [1-9]
		
		// TODO Step 3: Assign known values from the partial board to the variables
		
		// TODO: Step 4 - Defines rules for the outer-most 9x9 square

		// TODO: Step 5 - Define the rules for 9 inner 3x3 squares
	
		// Step 6: Check and show model
		buffer.append(NL);
		buffer.append(";; Check and show the identified solution" + NL);
		buffer.append("(check)" + NL);
		buffer.append("(show-model)" + NL);
		
		// Step 5: Return the Yices code for use by the superclass
		return buffer.toString();
	}

	
	@Override
	protected void interpretResult(Map<String, String> result, int[][] board) {
		if(result == null)
			return;
		
		// // TODO: For each <K,V> in result.entrySet(), set board[x][y] = V
		// Clearly K must have knowledge of x and y in the String itself for this to work
		result.entrySet().stream().forEach(e -> {
			String key = e.getKey();
			String value = e.getValue();
			
			int x = 0 ; //FIXME: Step 1 - extract x from e.getKey(), i.e, extractX(key) 
			int y = 0 ; //FIXME: Step 2- extractY(key)
			int v = 0; //FIXME: Step 3 - extract value, i.e, Integer.parseInt(value)

			// Step 4 - Set x,y position of board to the value returned by Yices
			board[x][y] = v;
		});
	}
}
