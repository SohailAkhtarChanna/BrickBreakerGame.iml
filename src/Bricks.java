import java.awt.*;

public class Bricks {

    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public Bricks(int rows,int cols){
        map = new int[rows][cols];

        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                map[i][j] = 1;
            }
        }

        brickWidth = 540/cols;
        brickHeight = 150/rows;
    }

    public void draw(Graphics2D g){

        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
               if(map[i][j]>0){
                   g.setColor(Color.CYAN);
                   g.fillRect(j*brickWidth + 80, i*brickHeight + 50,brickWidth,brickHeight);
                   g.setStroke(new BasicStroke(3)); //for setting the border of bricks
                   g.setColor(Color.BLACK);
                   g.drawRect(j*brickWidth + 80, i*brickHeight + 50,brickWidth,brickHeight);
               }
            }
        }
    }

    public void setBrickValue(int value,int row,int col){
        map[row][col] = value;
    }
}
