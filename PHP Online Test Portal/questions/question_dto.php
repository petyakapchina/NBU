<?php

class QuestionDTO
{

    private $test_id;
    private $question;
    private $answers;

    public function __construct()
    {
    }

    public function setTestId($test_id)
    {
        $this->test_id = $test_id;
    }

    public function getTestId()
    {
        return $this->test_id;
    }

    public function setQuestion($question)
    {
        $this->question = $question;
    }

    public function getQuestion()
    {
        return $this->question;
    }


    public function setAnswers($answers)
    {
        $this->answers = $answers;
    }

    public function getAnswers()
    {
        return $this->answers;
    }
}
