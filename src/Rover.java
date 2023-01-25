import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Rover {
    private static final String CARDINAL_DIRECTIONS = "NESW";
    private static final String SPACE = " ";

    private static void checkFile(Scanner input) throws Exception {
            int i = 0;
            while (input.hasNextLine()) {
                i++;
                if (i == 1) {
                    checkFirstLine(input.nextLine());
                } else if (i % 2 == 0) {
                    checkRover(input.nextLine());
                } else if (i % 2 == 1) {
                    checkCommands(input.nextLine());
                }
            }
            if (i % 2 != 1) {
                throw new Exception("File format is wrong.");
            }

        input.close();
    }

    private static void checkCommands(String nextLine) throws Exception {
        for (char command : nextLine.toCharArray()) {
            int i = 0;
            for(char cardinalDirection : "LMR".toCharArray()) {
                if (cardinalDirection == command)
                {
                    i++;
                }
            }
            if (i != 1) {
                throw new Exception("At least one command for the rover is invalid.");
            }
        }
    }

    private static void checkRover(String nextLine) throws Exception {
        String[] values = nextLine.split(SPACE);
        if (values.length != 3) {
            throw new Exception("Rover placement and direction not properly defined.");
        }
        try {
            Integer.parseInt(values[0]);
            Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            throw new Exception("At least one of the rover position is not a number.");
        }
        int i = 0;
        for(char cardinalDirection : CARDINAL_DIRECTIONS.toCharArray()) {
            if (cardinalDirection == values[2].toCharArray()[0])
            {
                i++;
            }
        }
        if (i != 1 || values[2].length() != 1) {
            throw new Exception("Rover direction is invalid.");
        }
    }

    private static void checkFirstLine(String nextLine) throws Exception {
        String[] values = nextLine.split(SPACE);
        if (values.length != 2) {
            throw new Exception("Plateau boundaries not properly defined.");
        }
        try {
            Integer.parseInt(values[0]);
            Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            throw new Exception("At least one of the plateau boundaries is not a number.");
        }
    }

    public static void main(String[] args) {
        try {
            checkFile(getInput(args));
            Scanner input = getInput(args);
            explorePlateau(input);
            input.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Scanner  getInput(String[] args) throws FileNotFoundException {
        return new Scanner(new File(args[0]));
    }

    private static void explorePlateau(Scanner input) {
        int[] upper_right = getUpperRight(input);
        while (input.hasNextLine()) {
            try {
                String[] position = input.nextLine().split(SPACE);
                int x = Integer.parseInt(position[0]);
                int y = Integer.parseInt(position[1]);
                int direction = CARDINAL_DIRECTIONS.indexOf(position[2]);
                String commands = input.nextLine();
                int[] current_position = {x, y, direction};
                for (char command : commands.toCharArray()) {
                    move_rover(current_position, command);
                    x = current_position[0];
                    y = current_position[1];
                    direction = current_position[2];
                    if (isOutOfPlateau(upper_right, x, y)) {
                        throw new Exception("Rover went out of plateau.");
                    }
                }
                System.out.println(x + SPACE + y + SPACE + CARDINAL_DIRECTIONS.charAt(direction));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean isOutOfPlateau(int[] upper_right, int x, int y) {
        return x < 0 || y < 0 || x > upper_right[0] || y > upper_right[1];
    }

    private static void move_rover(int[] current_position, char command) {
        int x = current_position[0];
        int y = current_position[1];
        int direction = current_position[2];
        switch (command) {
            case 'L':
                direction = (direction - 1) < 0 ? direction + 3 : direction - 1;
                break;
            case 'R':
                direction = (direction + 1) % 4;
                break;
            case 'M': switch (direction) {
                case 0 -> y += 1;
                case 1 -> x += 1;
                case 2 -> y -= 1;
                case 3 -> x -= 1;
            }
                break;
        }
        current_position[0] = x;
        current_position[1] = y;
        current_position[2] = direction;
    }

    private static int[] getUpperRight(Scanner input) {
        String[] upper_right_coordinates = input.nextLine().split(SPACE);
        return new int[] {
                Integer.parseInt(upper_right_coordinates[0]),
                Integer.parseInt(upper_right_coordinates[1])
        };
    }
}
