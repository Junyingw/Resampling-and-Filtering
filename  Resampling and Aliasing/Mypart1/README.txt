In your program you will need to display two images side by side
 
1. Your original image displayed on the left – This is an image of size 512x512 that
you will create based on the criteria explained below.

2. Your processed output image displayed on the right – This image is the output of
your algorithms on the original image to create a resampled image depending on parameters explained below.

Input to your program will take three parameters where
• The first parameter n is the number of lines to create an image with radial pattern
of n black lines starting from the center of the image towards the boundaries. The image has a white background. Each consecutive line is separated by 360/n degrees. The idea here is by increasing n, you can increase the frequency content in an image.

• The second parameter s will be scaling value that scales the input image down by a factor. This is a floating point number eg s=2.0 will scale the image down to 256x256. Note s need not be a complete integer.

• The third parameter will be a boolean value (0 or 1) suggesting whether or not you want to deal with aliasing. A 0 signifies do nothing (aliasing will remain in your output) – which means you need copy the direct mapped value from input to output. A value 1 signifies that anti-aliasing should be performed – which means that instead of the direct mapped value you need to copy a low pass filtered value to the output. See lecture for more explanation of this in class.
