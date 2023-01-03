package com.feng.baseframework.groovy

def methodName = _1
def methodParameters = _2
def methodReturnType = _3
def date = _4
def time = _5
def projectName = ""
try {
    projectName = _6
}catch (Exception e){
    projectName = ""
}

def commentInfoForIDEA(methodName, methodParameters, methodReturnType, date, time, projectName) {
    def author = "lanhaifeng"
    def copyright = "lanhaifeng All rights reserved."

    def outputParams = ""
    for (param in methodParameters) {
        outputParams += " * @param " + param + " \n"
    }
    def outputReturnType = ""
    if (methodReturnType != "void") {
        outputReturnType = " * @return " + methodReturnType + "\n"
    }

    def outputDesc = " * @description  \n"
    def outputAuthor = " * @author " + author + "\n"
    def outputDateTime = " * @createDate " + date + " " + time + "\n"
    def outputProjectName = " * @project " + projectName + "\n"

    def outputClassOtherInfo = " * @Copyright " + copyright + "\n"
    outputClassOtherInfo += " * @Reviewed " + "\n"
    outputClassOtherInfo += " * @UpateLog    Name    Date    Reason/Contents\n"
    outputClassOtherInfo += " *             ---------------------------------------\n"
    outputClassOtherInfo += " *                ****    ****    **** \n"


    def result = ""

    if (methodName) {
        result += outputProjectName
        result += outputDesc
        result += outputParams
        result += outputReturnType
        result += outputAuthor
        result += outputDateTime
        result += outputClassOtherInfo
    }
    result += " *"
    return result
}

return commentInfoForIDEA(methodName, methodParameters, methodReturnType, date, time, projectName)
