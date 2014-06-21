%%% Cleaning the workspace and history command %%%
clear;
clc;

%%% X and Y coordinates for the bubbles' centers %%%
bubblesCenterYCoordinates = [294 333 372 411 450 489 528 567 606 645 ...
    684 723 762 801 840];
bubblesCenterXCoordinates = [168 210 252 294 521 563 605 647];

%%%  Computing the corresponding (x, y) coordinate for each bubble %%%
bubblesCenterCoordinates = computeBubblesCenters( ...
    bubblesCenterXCoordinates, bubblesCenterYCoordinates);

%%% Computing the set of answers belong to each question %%%
answersForQuestions = computeAnswersBubbles('only_logos_template.png', ...
    '20140527_091346.jpg', bubblesCenterCoordinates, 80, 13);

%%% Displaying the result of processing the image %%%
for questionsCounter = 1:size(answersForQuestions, 2)
    % Question #NUMBER -> [0/1 0/1 0/1 0/1]
    fprintf(1, '%s%d -> [%d %d %d %d]\n', 'Question #', ...
        answersForQuestions(questionsCounter).id, ...
        answersForQuestions(questionsCounter).items(1), ...
        answersForQuestions(questionsCounter).items(2), ...
        answersForQuestions(questionsCounter).items(3), ...
        answersForQuestions(questionsCounter).items(4));
end