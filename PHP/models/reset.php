<?php
    class Reset{
        private $id;
        private $email;
        private $token;
        private $created;
        private $available;

        function __construct($email, $created, $available)
        {
            $this->email = $email;
            $this->created = $created;
            $this->available = $available;
        }

        public function getCreated()
        {
            return $this->created;
        }
        public function getEmail()
        {
            return $this->email;
        }
        public function getAvailable()
        {
            return $this->available;
        }
    }
