#!/bin/bash

totalLineCount=0
fileLineCount=0
fileCount=0

for file in $(find $1 -name $2); do
  echo $file
  let fileCount=fileCount+1
#  echo $fileCount
  fileLineCount=`wc -l $file`
#  echo $fileLineCount
  fileLineCount=`expr match "$fileLineCount" '\([0-9]*\)'`
#  echo $fileLineCount
  let totalLineCount=totalLineCount+fileLineCount
done

echo
echo Total Number of Files = $fileCount
echo Total Number of Lines = $totalLineCount
