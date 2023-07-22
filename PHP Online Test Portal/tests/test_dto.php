<?php

class TestDTO
{

    private $id;
    private $title;
    private $duration;
    private $active;
    private $user;
    private $questions;
    private $url;

    public function setId($id)
    {
        $this->id = $id;
    }

    public function setTitle($title)
    {
        $this->title = $title;
    }

    public function setDuration($duration)
    {
        $this->duration = $duration;
    }

    public function setActive($active)
    {
        $this->active = $active;
    }

    public function setUser($user)
    {
        $this->user = $user;
    }

    public function setQuestions($questions)
    {
        $this->questions = $questions;
    }

    public function getId()
    {
        return $this->id;
    }

    public function getTitle()
    {
        return $this->title;
    }

    public function getDuration()
    {
        return $this->duration;
    }

    public function getActive()
    {
        return $this->active;
    }

    public function getUser()
    {
        return $this->user;
    }

    public function getQuestions()
    {
        return $this->questions;
    }

    public function setUrl($url)
    {
        $this->url = $url;
    }

    public function getUrl()
    {
        return $this->url;
    }
}
