#!/bin/bash

prefix="public static String "
while read line
do
    name=`echo $line | awk '{print $1;}'`
    if  [[ -n $name ]] && [[ ${name:0:1} != '#' ]] ;
    then
	props="$props\t$prefix$name\;\n\n"
    fi
done < rytresources.properties

sed -e "s;%props%;$props;" RytongUIMessages.tmp > RytongUIMessages.java 
