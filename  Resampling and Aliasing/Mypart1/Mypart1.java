
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.lang.*;
import java.awt.Graphics;
import java.awt.Graphics2D; 
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.awt.*;
import java.awt.image.AffineTransformOp;

public class Mypart1 {
    
	JFrame frame;
	JLabel lbIm1;
	JLabel lbIm2;
	int LImageWidth=  512; // Width of left image
    int RImageWidth = 512; // Width of right image
	BufferedImage LeftImage=null;
    BufferedImage RightImage=null;
    static boolean start = true;
	
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

	// Draws a black line on the given buffered image from the pixel defined by (x1, y1) to (x2, y2)
	public void drawLine2(BufferedImage image, int x1, int y1, int x2, int y2) {
		Graphics2D g = image.createGraphics();
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1));
		g.drawLine(x1, y1, x2, y2);
		g.drawImage(image, 0, 0, null);
    }
  
	//Function to draw lines, using two coordinates to draw a line
    public BufferedImage drawLines( BufferedImage Image, int x2, int y2, int width){
        if(start==true){
            Image = WhitenImage(Image, width);
            start=false;
        }
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
                y_start = y2;
                x_end = x1;
                y_end = y1;
            }
            k = (y_end - y_start)/(double)(x_end- x_start);
        }
        else {
            FLAG = false;
            if(dy < 0) {
                x_start = x2;
                y_start = y2;
                x_end = x1;
                y_end = y1;
            }
            if(dx == 0) {k = 0;}
            else {k = (x_end- x_start)/(double)(y_end - y_start);}
        }
        if(FLAG) {
            double y = y_start+0.5;
            for(int x = x_start; x <x_end; x++) {
                y = y + k;
                g.drawLine(x,(int)Math.floor(y), x_end, y_end);
            }
        }
        else {
            double x = x_start+0.5;
            for(int y = y_start; y <y_end; y++) {
                x = x + k;
                g.drawLine((int)Math.floor(x), y,x_end, y_end); 
            }
        }
        return Image;
    }

// Function of scaling images, from left image to right image.
    public BufferedImage scaleImage(BufferedImage Before,float Scale){
        BufferedImage After = new BufferedImage(RImageWidth, RImageWidth, BufferedImage.TYPE_INT_RGB);
        After = WhitenImage(After, RImageWidth);
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
		return newImage;
    }
    
   //Function of drawing images, using drawlines()
        public BufferedImage DrawImage (BufferedImage img, int draw_width,int num_of_lines){
        float degree = 0;
        float part = 360.0f/(float)num_of_lines;
        float k=0;
        BufferedImage draw_img=img;
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
                draw_img = drawLines(draw_img, 511, Math.round((255*k) + 256), draw_width);
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
            degree = degree + part;
            k = (float)Math.tan(degree * Math.PI/180);
        }
        return draw_img;
    }

    // Show two images
	public void showIms(String[] args){

		String param0 = args[0]; // Number of lines
		String param1 = args[1]; // Scaling factor
		String param2 = args[2]; // Aliasing
		System.out.println("The first parameter number of lines was: " + param0);
		System.out.println("The second parameter scaling factor was: " + param1);
		System.out.println("The third parameter 0 or 1 for aliasing was: " + param2);
        int n = Integer.parseInt(param0);
        float s = Float.valueOf(param1);
        float a = Float.valueOf(param2);
        
        // Create images and draw n lines
        RightImage = new BufferedImage(RImageWidth,RImageWidth,BufferedImage.TYPE_INT_RGB);
        LeftImage = new BufferedImage(LImageWidth, LImageWidth, BufferedImage.TYPE_INT_RGB);
        LeftImage = WhitenImage(LeftImage, LImageWidth);
        LeftImage = DrawImage(LeftImage, LImageWidth, n);

        // According to input parameter,do scale image or anti-aliasing
        if(a==1) RightImage = AverageFiltering(LeftImage);
        if(s!=0.0) {
            RImageWidth = Math.round((float)LImageWidth/s);
            RightImage= scaleImage(LeftImage, s);
        }
        else { 
            RImageWidth = LImageWidth;
            RightImage = WhitenImage(RightImage, RImageWidth);
        }

        // draw black lines of all around the shapes 
		drawLine2(LeftImage, 0, 0, LImageWidth-1, 0);				// top edge
		drawLine2(LeftImage, 0, 0, 0, LImageWidth-1);				// left edge
		drawLine2(LeftImage, 0, LImageWidth-1, LImageWidth-1, LImageWidth-1);	// bottom edge
        drawLine2(LeftImage, LImageWidth-1, LImageWidth-1, LImageWidth-1, 0); 	// right edge
        drawLine2(RightImage, 0, 0, RImageWidth-1, 0);				// top edge
		drawLine2(RightImage, 0, 0, 0, RImageWidth-1);				// left edge
		drawLine2(RightImage, 0, RImageWidth-1, RImageWidth-1, RImageWidth-1);	// bottom edge
		drawLine2(RightImage, RImageWidth-1, RImageWidth-1, RImageWidth-1, 0); 	// right edge
		
		// Use labels to display the images
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		JLabel lbText1 = new JLabel("Original image (Left)");
		lbText1.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lbText2 = new JLabel("Image after modification (Right)");
		lbText2.setHorizontalAlignment(SwingConstants.CENTER);
		lbIm1 = new JLabel(new ImageIcon(LeftImage));
		lbIm2 = new JLabel(new ImageIcon(RightImage));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(lbText1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		frame.getContentPane().add(lbText2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		frame.getContentPane().add(lbIm2, c);

		frame.pack();
        frame.setVisible(true);
        
	}

	public static void main(String[] args) {
		Mypart1 ren = new Mypart1();
		ren.showIms(args);
	}

}