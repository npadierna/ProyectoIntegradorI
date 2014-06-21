function answersQuestions = findCorrectsAnswersForEachQuestion( ...
    aReferringExamGrayImage, bubblesCenterCoordinates, threshold, radius)

    %%% Defining a structure for a question element %%%
    answersQuestions = struct(struct('id', 0, 'items', 0));
    %%% Loading the "Referring Image" in a "Black And White" color %%%
    aReferringExamBlackAndWhiteImage = im2bw(aReferringExamGrayImage, 0.5);
    %%% Sequential counter for questions %%%
    counter = 1;

    %%% Showing the Black And White image load to algorithm %%%
    figure();
    imshow(aReferringExamBlackAndWhiteImage);
    title('Referring Exam in Black And White');
    hold on;

%     scatter(bubblesCenterCoordinates(:, 1), ...
%     bubblesCenterCoordinates(:, 2), 5, 'MarkerEdgeColor', 'r', ...
%         'MarkerFaceColor', 'r');
%     hold on;
%     scatter(bubblesCenterCoordinates(:, 1), ...
%         bubblesCenterCoordinates(:, 2), 100, 'MarkerEdgeColor', 'r');
%     hold on;

    %%% Cycle for analyzing each questions composed by 4 bubbles %%%
    for columnBubbleCounter = 1:(size(bubblesCenterCoordinates, 1) / 4)
        % Array for storing the amount of black pixels for each bubble
        blackPixelCounter = zeros(1, 4);

        %%% Cycle for analyzing each bubble in this question %%%
        for rowBubbleCounter = 1:4
            % Obtaining the bubbles in this question
            position = (columnBubbleCounter - 1) * 4 + rowBubbleCounter;

            % Invocation to the black pixel counter method
            blackPixelCounter(rowBubbleCounter) = countBlackPixels( ...
                aReferringExamBlackAndWhiteImage, ...
                bubblesCenterCoordinates(position, 1), ...
                bubblesCenterCoordinates(position, 2), radius);
        end

        % Array for storing the correct answers in this question
        answers = zeros(1, 4);
        for i = 1:size(answers, 2)
            % Comparison between the black pixels amount and the threshold
            answers(i) = blackPixelCounter(i) > threshold;
        end
        % Storing this result in the question's structure
        answersQuestions(counter) = struct('id', counter, 'items', ...
            answers);
        % Increasing the questions' counter
        counter = counter + 1;
    end
end