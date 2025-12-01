module Main where
import Data.Char (isDigit, digitToInt)
import Data.Maybe (fromJust)

main :: IO ()
main = solve_3_2 0 [] []

data Point = Point Int Int deriving Show
data PartNumber = PartNumber Int Point Point deriving Show
newtype Symbol = Symbol Point deriving Show

solve_3_2 :: Int -> [PartNumber] -> [Symbol] -> IO ()
solve_3_2 line pns sym = getLine >>= \input -> if null input
        then print (sum (map (`getGearValue` pns) sym))
        else solve_3_2 (line+1) (getNumbers line 0 input [] pns) (getSymbols line 0 input sym)

getNumbers :: Int -> Int -> String -> [Int] -> [PartNumber] -> [PartNumber]
getNumbers line index str digits numbers
    | null str && null digits = numbers
    | null str && not (null digits) = numbers ++ [PartNumber (intListToInt digits) (Point line (index - length digits)) (Point line (index-1))]
    | isDigit (head str) = getNumbers line (index+1) (tail str) (digits ++ [digitToInt (head str)]) numbers
    | not (isDigit (head str)) && not (null digits) = getNumbers line (index+1) (tail str) []  (numbers ++ [PartNumber (intListToInt digits) (Point line (index - length digits)) (Point line (index-1))])
    | not (isDigit (head str)) = getNumbers line (index+1) (tail str) [] numbers
    | otherwise = numbers

getSymbols :: Int -> Int -> String -> [Symbol] -> [Symbol]
getSymbols line index str sym
    | null str = sym
    | head str == '*' = getSymbols line (index+1) (tail str) (Symbol (Point line index):sym)
    | otherwise = getSymbols line (index+1) (tail str) sym

intListToInt :: [Int] -> Int
intListToInt list = intListToInt_acc list 0 where
        intListToInt_acc [] n = n
        intListToInt_acc ls n = intListToInt_acc (init (map (* 10) ls)) (n+ last ls)

getGearValue :: Symbol -> [PartNumber] -> Int 
getGearValue sym pns = if isGearPart sym pns 
        then product (map getValue (filter (`isAdjacent` sym) pns))
        else 0

isGearPart :: Symbol -> [PartNumber] -> Bool
isGearPart sym pns = length (filter (`isAdjacent` sym) pns) == 2

isAdjacent :: PartNumber -> Symbol -> Bool
isAdjacent (PartNumber _ (Point s_y s_x) (Point e_y e_x)) (Symbol (Point p_y p_x)) = elem p_y [s_y-1 .. e_y+1] && elem p_x [s_x-1 .. e_x+1]

getValue :: PartNumber -> Int
getValue (PartNumber n _ _) = n