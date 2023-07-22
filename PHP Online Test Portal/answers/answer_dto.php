<?php

class AnswerDTO
{
    private $question_id;
    private $answer_id;
    private $text;
    private $correct;
    private $marked;

    public function __construct()
    {
    }

    public function setQuestionId($question_id)
    {
        $this->question_id = $question_id;
    }

    public function getQuestionId()
    {
        return $this->question_id;
    }

    public function setAnswer($text)
    {
        $this->text = $text;
    }

    public function getAnswer()
    {
        return $this->text;
    }

    public function setCorrect($correct)
    {
        $this->correct = $correct;
    }

    public function isCorrect()
    {
        return $this->correct;
    }

    public function setAnswerId($answer_id)
    {
        $this->answer_id = $answer_id;
    }

    public function getAnswerId()
    {
        return $this->answer_id;
    }

    public function setMarked($marked)
    {
        $this->marked = $marked;
    }

    public function getMarked()
    {
        return $this->marked;
    }

    public function getAnwserPK()
    {
        return $this->question_id ."_". $this->answer_id;
    }
}
