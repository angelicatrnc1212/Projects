 /*
Name: Angelica Charvensym
Date: March 15, 2023
Description: This program creates a Yahtzee game and returns the total score
*/

import java.util.Scanner;
import java.util.Arrays;
public class Yahtzee{

    /**
     * The main method involves several methods.
     * 
     */
    public static void main(String[]args){ 

        //Printing the welcoming prompt and creates array for the 5 dice and scorecard.
        Scanner scan = new Scanner(System.in);
        System.out.println("WELCOME TO THE YAHTZEE GAME!");
        int [] numberOfDice = new int[5];
        int [] scorecard = new int [13];


        //Prints the whole thing inside for 13 times to fill the scorecard
        for (int i = 1; i <= scorecard.length; i++){

            //Fill the dices array with random number
            rollDice(numberOfDice);
            System.out.println("\nTurn " + i + ": " + Arrays.toString(numberOfDice));

            //Calls the reroll method until it satistfied the user or the dice has been rerolled 3 times
            reroll(numberOfDice);


            //User choose which category to are they fulfilling
             System.out.println("Which item would you like to score on your score card?: " +
                "\n0=aces 1=twos 2=threes 3=fours 4=fives 5=sixes" +
                "\n6=three of a kind 7=four of a kind 8=full house" +
                "\n9=small straight 10=large straight 11=yahtzee 12=chance");
            int category = scan.nextInt();

            //Checks whether the category has been chosen or not
            boolean checkCheating = notCheating(scorecard, category);

                //If it has been chosen, it will continue asking to choose for the category
                while(checkCheating == false){
                    System.out.println("This category is filled, please choose another category.\n");
                    System.out.println("\nWhich item would you like to score on your score card?: " +
                    "\n0=aces 1=twos 2=threes 3=fours 4=fives 5=sixes" +
                    "\n6=three of a kind 7=four of a kind 8=full house" +
                    "\n9=small straight 10=large straight 11=yahtzee 12=chance");
                    category = scan.nextInt();
                    checkCheating = notCheating(scorecard, category);
                }
            
            //Filling the scorecard based on the chosen category and prints out the scorecard
            scoreDice(scorecard, numberOfDice,category);
            System.out.println("Scoresheet: " + Arrays.toString(scorecard));
        }
              

        //After finishing all the turns, it sums up all the score with totalScore method and prints it out
        int score = totalScore(scorecard);
        System.out.println("Total Score is: " + score + "\nThank you for playing!");  
    }



    /**
     * This method does not return anything and accepts an array of integer.
     * It is used to generate random numbers.
     * It sets random number in range 1-6 (inclusive) for each element in the array.
     * @param diceArray 
     */
    public static void rollDice(int[] diceArray){
        for (int i = 0; i < diceArray.length; i++){
            diceArray[i] = (int) ((Math.random() * 6) + 1);
        }
    }


    /**
     * This method does not return anything and accepts an array of integer.
     * It is used to generate another random number on specified elements in the array.
     * It prints a prompt on how many element in array that you would like to change and asks for the index of the elements
     * Using the rollDice method, it will generate another random number and sets the new number as the value.
     * @param diceArray
     */
    public static void reroll(int[] diceArray){
        Scanner scan = new Scanner(System.in);
        int i = 1;
        do{
            System.out.println("Reroll attempt " + i + ": How many dices would you like to reroll? ");
            int rerollDice = scan.nextInt();
            if (rerollDice == 0){
                break;
            } else {
            System.out.println("Which dices would you like to reroll? (0-4)");
                for (int j = 0; j < rerollDice; j++){
                 int indexRerollDice = scan.nextInt();
                 diceArray[indexRerollDice] = (int) ((Math.random() * 6) + 1);
                }
                System.out.println(Arrays.toString(diceArray));
            i++;
            }
        } while(i < 4);
        System.out.println("\nYour final dices are " + Arrays.toString(diceArray));
        
    }

    /**
     * This method accepts an array and returns an integer.
     * It is used to sum up all of the value in the array and returns the sum of it.
     */

    public static int sumAllDice(int[] dice){
        int sum = 0;
        for(int k = 0; k < dice.length; k++){
            sum += dice[k];
        }
        return sum;
    }


    /**
     * This method accepts an array and an integer , then returns an integer.
     * It is used to sum up all of the element in the integer that is equal to the facevalue inputed by user.
     * Then, it returns the sum of it.
     * @param dice - an array of integers
     * @param faceValue - an integer
     * @return the sum
     */
    public static int sumOfDice(int[] dice, int faceValue){
        int sum = 0;
        for(int k = 0; k < dice.length; k++){
            if (dice[k] == faceValue){
                sum += dice[k];
            }
        }
        return sum;
    }


    /**
     * This method accepts 2 arrays and an integer.
     * It sets a score(value) according to the category the user choose to the first array inputed. 
     * If the user pick category 0-5, it uses the sumOfDice method to sum the total of facevalue in second array.
     * If the user pick category 6, 7, 12, it uses the sumAllDice method to sum up all the value in the second array.
     * If the user pick other categories, it will set a certain value.
     * @param scorecard - first array where the scores are inserted.
     * @param dice - second array to get the total points based on the category
     * @param category - the index of first array
     */
    public static void scoreDice(int[] scorecard, int[] dice, int category){
        
        if (0 <= category && category <= 5){
            int score = sumOfDice(dice, category+1);
            scorecard[category] = score;
        } else if (category == 6 || category == 7 || category == 12){
            int score = sumAllDice(dice);
            scorecard[category] = score;
        } else if (category == 8){
            scorecard[category] = 25;
        } else if (category == 9){
            scorecard[category] = 30;
        } else if (category == 10){
            scorecard[category] = 40;
        } else if (category == 11){
            scorecard[category] = 50;
        }
    }


    /**
     * This method accepts an array and returns an integer.
     * It sums up all of the value in the array and returns it as the sum.
     * @param scoreCard - an array
     * @return
     */
    public static int totalScore (int[] scoreCard){
        int sum = 0;
        for(int k = 0; k < scoreCard.length; k++){
            sum += scoreCard[k];
        }
        return sum;
    }


    /**
     * This method accepts an array and an integer.
     * It is used to make sure that the user does not insert the value to an already filled index.
     * If the index is filled and the user tried to put to that same index, it will return the sum.
     * @param scoreCard - an array
     * @param category - an integer
     * @return
     */
    public static boolean notCheating (int[] scoreCard, int category){
        boolean notCheating = true;
        if (scoreCard[category] != 0){
            notCheating = false;
        }
        return notCheating;
    }
}
