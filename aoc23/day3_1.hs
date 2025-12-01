module Main where
import Data.Char (isDigit, digitToInt)
import Data.Maybe (fromJust)

main :: IO ()
main = solve_3_1 0 [] []

data Point = Point Int Int deriving Show
data PartNumber = PartNumber Int Point Point deriving Show
newtype Symbol = Symbol Point deriving Show

solve_3_1 :: Int -> [PartNumber] -> [Symbol] -> IO ()
solve_3_1 line pns sym = getLine >>= \input -> if null input
        then print (sum (map getValue (filter (`isPartNumber` sym) pns)))
        else solve_3_1 (line+1) (getNumbers line 0 input [] pns) (getSymbols line 0 input sym)

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
    | isSymbol (head str) = getSymbols line (index+1) (tail str) (Symbol (Point line index):sym)
    | otherwise = getSymbols line (index+1) (tail str) sym

isSymbol :: Char -> Bool
isSymbol c = c /= '.' && not (isDigit c)

intListToInt :: [Int] -> Int
intListToInt list = intListToInt_acc list 0 where
        intListToInt_acc [] n = n
        intListToInt_acc ls n = intListToInt_acc (init (map (* 10) ls)) (n+ last ls)

isPartNumber :: PartNumber -> [Symbol] -> Bool
isPartNumber _ [] = False
isPartNumber pn ls = isAdjacent pn (head ls) || isPartNumber pn (tail ls)

isAdjacent :: PartNumber -> Symbol -> Bool
isAdjacent (PartNumber _ (Point s_y s_x) (Point e_y e_x)) (Symbol (Point p_y p_x)) = elem p_y [s_y-1 .. e_y+1] && elem p_x [s_x-1 .. e_x+1]

getValue :: PartNumber -> Int
getValue (PartNumber n _ _) = n
