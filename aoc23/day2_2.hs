module Main where

data Color = Red | Green | Blue deriving Show

main :: IO ()
main = solve_2_2 []

solve_2_2 :: [Int] -> IO ()
solve_2_2 ls = getLine >>= \input -> if null input then print (sum ls) 
        else solve_2_2 (getGamePoints (minGame (drop 2 (words input)) (0,0,0)):ls)

getGamePoints :: (Int, Int, Int) -> Int 
getGamePoints (red, green, blue) = red * green * blue

minGame :: [String] -> (Int, Int, Int) -> (Int, Int, Int)
minGame [] (r,g,b) = (r,g,b)
minGame str (red, green, blue) = let 
        currentNum = read (head str) :: Int 
        in case strToColor (init (str!!1)) of 
                            Green -> minGame (drop 2 str) (red, max green currentNum, blue)
                            Blue -> minGame (drop 2 str) (red, green, max blue currentNum)
                            Red -> minGame (drop 2 str) (max red currentNum, green, blue)

strToColor :: String -> Color
strToColor "green" = Green
strToColor "gree" = Green
strToColor "blue" = Blue
strToColor "blu" = Blue
strToColor "red" = Red
strToColor "re" = Red
strToColor _ = Green