#!/bin/bash

list1=()
list2=()

while IFS=' ' read -r id1 id2; do 
	list1+=($id1)
	list2+=($id2)
done < "./inputs/day1.txt"

readarray -t list1_sort < <(printf "%s\n" "${list1[@]}" | sort -n)
readarray -t list2_sort < <(printf "%s\n" "${list2[@]}" | sort -n)

sum=0
similarity=0

for i in "${!list1_sort[@]}"; do 
	difference="$((${list1_sort[$i]} - ${list2_sort[$i]}))"
	abs=${difference#-}
	sum=$((sum + abs))

	current_number=${list1_sort[$i]}
	count=$(printf "%s\n" "${list2[@]}" | grep -c "$current_number")
	similarity=$((similarity + count * $current_number))
done

echo "Part 1: $sum"
echo "Part 2: $similarity"



