<?php
class User
{
    private $id;
    private $email;
    private $hash_password;
    private $avatar;
    private $fullname;

    function __construct($id, $email, $avatar, $fullname, $hash_password)
    {
        $this->fullname = $fullname;
        $this->avatar = $avatar;
        $this->id = $id;
        $this->email = $email;
        $this->hash_password = $hash_password;
    }

    public function getId()
    {
        return $this->id;
    }
    public function getEmail()
    {
        return $this->email;
    }
    public function getHashPassword()
    {
        return $this->hash_password;
    }
    public function getAvatar()
    {
        return $this->avatar;
    }
    public function getFullname()
    {
        return $this->fullname;
    }
}
