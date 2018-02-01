/*
* Skeleton Program code for the AQA A Level Paper 1 2018 examination
* this code should be used in conjunction with the Preliminary Material
* written by the AQA Programmer Team
* developed using Netbeans IDE 8.1
*
*/

package words.with.aqa;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

public class Main {
    
    class QueueOfTiles // linear queue: 1 per game, like tile bag, FIFO
    {
        protected char[] contents; // use an array for chars in queue
        protected int rear; // keep track of rear of queue
        protected int maxSize;
        
        QueueOfTiles(int maxSize) // constructor
        {
            contents = new char[maxSize];// create contents array up to max allowed size
            rear = -1; // initially it is empty
            this.maxSize = maxSize; //set max size of queue
            for (int count = 0; count < maxSize; count++) //iterate through up to max size
            { 
                add(); // add letters to the array
            }
        }

        boolean isEmpty() // check if queue is empty
        {
            if (rear == -1)  // if empty
            {
                return true; //true
            }
            else // else
            {
                return false; // false
            }
        }
        
        char remove() // remove first element of queue
        {
            if (isEmpty()) {
                return '\n'; // new line at end of queue
            }
            else
            {
                char item = contents[0]; // store the first item of queue in a variable to be returned
                for (int count = 1; count < rear + 1; count++) { // move each element one forwards in queue
                    contents[count - 1] = contents[count];
                }
                contents[rear] = '\n'; // change last element to empty (new line)
                rear -= 1; // update index of rear element
                return item; //return first element
            }
        }
        
        void add() // add random chars to queue 
        {
            Random rnd = new Random(); 
            if (rear < maxSize - 1) // if haven't reached max size of queue 
            {
                int randNo = rnd.nextInt(25); // generate random number
                rear += 1; // add 1 to the rear
                contents[rear] = (char)(65 + randNo); // add 65 to random number (so that lower case  alphabet letter is stored)
            }
        }
        
        void show()
        {
            if (rear != -1) //if not empty
            {
                Console.println();
                Console.print("The contents of the queue are: ");
                for(char item : contents) // iterate through queue
                {
                    Console.print(item); // print each of the queue
                }
                Console.println();
            }
        }
    }
        
    Map createTileDictionary() // links each letter to its score
    {
        Map<Character, Integer> tileDictionary = new HashMap<Character, Integer>(); // create map
        for (int count = 0; count < 26; count++) 
        {
            switch (count) {
                case 0: // a
                case 4: // e
                case 8: // i
                case 13: // n
                case 14: // o
                case 17: // r
                case 18: // s
                case 19: // t
                    tileDictionary.put((char)(65 + count), 1);
                    break;
                case 1: // b
                case 2: // c
                case 3: // d
                case 6: // g
                case 11: // l
                case 12: // m
                case 15: // p
                case 20: // u
                    tileDictionary.put((char)(65 + count), 2);
                    break;
                case 5: // f
                case 7: // h
                case 10: // k
                case 21: // v
                case 22: // w
                case 24: // y
                    tileDictionary.put((char)(65 + count), 3);
                    break;
                default: // j, q , x , z 
                    tileDictionary.put((char)(65 + count), 5);
                    break;
            }
        }
        return tileDictionary;
    }

    void displayTileValues(Map tileDictionary, String[] allowedWords) // prints out all tiles and their associated values
    {
        Console.println();
        Console.println("TILE VALUES");
        Console.println();
        for (Object letter : tileDictionary.keySet()) // iterate through letters in dictionary 
        {
            int points = (int)tileDictionary.get(letter);  // get the points for the letter
            Console.println("Points for " + letter + ": " + points); // print the letter and points 
        }
        Console.println();
    }

    String getStartingHand(QueueOfTiles tileQueue, int startHandSize) // takes a starting hand for the player from the queue, and then replenishes queue  
    {
        String hand = "";
        for (int count = 0; count < startHandSize; count++) { // iterate from 0 to size of hand
            hand += tileQueue.remove(); // dequeue from queue and contatonate into string
            tileQueue.add(); // enqueue another char to the queue (so the number of elements in it remains the same). 
        }
        return hand;
    }

