class Dir:
    def __init__(self, name):
        self.name = name
        self.parent = None
        self.dirs = []
        self.files = []
        
    def putDir(self, dirName):
        newDir = Dir(dirName)
        newDir.setParent(self)
        self.dirs.append(newDir)
    
    def putFile(self, name, size):
        self.files.append(File(name, size))
    
    def getSize(self):
        sum = 0
        for file in self.files:
            sum += file.getSize()
        for dirt in self.dirs:
            sum += dirt.getSize()
        return sum
            
    def getName(self):
        return self.name
    
    def getChild(self, name):
        for dirt in self.dirs:
            if(dirt.getName() == name):
                return dirt
        return 'Error'
        
    def getChilds(self):
        return self.dirs
    
    def getFiles(self):
        return self.files
    
    def setParent(self, dirt):
        self.parent = dirt
    
    def getParent(self):
        return self.parent
        
    def printTree(self):
        print(f"  Tree for {self.getName()}, size={self.getSize()}", end='\n  ')
        for dirt in self.dirs:
            dirt.printTree()
            
        # for file in self.files:
        #     file.printMe()
        
    def smallFiles(self):
        sum = 0
        size = self.getSize()
        if size < 100000:
            sum += size
        for dirt in self.dirs:
            sum += dirt.smallFiles()
        return sum
        
    def getFileSizeList(self):
        lst = []
        lst.append(self.getSize())
        for dirt in self.dirs:
            lst = lst + dirt.getFileSizeList()
            
        return lst
        
class File:
    def __init__(self, name, size):
        self.name = name
        self.size = int(size)
    
    def getSize(self):
        return self.size
        
    def printMe(self):
        print(f"      {self.name}, {self.size}")
   
root = Dir("/")       
blocks = s.split('$')
lines = []

for block in blocks:
    splt = block.split('\n')
    splt.pop(len(splt)-1)
    lines.append(splt)

currentDir = root
for line in lines:
    if len(line) == 1 and line[0][1:3] == "cd":
        dirName = line[0].split(' ')[2]
        if dirName == '/':
            currentDir = root
        elif dirName == '..':
            currentDir = currentDir.getParent()
        else:
            currentDir = currentDir.getChild(dirName)
    elif len(line) == 1 and line[0][1:3] != "cd":
        pass
    else: 
        for exp in line[1:]:
            dirOrSize, name = exp.split(' ')
            if dirOrSize == "dir":
                currentDir.putDir(name)
            else:
                currentDir.putFile(name, dirOrSize)

space = 70000000
unused_space = space - root.getSize()
space_needed = 30000000 - unused_space

lst = root.getFileSizeList()
min = space
for item in lst:
    min = item if item < min and item > space_needed else min

print("Lösung 1:", root.smallFiles())
print("Lösung 2: ", min)