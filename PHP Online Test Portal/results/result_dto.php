<?php

class ResultDTO
{
    private $testTitle;
    private $studentName;
    private $question;
    private $selectedAnswer;
    private $correctAnswer;
    private $isCorrect;

    // Constructor
    public function __construct($testTitle, $studentName, $question, $selectedAnswer, $correctAnswer, $isCorrect)
    {
        $this->testTitle = $testTitle;
        $this->studentName = $studentName;
        $this->question = $question;
        $this->selectedAnswer = $selectedAnswer;
        $this->correctAnswer = $correctAnswer;
        $this->isCorrect = $isCorrect;
    }

    // Getters
    public function getTestTitle()
    {
        return $this->testTitle;
    }

    public function getStudentName()
    {
        return $this->studentName;
    }

    public function getQuestion()
    {
        return $this->question;
    }

    public function getSelectedAnswer()
    {
        return $this->selectedAnswer;
    }

    public function getCorrectAnswer()
    {
        return $this->correctAnswer;
    }

    public function getIsCorrect()
    {
        return $this->isCorrect;
    }
}