    String[] loadAllowedWords() // loads the allowed words from file and stores in string array
    {
        String[] allowedWords = {}; // construct empty string
        try {
            Path filePath = new File("aqawords.txt").toPath(); // try to load file
            Charset charset = Charset.defaultCharset(); // use the same char set as in text file        
            List<String> stringList = Files.readAllLines(filePath, charset); // put all contents of the File into a stringList
            allowedWords = new String[stringList.size()]; // convert list into array
            int count = 0;
            for(String word: stringList) // iterate through list
            {
                allowedWords[count] = word.trim().toUpperCase();// place upper case word (with no trailing/leading whitespace) into the array of allowed words
                count++; // increment counter
            }
        } catch (IOException e) { // catch errors(e.g. file not found)
        }
        return allowedWords;            
    }
    
    boolean checkWordIsInTiles(String word, String playerTiles) // checks if typed word is contained within a player's tiles
    {
        boolean inTiles = true;
        String copyOfTiles = playerTiles;
        for (int count = 0; count < word.length(); count++) { // iterate through the word 
            if(copyOfTiles.contains(word.charAt(count) + "")) // if the player tiles contain the current character of the word
            {
                copyOfTiles = copyOfTiles.replaceFirst(word.charAt(count) + "", ""); // remove that character from the current copy of tiles
            }
            else 
            {
              inTiles = false;// if one char of a word isn't found, then the word isn't in the tiles so return false 
            }
        }
        return inTiles;
    }
    
    boolean checkWordIsValid(String word, String[] allowedWords) // checks a word played against the array of allowed words, currently linear search (implement binary)
    {
        boolean validWord = false; // set the word to being not valid
        int count = 0;
        while(count < allowedWords.length && !validWord) // iterate through the allowed words array
        {
            if(allowedWords[count].equals(word)) // if the word is found in allowedWords
            {
                validWord = true; // the word is valid
            }
            count += 1; // increment the counter
        }
        return validWord;
    }

    void addEndOfTurnTiles(QueueOfTiles tileQueue, Tiles playerTiles,
            String newTileChoice, String choice) // deals with adding tiles to hand at end of turn (from queue)
    {
        int noOfEndOfTurnTiles;
        if(newTileChoice.equals("1"))
        {
            noOfEndOfTurnTiles = choice.length();  //replace tiles used
        }
        else if(newTileChoice.equals("2"))
        {
            noOfEndOfTurnTiles = 3; //add 3 tiles
        }   
        else
        {
            noOfEndOfTurnTiles = choice.length()+3; // replace tiles and add 3
        }
        for (int count = 0; count < noOfEndOfTurnTiles; count++) 
        {
            playerTiles.playerTiles += tileQueue.remove();//dequeue the desired number of tiles
            tileQueue.add(); //refill queue
        }
    }
    
    void fillHandWithTiles(QueueOfTiles tileQueue, Tiles playerTiles,
            int maxHandSize) // fills a player's hand with tiles (taken from queue)
    {
        while(playerTiles.playerTiles.length() <= maxHandSize) //while hand isn't full
        {
            playerTiles.playerTiles += tileQueue.remove(); // dequeue a tile and add to player hand
            tileQueue.add(); // replenish queue
        }
    }
    
    int getScoreForWord(String word, Map tileDictionary) // calculates score of a word
    {
        int score = 0;
        for (int count = 0; count < word.length(); count++)  //iterate through the word
        {
            score += (int)tileDictionary.get(word.charAt(count)); // retrieve score for each letter and add to word score
        }
        if(word.length() > 7) //if the word is very long add 20 to score
        {
            score += 20;
        }
        else if(word.length() > 5) //if it is quite long, add 5 to score
        {
            score += 5;
        }
        return score;       
    }
    
    void updateAfterAllowedWord(String word, Tiles playerTiles, 
            Score playerScore, TileCount playerTilesPlayed, Map tileDictionary, 
            String[] allowedWords) // updates player info after they play a valid word
    {
        playerTilesPlayed.numberOfTiles += word.length(); // update the number of tiles played with the length of the word
        for(char letter : word.toCharArray())
        {
            playerTiles.playerTiles = playerTiles.playerTiles.replaceFirst(letter + "", ""); // remove the letters that were played
        }
        playerScore.score += getScoreForWord(word, tileDictionary);     // calculate and update player score  
    }
    
    int updateScoreWithPenalty(int playerScore, String playerTiles, 
            Map tileDictionary) // updates score with the penalty from having tiles left at end of game
    {
        for (int count = 0; count < playerTiles.length(); count++) // for each tile left in hand
        {
            playerScore -= (Integer)tileDictionary.get(playerTiles.charAt(count)); // lose points equal to the value of the tile
        }
        return playerScore;
    }
    
