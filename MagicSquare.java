//package cs445.a3;

import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class MagicSquare {
    // This static value represents the width of the current square
    static int size = -1;
	// Holds the original numbers if in fill mode
	static int[] origNums;
	// Holds the magic sum
	static int magicSum;
	// Indicates fill mode
	static boolean fillMode;

	//************************************************************Full Solution Method***************************************************************//
    public static boolean isFullSolution(int[][] square) {
		
		// checks to see if all the numbers are filled
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (square[i][j] == 0) return false;
			}
		}
		
		// checks to see if the square is valid if the numbers are all filled
		if (reject(square)) return false;
		
		// if the square is valid, it is a full solution
        return true;
    }

	//************************************************************Reject Method**********************************************************************//
	public static boolean reject(int[][] square) {
		//initialize variables
		int rowSum;
		int colSum;
		int rowZeros;
		int colZeros;
		int count = 0;
		int[] nums = new int[size*size];
		int n;
		boolean dup;
		
		// gets the numbers that are already in the square
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (square[i][j] != 0) {
					nums[square[i][j] - 1] = square[i][j];	
				}
				count++;
			}
		}
		
		// checks the sums of the rows and columns
		for (int i = 0; i < size; i++) {
			rowSum = 0;
			colSum = 0;
			rowZeros = 0;
			colZeros = 0;
			for (int j = 0; j < size; j++) {
				rowSum += square[i][j];
				colSum += square[j][i];
				if (square[i][j] == 0) {
					rowZeros++;
				}
				if (square[j][i] == 0) {
					colZeros++;
				}
			}
			
			// if the sum is greater than the magic sum, the square is rejected
			if (rowSum > magicSum || colSum > magicSum) return true;
			// does some additional checks to reduce number of iterations needed
			if (rowZeros == 0) {
				if (rowSum != magicSum) return true;
			} else if (rowZeros == 1) {
				if (rowSum < magicSum-size*size) return true;
				if (rowSum == magicSum) return true;
				n = magicSum - rowSum;
				if (nums[n-1] == n) return true;
			} else if (rowZeros == 2) {
				if (rowSum == magicSum) return true;
				if (rowSum < magicSum-size*size-size*size+1) return true;
			}
			if (colZeros == 0) {
				if (colSum != magicSum) return true;
			} else if (colZeros == 1) {
				if (colSum < magicSum-size*size) return true;
			} else if (rowZeros == 2) {
				if (rowSum < magicSum-size*size-size*size+1) return true;
			}
		}		
		// if the square passes all checks, it is approved
        return false;
    }
	
	//************************************************************Extend Method**********************************************************************//
    public static int[][] extend(int[][] square) {
		// initialize variables
		int[][] temp = new int[size][size];
		int[] nums = new int[size*size];
		int count = 0;
		boolean dup;
		int number = 0;
		int row;
		int col;
		int rowSum = 0;
		int colSum = 0;
		int rowZeros = 0;
		int colZeros = 0;
		int fixed = 0;
		
		//copy into a temporary square
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				temp[i][j] = square[i][j];
				//if (square[i][j] < min) min = square[i][j];
			}
		}
		
		// gets the numbers that are already in the square
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				nums[count] = square[i][j];
				count++;
			}
		}
		
		// finds a new numbers to enter into the square. Uses the sums and the number of zeros left in the array to find the best number to place
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (square[i][j] == 0 && fixed == 0) {
					row = i;
					col = j;
					
					while (row >= 0) {
						colSum += square[row][j];
						if(square[row][j] == 0) colZeros++;
						row--;
					}
					row = i;
					while (row+1 < size) {
						colSum += square[row+1][j];
						if(square[row+1][j] == 0) colZeros++;
						row++;
					}
					while (col >= 0) {
						rowSum += square[i][col];
						if(square[i][col] == 0) rowZeros++;
						col--;
					}
					col = j;
					while (col+1 < size) {
						rowSum += square[i][col+1];
						if(square[i][col+1] == 0) rowZeros++;
						col++;
					}
					
					if (rowZeros == 1 && colZeros == 1) {
						number = magicSum - rowSum;
						if (number + colSum != magicSum) return null;
						for (int k = 0; k < nums.length; k++) {
							if (number == nums[k]) return null;
						}
					} else if (rowZeros == 1) {
						number = magicSum - rowSum;
						for (int k = 0; k < nums.length; k++) {
							if (number == nums[k]) return null;
						}						
					} else if (colZeros == 1) {
						number = magicSum - colSum;
						for (int k = 0; k < nums.length; k++) {
							if (number == nums[k]) return null;
						}
					} else {
						// if a number cannot be found using the sums and number of zeros, the smallest available number is added
						for (int k = square[i][j]; k <= nums.length; k++) {
							dup = true;
							for (int l = 0; l < nums.length; l++) {
								if (k == nums[l]) {
									dup = false;
								}
							}
							if (dup) {
								number = k;
								break;
							}
						}
					}
					fixed = 1;
				}
			}
		}
		
		//adds the number
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (temp[i][j] == 0) {
					temp[i][j] = number;
					return temp;
				}
			}
		}
		
		// if the square cannot be extended, no square is returned
        return null;
    }
	
	//************************************************************Next Method**********************************************************************//
    public static int[][] next(int[][] square) {
		// initialize variables
		int[][] temp = new int[size][size];
		int fixed = 0;
		int[] nums = new int[size*size];
		int count = 0;
		boolean dup;
		int number = 0;
		int max = 9;
		int broken = 0;
		boolean found = false;
		int row;
		int col;
		int rowSum = 0;
		int colSum = 0;
		int rowZeros = 0;
		int colZeros = 0;
		int min = square[0][0];
		
		// gets the numbers that are already in the square
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (square[i][j] != 0) {
					nums[square[i][j] - 1] = square[i][j];
					if (square[i][j] < min) {
						min = square[i][j];
					}
				}
				count++;
			}
		}
		
		// finds the maximum number not in the square
		for (int i = nums.length - 1; i >= 0; i--) {
			if (nums[i] == 0) {
				max = i+1;
				break;
			}
		}
		
		//iterates through the array backward. when it reaches a number, it checks to see if it was one of the original numbers entered
		for (int i = size - 1; i >= 0; i--) {
			for (int j = size - 1; j >= 0; j--) {
				//fills in the temporary array
				temp[i][j] = square[i][j];
				if (fixed == 0 && square[i][j] != 0) {
					if(fillMode) {
						found = false;
						for (int k = 0; k < origNums.length; k++) {
							if (square[i][j] == origNums[k]) {
								found = true;
							}
						}
						if (found) {
							continue;
						}
					}
				
					if (square[i][j] >= max) {
						return null;
					}
//********************finds the best next number****************************************************************//					
					
					row = i;
					col = j;
					while (row >= 0) {
						colSum += square[row][j];
						if(square[row][j] == 0) colZeros++;
						row--;
					}
					row = i;
					while (row+1 < size) {
						colSum += square[row+1][j];
						if(square[row+1][j] == 0) colZeros++;
						row++;
					}
					while (col >= 0) {
						rowSum += square[i][col];
						if(square[i][col] == 0) rowZeros++;
						col--;
					}
					col = j;
					while (col+1 < size) {
						rowSum += square[i][col+1];
						if(square[i][col+1] == 0) rowZeros++;
						col++;
					}
					
					if ((rowZeros == 0 && rowSum == magicSum) || (colZeros == 0 && colSum == magicSum)) {
						return null;
					} else {
					
						for (int k = square[i][j]; k <= nums.length; k++) {
							dup = true;
							for (int l = 0; l < nums.length; l++) {
								if (k == nums[l]) {
									dup = false;
								}
							}
							if (dup) {
								number = k;
								break;
							}
						}
					}		
					
//***********************************************************************************//
					//puts the number in
					temp[i][j] = number;
					fixed = 1;
				}
			}
		}
		
		//returns the array with the adjusted number
		if (fixed == 1) {
			return temp;
		}
		
		//if there is no other number to try, no square is returned
        return null;
    }

	//************************************************************Test IsFullSolution Method**********************************************************************//
    static void testIsFullSolution() {
	
		// creates full Solutions
		int[][][] fullSolutions = new int [][][] {
			{
				// true
				{1,5,9},
				{6,7,2},
				{8,3,4},
			},
			{
				{1,6,8},
				{5,7,3},
				{9,2,4},
			},
			{
				{8,1,6},
				{3,5,7},
				{4,9,2},
			},
			{
				{11,18,25,2,9},
				{10,12,19,21,3},
				{4,6,13,20,22},
				{23,5,7,14,16},
				{17,24,1,8,15},
			},
		};
		// creates not full solutions
		int[][][] notFullSolutions = new int [][][] {
			{
				// false
				{2,5,9},
				{6,7,1},
				{8,3,4},
			},
			{
				{1,5,9},
				{6,7,2},
				{8,3,0},
			},
			{
				{1,5,9},
				{6,0,2},
				{8,3,4},
			},
			{
				{1,5,9},
				{6,2,2},
				{8,3,4},
			},
			{
				{1,5,9},
				{6,1,2},
				{0,3,4},
			},
			{
				{2,0,0},
				{0,1,3},
				{5,0,0},
			},
			{
				{11,18,25,2,10},
				{9,12,19,21,3},
				{4,6,13,20,22},
				{23,5,7,14,16},
				{17,24,1,8,15},
			},
			{
				{11,18,25,2,9},
				{10,12,19,21,3},
				{5,6,13,20,22},
				{23,5,7,14,16},
				{17,24,1,8,15},
			},	
		};
		
		//tests the full solutions
		System.err.println("These should be full:");
        for (int[][] test : fullSolutions) {
			size = test.length;
			magicSum = size*(size*size+1)/2;
            if (isFullSolution(test)) {
                System.err.println("\tFull sol'n:\t");
				printSquare(test);
            } else {
                System.err.println("\tNot full sol'n:\t");
				printSquare(test);
            }
        }
		
		//tests the not full solutions
		System.out.println("These NOT should be full:");
		for (int[][] test : notFullSolutions) {
			size = test.length;
			magicSum = size*(size*size+1)/2;
			if (isFullSolution(test)) {
				System.out.println("\tFull sol'n:\t");		
				printSquare(test);
			} else {
				System.out.println("\tNot full sol'n:\t");
				printSquare(test);
			}
		}
		
		
    }

	//************************************************************Test Reject Method**********************************************************************//
    static void testReject() {
		
		// creates not rejected squares
        int[][][] notRejected = new int [][][] {
			{
				{1,5,9},
				{6,7,2},
				{8,3,4},
			},
			{
				{1,6,8},
				{5,7,3},
				{9,2,4},
			},
			{
				{8,1,6},
				{3,5,7},
				{4,9,2},
			},
			{
				{11,18,25,2,9},
				{10,12,19,21,3},
				{4,6,13,20,22},
				{23,5,7,14,16},
				{17,24,1,8,15},
			},
		};
		
		// creates rejected squares
		int[][][] rejected = new int [][][] {
			{
				{2,5,9},
				{6,7,1},
				{8,3,4},
			},
			{
				{9,8,0},
				{0,0,0},
				{0,0,0},
			},
			{
				{1,5,9},
				{7,0,0},
				{2,0,0},
			},
			{
				{11,1,7,22,24},
				{9,4,2,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
			},
			{
				{11,1,7,2,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
			},
		};
	
		// tests not rejected squares
		System.err.println("These should NOT be rejected:");
        for (int[][] test : notRejected) {
			size = test.length;
			magicSum = size*(size*size+1)/2;
            if (reject(test)) {
                System.err.println("\tRejected:\t");
				printSquare(test);
            } else {
                System.err.println("\tNot rejected:\t");
				printSquare(test);
            }
        }
		
		//tests rejected squares
		System.out.println("These should be rejected:");
		for (int[][] test : rejected) {
			size = test.length;
			magicSum = size*(size*size+1)/2;
			if (reject(test)) {
                System.err.println("\tRejected:\t");
				printSquare(test);
            } else {
                System.err.println("\tNot rejected:\t");
				printSquare(test);
            }
		}
    }

	//************************************************************Test Extend Method**********************************************************************//
    static void testExtend() {
        
		// creates not extendable squares
		int[][][] noExtend = new int [][][] {
			{
				{1,5,9},
				{6,7,2},
				{8,3,4},
			},
			{
				{1,6,8},
				{5,7,3},
				{9,2,4},
			},
			{
				{8,1,6},
				{3,5,7},
				{4,9,2},
			},
			{
				{2,5,9},
				{6,7,1},
				{8,3,0},
			},
			{
				{11,18,25,2,9},
				{10,12,19,21,3},
				{4,6,13,20,22},
				{23,5,7,14,16},
				{17,24,1,8,15},
			},
		};
		
		// creates extendable squares 
		int[][][] extend = new int [][][] {
			{
				{1,5,9},
				{6,7,2},
				{8,0,0},
			},
			{
				{1,5,9},
				{6,0,0},
				{0,0,0},
			},
			{
				{0,0,0},
				{0,0,0},
				{0,0,0},
			},
			{
				{1,5,9},
				{0,0,0},
				{0,0,0},
			},
			{
				{11,18,25,2,10},
				{9,12,19,21,3},
				{4,6,13,20,22},
				{23,0,0,0,0},
				{0,0,0,0,0},
			},
			{
				{11,18,25,2,9},
				{10,12,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
			},	
		};
		
		// tests not extendable squares
		System.err.println("These can NOT be extended:");
        for (int[][] test : noExtend) {
			size = test.length;
            System.err.println("\tExtended ");
			printSquare(test);
			System.err.println(" to ");
			printSquare(extend(test));
        }
	
		// tests extendable squares
        System.err.println("These can be extended:");
        for (int[][] test : extend) {
			size = test.length;
            System.err.println("\tExtended ");
			printSquare(test);
			System.err.println(" to ");
			printSquare(extend(test));
        }
		
		
    }

	//************************************************************Test Next Method**********************************************************************//
    static void testNext() {
      
		// creates no next squares
		int[][][] noNext = new int [][][] {
			{
				{1,9,0},
				{0,0,0},
				{0,0,0},
			},
			{
				{1,5,9},
				{2,6,8},
				{0,0,0},
			},
			{
				{1,5,9},
				{0,0,0},
				{0,0,0},
			},
		};
		
		//creates next squares
		int[][][] next = new int [][][] {
			{
				{1,0,0},
				{0,0,0},
				{0,0,0},
			},
			{
				{1,5,9},
				{6,7,2},
				{8,0,0},
			},
			{
				{1,5,9},
				{6,0,0},
				{0,0,0},
			},
			{
				{11,18,25,2,10},
				{9,12,19,21,3},
				{4,6,13,20,22},
				{23,0,0,0,0},
				{0,0,0,0,0},
			},
			{
				{11,18,25,2,9},
				{10,12,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
			},	
		};
		
		// tests no next squares
		System.err.println("These can NOT be next'd:");
        for (int[][] test : noNext) {
			size = test.length;
            System.err.println("\tNexted ");
			printSquare(test);
			System.err.println(" to ");
			printSquare(next(test));
        }

		// tests next squares
        System.err.println("These can be next'd:");
        for (int[][] test : next) {
			size = test.length;
            System.err.println("\tNexted ");
			printSquare(test);
			System.err.println(" to ");
			printSquare(next(test));
        }		
		
    }

    static String pad(int num) {
        int sum = size * (size * size + 1) / 2;
        int width = Integer.toString(sum).length();
        String result = Integer.toString(num);
        while (result.length() < width) {
            result = " " + result;
        }
        return result;
    }

    public static void printSquare(int[][] square) {
        if (square == null) {
            System.out.println("No solution");
            return;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(pad(square[i][j]) + " ");
            }
            System.out.print("\n");
        }
    }

    public static int[][] readSquare(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        int[][] square = new int[size][size];
		// keeps track of the original square
		origNums = new int[size*size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                square[i][j] = scanner.nextInt();
				origNums[count] = square[i][j];
				count++;
            }
        }
        return square;
    }

	//************************************************************Solve Method**********************************************************************//
    public static int[][] solve(int[][] square) {
        if (reject(square)) return null;
        if (isFullSolution(square)) return square;
        int[][] attempt = extend(square);
        while (attempt != null) {
            int[][] solution;
            solution = solve(attempt);
            if (solution != null) return solution;
            attempt = next(attempt);
        }
        return null;
    }

	//************************************************************Main Method**********************************************************************//
    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("-t")) {
            System.out.println("Running tests...");
            testIsFullSolution();
            testReject();
            testExtend();
            testNext();
        } else if (args.length >= 2) {
            try {
                // First get the specified size
                size = Integer.parseInt(args[0]);
				// Calculates the magic sum
				magicSum = size*(size*size+1)/2;
                // Then read the square from the file
                int[][] square = readSquare(args[1]);
				fillMode = true;
				
                System.out.println("Starting square:");
                printSquare(square);
                System.out.println("\nSolution:");
                printSquare(solve(square));
            } catch (NumberFormatException e) {
                // This happens if the first argument isn't an int
                System.err.println("First argument must be size");
            } catch (FileNotFoundException e) {
                // This happens if the second argument isn't an existing file
                System.err.println("File " + args[1] + " not found");
            }
        } else if (args.length >= 1) {
            try {
                // First get the specified size
                size = Integer.parseInt(args[0]);
				magicSum = size*(size*size+1)/2;
                // Then initialize to a blank square
                int[][] square = new int[size][size];
				fillMode = false;
				
                System.out.println("Starting square:");
                printSquare(square);
                System.out.println("\nSolution:");
                printSquare(solve(square));
            } catch (NumberFormatException e) {
                // This happens if the first argument isn't an int
                System.err.println("First argument must be size");
            }
        } else {
            System.err.println("See usage in assignment description");
        }
    }
}

