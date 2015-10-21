#!/usr/bin/python
#postgres
import psycopg2
def readFile(file):
    with open(file, 'r') as f:
        return f.read()  

def testPlugin_main(dbConnection):
    try:
        dbCursor = dbConnection.cursor()
        dbCursor.execute(readFile("03test.ignoresql"))
        dbConnection.commit()
        dbResult =  dbCursor.fetchone()
        return dbResult == (True,)
    except psycopg2.Error as e:
        print "module DB error"
        print e
        return False