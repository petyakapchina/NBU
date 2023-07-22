<?php

class StudentDTO
{
    private $number;
    private $name;

    public function setNumber($number)
    {
        $this->number = $number;
    }

    public function geNumber()
    {
        return $this->number;
    }

    public function setName($name)
    {
        $this->name = $name;
    }

    public function getName()
    {
        return $this->name;
    }
}
