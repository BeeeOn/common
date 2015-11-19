#!/usr/bin/python

import sys
import glob
import os
import StringIO
import argparse
import re

# beeeon files
import xml_utils
import utils

import ui_test_handler

SUCCESS = 0
ERROR_TEST_FAIL = 1


class testRunner:
    def __init__(self, filterRegex = ".*"):
        self.filterRegex = filterRegex
        self.allTestsDict = {}
        self.stopOnFail = False

    def setBeeeOnSegmentTestfw(self, test):
        newTestsDict = test.getSupportedTests()

        self.allTestsDict.update(newTestsDict)

        if len(self.allTestsDict) > len(set(self.allTestsDict)):
            print utils.warning("extension conflict!")
            sys.stdout.flush()
            return False

    def setTestSegments(self, segments):
        self.segments = segments

    def runSingleTestPart(self, testPartFile):
        testFile = os.path.basename(testPartFile)
        if("." not in testFile):
            print utils.failure("extension missing")
            sys.stdout.flush()
            return False

        fileName, fileExt = testFile.split(".")

        if("ignore" in fileExt):
            return True

        if(fileExt not in self.allTestsDict.keys()):
            print utils.failure("unknown extension: " + fileExt)
            sys.stdout.flush()
            return False

        return self.allTestsDict[fileExt](testPartFile)

    def run(self):
        testModuleDirs = filter(os.path.isdir, os.listdir('.'))
        unitTestPassed = True
        # module tests
        for testModuleDir in testModuleDirs:

            isTesting = False
            for segment in self.segments:
                if re.match(segment + "_*", testModuleDir):
                    isTesting = True
                    break

            if not isTesting:
                continue

            testModuleFilter = "^" + self.filterRegex.split("/")[0]
            # skip if filter regex not match
            if not re.match(testModuleFilter, testModuleDir):
                continue
            print ""
            sys.stdout.flush()
            print "<",testModuleDir
            sys.stdout.flush()
            testDirs = next(os.walk(testModuleDir))[1]

            moduleTestsPassed = True
            # test cases
            for testDir in sorted(testDirs):
                if not re.match(self.filterRegex, testModuleDir+"/"+testDir):
                    continue
                if "ignore" in testDir:
                    print "  +", utils.info(testDir), utils.warning("IGNORE")
                    sys.stdout.flush()
                    continue

                print "  +", utils.info(testDir), ":",
                sys.stdout.flush()
                testPartFiles = sorted(os.listdir(testModuleDir + '/' + testDir))
                # print testPartFiles,
                singleTestPassed = True
                # test parts
                for testPartFile in sorted(testPartFiles):
                    # print "   +", testPartFile,
                    testPartPassed = self.runSingleTestPart(testModuleDir + '/' + testDir + '/' + testPartFile)
                    if(testPartPassed):
                        print testPartFile,
                        sys.stdout.flush()
                        pass
                    else:
                        singleTestPassed = False
                        print utils.failure(testPartFile) ,
                        sys.stdout.flush()
                        # dont break, we need to clean after test

                if(singleTestPassed):
                    print utils.success("OK")
                    sys.stdout.flush()
                else:
                    print utils.failure("FAIL")
                    sys.stdout.flush()
                    moduleTestsPassed = False
                    if(self.stopOnFail):
                        break

            if(moduleTestsPassed):
                print utils.success("> module "+ testModuleDir +" OK")
                sys.stdout.flush()
            else:
                print utils.failure("> module "+ testModuleDir +" FAIL")
                sys.stdout.flush()
                unitTestPassed = False
                if(self.stopOnFail):
                  break

        if(unitTestPassed):
            print utils.success("Tests OK")
            sys.stdout.flush()
            return SUCCESS
        else:
            print utils.failure("Tests FAIL")
            sys.stdout.flush()
            unitTestPassed = False
            return ERROR_TEST_FAIL

        return SUCCESS
