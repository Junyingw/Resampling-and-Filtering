import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.sun.javafx.tools.packager.Param;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D; 


public class MyExtraCredit extends JPanel {

	public JFrame frame;
	public JLabel lbIm1;
    public JLabel lbIm2; 
    public Timer LeftTimer, RightTimer;
    public float LeftVideofps = 60;
    public float RightVideofps;
    public int LeftDelay = Math.round(10000.0f/LeftVideofps);
    public int RightDelay;
    public JLabel LeftPart, RightPart;
    public int ImageWidth = 512;
    public float RImageWidth;
    public float LeftCounter = 0, RightCounter = 0;
    public float LeftStart_degree, RightStart_degree;
    public ImageIcon TemptLeft, TemptRight;
    public BufferedImage LeftVideo=new BufferedImage(ImageWidth, ImageWidth, BufferedImage.TYPE_INT_RGB);
    public BufferedImage RightVideo = new BufferedImage(ImageWidth, ImageWidth, BufferedImage.TYPE_INT_RGB);
    public BufferedImage LeftVideoTempt, RightVideoTempt;

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

    // Function of scaling images, from left image to right image.
    public BufferedImage scaleImage(BufferedImage Before,float Scale){
        BufferedImage After = new BufferedImage(Math.round(RImageWidth), Math.round(RImageWidth), BufferedImage.TYPE_INT_RGB);
        After = WhitenImage(After, Math.round(RImageWidth));
        int w=0, h=0;
        w=Before.getWidth();
        h=Before.getHeight();
        int x = 0, y = 0;
        for(float i = 0;i< w;i+=Scale){
            y=0;
            for(float j = 0;j< h;j+=Scale){
                if(x<RImageWidth && y< RImageWidth)
                    After.setRGB(x, y, Before.getRGB(Math.round(i), Math.round(j)));
                else
                    break;
                y++;
            }
            x++;
        } 
        return After;
    }
    
    //Using average filtering to reduce aliasing
    public BufferedImage AverageFiltering(BufferedImage original){
        int w = original.getWidth();
        int h = original.getHeight(); 
        int pix[] = new int[w*h];
        int newpix[] = new int[w*h];
        original.getRGB(0, 0, w, h, pix, 0, w);
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        newImage=original;
        newImage = WhitenImage(newImage, w); 
		ColorModel cm = ColorModel.getRGBdefault();
		int r=0,g=0,b=0;
		for(int y=0; y<h; y++) {
			for(int x=0; x<w; x++) {
				if(x!=0 && x!=w-1 && y!=0 && y!=h-1) {
					r = (cm.getRed(pix[x-1+(y-1)*w]) + cm.getRed(pix[x+(y-1)*w])+ cm.getRed(pix[x+1+(y-1)*w])
						+ cm.getRed(pix[x-1+(y)*w]) + cm.getRed(pix[x+(y)*w]) + cm.getRed(pix[x+1+(y)*w])
                        + cm.getRed(pix[x-1+(y+1)*w]) + cm.getRed(pix[x+(y+1)*w]) + cm.getRed(pix[x+1+(y+1)*w]))/9;
                    g = (cm.getGreen(pix[x-1+(y-1)*w]) + cm.getGreen(pix[x+(y-1)*w])+ cm.getGreen(pix[x+1+(y-1)*w])
						+ cm.getGreen(pix[x-1+(y)*w]) + cm.getGreen(pix[x+(y)*w]) + cm.getGreen(pix[x+1+(y)*w])
                        + cm.getGreen(pix[x-1+(y+1)*w]) + cm.getGreen(pix[x+(y+1)*w]) + cm.getGreen(pix[x+1+(y+1)*w]))/9;
                    b = (cm.getBlue(pix[x-1+(y-1)*w]) + cm.getBlue(pix[x+(y-1)*w])+ cm.getBlue(pix[x+1+(y-1)*w])
                        + cm.getBlue(pix[x-1+(y)*w]) + cm.getBlue(pix[x+(y)*w]) + cm.getBlue(pix[x+1+(y)*w])
                        + cm.getBlue(pix[x-1+(y+1)*w]) + cm.getBlue(pix[x+(y+1)*w]) + cm.getBlue(pix[x+1+(y+1)*w]))/9;
                    newpix[y*w+x] = 255<<24 | r<<16 | r<<8 |r;
                    newpix[y*w+x] = 255<<24 | g<<16 | g<<8 |g;
                    newpix[y*w+x] = 255<<24 | b<<16 | b<<8 |b;
				} else {
					newpix[y*w+x] = pix[y*w+x];
				}
			}
        }
        newImage.setRGB(0, 0, w, h, newpix, 0, w);
		return(newImage);
    }

