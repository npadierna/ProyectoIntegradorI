function bubblesCenterCoordinates = computeBubblesCenters( ...
    bubblesCenterXCoordinates, bubblesCenterYCoordinates)

    %%% Initializing the matrix for storing the (x, y) coordinates %%%
    bubblesCenterCoordinates = zeros(size(bubblesCenterXCoordinates, ...
        2) * size(bubblesCenterYCoordinates, 2), 2);
    %%% Sequential counter for questions %%%
    counter = 1;
    yCoordinatesSize = size(bubblesCenterXCoordinates, 2);

    %%% Cycle #1, for the first questions column %%%
    for yCenter = 1:size(bubblesCenterYCoordinates, 2)
        for xCenter = 1:(yCoordinatesSize / 2)
            % Storing the X coordinate for a bubble
            bubblesCenterCoordinates(counter, 1) = ...
                bubblesCenterXCoordinates(1, xCenter);
            % Storing the Y coordinate for a bubble
            bubblesCenterCoordinates(counter, 2) = ...
                bubblesCenterYCoordinates(1, yCenter);
            % Increasing the questions' counter
            counter = counter + 1;
        end
    end

    %%% Cycle #2, for the second questions column %%%
    for yCenter = 1:size(bubblesCenterYCoordinates, 2)
        for xCenter = ((yCoordinatesSize / 2) + 1):yCoordinatesSize
            % Storing the X coordinate for a bubble
            bubblesCenterCoordinates(counter, 1) = ...
                bubblesCenterXCoordinates(1, xCenter);
            % Storing the Y coordinate for a bubble
            bubblesCenterCoordinates(counter, 2) = ...
                bubblesCenterYCoordinates(1, yCenter);
            % Increasing the questions' counter
            counter = counter + 1;
        end
    end
end