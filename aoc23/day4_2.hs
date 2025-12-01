module Main where

import Data.Char (digitToInt, isDigit)
import Data.List (intersect)

main :: IO ()
main = solve_4_2 []

solve_4_2 :: [Int] -> IO ()
solve_4_2 ls = getLine >>= \str -> if null str then print (sum (getCopyList ls)) 
    else solve_4_2 (ls ++ [numOfCommonValues str])

getCopyList :: [Int] -> [Int]
getCopyList ls = copy_acc 0 ls [1 | x <- [1 .. length ls]] where
        copy_acc :: Int -> [Int] -> [Int] -> [Int]
        copy_acc _ [] copies = copies
        copy_acc index list copies = copy_acc (index+1) (tail list) (fillList index (head list) copies)

fillList :: Int -> Int -> [Int] -> [Int]
fillList index adding copies = [if n <= (index+adding) && n > index then (copies!!n)+(copies!!index) else copies!!n | n <- [0 .. length copies - 1]]

numOfCommonValues :: String -> Int
numOfCommonValues str = let split = splitStr str in length (stringToIntList (fst split) `intersect` stringToIntList (snd split))

splitStr :: String -> (String, String)
splitStr str = split_int 0 str ""  where
        split_int :: Int -> String -> String -> (String, String)
        split_int part line first
            | null line = (first, line)
            | part == 0 && head line /= ':' = split_int 0 (tail line) first
            | part == 0 && head line == ':' = split_int 1 (tail line) first 
            | part == 1 && head line /= '|' = split_int 1 (tail line) (first ++ [head line])
            | part == 1 && head line == '|' = split_int 2 (tail line) first
            | part == 2 = (first, line)
            | otherwise = ("Something", "wrong")

stringToIntList :: String -> [Int] 
stringToIntList str = toList_int str [] where
        toList_int :: String -> [Int] -> [Int]
        toList_int [] acc = acc
        toList_int [x] acc = acc
        toList_int str acc = toList_int (drop 3 str) (strToInt (take 3 str) : acc)

strToInt :: String -> Int 
strToInt str 
    | length str /= 3 = 0
    | isDigit (str!!1) = 10 * digitToInt (str!!1) + digitToInt (str!!2)
    | otherwise = digitToInt (str!!2)
