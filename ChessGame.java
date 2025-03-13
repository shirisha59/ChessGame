import java.util.Scanner;

public class ChessGame {
    private static final int BOARD_SIZE = 8;
    private static char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private static boolean whiteTurn = true;

    public static void main(String[] args) {
        initializeBoard();
        printBoard();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (whiteTurn) {
                System.out.println("White's turn. Enter move (e.g., e7 e5): ");
            } else {
                System.out.println("Black's turn. Enter move (e.g., e2 e4): ");
            }

            String move = scanner.nextLine();
            String[] parts = move.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input. Please enter your move in the format 'e2 e4'.");
                continue;
            }

            int[] start = parsePosition(parts[0]);
            int[] end = parsePosition(parts[1]);

            if (start == null || end == null) {
                System.out.println("Invalid input. Please enter your move in the format 'e2 e4'.");
                continue;
            }

            if (movePiece(start[0], start[1], end[0], end[1])) {
                printBoard();
                whiteTurn = !whiteTurn; // Switch turns
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    private static void initializeBoard() {
        // Initialize pieces
        // Rooks
        board[0][0] = board[0][7] = 'R';
        board[7][0] = board[7][7] = 'r';
        // Knights
        board[0][1] = board[0][6] = 'N';
        board[7][1] = board[7][6] = 'n';
        // Bishops
        board[0][2] = board[0][5] = 'B';
        board[7][2] = board[7][5] = 'b';
        // Queens
        board[0][3] = 'Q';
        board[7][3] = 'q';
        // Kings
        board[0][4] = 'K';
        board[7][4] = 'k';
        // Pawns
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[1][i] = 'P';
            board[6][i] = 'p';
        }
        // Initialize empty spaces
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '.';
            }
        }
    }

    private static void printBoard() {
		System.out.println("WHITES ARE CAPITAL LETTERS\nBLACKS ARE SMALL LETTERS");
		System.out.println();
System.out.print("  ");
        for (int i = 0; i <= BOARD_SIZE; i++) {
	   
            if(i!=0){
		for (int j = 0; j < BOARD_SIZE; j++) {
			if(j==0){
		System.out.print((9-i)+"     ");
	}
                	System.out.print(board[i-1][j] + "     ");
           	}
		}
	    else if(i==0){
		for(int j=0;j<BOARD_SIZE;j++){
			System.out.print("     "+(char)(97+j));
		}
	    }
	   
	    
            for(int j=0;j<4;j++)System.out.println();
        }
    }

    private static boolean movePiece(int startX, int startY, int endX, int endY) {
        char piece = board[startX][startY];
        if (piece == '.' || (whiteTurn && Character.isLowerCase(piece)) || (!whiteTurn && Character.isUpperCase(piece))) {
            return false;
        }

        boolean validMove = false;

        switch (Character.toUpperCase(piece)) {
            case 'P':
                validMove = isValidPawnMove(startX, startY, endX, endY, piece);
                break;
            case 'R':
                validMove = isValidRookMove(startX, startY, endX, endY);
                break;
            case 'N':
                validMove = isValidKnightMove(startX, startY, endX, endY);
                break;
            case 'B':
                validMove = isValidBishopMove(startX, startY, endX, endY);
                break;
            case 'Q':
                validMove = isValidQueenMove(startX, startY, endX, endY);
                break;
            case 'K':
                validMove = isValidKingMove(startX, startY, endX, endY);
                break;
        }

        if (validMove) {
            board[endX][endY] = piece;
            board[startX][startY] = '.';
        }

        return validMove;
    }

    private static boolean isValidPawnMove(int startX, int startY, int endX, int endY, char piece) {
        if (piece == 'P') {
            if (startY == endY && board[endX][endY] == '.' && ((startX == 1 && endX == startX + 2) || (endX == startX + 1))) {
                return true;
            }
            if (Math.abs(startY - endY) == 1 && endX == startX + 1 && Character.isLowerCase(board[endX][endY])) {
                return true;
            }
        } else if (piece == 'p') {
            if (startY == endY && board[endX][endY] == '.' && ((startX == 6 && endX == startX - 2) || (endX == startX - 1))) {
                return true;
            }
            if (Math.abs(startY - endY) == 1 && endX == startX - 1 && Character.isUpperCase(board[endX][endY])) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidRookMove(int startX, int startY, int endX, int endY) {
        if (startX == endX) {
            for (int y = Math.min(startY, endY) + 1; y < Math.max(startY, endY); y++) {
                if (board[startX][y] != '.') return false;
            }
            return board[endX][endY] == '.' || Character.isUpperCase(board[startX][startY]) != Character.isUpperCase(board[endX][endY]);
        }
        if (startY == endY) {
            for (int x = Math.min(startX, endX) + 1; x < Math.max(startX, endX); x++) {
                if (board[x][startY] != '.') return false;
            }
            return board[endX][endY] == '.' || Character.isUpperCase(board[startX][startY]) != Character.isUpperCase(board[endX][endY]);
        }
        return false;
    }

    private static boolean isValidKnightMove(int startX, int startY, int endX, int endY) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        return dx * dy == 2 && (board[endX][endY] == '.' || Character.isUpperCase(board[startX][startY]) != Character.isUpperCase(board[endX][endY]));
    }

    private static boolean isValidBishopMove(int startX, int startY, int endX, int endY) {
        if (Math.abs(endX - startX) != Math.abs(endY - startY)) return false;
        int dx = endX > startX ? 1 : -1;
        int dy = endY > startY ? 1 : -1;
        for (int i = 1; i < Math.abs(endX - startX); i++) {
            if (board[startX + i * dx][startY + i * dy] != '.') return false;
        }
        return board[endX][endY] == '.' || Character.isUpperCase(board[startX][startY]) != Character.isUpperCase(board[endX][endY]);
    }

    private static boolean isValidQueenMove(int startX, int startY, int endX, int endY) {
        return isValidRookMove(startX, startY, endX, endY) || isValidBishopMove(startX, startY, endX, endY);
    }

    private static boolean isValidKingMove(int startX, int startY, int endX, int endY) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        return (dx <= 1 && dy <= 1) && (board[endX][endY] == '.' || Character.isUpperCase(board[startX][startY]) != Character.isUpperCase(board[endX][endY]));
    }

    private static int[] parsePosition(String position) {
        if (position.length() != 2) return null;
        char file = position.charAt(0);
        char rank = position.charAt(1);
        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') return null;
        return new int[]{8 - (rank - '0'), file - 'a'};
    }
}