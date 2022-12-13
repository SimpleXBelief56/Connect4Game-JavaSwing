import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args){
        GameBoard game = new GameBoard();
        game.start();
    }
}

class GameBoard{
    JFrame frame = new JFrame();
    private String playerTurn = "red";
    List<PlayArea> playboxes = new ArrayList<>();
    List<List<Integer>> horizontal = new ArrayList<>();
    List<List<Integer>> rightside = new ArrayList<>();
    List<List<Integer>> leftside = new ArrayList<>();
    public GameBoard(){
        GridLayout gridlayout = new GridLayout(6,7);
        
        gridlayout.setVgap(5);
        gridlayout.setHgap(5);
        frame.setLayout(gridlayout);
        int column = 0;
        for(int i = 0; i < 6*7; i++){
            
            System.out.println(i);
            column++;
            PlayArea button = new PlayArea();
            button.setContentAreaFilled(false);
            // StringBuilder stringPosition = new StringBuilder();
            


            if(i < 7){
                System.out.println("First Row: " + i);
                button.setPosition(6, column);
            }
            if(i >= 7 && i < 14){
                System.out.println("Second Row: " + i);
                button.setPosition(5, column);
            }
            if(i >= 14 && i < 21){
                System.out.println("Third Row: " + i);
                button.setPosition(4, column);
            }
            if(i >= 21 && i < 28){
                System.out.println("Fourth Row: " + i);
                button.setPosition(3, column);
            }
            if(i >= 28 && i < 35){
                System.out.println("Fifth Row: " + i);
                button.setPosition(2, column);
            }
            if(i >= 35){
                button.setPosition(1, column);
            }

            if(column == 7){
                column = 0;
            }
            button.addActionListener(e -> {
                PlayArea savedPosition = new PlayArea();
                for(PlayArea circle: playboxes){
                    if(circle.column == button.column){
                        System.out.println(String.valueOf(circle.isFilled) +" at " + circle.row + "," + circle.column);
                        if(!circle.isFilled){
                            savedPosition = circle;
                        }
                    }
                }
                savedPosition.drawCircle(playerTurn);
                this.checkWinner();
                if(playerTurn == "red"){
                    playerTurn = "yellow";
                } else {
                    playerTurn = "red";
                }
            });

            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            button.emptySpace();
            playboxes.add(button);
            frame.add(button);
        }
    }

    public void start(){
        frame.setSize(600,600);
        frame.setVisible(true);
    }

    // Return List Of Filled Areas
    public List<PlayArea> getFilledAreas(){
        List<PlayArea> filled = new ArrayList<>();
        for(PlayArea circle : playboxes){
            if(circle.isFilled){
                filled.add(circle);
            }
        }
        return filled;
    }

