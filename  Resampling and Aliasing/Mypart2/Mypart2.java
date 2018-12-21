import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
  
public class Mypart2 extends JPanel {
    
    public JFrame frame; 
    public JLabel LeftPart, RightPart;
    public Timer LeftTimer, RightTimer;
    public ImageIcon TemptLeft, TemptRight; 
    public float LeftCounter = 0, RightCounter = 0;
    public float LeftStart_degree, RightStart_degree;
    public float LeftVideofps = 60; //defult value of left_fps is 60
    public float RightVideofps;
    public int ImageWidth = 512;
    public BufferedImage LeftVideo=new BufferedImage(ImageWidth, ImageWidth, BufferedImage.TYPE_INT_RGB);
    public BufferedImage RightVideo = new BufferedImage(ImageWidth, ImageWidth, BufferedImage.TYPE_INT_RGB);
    public int LeftDelay = Math.round(10000.0f/LeftVideofps);
    public int RightDelay;
    
    //Function to whiten image
    public BufferedImage WhitenImage(BufferedImage Image, int Width){
        for(int y = 0; y < Width; y++){
            for(int x = 0; x < Width; x++){
                byte r = (byte)255;
                byte g = (byte)255;
                byte b = (byte)255;
                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                Image.setRGB(x,y,pix);
            }
        }
        return Image;
    }

    // Update left image to get the left video, according to the start degree
    public void UpdateLeftImage(int n ){
        LeftCounter = (LeftCounter + LeftStart_degree)%360;
        TemptLeft = DrawImage(LeftVideo, ImageWidth, n, LeftCounter);
        TemptLeft.getImage().flush();
        LeftPart.setIcon(TemptLeft);
        LeftPart.revalidate();
        LeftPart.repaint();
    }

    // Update right image to get the right video, according to the start degree
    public void UpdateRightImage(int n){
        RightCounter = (RightCounter + RightStart_degree)%360;
        TemptRight = DrawImage(RightVideo, ImageWidth, n ,RightCounter);
        TemptRight.getImage().flush();
        RightPart.setIcon(TemptRight);
        RightPart.revalidate();
        RightPart.repaint();
    }

