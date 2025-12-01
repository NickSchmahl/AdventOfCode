module Main where

import Data.Char (isDigit, digitToInt)

data MapEntry = MapEntry Int Int Int deriving Show

main :: IO ()
main = solve_5_1 8 [] []

solve_5_1 :: Int -> [(Int, Int)] -> [MapEntry] -> IO () 
solve_5_1 mapsToGo seeds map = getLine >>= \str -> 
        if mapsToGo == 8 then solve_5_1 (mapsToGo-1) (parseSeeds str) map 
        else if null str && mapsToGo /= 0 then solve_5_1 (mapsToGo-1) (updateSeeds seeds map) [] 
        else if null str && mapsToGo == 0 then print "Lowest location number:" >> print (minimum (updateSeeds seeds map))
        else if not (isDigit (head str)) then solve_5_1 mapsToGo seeds map
        else solve_5_1 mapsToGo seeds (addLineToMap str map)

updateSeeds :: [(Int, Int)] -> [MapEntry] -> [(Int, Int)]
updateSeeds seeds em = map (`updateSeed` em) seeds where
        updateSeed :: [(Int, Int)] -> [MapEntry] -> [(Int, Int)] 
        updateSeed seed [] = [seed]
        updateSeed seed (me:mes) 
                | fst seed > endSeedRange me = [seed] -- komplett davor
                | snd seed < beginSeedRange me = [seed] -- komplett danach
                | fst seed <= beginSeedRange me && snd seed > endSeedRange me = [] -- halb vorne drin 
                | otherwise = [(0,0)]

endSeedRange :: MapEntry -> Int
endSeedRange (MapEntry m1 m2 m3) = m2 + m3 - 1

beginSeedRange :: MapEntry -> Int 
beginSeedRange (MapEntry m1 m2 m3) = m2

differenceMapEntry :: MapEntry -> Int 
differenceMapEntry (MapEntry m1 m2 m3) = m2-m1

parseSeeds :: String -> [(Int, Int)]
parseSeeds str = parseSeeds_int str [] (-1) [] where
        parseSeeds_int :: String -> [(Int, Int)] -> Int -> [Int] -> [(Int, Int)]
        parseSeeds_int stri c_ls start_range digits 
                | null stri = (start_range, start_range+intListToInt digits-1) : c_ls
                | isDigit (head stri) = parseSeeds_int (tail stri) c_ls start_range (digits ++ [digitToInt (head stri)])
                | not (isDigit (head stri)) && not (null digits) = 
                        if start_range == -1    
                            then parseSeeds_int (tail stri) c_ls (intListToInt digits) []
                            else parseSeeds_int (tail stri) ((start_range, start_range+intListToInt digits-1) : c_ls) (-1) []
                | otherwise = parseSeeds_int (tail stri) c_ls start_range digits

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
