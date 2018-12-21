Change part2 of your assignment to take in two additional parameters – one to deal with scaling the temporal signal (just as you did with the static image in part 1) and the second to deal with anti aliasing in the resultant signal in both spatial and temporal domains. 

Correspondingly,
• The fourth parameter s will be a scale factor that scales the input video down by a factor. This is a floating point number eg – s=2.0 will scale the video down to 256x256. Note s need not be a complete integer. Also if the fourth parameter above is a 1, then you need to perform spatial antialiasing (like part1) along with temporal antialiasing.

• The fifth parameter will be a boolean value (0 or 1) suggesting whether or not you want to deal with aliasing. A 0 signifies do nothing (temporal and spatial aliasing will remain in your output). A value 1 signifies that temporal & spatial anti- aliasing should be performed – you need to design a method to decrease temporal aliasing that shows better output videos as well as incorporate the anti aliasing method for spatial aliasing that you implemented in part 1.

Together with these two parameters you should be able to create scaled videos of your input at different frame rates and simultaneously minimize any aliasing effects due to resampling temporarily and spatially.