    String getChoice() // asks player for choice (and shows options)
    {
        Console.println();
        Console.println("Either:");
        Console.println("     enter the word you would like to play OR");
        Console.println("     press 1 to display the letter values OR");
        Console.println("     press 4 to view the tile queue OR");
        Console.println("     press 7 to view your tiles again OR");
        Console.println("     press 0 to fill hand and stop the game.");
        Console.print("> ");
        String choice = Console.readLine();
        Console.println();
        choice = choice.toUpperCase(); 
        return choice;
    }
    
    String getNewTileChoice() // asks player for choice or replacing tiles (after a valid word is played) 
    {
      String newTileChoice = "";
      do //repeat until valid choice is entered
      {
          Console.println("Do you want to:"); 
          Console.println("     replace the tiles you used (1) OR");
          Console.println("     get three extra tiles (2) OR");
          Console.println("     replace the tiles you used and get three extra tiles (3) OR");
          Console.println("     get no new tiles (4)?");
          Console.print("> ");
          newTileChoice = Console.readLine();
      } while(!"1".equals(newTileChoice) && !"2".equals(newTileChoice) &&
              !"3".equals(newTileChoice) && !"4".equals(newTileChoice));
      return newTileChoice;   
    }
    
    void displayTilesInHand(String PlayerTiles) //prints out a player's hand
    {
        Console.println();
        Console.println("Your current hand: " + PlayerTiles);
    }
    
    void haveTurn(String playerName, Tiles playerTiles, 
            TileCount playerTilesPlayed, Score playerScore, Map tileDictionary, 
            QueueOfTiles tileQueue, String[] allowedWords, int maxHandSize, 
            int noOfEndOfTurnTiles) // player's turn
    {
      Console.println();
      Console.println(playerName + " it is your turn."); // display player name
      displayTilesInHand(playerTiles.playerTiles); // display player tiles
      String newTileChoice = "2";
      boolean validChoice = false;
      boolean validWord;
      while (!validChoice) // while the player hasn't done something valid
      {
        String choice = getChoice(); // get player's choice
        if (choice.equals("1"))
        {
          displayTileValues(tileDictionary, allowedWords);// display value of each character
        }
        else if (choice.equals("4"))
        {
          tileQueue.show(); // show queue of tiles
        }
        else if (choice.equals("7"))
        {
          displayTilesInHand(playerTiles.playerTiles); // show tiles in the player's hand
        }
        else if (choice.equals("0"))
        {
          validChoice = true; //break from while loop 
          fillHandWithTiles(tileQueue, playerTiles, maxHandSize); // fill hand to max size
        }
        else
        {
          validChoice = true;
          if (choice.length() == 0)
          {
            validWord = false;
          }
          else
          {
            validWord = checkWordIsInTiles(choice, playerTiles.playerTiles); // check user has typed something from their hand
          }
          if (validWord) 
          {
            validWord = checkWordIsValid(choice, allowedWords); // check if the word is in allowed words list
            if (validWord) // if it is 
            {
              Console.println();
              Console.println("Valid word");
              Console.println();
              updateAfterAllowedWord(choice, playerTiles, playerScore, 
                      playerTilesPlayed, tileDictionary, allowedWords); // update player information based on allowed words 
              newTileChoice = getNewTileChoice(); // give player a choice of getting new tiles
            }
          }
          if (!validWord)
          {
            Console.println();
            Console.println("Not a valid attempt, you lose your turn.");
            Console.println();
          }
          if (!newTileChoice.equals("4"))
          {
            addEndOfTurnTiles(tileQueue, playerTiles, newTileChoice, choice); // add the tiles they have selected
          }
          Console.println();
          Console.println("Your word was:" + choice);
          Console.println("Your new score is:" + playerScore.score);
          Console.println("You have played " + playerTilesPlayed.numberOfTiles + " tiles so far in this game.");
        }
      }
    }
    
    void displayWinner(int playerOneScore, int playerTwoScore) // compares scores and displays winner
    {
      Console.println();
      Console.println("**** GAME OVER! ****");
      Console.println();
      Console.println("Player One your score is " + playerOneScore);
      Console.println("Player Two your score is " + playerTwoScore);
      if (playerOneScore > playerTwoScore)
      {
        Console.println("Player One wins!");
      }
      else if (playerTwoScore > playerOneScore)
      {
        Console.println("Player Two wins!");
      }
      else
      {
        Console.println("It is a draw!");
      }
      Console.println()  ;
    }
    
