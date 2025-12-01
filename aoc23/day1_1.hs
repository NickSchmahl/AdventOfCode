module Main where
import Data.Char ( digitToInt, isDigit )

main :: IO ()
main = do solve_1_1 []

solve_1_1 :: [Int] -> IO ()
solve_1_1 ls = getLine >>= \input -> if null input then print (sum ls) else solve_1_1 (10 * findFirstNumber input + findFirstNumber (reverse input) : ls)

findFirstNumber :: String -> Int 
findFirstNumber [] = 0
findFirstNumber (c:cs) = if isDigit c then digitToInt c else findFirstNumber cs
