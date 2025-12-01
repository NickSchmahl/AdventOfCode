module Main where
import Data.Char (digitToInt, isDigit)
import Data.List (intersect)

main :: IO ()
main = solve_4_1 0

solve_4_1 :: Int -> IO ()
solve_4_1 pts = getLine >>= \str -> if null str then print pts
        else solve_4_1 (pts + getPointsOfLine str)

getPointsOfLine :: String -> Int
getPointsOfLine str = let 
    (winStr, mineStr) = splitStr str
    (win, mine) = (stringToIntList winStr, stringToIntList mineStr)
    in powerTwo (length (intersect win mine)-1)

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

powerTwo :: Int -> Int 
powerTwo n = if n < 0 then 0 else power_acc n 1 where
        power_acc x y = if x == 0 then y else power_acc (x-1) (y*2)

