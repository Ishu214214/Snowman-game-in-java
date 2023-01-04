import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Instance of the game
public class Game extends JPanel{
    
    int crx,cry;	
    int car_x,car_y;    
    int speedX,speedY;	
    int nOpponent;      
    String imageLoc[]; 
    int lx[],ly[];  
    int score;      
    int highScore;  
    int speedOpponent[]; 
    boolean isFinished; 
    boolean isUp, isDown, isRight, isLeft;  

    public Game(){
        crx = cry = -999;   

        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) { 
                stopCar(e); 
            }
            public void keyPressed(KeyEvent e) { 
                moveCar(e); 
            }
        });
        setFocusable(true); 
        car_x = car_y = 300;    
        isUp = isDown = isLeft = isRight = false;   
        speedX = speedY = 0;    
        nOpponent = 0;  
        lx = new int[20]; 
        ly = new int[20]; 
        imageLoc = new String[20];
        speedOpponent = new int[20]; 
        isFinished = false; 
        score = highScore = 0;  
    }
    


    public void paint(Graphics g){
        super.paint(g);
        Graphics2D obj = (Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try{
            obj.drawImage(getToolkit().getImage("images/1.png"), 0, 0 ,this); 
            if(cry >= -499 && crx >= -499) 
                obj.drawImage(getToolkit().getImage("images/1.jpg"),crx,cry,this); 
            
            obj.drawImage(getToolkit().getImage("images/sn.jpeg"),car_x,car_y,this);   
            
            if(isFinished){ 
                obj.drawImage(getToolkit().getImage("images/boom.png"),car_x-30,car_y-30,this); 
            }
            
            if(this.nOpponent > 0){ 
                for(int i=0;i<this.nOpponent;i++){ 
                    obj.drawImage(getToolkit().getImage(this.imageLoc[i]),this.lx[i],this.ly[i],this); 
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
    


    void moveRoad(int count){
        if(crx == -999 && cry == -999){ 
            if(count%10 == 0){  
                crx = 499;      
                cry = 0;
            }
        }
        else{  
            crx--; 
        }
        if(crx == -499 && cry == 0){ 
            
            crx = cry = -999;   
        }
        car_x += speedX; 
        car_y += speedY; 
        
        
        if(car_x < 0)   
            car_x = 0;  
        
        
        if(car_x+93 >= 500) 
            car_x = 500-93; 
        
        
        if(car_y <= 124)    
            car_y = 124;
        
        
        if(car_y >= 364-50) 
            car_y = 364-50; 
        
        
        for(int i=0;i<this.nOpponent;i++){ 
            this.lx[i] -= speedOpponent[i]; 
        }
        
        
        int index[] = new int[nOpponent];
        for(int i=0;i<nOpponent;i++){
            if(lx[i] >= -127){
                index[i] = 1;
            }
        }
        int c = 0;
        for(int i=0;i<nOpponent;i++){
            if(index[i] == 1){
                imageLoc[c] = imageLoc[i];
                lx[c] = lx[i];
                ly[c] = ly[i];
                speedOpponent[c] = speedOpponent[i];
                c++;
            }
        }
        
        score += nOpponent - c; 
        
        if(score > highScore)
            highScore = score;  
        
        nOpponent = c;
        
        //Check for collision
        int diff = 0; //difference between users car and opponents car initially set to zero
        for(int i=0;i<nOpponent;i++){ //for all opponent cars
            diff = car_y - ly[i]; //diff is the distance between the user's car and the opponent car
            if((ly[i] >= car_y && ly[i] <= car_y+46) || (ly[i]+46 >= car_y && ly[i]+46 <= car_y+46)){   //if the cars collide vertically
                if(car_x+87 >= lx[i] && !(car_x >= lx[i]+87)){  //and if the cars collide horizontally
                    System.out.println("My object : "+car_x+", "+car_y);
                    System.out.println("Colliding object : "+lx[i]+", "+ly[i]);
                    this.finish(); //end game and print end message
                }
            }
        }
    }
    
    //function that will display message after user has lost the game
    void finish(){
        String str = "";    //create empty string that will be used for a congratulations method
        isFinished = true;  //indicates that game has finished to the rest of the program
        this.repaint();     //tells the window manager that the component has to be redrawn
        if(score == highScore && score != 0) //if the user scores a new high score, or the same high score
            str = "\nCongratulations!!! Its a high score";  //create a congratulations message
        JOptionPane.showMessageDialog(this,"Game Over!!!\nYour Score : "+score+"\nHigh Score : "+highScore+str,     "Game Over", JOptionPane.YES_NO_OPTION);    //displays the congratulations message and a message saying game over and the users score and the high score
        System.exit(ABORT); //terminate game
    }
    
    
    //function that handles input by user to move the user's car up, left, down and right
    public void moveCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){   //if user clicks on the up arrow key
            isUp = true;
            speedX = 1;     //moves car foward
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){ //if user clicks on the down arrow key
            isDown = true;
            speedX = -2;    //moves car backwards
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){ //if user clicks on the right arrow key
            isRight = true;
            speedY = 1;     //moves car to the right
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){ //if user clicks on the left arrow key
            isLeft = true;
            speedY = -1;    //moves car to the left
        }
    }
    
    //function that handles user input when the car is supposed to be stopped
    public void stopCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){   //if user clicks on the up arrow key
            isUp = false;
            speedX = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){    //if user clicks on the down arrow key
            isDown = false;
            speedX = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){    //if user clicks on the left arrow key
            isLeft = false;
            speedY = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){   //if user clicks on the right arrow key
            isRight = false;
            speedY = 0; //set speed of car to zero
        }
    }
    
    //main method where the java application begins processing
    public static void main(String args[]){
        JFrame frame = new JFrame("snowman Game");   //creating a new JFrame window to display the game
        Game game = new Game(); //creating a new instance of a Game
        frame.add(game);		//Graphics2D components are added to JFrame Window
        frame.setSize(500,500); //setting size of screen to 500x500
        frame.setVisible(true); //allows the JFrame and its children to displayed on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int count = 1, c = 1;
        while(true){
            game.moveRoad(count);   //move the road
            while(c <= 1){
                game.repaint();     //redraw road to match new locations
                try{
                    Thread.sleep(5);    //wait so that the road appears to be moving continously
                }
                catch(Exception e){
                    System.out.println(e);
                }
                c++;
            }
            c = 1;
            count++; //increment count value
            if(game.nOpponent < 4 && count % 200 == 0){ //if there is less than 4 cars and count timer reaches 200
                game.imageLoc[game.nOpponent] = "images/obj_"+((int)((Math.random()*100)%3)+1)+".png"; //assign images to the opponent cars
                game.lx[game.nOpponent] = 499; //set opponent cars start positions
                int p = (int)(Math.random()*100)%4; //create a random number that is the remainder of a number between 0 and 100 is divided by 4.
                if(p == 0){     //if the remainder is 0
                    p = 250;    //place the car in the fourth lane
                }
                else if(p == 1){ //if the remainder is 1
                    p = 300;    //place the car in the second lane
                }
                else if(p == 2){ //if the remainder is 2
                    p = 185;    //place the car in the third lane
                }
                else{           //otherwise
                    p = 130;    //place the car in the fourth  lane
                }
                game.ly[game.nOpponent] = p; //assign lane position to car
                game.speedOpponent[game.nOpponent] = (int)(Math.random()*100)%2 + 2; //sets the speed of the new opponent car to a random number that is the remainder of a number between 0 and 100, plus 2
                game.nOpponent++; //add the car to the game
            }
        }
    }
}
