function bubblesCenterCoordinates = computeBubblesCenters( ...
    bubblesCenterXCoordinates, bubblesCenterYCoordinates, columnsAmout)

    %%% Initializing the matrix for storing the (x, y) coordinates %%%
    bubblesCenterCoordinates = zeros(size(bubblesCenterXCoordinates, ...
        2) * size(bubblesCenterYCoordinates, 2), 2);
    %%% Sequential counter for questions %%%
    counter = 1;
    column = size(bubblesCenterXCoordinates, 2) / columnsAmout;
    
    for questionItem = 1:columnsAmout
       for yCoordinate = 1:size(bubblesCenterYCoordinates, 2)
          for xCoordinate = ((questionItem - 1) * column + 1):( ...
                  questionItem * column)
              bubblesCenterCoordinates(counter, 1) = ...
                  bubblesCenterXCoordinates(1, xCoordinate);
              bubblesCenterCoordinates(counter, 2) = ...
                  bubblesCenterYCoordinates(1, yCoordinate);
              counter = counter + 1;
          end
       end
    end
end