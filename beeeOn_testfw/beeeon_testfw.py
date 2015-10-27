#!/usr/bin/python

import sys
import glob
import os
import StringIO
import argparse
import re
# avoid python bytecode in import scripts
sys.dont_write_bytecode = True

import base_test_handler
import ui_test_handler
import ada_test_handler
import beeeon_test_runner

SUCCESS = 0
ERROR_PARAMS = 2
ERROR_TEST_FAIL = 1

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--filter", nargs='?', default=".*", const=".*",
                        help="filter module to run, support regex, example: module[12]/.*")
    # parser.add_argument("--dbstring", nargs='?', default=DB_CON_STRING, const=DB_CON_STRING,
    #                    help="database connection string")
    # parser.add_argument("--ports", nargs='+', default=PORT_DEFAULT_ARR,
    #                    help="set available hosts for option *.input[host abbr.], *.output[host abbr.]. "
    #                         "\nformat: [host abbr.]:[host port]"
    #                         "\nexample: ui:1234 ada:2345")
    parser.add_argument("--testsdir", nargs='?', default='./beeeon_tests',
                        help="set directory of tests")
    parser.add_argument("--segments", nargs='+', default=['ada', 'ui'],
                        help="register some segments(ui_server, AdaApp, ...), probably you want use this with --filter")
    args = parser.parse_args()


    # check filter regex validity
    try:
        re.compile(args.filter)
    except re.error:
        print "regex is not valid"
        return ERROR_PARAMS

    print 'START'

    os.chdir(args.testsdir)

    beeeOnTestRunner = beeeon_test_runner.testRunner(args.filter)

    beeeOnTestRunner.setBeeeOnSegmentTestfw(base_test_handler.BaseTestHandler())
    if("ui" in args.segments):
        beeeOnTestRunner.setBeeeOnSegmentTestfw(ui_test_handler.UiTestHandler())
    if("ada" in args.segments):
        beeeOnTestRunner.setBeeeOnSegmentTestfw(ada_test_handler.AdaTestHandler())

    '''
    place for register new BeeeOn Segment testfw
    '''

    return beeeOnTestRunner.run()

if __name__ == "__main__":
    sys.exit(main())