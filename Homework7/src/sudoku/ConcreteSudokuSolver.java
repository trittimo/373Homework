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

		
		
		// Step 1 - Define necessary variables
		buffer.append(";; Define the board variables\n");
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				buffer.append("(define b" + Integer.toString(i) + Integer.toString(j) + " :: int)\n");
			}
		}

		buffer.append("\n");
		
		// Step 2 - Define that all positions can have value in the range [1-9]
		buffer.append(";; Define the range restriction for the positions\n");
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				String pos = "b" + Integer.toString(i) + Integer.toString(j);
				String cond = String.format("(assert (and (>= %s 1) (<= %s 9)))", pos, pos);
				buffer.append(cond + "\n");
			}
		}
		
		buffer.append("\n");
		
		// Step 3: Assign known values from the partial board to the variables
		buffer.append(";; Assign known values to the board\n");
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == 0) {
					continue;
				}
				String pos = "b" + Integer.toString(i) + Integer.toString(j);
				String val = Integer.toString(board[i][j]);
				String cond = String.format("(assert (= %s %s))", pos, val);
				buffer.append(cond + "\n");
			}
		}
		
		buffer.append("\n");
		
		// Step 4 - Defines rules for the outer-most 9x9 square
		buffer.append(";; Assert that none of the columns/rows have equal values\n");
		// Assert that none of the columns have equal values
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				notEqualRowCol(buffer, i, j);
			}
		}
		
		
		// Step 5 - Define the rules for 9 inner 3x3 squares
		buffer.append(";; Assert that none of the squares have equal values\n");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				notEqualSquare(buffer, i, j);
			}
		}
		
		// Step 6: Check and show model
		buffer.append(NL);
		buffer.append(";; Check and show the identified solution" + NL);
		buffer.append("(check)" + NL);
		buffer.append("(show-model)" + NL);
		
		// Step 5: Return the Yices code for use by the superclass
		return buffer.toString();
	}
	
	private void notEqualSquare(StringBuffer buffer, int row, int col) {
		String pos = String.format("b%d%d", row, col);
		int rowStart = row / 3;
		int colStart = col / 3;
		
		rowStart *= 3;
		colStart *= 3;
		for (int y = rowStart; y < rowStart + 3; y++) {
			for (int x = colStart; x < colStart + 3; x++) {
				if (x == col && y == row)
					continue;
				buffer.append(String.format("(assert (/= %s b%d%d))\n", pos, y, x));
			}
		}
		buffer.append("\n");
	}

	private void notEqualRowCol(StringBuffer buffer, int row, int col) {
		String pos = String.format("b%d%d", row, col);
		
		// Nothing in column is equal
		for (int i = 0; i < 9; i++) {
			if (i == row)
				continue;
			buffer.append(String.format("(assert (/= %s b%d%d))\n", pos, i, col));
		}
		buffer.append("\n");
		
		// Nothing in row is equal
		for (int j = 0; j < 9; j++) {
			if (j == col)
				continue;
			buffer.append(String.format("(assert (/= %s b%d%d))\n", pos, row, j));
		}
		buffer.append("\n\n");
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
			
			
			int x = Integer.parseInt(key.substring(1,2)); //FIXME: Step 1 - extract x from e.getKey(), i.e, extractX(key) 
			int y = Integer.parseInt(key.substring(2, 3)); //FIXME: Step 2- extractY(key)
			int v = Integer.parseInt(value); //FIXME: Step 3 - extract value, i.e, Integer.parseInt(value)

			// Step 4 - Set x,y position of board to the value returned by Yices
			board[x][y] = v;
		});
	}
}
