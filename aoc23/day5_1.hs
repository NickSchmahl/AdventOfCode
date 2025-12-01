module Main where

import Data.Char (isDigit, digitToInt)

data MapEntry = MapEntry Int Int Int deriving Show

main :: IO ()
main = solve_5_1 8 [] []

solve_5_1 :: Int -> [Int] -> [MapEntry] -> IO () 
solve_5_1 mapsToGo seeds map = getLine >>= \str -> 
        if mapsToGo == 8 then solve_5_1 (mapsToGo-1) (parseSeeds str) map 
        else if null str && mapsToGo /= 0 then solve_5_1 (mapsToGo-1) (updateSeeds seeds map) [] 
        else if null str && mapsToGo == 0 then print "Lowest location number:" >> print (minimum (updateSeeds seeds map))
        else if not (isDigit (head str)) then solve_5_1 mapsToGo seeds map
        else solve_5_1 mapsToGo seeds (addLineToMap str map)

updateSeeds :: [Int] -> [MapEntry] -> [Int]
updateSeeds seeds em = map (`updateSeed` em) seeds where
        updateSeed :: Int -> [MapEntry] -> Int 
        updateSeed seed [] = seed
        updateSeed seed ((MapEntry m1 m2 m3):map) = if seed >= m2 && seed <= m2+m3-1 then seed + m1 - m2 else updateSeed seed map 

parseSeeds :: String -> [Int]
parseSeeds str = parseSeeds_int str [] [] where
        parseSeeds_int :: String -> [Int] -> [Int] -> [Int]
        parseSeeds_int stri c_ls digits 
                | null stri = intListToInt digits : c_ls
                | isDigit (head stri) = parseSeeds_int (tail stri) c_ls (digits ++ [digitToInt (head stri)])
                | not (isDigit (head stri)) && not (null digits) = parseSeeds_int (tail stri) (intListToInt digits : c_ls) []
                | otherwise = parseSeeds_int (tail stri) c_ls digits

addLineToMap :: String -> [MapEntry] -> [MapEntry]
addLineToMap str map = addLine_int str 0 (MapEntry 0 0 0) [] where
        addLine_int :: String -> Int -> MapEntry -> [Int] -> [MapEntry]
        addLine_int stri index me digits 
                | null stri = map ++ [addToEntry (intListToInt digits) index me]
                | isDigit (head stri) = addLine_int (tail stri) index me (digits ++ [digitToInt (head stri)])
                | otherwise = addLine_int (tail stri) (index+1) (addToEntry (intListToInt digits) index me) []

intListToInt :: [Int] -> Int 
intListToInt ls = intList_acc ls 0 where
        intList_acc [] acc = acc
        intList_acc list acc = intList_acc (init (map (*10) list)) (acc + last list)

addToEntry :: Int -> Int -> MapEntry -> MapEntry
addToEntry num index (MapEntry m1 m2 m3) 
        | index > 2 || index < 0 = MapEntry m1 m2 m3
        | index == 0 = MapEntry num m2 m3
        | index == 1 = MapEntry m1 num m3
        | index == 2 = MapEntry m1 m2 num 
