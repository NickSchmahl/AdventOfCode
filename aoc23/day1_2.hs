module Main where
import Data.Char ( digitToInt, isDigit )

main :: IO ()
main = do solve_1_2 []

solve_1_2 :: [Int] -> IO ()
solve_1_2 ls = getLine >>= \input -> if null input then print (sum ls) else solve_1_2 (10 * findFirstTypedNumber input + findFirstTypedNumberRev (reverse input) : ls)

findFirstTypedNumber :: String -> Int 
findFirstTypedNumber [] = 0 
findFirstTypedNumber (c:cs) 
    | isDigit c = digitToInt c
    | take 3 (c:cs) == "one" = 1
    | take 3 (c:cs) == "two" = 2
    | take 5 (c:cs) == "three" = 3
    | take 4 (c:cs) == "four" = 4
    | take 4 (c:cs) == "five" = 5
    | take 3 (c:cs) == "six" = 6
    | take 5 (c:cs) == "seven" = 7
    | take 5 (c:cs) == "eight" = 8
    | take 4 (c:cs) == "nine" = 9
    | otherwise = findFirstTypedNumber cs

findFirstTypedNumberRev :: String -> Int 
findFirstTypedNumberRev [] = 0 
findFirstTypedNumberRev (c:cs) 
    | isDigit c = digitToInt c
    | take 3 (c:cs) == "eno" = 1
    | take 3 (c:cs) == "owt" = 2
    | take 5 (c:cs) == "eerht" = 3
    | take 4 (c:cs) == "ruof" = 4
    | take 4 (c:cs) == "evif" = 5
    | take 3 (c:cs) == "xis" = 6
    | take 5 (c:cs) == "neves" = 7
    | take 5 (c:cs) == "thgie" = 8
    | take 4 (c:cs) == "enin" = 9
    | otherwise = findFirstTypedNumberRev cs