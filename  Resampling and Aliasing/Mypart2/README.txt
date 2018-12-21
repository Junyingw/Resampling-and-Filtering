In your program you will need to display two videos side by side

1. Your original video displayed on the left – This is video of size 512x512 that you
will create based on the criteria explained below. This is radial pattern just as in the previous case, but it is also rotating clockwise at a certain specified speed. When create and update your image at the respective times, it should simulate a rotating wheel.

2. Your processed output video displayed on the right – The output video is also of size 512x512 but in order to simulate temporal aliasing effects it will be given an fps rate of display, which means your output will be updated at specific times.

Input to your program will take four parameters where
• The first parameter n is the number of lines to create an image with radial pattern
of n black lines starting from the center of the image towards the boundaries. The image has a white background. Each consecutive line is separated by 360/n degrees. The idea here is by increasing n, you can increase the frequency content in an image.

• The second parameter s will be a speed of rotations in terms of rotations per second. This is a floating point number eg s=2.0 indicates that the wheel is making two full rotations in a second, s=7.5 indicates that the wheel is making seven and a half rotations in a second. Remember this is the original input video signal with a very high display rate.

• The third parameter will be an fps value suggesting that not all frames of the input video are displayed, but only a specific frames per second are displayed.