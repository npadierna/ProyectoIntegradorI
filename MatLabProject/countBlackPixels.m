function amoutOfBlackPixels = countBlackPixels( ...
    aReferringExamBlackAndWhiteImage, xCoordinate, yCoordinate, radius)

    %%% Variable used for saving the amount of black pixels found %%%
    amoutOfBlackPixels = 0;

    %%% Cycly for couting the amount of black pixels in a rectangle area %%%
    for x = (xCoordinate - radius):(xCoordinate + radius)
       for y = (yCoordinate - radius):(yCoordinate + radius)
           % Are the X an Y coordinates into a legal area in the image?
           if ((y <= size(aReferringExamBlackAndWhiteImage, 1)) && ...
                   (x <=  size(aReferringExamBlackAndWhiteImage, 2)))

               % Obtaining the pixel in (Y, X) coordinate -> [row, column]
               pixelValue = aReferringExamBlackAndWhiteImage(round(y), ...
                   round(x));
               % Is the current pixel equals to a black pixel?
               if (pixelValue == 0)
                  % Increasing the black pixels' counter
                  amoutOfBlackPixels = amoutOfBlackPixels + 1;
               end
           end
       end
    end
end