    // Function to show the videoes
    private void ShowVideo(){
      JFrame frame = new JFrame("Original video (Left)--------Output video (Right)");
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	//Function to draw lines
    public BufferedImage drawLines( BufferedImage Image, int x2, int y2, int width){
        int x1 = 256, y1 = 256;
        int x_start=x1, y_start=y1;
        int x_end=x2, y_end=y2;
        int dx = x2 - x1;
        int dy = y2 - y1;
        double k = 0;
        boolean FLAG;
        Graphics2D g = Image.createGraphics();
        g.setColor(Color.black);
        if(dx != 0) k = dy/(double)dx;
        if(Math.abs(k) <= 1 && dx !=0) {
            FLAG = true;
            if(dx < 0) {
                x_start = x2;
                x_end = x1;
                y_start = y2;
                y_end = y1;
            }
            k = (y_end - y_start)/(double)(x_end- x_start);
        }
        else {
            FLAG = false;
            if(dy < 0) {
                x_start = x2;
                x_end = x1;
                y_start = y2;
                y_end = y1;
            }
            if(dx == 0) {k = 0;}
            else {k = (x_end- x_start)/(double)(y_end - y_start);}
        }
        if(FLAG) {
            double y = y_start;
            for(int x = x_start + 1; x <= x_end; x++) {
                y = y + k;
                g.drawLine(x,(int)Math.floor(y), x_end, y_end);
            }
        }
        else {
            double x = x_start;
            for(int y = y_start + 1; y <= y_end; y++) {
                x = x + k;
                g.drawLine((int)Math.floor(x), y,x_end, y_end);
            }
        }
        return Image;
    }

    // Function to draw two images, using drawlines function
        public ImageIcon DrawImage (BufferedImage img, int draw_width,int num_of_lines, float degreeStep){
        float degree = degreeStep;
        float k=0;
        float part = 360.0f/(float)num_of_lines;
        degree = degree%360; 
        k = (float)Math.tan(degree*Math.PI/180);
        BufferedImage draw_img=img;
        Graphics2D g = img.createGraphics();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
        draw_img = WhitenImage(draw_img, ImageWidth);
        for(int i=0;i<num_of_lines;i++){
            if(degree==0){
                draw_img = drawLines(draw_img, 512, 256, draw_width);
            }
            if(degree==45){
                draw_img = drawLines(draw_img, 512, 512, draw_width);
            }
            if(degree==90){
                draw_img = drawLines(draw_img, 256, 512, draw_width);
            }
            if(degree==135){
                draw_img = drawLines(draw_img ,0, 512, draw_width);
            }
            if(degree==180){
                draw_img = drawLines(draw_img,0, 256, draw_width);
            }
            if(degree==225){
                draw_img = drawLines(draw_img,0, 0,draw_width);
            }
            if(degree==270){
                draw_img = drawLines(draw_img,256, 0,draw_width);
            }
            if(degree==315){
                draw_img = drawLines(draw_img,512, 0,draw_width);
            }
            if(0<degree && degree<45) {
                draw_img = drawLines(draw_img, 512, Math.round((256*k) + 256), draw_width);
            } 
            if(45<degree && degree<90){
                draw_img = drawLines(draw_img, Math.round((256/k) + 256), 512, draw_width);
            }
            if(90<degree && degree<135){
                draw_img = drawLines(draw_img, Math.round((256/k) + 256), 512, draw_width);
            }
            if(135<degree && degree<180){
                draw_img = drawLines(draw_img,0, Math.round((-256*k) + 256), draw_width);
            }
            if(180<degree && degree<225){
                draw_img = drawLines(draw_img,0, Math.round((-256*k) + 256), draw_width);
            }
            if(225<degree && degree<270){
                draw_img = drawLines(draw_img, Math.round((-256/k) + 256), 0, draw_width);
            }
            if(270<degree && degree<315){
                draw_img = drawLines(draw_img, Math.round((-256/k) + 256), 0, draw_width);
            } 
            if(315<degree && degree<360){
                draw_img = drawLines(draw_img, 512, Math.round((256*k) + 256), draw_width);
            }
            degree += part;
            degree = degree%360;
            k = (float)Math.tan(degree*Math.PI/180);
        }
        g.drawLine(0,0,0,511);
        g.drawLine(0,511,511,511);
        g.drawLine(511,511,511,0);
        g.drawLine(0,0,511,0);
        g.drawImage(img, 0, 0, null);
        return (new ImageIcon(draw_img));
    }

    // Function that uses Timer() to convert left and right images into videoes. 
    public Mypart2(String[] Param){   
		String param0 = Param[0]; // Number of lines
		String param1 = Param[1]; // Scaling factor
        String param2 = Param[2]; // Aliasing
        
		System.out.println("The first parameter number of lines was: " + param0);
		System.out.println("The second parameter speed of rotation was: " + param1);
        System.out.println("The third parameter fps was: " + param2);
        
        int n = Integer.parseInt(param0);
        float s = Float.valueOf(param1);
        float RightVideofps = Float.valueOf(param2);

        RightDelay = Math.round(10000.0f/RightVideofps);

        LeftStart_degree = (360/LeftVideofps)*s;
        RightStart_degree = (360/RightVideofps)*s;

        // Use labels to display the images
        LeftPart = new JLabel(DrawImage(LeftVideo, ImageWidth, n, LeftCounter));
        RightPart = new JLabel(DrawImage(RightVideo, ImageWidth, n, RightCounter));
        this.add(LeftPart);
        this.add(RightPart);

        //Using timer and the delay time update two images, makeing two wheels seem like rotating
        LeftTimer = new Timer(LeftDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateLeftImage(n);
            }
        });
        LeftTimer.start();

        RightTimer = new Timer(RightDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateRightImage(n);
            }
        });
        RightTimer.start();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mypart2(args).ShowVideo();
            }
        });
    }

}