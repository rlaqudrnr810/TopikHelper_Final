package com.example.topikhelper;

public class VirtualTestResult {
    private String questionNum;
    private String realAnswer;
    private String myAnswer;

    private String abcd;

    public VirtualTestResult(){ }
    public VirtualTestResult(String questionNum, String realAnswer, String myAnswer) {
        this.questionNum = questionNum;
        this.realAnswer = realAnswer;
        this.myAnswer = myAnswer;
    }


    public String getQuestionNum() {
        return questionNum;
    }
    public String getRealAnswer() {
        return realAnswer;
    }
    public String getMyAnswer() {
        return myAnswer;
    }

    public String getText() {return this.abcd;}

    public void setQuestionNum(String questionNum) {
        this.questionNum = questionNum;
    }
    public void setRealAnswer(String realAnswer) {
        this.realAnswer = realAnswer;
    }
    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    public void setText(String text){abcd=text;}

    @Override
    public String toString() {
        return "VirtualTestResult{" +
                "questionNum='" + questionNum + '\'' +
                ", realAnswer='" + realAnswer + '\'' +
                ", myAnswer='" + myAnswer + '\'' +
                '}';
    }
}