    void playGame(String[] allowedWords, Map tileDictionary, boolean randomStart, 
            int startHandSize, int maxHandSize, int maxTilesPlayed, 
            int noOfEndOfTurnTiles) // plays the game?
    {
      Score playerOneScore = new Score(); // player one's score is 50
      playerOneScore.score = 50;
      Score playerTwoScore = new Score(); // player two's score is 50
      playerTwoScore.score = 50;
      TileCount playerOneTilesPlayed = new TileCount(); // player one's tile count is 0
      playerOneTilesPlayed.numberOfTiles = 0;
      TileCount playerTwoTilesPlayed = new TileCount();// player two's tile count is 0
      playerTwoTilesPlayed.numberOfTiles = 0;
      Tiles playerOneTiles = new Tiles(); 
      Tiles playerTwoTiles = new Tiles();
      QueueOfTiles tileQueue  = new QueueOfTiles(20); 
      if(randomStart)
      {
        playerOneTiles.playerTiles = getStartingHand(tileQueue, startHandSize); // give player one a random hand
        playerTwoTiles.playerTiles = getStartingHand(tileQueue, startHandSize); // give player two a random hand
      }
      else
      { // for training game
        playerOneTiles.playerTiles = "BTAHANDENONSARJ";
        playerTwoTiles.playerTiles = "CELZXIOTNESMUAA";
      }
      while (playerOneTilesPlayed.numberOfTiles <= maxTilesPlayed && 
              playerTwoTilesPlayed.numberOfTiles <= maxTilesPlayed && 
              playerOneTiles.playerTiles.length() < maxHandSize && 
              playerTwoTiles.playerTiles.length() < maxHandSize)
      { // while the game should continue
        haveTurn("Player One", playerOneTiles, playerOneTilesPlayed, playerOneScore, 
                tileDictionary, tileQueue, allowedWords, maxHandSize, 
                noOfEndOfTurnTiles); // player one takes a turn
        Console.println();
        Console.println("Press Enter to continue");
        Console.readLine();
        Console.println();
        haveTurn("Player Two", playerTwoTiles, playerTwoTilesPlayed, playerTwoScore, 
                tileDictionary, tileQueue, allowedWords, maxHandSize, 
                noOfEndOfTurnTiles); // player two takes a turn
      }
      playerOneScore.score = updateScoreWithPenalty(playerOneScore.score, 
              playerOneTiles.playerTiles, tileDictionary); 
      playerTwoScore.score = updateScoreWithPenalty(playerTwoScore.score, 
              playerTwoTiles.playerTiles, tileDictionary); // calculate final scores
      displayWinner(playerOneScore.score, playerTwoScore.score); // display winner  
    }

    void displayMenu() //prints out the menu
    {
      Console.println();
      Console.println("=========");
      Console.println("MAIN MENU");
      Console.println("=========");
      Console.println();
      Console.println("1. Play game with random start hand");
      Console.println("2. Play game with training start hand");
      Console.println("9. Quit");
      Console.println();
    }  
    
    /*
    * Class Tiles is used to allow the value of a String variable to be 
    * returned from a subroutine
    */
    class Tiles
    {
        String playerTiles;
    } 
    /*
    * Class TileCount is used to allow the value of an integer variable to be 
    * returned from a subroutine
    */
    class TileCount
    {
        int numberOfTiles;
    }
    /*
    * Class Score is used to allow the value of an integer variable to be 
    * returned from a subroutine
    */
    class Score
    {
        int score;
    }   
           
    Main()
    {
      Console.println("++++++++++++++++++++++++++++++++++++++");
      Console.println("+ Welcome to the WORDS WITH AQA game +");
      Console.println("++++++++++++++++++++++++++++++++++++++");
      Console.println();
      Console.println();
      String[] allowedWords = loadAllowedWords(); // load file for allowed words
      Map tileDictionary = createTileDictionary(); // map letters to their score
      int maxHandSize = 20;
      int maxTilesPlayed = 50;
      int noOfEndOfTurnTiles = 3;
      int startHandSize = 15;
      String choice = "";
      while(!choice.equals("9"))
      {
        displayMenu();
        Console.println("Enter your choice: ");
        choice = Console.readLine();
        if (choice.equals("1"))
        {
          playGame(allowedWords, tileDictionary, true, startHandSize, 
                  maxHandSize, maxTilesPlayed, noOfEndOfTurnTiles); //random hand
        }
        else if (choice.equals("2"))
        {
          playGame(allowedWords, tileDictionary, false, 15, maxHandSize, 
                  maxTilesPlayed, noOfEndOfTurnTiles); // training hand
        }
      }
    }
    
    public static void main(String[] args) 
    {
        new Main();
    }
    
}