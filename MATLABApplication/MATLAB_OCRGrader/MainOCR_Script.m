%%% Cleaning the workspace and history command %%%
clear;
clc;

%%% Reading Numbers Image %%%
I = imread('numbers.png');
%%% Loading the images in a "Gray Scaled" color %%%
I = rgb2gray(I);

%%% Assigning Gray Threshold %%%
th  = graythresh(I);
thc = 0.5;
%%% Loading the "Numbers Image" in a "Black And White" color %%%
bw = im2bw(I, th);

%%% Showing Black And White Image %%%
figure; imshow(bw);

%%% Obtaining Digits using Matlab OCR Function %%%
results = ocr(bw, 'CharacterSet', '0123456789');
%%% Showing result in text way %%%
results.Text


%%% Showing result using Regular Expression %%%

regularExpr = '\d';
bboxes = locateText(results, regularExpr, 'UseRegexp', true);
digits = regexp(results.Text, regularExpr, 'match');
Idigits = insertObjectAnnotation(I, 'rectangle', bboxes, digits);

figure; imshowpair(bw, Idigits, 'montage');
figure; imshow(Idigits);


%%% Showing result using Character Confidences %%%

[sortedConf, sortedIndex] = sort(results.CharacterConfidences, 'descend');

indexesNaNsRemoved = sortedIndex( ~isnan(sortedConf) );
topTenIndexes = indexesNaNsRemoved(1:10);

digits2 = num2cell(results.Text(topTenIndexes));
bboxes2 = results.CharacterBoundingBoxes(topTenIndexes, :);

Idigits2 = insertObjectAnnotation(I, 'rectangle', bboxes2, digits2);

figure; imshowpair(bw, Idigits2, 'montage');