    public void checkWinner(){
        /**
         * [ ][ ][ ][ ][ ][ ][ ]
         * [ ][ ][ ][ ][ ][ ][ ]
         * [ ][ ][o][ ][x][ ][ ]
         * [ ][ ][x][o][ ][ ][ ]
         * [ ][ ][o][x][o][ ][ ]
         * [ ][o][o][x][x][o][ ]
         */
        List<PlayArea> filled = getFilledAreas();
        List<PlayArea> current = new ArrayList<>();
        for(PlayArea area : filled){
            if(area.playareaColor.equals(playerTurn)){
                System.out.println("Filled Area: " + area.getPosition());
                current.add(area);
            }
        }

        // Algorithm
        /**
         * TEST CASE: [1,1]
         * EXPECTED COORDINATES:
         *          [2,2], [3,3], [4,4]
         *          [2,1], [3,1], [4,1]
         *          [1,2], [1,3], [1,4]
         *          [0,1], [-1,2], [-2,3] {Invalid Goes Past Left Wall}
         * 
         * SO OUR FORMULATED FORMULA IS: 
         *          [r,c] where r = row and c = column
         *          Right Bound
         *              r2 = r+3 and c2 = c+3
         *              if r2 > 6 {INVALID} if c2 > 7 {INVALID}
         *              if r2 F-T-E and c2 F-T-E then right bound D-N-E
         *          Left Bound
         *              r2 = r+3 and c2 = c-3
         *              if r2 > 6 {INVALID} if c2 < 1 {INVALID}
         *              if r2 F-T-E and c2 F-T-E then left bound D-N-E
         *          Upper Bound
         *              r2 = r+3 and c2 = c
         *              if r2 > 6 {INVALID} and c2 > 7 {INVALID}
         *              if r2 F-T-E and c2 F-T-E then upper bound D-N-E
         * 
         * PLAYAREA DEFINED LIMITS ARE [THEN]:
         *          minimum: [r,c]
         *          maximum: [r+3, c+3]
         * 
         *                    [4,5]
         *                  [3,4]
         *                [2,3]
         *             [1,2]
         *             [r,c] [c-r=1] [r+3,c+3]
         *          piece [pr<r2,pc<c2]
         *              else
         *          D-N-B
         * 
         *          [5,1]
         *               [4,2]
         *                    [3,3]
         *                         [2,4]
         *                [r,c][r2,c2]
         *                [r+3,c-3]
         *                [r2>pr>r,c2<pc<c]
         *                
         */

        
        // position.add(2);
        // position.add(2);
        // horizontal.add(position);
        // System.out.println("positions: " + horizontal.toString());

        for(PlayArea play : current){
            List<Integer> position = new ArrayList<>();
            int piececount = 1;

            for(int i = 1; i < 4; i++){

                for(PlayArea piece: current){
                    if(piece.row == play.row && piece.column == play.column+i){
                        System.out.println("found piece: " + piece.getPosition());
                        piececount++;
                    }
                }

                // position.clear();
                // position.add(play.row);
                // position.add(play.column+i);
                // if(!horizontal.contains(new ArrayList<>(Arrays.asList(play.row, play.column+i)))){
                //     horizontal.add(new ArrayList<>(Arrays.asList(play.row, play.column+i)));
                // }
            }

            if(piececount == 4){
                System.out.println(playerTurn + " has won the connect4game");
                //System.exit(1);
            }

            piececount = 1;

            for(int i = 0; i < 4; i++){
                
                for(PlayArea piece: current){
                    if(piece.row == play.row+i && piece.column == play.column){
                        piececount++;
                    }
                }
            }

            if(piececount == 4){
                System.out.println(playerTurn + " has won the connect4game");
                //System.exit(1);
            }

            piececount = 1;
            
            // for(int i = 1; i < 4; i++){

            //     for(PlayArea piece: current){
            //         if(piece.row == play.row && piece.column == play.column-i){
            //             piececount++;
            //         }
            //     }

            //     position.clear();
            //     position.add(play.row);
            //     position.add(play.column-i);
            //     if(!horizontal.contains(new ArrayList<>(Arrays.asList(play.row, play.column-i)))){
            //         horizontal.add(new ArrayList<>(Arrays.asList(play.row, play.column-i)));
            //     }
            // }

            // if(piececount == 4){
            //     System.out.println(playerTurn + " has won the connect4game");
            //     //System.exit(1);
            // }

            // piececount = 1;

            if(play.row <= 4){
                for(int i = 1; i < 4; i++){

                    for(PlayArea piece: current){
                        if(piece.row == play.row+i && piece.column == play.column+i){
                            piececount++;
                        }
                    }
                

                    position.clear();
                    position.add(play.row+i);
                    position.add(play.column+i);
                    if(!rightside.contains(new ArrayList<>(Arrays.asList(play.row+i, play.column+i)))){
                        rightside.add(new ArrayList<>(Arrays.asList(play.row+i, play.column+i)));
                    }
                }

                if(piececount == 4){
                    System.out.println(playerTurn + " has won the connect4game");
                    //System.exit(1);
                }

                piececount = 1;

                for(int i = 1; i < 4; i++){

                    for(PlayArea piece: current){
                        if(piece.row == play.row+i && piece.column == play.column-i){
                            piececount++;
                        }
                    }

                    position.clear();
                    position.add(play.row+i);
                    position.add(play.column-i);
                    if(!leftside.contains(new ArrayList<>(Arrays.asList(play.row+i, play.column-i)))){
                        leftside.add(new ArrayList<>(Arrays.asList(play.row+i, play.column-i)));
                    }
                }

                if(piececount == 4){
                    System.out.println(playerTurn + " has won the connect4game");
                    //System.exit(1);
                }

                piececount = 1;

            }




            // if(play.column <= 4){
            //     System.out.println("equals");
                    
            //     for(int i = 1; i < 4; i++){

            //         for(PlayArea piece: current){
            //             if(piece.row == play.row && piece.column == play.column+i){
            //                 System.out.println("found piece: " + piece.getPosition());
            //                 piececount++;
            //             }
            //         }

            //         // position.clear();
            //         // position.add(play.row);
            //         // position.add(play.column+i);
            //         // if(!horizontal.contains(new ArrayList<>(Arrays.asList(play.row, play.column+i)))){
            //         //     horizontal.add(new ArrayList<>(Arrays.asList(play.row, play.column+i)));
            //         // }
            //     }

            //     if(piececount == 4){
            //         System.out.println(playerTurn + " has won the connect4game");
            //         //System.exit(1);
            //     }

            //     piececount = 1;

            //     for(int i = 0; i < 4; i++){
                    
            //         for(PlayArea piece: current){
            //             if(piece.row == play.row+i && piece.column == play.column){
            //                 piececount++;
            //             }
            //         }
            //     }

            //     if(piececount == 4){
            //         System.out.println(playerTurn + " has won the connect4game");
            //         //System.exit(1);
            //     }

            //     piececount = 1;
                
            //     for(int i = 1; i < 4; i++){

            //         for(PlayArea piece: current){
            //             if(piece.row == play.row && piece.column == play.column-i){
            //                 piececount++;
            //             }
            //         }

            //         position.clear();
            //         position.add(play.row);
            //         position.add(play.column-i);
            //         if(!horizontal.contains(new ArrayList<>(Arrays.asList(play.row, play.column-i)))){
            //             horizontal.add(new ArrayList<>(Arrays.asList(play.row, play.column-i)));
            //         }
            //     }

            //     if(piececount == 4){
            //         System.out.println(playerTurn + " has won the connect4game");
            //         //System.exit(1);
            //     }

            //     piececount = 1;

            //     if(play.row <= 4){
            //         for(int i = 1; i < 4; i++){

            //             for(PlayArea piece: current){
            //                 if(piece.row == play.row+i && piece.column == play.column+i){
            //                     piececount++;
            //                 }
            //             }
                    

            //             position.clear();
            //             position.add(play.row+i);
            //             position.add(play.column+i);
            //             if(!rightside.contains(new ArrayList<>(Arrays.asList(play.row+i, play.column+i)))){
            //                 rightside.add(new ArrayList<>(Arrays.asList(play.row+i, play.column+i)));
            //             }
            //         }

            //         if(piececount == 4){
            //             System.out.println(playerTurn + " has won the connect4game");
            //             //System.exit(1);
            //         }

            //         piececount = 1;

            //         for(int i = 1; i < 4; i++){

            //             for(PlayArea piece: current){
            //                 if(piece.row == play.row+i && piece.column == play.column-i){
            //                     piececount++;
            //                 }
            //             }

            //             position.clear();
            //             position.add(play.row+i);
            //             position.add(play.column-i);
            //             if(!leftside.contains(new ArrayList<>(Arrays.asList(play.row+i, play.column-i)))){
            //                 leftside.add(new ArrayList<>(Arrays.asList(play.row+i, play.column-i)));
            //             }
            //         }

            //         if(piececount == 4){
            //             System.out.println(playerTurn + " has won the connect4game");
            //             //System.exit(1);
            //         }

            //         piececount = 1;

            //     }
                    
                
            // } else if(play.column < 4){
            //     for(int i = 1; i < 4; i++){

            //         for(PlayArea piece: current){
            //             if(piece.row == play.row && piece.column == play.column+i){
            //                 piececount++;
            //             }
            //         }

            //         position.clear();
            //         position.add(play.row);
            //         position.add(play.column+i);
            //         if(!horizontal.contains(new ArrayList<>(Arrays.asList(play.row, play.column+i)))){
            //             horizontal.add(new ArrayList<>(Arrays.asList(play.row, play.column+i)));
            //         } 
            //     }

            //     if(piececount == 4){
            //         System.out.println(playerTurn + " has won the connect4game");
            //         //System.exit(1);
            //     }

            //     piececount = 1;

            //     if(play.row < 4){
            //         for(int i = 1; i < 4; i++){

            //             for(PlayArea piece: current){
            //                 if(piece.row == play.row+i && piece.column == play.column+i){
            //                     piececount++;
            //                 }
            //             }

            //             position.clear();
            //             position.add(play.row+i);
            //             position.add(play.column+i);
            //             if(!rightside.contains(new ArrayList<>(Arrays.asList(play.row+i, play.column+i)))){
            //                 rightside.add(new ArrayList<>(Arrays.asList(play.row+i, play.column+i)));
            //             } 
            //         }

            //         if(piececount == 4){
            //             System.out.println(playerTurn + " has won the connect4game");
            //             //System.exit(1);
            //         }

            //         piececount = 1;
            //     }
            // } else if(play.column > 4) {
            //     for(int i = 1; i < 4; i++){

            //         for(PlayArea piece: current){
            //             if(piece.row == play.row && piece.column == play.column-i){
            //                 piececount++;
            //             }
            //         }

            //         position.clear();
            //         position.add(play.row);
            //         position.add(play.column-i);
            //         if(!horizontal.contains(new ArrayList<>(Arrays.asList(play.row, play.column-i)))){
            //             horizontal.add(new ArrayList<>(Arrays.asList(play.row, play.column-i)));
            //         } 
            //     }

            //     if(piececount == 4){
            //         System.out.println(playerTurn + " has won the connect4game");
            //         //System.exit(1);
            //     }

            //     piececount = 1;

            //     if(play.row <= 4){
            //         for(int i = 1; i < 4; i++){

            //             for(PlayArea piece: current){
            //                 if(piece.row == play.row+i && piece.column == play.column-i){
            //                     piececount++;
            //                 }
            //             }

            //             // position.clear();
            //             // position.add(play.row+i);
            //             // position.add(play.column-i);
            //             // if(!leftside.contains(new ArrayList<>(Arrays.asList(play.row+i, play.column-i)))){
            //             //     leftside.add(new ArrayList<>(Arrays.asList(play.row+i, play.column-i)));
            //             // } 
            //         }

            //         if(piececount == 4){
            //             System.out.println(playerTurn + " has won the connect4game");
            //             //System.exit(1);
            //         }

            //         piececount = 1;

            //     }
            // } else {
            //     System.out.println("nothing else to check on this loop");
            // }
            
            // System.out.println("Current Piece Stats");
            // System.out.println("Horizontal Positions: " + horizontal.toString());
            // System.out.println("Right Side: " + rightside.toString());
            // System.out.println("Left Side: " + leftside.toString());
            // System.out.println("Position:" + play.getPosition());
            // Check for the positions
            // int rowcount = 1;
            // for(List poi : horizontal){
            //     for(PlayArea played : current){
            //         if(poi.get(0).equals(played.row) && poi.get(1).equals(played.column)){
            //             System.out.println(poi.get(0) + "," + poi.get(1) + " -> " + played.row + "," + played.column);
            //             rowcount++;
            //         }
            //     }    
            // }

            // horizontal.clear();
            // rightside.clear();
            // leftside.clear();
            
        }


        //Horizontal Positions: [[1, 5], [1, 6], [1, 7], [1, 3], [1, 2], [1, 1], [2, 5], [2, 6], [2, 7], [2, 3], [2, 2], [2, 1], [1, 5], [1, 6], [1, 7], [1, 3], [1, 2], [1, 1], [2, 5], [2, 6], [2, 7], [2, 3], [2, 2], [2, 1], [1, 5], [1, 6], [1, 7], [1, 3], [1, 2], [1, 1]]

        // int rowcount = 1;
        // for(List poi : horizontal){
        //     for(PlayArea played : current){
        //         if(poi.get(0).equals(played.row) && poi.get(1).equals(played.column)){
        //             System.out.println(poi.get(0) + "," + poi.get(1) + " -> " + played.row + "," + played.column);
        //             rowcount++;
        //         }
        //     }    
        // }

        // for(PlayArea piece: current){
        //     System.out.println("Piece Positions: " + piece.getPosition());
        // }

        

        // for(PlayArea played : current){
        //     for(List poi : horizontal){
        //         if(poi.get(0).equals(played.row) && poi.get(1).equals(played.column)){
        //             rowcount++;
        //         }
        //     }
        // } 

        

        // System.out.println("Counts: + " + String.valueOf(rowcount));

        

        
        
    }
}

class PlayArea extends JButton{
    public String position = "";
    public int row = 0;
    public int column = 0;
    public boolean isFilled = false;
    public String playareaColor;
    Color[] c = {Color.RED, Color.YELLOW};

    // public PlayArea(int row, int column){
    //     position = String.format("%d,%d", row, column);
    // }

    public void setPosition(int row, int column){
        // position = String.format("%d,%d", row, column);
        this.row = row;
        this.column = column;
 
    }

    public String getPosition(){
        return String.format("%d,%d", this.row, this.column);
    }

    public void emptySpace(){
        this.setBackground(Color.WHITE);
        this.setIcon(new ImageIcon("null.png"));
    }

    public void drawCircle(String player){
        if(!this.isFilled){
            if(player == "red"){
                this.setIcon(new ImageIcon("red.png"));
                this.playareaColor = "red";
            } else {
                this.setIcon(new ImageIcon("yellow.png"));
                this.playareaColor = "yellow";
            }
            this.isFilled = true;

        } else {
            System.out.println("Filled Already");
        }
    }

}