    public void UpdateLeftImage(int n ){
        LeftCounter = (LeftCounter + LeftStart_degree)%360;
        LeftVideoTempt = DrawImage(LeftVideo, ImageWidth, n, LeftCounter);
        TemptLeft = new ImageIcon(LeftVideoTempt);
        TemptLeft.getImage().flush();
        LeftPart.setIcon(TemptLeft);
        LeftPart.revalidate();
        LeftPart.repaint();
    }

    public void UpdateRightImage(int n, float s,float scalef, float a){
        RightCounter = (RightCounter + RightStart_degree)%360;
        RightVideoTempt = DrawImage(LeftVideo, ImageWidth, n ,RightCounter); 
        if(a==1) RightVideoTempt = AverageFiltering(LeftVideo);
        if(scalef!=0.0) RightVideoTempt = scaleImage(LeftVideo,scalef);
        TemptRight = new ImageIcon(RightVideoTempt);
        TemptRight.getImage().flush();
        RightPart.setIcon(TemptRight);
        RightPart.revalidate();
        RightPart.repaint();
    }

   // Function to show the videoes
    public void ShowVideo(){
      JFrame frame = new JFrame("MyExtraCredit");
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
        if((dx==0&& dy!=0)||(dx!=0 && dy==0)){
            g.drawLine(x_start, y_start, x_end, y_end);
        }
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
            double y = y_start+ 0.5;
            for(int x = x_start + 1; x <= x_end; x++) {
                y = y + k;
                g.drawLine(x,(int)Math.floor(y), x_end, y_end);
            }
        }
        else {
            double x = x_start+ 0.5;
            for(int y = y_start + 1; y <= y_end; y++) {
                x = x + k;
                g.drawLine((int)Math.floor(x), y,x_end, y_end);
            }
        }
        return Image;
    }

      //Function of drawing images, using drawlines()
        public BufferedImage DrawImage (BufferedImage img, int draw_width,int num_of_lines, float degreeStep){
        float degree = degreeStep;
        float k=0;
        float part = 360.0f/(float)num_of_lines;
        degree = degree%360; 
        k = (float)Math.tan(degree*Math.PI/180);
        BufferedImage draw_img = img;
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
            g.drawLine(0,0,0,511);
            g.drawLine(0,511,511,511);
            g.drawLine(511,511,511,0);
            g.drawLine(0,0,511,0);
            g.drawImage(img, 0, 0, null);
        }
        return draw_img;
    }

    public MyExtraCredit(String[] Param){   
		String param0 = Param[0]; 
		String param1 = Param[1]; 
        String param2 = Param[2]; 
        String param3 = Param[3]; 
        String param4 = Param[4]; 
        
		System.out.println("The first parameter number of lines was: " + param0);
		System.out.println("The second parameter speed of rotation was: " + param1);
        System.out.println("The third parameter fps was: " + param2);
        System.out.println("The fourth parameter scale factor was: " + param3);
        System.out.println("The third parameter whether anti-aliasing was: " + param4);

        int n = Integer.parseInt(param0);   // Number of lines
        float s = Float.valueOf(param1);    // Speed of rotation
        float RightVideofps = Float.valueOf(param2);    // FPS
        float scalef = Float.valueOf(param3);// Scale factor
        float a = Float.valueOf(param4);    // 0 or 1 anti-aliasing

        RightDelay = Math.round(10000.0f/RightVideofps);

        // zoom out the video if the scale is not equal to 0.
        if(scalef!=0.0) RImageWidth = (float)ImageWidth/scalef;
        else RImageWidth = ImageWidth;
        
        //Avoid temporal aliasing,accorinding to Nyquist's throrem 
        if(a==1 && RightVideofps <= 2.0*s) RightVideofps = 3.0f*s;

        LeftStart_degree = (360/LeftVideofps)*s;
        RightStart_degree = (360/RightVideofps)*s;
        
       // Use labels to display the images
        LeftPart = new JLabel(new ImageIcon(DrawImage(LeftVideo, ImageWidth, n, LeftCounter)));
        RightPart = new JLabel (new ImageIcon(DrawImage(RightVideo, ImageWidth, n, RightCounter)));
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
                UpdateRightImage(n, s, scalef , a);
            }
        });
        RightTimer.start();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyExtraCredit(args).ShowVideo();
            }
        });
    }

}