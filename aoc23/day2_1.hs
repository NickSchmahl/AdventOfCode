module Main where
import Data.Char (isNumber, isDigit)

main :: IO ()
main = solve_2_1 1 []

-- Grab Red Green Blue
data Color = Green | Red | Blue

solve_2_1 :: Int -> [Int] -> IO ()
solve_2_1 index ls = getLine >>= \input -> if null input then print (sum ls) else
            if isValidGame input then solve_2_1 (index + 1) (index:ls) else solve_2_1 (index+1) ls

isValidGame :: String -> Bool
isValidGame str = valid_in (drop 2 (words str)) where
        valid_in :: [String] -> Bool
        valid_in [] = True
        valid_in [el] = True
        valid_in ls = case init (ls!!1) of
                                "green" -> isColorValid Green (read (head ls) :: Int) && valid_in (drop 2 ls)
                                "gree" -> isColorValid Green (read (head ls) :: Int) && valid_in (drop 2 ls)
                                "blue" -> isColorValid Blue (read (head ls) :: Int) && valid_in (drop 2 ls)
                                "blu" -> isColorValid Blue (read (head ls) :: Int) && valid_in (drop 2 ls)
                                "red" -> isColorValid Red (read (head ls) :: Int) && valid_in (drop 2 ls)
                                "re" -> isColorValid Red (read (head ls) :: Int) && valid_in (drop 2 ls)
                                _ -> False

isColorValid :: Color -> Int -> Bool
isColorValid Red x = x <= 12
isColorValid Green x = x <= 13
isColorValid Blue x = x <= 14