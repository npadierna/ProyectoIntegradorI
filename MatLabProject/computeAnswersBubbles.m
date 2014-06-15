function answersForQuestions = computeAnswersBubbles(onlyLogosTemplatePath, ...
    aReferringExamPath, bubblesCenterCoordinates, threshold, radius)

    %%% Loading the images in a "Gray Scaled" color %%%
    onlyLogosTemplateGrayImage = rgb2gray(imread(onlyLogosTemplatePath));
%     aReferringExamGrayImage = rgb2gray(imread(aReferringExamPath));
    aReferringExamGrayImage = imrotate(rgb2gray(imread( ...
        aReferringExamPath)), +(45));

    %%% Showing the both images load to algorithm %%%
    figure();
    imshow(onlyLogosTemplateGrayImage);
    title('Only Logos Template Gray Image');
    hold on;
    
    figure();
    imshow(aReferringExamGrayImage);
    title('A Referring Exam Gray Image');
    hold on;

    %%% Detecting the main "Features" of each image using SURF method %%%
    onlyLogosTemplateFeatures = detectSURFFeatures( ...
        onlyLogosTemplateGrayImage);
    aReferringExamFeatures = detectSURFFeatures(aReferringExamGrayImage);

    %%% Showing the main "Features" discoveried in each image %%%
    title('Key Points of Features in: Only Logos Template Gray Image');
    plot(onlyLogosTemplateFeatures.selectStrongest(100));
    hold on;
    
    title('Key Points of Features in: A Reffering Exam Gray Image');
    plot(aReferringExamFeatures.selectStrongest(100));
    hold on;

    %%% Detecting the main "Features Descriptors" of each image %%%
    [onlyLogosTemplateDescriptors, onlyLogosTemplateFeatures] = ...
        extractFeatures(onlyLogosTemplateGrayImage, ...
        onlyLogosTemplateFeatures);
    [aReferringExamDescriptors, aReferringExamFeatures] = ...
        extractFeatures(aReferringExamGrayImage, ...
        aReferringExamFeatures);

    %%% Matching the main "Features" found between both images %%%
    imagesPairsMatches = matchFeatures(onlyLogosTemplateDescriptors, ...
        aReferringExamDescriptors, 'MaxRatio', 0.6);

    %%% Displaying the result of Matching their points %%%
    onlyLogosTempateMatchedPoints = onlyLogosTemplateFeatures( ...
        imagesPairsMatches(:, 1), :);
    aReferringExamMatchedPoints = aReferringExamFeatures( ...
        imagesPairsMatches(:, 2), :);

    % figure();
%     showMatchedFeatures(onlyLogosTemplateGrayImage, ...
%         aReferringExamGrayImage, onlyLogosTempateMatchedPoints, ...
%         aReferringExamMatchedPoints, 'montage');

    %%% Computing a "Geometric Transform" between the images %%%
    [geometricTransform, inlierOnlyLogosTemplatePoints, ...
        inlierStudentReferenceExam] = estimateGeometricTransform( ...
        onlyLogosTempateMatchedPoints, aReferringExamMatchedPoints, ...
        'affine');

    %%% Displaying the result of Matching their points, but using a Geometric Transform %%%
    figure();
    showMatchedFeatures(onlyLogosTemplateGrayImage, ...
        aReferringExamGrayImage, inlierOnlyLogosTemplatePoints, ...
        inlierStudentReferenceExam, 'montage');

    %%% Creating a polygon to enclose a proyection between the images %%%
    examPolygon = [1, 1; size(onlyLogosTemplateGrayImage, 2), 1; ...
        size(onlyLogosTemplateGrayImage, 2), size( ...
        onlyLogosTemplateGrayImage, 1); 1, size( ...
        onlyLogosTemplateGrayImage, 1); 1, 1];
    %%% Applying the geometric transform to polygon's points %%%
    transformedExamPolygon = transformPointsForward(geometricTransform, ...
        examPolygon);

    %%% Displaying the enclosing polygon into the "Refferring Exam" %%%
    figure();
    imshow(aReferringExamGrayImage);
    hold on;
    line(transformedExamPolygon(:, 1), transformedExamPolygon(:, 2), ...
        'Color', 'g', 'LineWidth', 2);

    %%% Applying the geometric transform to bubbles' centers' points %%%
    transformedBubblesCenterCoordinates = transformPointsForward( ...
        geometricTransform, bubblesCenterCoordinates);
    %%% Displaying the bubbles' centers' points transformed %%%
    hold on;
    scatter(transformedBubblesCenterCoordinates(:, 1), ...
        transformedBubblesCenterCoordinates(:, 2), 5, 'MarkerEdgeColor', ...
        'r', 'MarkerFaceColor', 'r');
    hold on;
    scatter(transformedBubblesCenterCoordinates(:, 1), ...
        transformedBubblesCenterCoordinates(:, 2), 100, 'MarkerEdgeColor', ...
        'r');
    hold on;

    %%% Analyzing each bubbles' questions for correct answers %%%
    answersForQuestions = findCorrectsAnswersForEachQuestion( ...
        aReferringExamGrayImage, transformedBubblesCenterCoordinates, ...
        threshold, radius);
end